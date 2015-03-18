# hawup
TutorLab

Part 1
Introduction
There are many cloud and other distributed or cluster-based services available today that allow a set of tasks to be run concurrently on many processor cores, possibly hosted by multiple, distinct computers. Our Enginering School has such a cluster, and many of us could not do our research expeditiously or reliably without it.
A more global and famous example of such a facility is Hadoop. From its beginnings, this service aimed to provide service that could scale from a single server to thousands of nodes. The service anticipates failure, in which case it can restart a computation on another node.
The most common programming paradigm for services such as Hadoop is the MapReduce paradigm:
•	A computation is decomposed into many copies of the same, or similar, computation These can be spread across multiple nodes for performance.
•	At, or near, the end of the nodes' computations, their results may be aggregated in some fashion. This is called the reduce step.
As an example, consider summing a million numbers:
Map
The million numbers are distributed among n nodes, so that each node receives approximately 1,000,000/n numbers to be summed.
Reduce
When a node has finished its work, it has a partial sum that must be added to all of the other nodes' sums to obtain the final answer. The reduce step therefore consists of obtaining and summing the nodes' partial sums. This can be done naively or cleverly.
In this portion of the course, you will implement a form of such an infrastructure. The work will be limited to a single machine, but tasks can be mapped across multiple threads. The pedagogical goals of this lab are related to the following questions:
•	How can work be distributed among a given set of Threads?
•	How is the performance of a computation influenced by the number of Threads devoted to that computation?
•	How do we implement reduce for these Threads?
•	What races arise in implementing such a system?
•	How do you maximize liveness (responsiveness of the system) while avoiding any deadlock?

Design
Instead of providing a design for you, we shall develop the design together during lecture. There are a few principles we want to observe:
•	We seek a design that supports MVC. This will allow us to author some visualization and GUI control components in subsequent assignments. 
•	In support of MVC, we will design our components to use PropertyChangeSupport to report on their activities. Part of our design will be to articulate the PCS messages. 
•	We assume the tasks run on our infrastructure can do so mostly independently of each other.
•	We must support a reduce activity that is initiated only after all mapped computations have finished.
•	We would like to maximize our use of the available computational resources. Thus, we would not like our nodes to be idle if there is some work to be done.

User Stories
One way to elicit information necessary to create a software project is to ask for a user story. User stories are one tenet of the agile software development process. In such settings, the user resides with the development group and can tell such stories on demand. Following is an example of a user story for this project:
I have workstations that have many cores, each capable of operating independently. Some of my cores are hyperthreaded, allowing even more concurrent execution.
Abstractly, I think of the workstations has having a certain nubmer of nodes, each of which can execute some task independently. I would like to make use of these nodes in running some MapReduce computations. I would like to submit a job, have the job decomposed into independent tasks, and then have those tasks distributed across the nodes of my machine. When the tasks are done, their partial results will be turned into a result for the job by some code I will supply.
This user will have more to say about what he or she wants in the upcoming installments of this lab. For now, we must implement only what has been described in the story. From that story, some unit tests will be written (my job, perhaps yours as well) and you will develop the code to meet those tests. Tempted though you may be, you will not at this point introduce any superfluous features, from the point of view of the user story. This parsimonious development style is also a tenet of the agile software development process.

The Objects and Interfaces
The details of these objects and interfaces will be decided in class, but it seems we need at least the following types for our design, based on the user story (see underlined words above).
HaWUp
is the main class for this project.
Node
is a resource that can execute a sequence of tasks, one after the other, when its run() method is invoked. A Node must execute independently, and so it must be .start()ed in its own Thread.
You have some work to do inside Node but the instantiation and .start()ing of Nodes is done in HaWUp's constructor.
Task<T>
is the smallest unit of execution that is defined for our system. A Task has a run() method that choreographs its execution. The actual work is performed by the abstract taskWork() method.
Each Task has a waitForResult() method that must block until the Task has finished. There are comments in the source file to direct your activity.
PartialResult
is the type of answer computed by each Task. A PartialResult can return its value and can also combine its result with another PartialResult.
Job<T>
is the work to be performed on HaWUp. This object is already programed for you. Each Job has an array of Task<T>s and an array of Nodes. The run() method of a Job distributes the tasks on the nodes, which causes them to begin execution. Its waitForAllTasks waits for tasks to complete, combines their results, and returns the result when everything is done.

Your work for this lab
As described in class, using Java's built-in wait() statement can make code look messy because of the checked exception you must expect.
As in class, you have Wrappers.wait(o) and Wrappers.notify(o) which handle the exception for you, leaving the rest of your code looking nicer. There is also Wrappers.sleep(ms), and all of these are found in the hawup.utils package.
1	Update your repository to obtain the code for this lab, which will appear in the labs source folder in a package called hawup. The unit tests and other tests are found in the hawup.testing package.  Different computers can operate at different speeds. To show the benefit of hawup, it is helpful to have an example that takes a known amount of time on one Node. You will next experiment to determine the appropriate repeat factor so that a computation takes between 45 and 60 seconds.  
2	Open and run the TestSumTakesTime unit test. It should fail, unless you are using a computer from the 1950's. But if you were doing that, your computer would be the size of the Urbauer building, and it probably does not have a web browser, so you wouldn't be reading this. Enough said.  Your task here is to modify the repeatFactor value until the test takes between 45 and 60 seconds. The unit test will check for this. Once you have determined the proper value, set the repeatFactor static variable to that value in the SumLoToHi class. 
3	Open and run the TestTimedRunnable. It will fail, and you need to fix it as described in the comments of TimedRunnable's getTime() method, which you will find in the hawup.utils package. Using what you learned in class:
◦	You must Wrappers.wait(this) in the getTime() method until this.end != null.
◦	You must also call Wrappers.notifyAll(this) just after any point in the code where this.end could become non-null.
4	
5	A similar problem exists in HaWUp, so open and run HaWUpTester. It should fail until you make the appropriate fixes in HaWUp as directed by the comments there.
Once you have finished your work here, and your unit tests are passing, demo your work to a TA.
 
Part 2
Overview
The Node class is important to this project, as each node acts to process the Tasks in its queue. The basic code for Node is straightforward and is given to you. The tricky part is to deploy the appropriate concurrency mechanisms so that the Node class performs reliably. Thos mechanisms include:
•	Use of synchronized
•	Use of guarded blocks (wait and notifyAll)
Instructions
•	Update your repository to get the TestNode unit tests in the hawup.testingnode package.
•	Follow the instructions in the Node class.
•	Follow the instructions in the Task class to make it work too.
When it passes the tests
You code may not pass the testRun() unit test, and that's OK.
You should now be able to run the examples that are in your repository. Try the sleeps one first, then try sum.
In the Main class, change the call to HaWUp to try different numbers of Nodes.
What number of nodes works best on your computer in terms of performance?
 
Part 3
Overview
In this installment you will create a visualization for HaWUp. The nature of the visualization is for you to decide, but from it one should be able to discern:
•	How many nodes are present on the HaWUp instance?
•	For each Node, is it busy or idle?
•	How many tasks are waiting in each Node's queue?
The message you need are issued by calls to publish (a wrapper for sending PropertyChangeSupport messages) that you find in the HaWUp.core classes.
Instructions
•	Update your repository to get the latest of any software pushed to you.
•	Look at the hawup.examples subpackages: sleeps, sum, and Hack. The Main class in each of those examples launches both the HaWUPp instance and the HaWUpViz visualizer. That visualizer currently doesn't do anything, but it has both a default constructor (used by WindowBuilder) and a constructor appropriate to an actual HaWUp instance.
To create the visualizer, make note of the messages issued from Nodes. The messages from Tasks may also be of interest. The visualizer shown in class was based on GridLayout, which divides the screen into a fixed number of rows and columns. The nice thing about that layout is that the grid's boxes expand and shrink with the window size.  Review the Video Demo to see that visualization.
 
Part 4
Choose one of the following to implement as an enhancement to your HaWUp. Some videos will be posted explaining how these should work.
•	Idle nodes steal tasks from busy ones
•	Don't wait for tasks to complete in turn. Instead, process them as they finish
Demo box to appear
 


