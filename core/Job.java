package hawup.core;

import hawup.util.Publisher;

import java.util.Set;

/**
 * Nothing for you to change here
 * 
 * A Job is the primary class for representing a computation
 *   (Set of Tasks) that should execute on a cluster of Nodes.
 *   
 * This code is given to you and you don't need to change anything
 *   here.
 * @author roncytron
 *
 * @param <T>
 */
public class Job<T> extends Publisher implements Runnable {

	final private Task<T>[] tasks;
	final private Node[]    cluster;

	/**
	 * Capture the Set of Tasks in an array so that
	 * the iterator is not kept "live" throughout the
	 * any computations
	 * 
	 * @param tasks
	 * @param h
	 */
	private Job(Set<Task<T>> tasks, HaWUp h) {
		this.tasks   = tasks.toArray(new Task[0]);
		this.cluster = h.getCluster().toArray(new Node[0]);
	}
	
	/**
	 * Generate a job that, when run, will put the provided tasks onto
	 *   the provided HaWUp instance.
	 * @param tp provides the Tasks to be run
	 * @param h the instance of HaWUp that will host the Tasks' execution
	 * @return a Job that when .run() will start the Tasks' execution
	 */
	public static<T> Job<T> genJob(TasksProvider<T> tp, HaWUp h) {
		return new Job<T>(tp.getTasks(h.getNumNodes()), h);
	}
	
	/**
	 * Get the Tasks for this Job and send them to the Nodes
	 *    for execution.
	 */
	public void run() {
		publish("startsetup");
		publish("tasks", this.tasks);
		//
		// Spread the Tasks across the Nodes, round-robin
		//
		int node=0;
		//
		// Make a local copy of the Tasks so we don't keep
		//   the iterator live throughout.  The call to addTask can
		//   block, if the associated Node's queue is full
		//
		for (Task<T> t : tasks) {
			publish("assigntask", t);
			//
			// Following call may block if node's queue is full
			//
			cluster[node].addTask(t);
			node = (node + 1) % cluster.length;
		}
		publish("endsetup");
	}

	/**
	 * Compute the complete result by reducing all of the
	 *   PartialResults from the Tasks.
	 * @return
	 */
	public PartialResult<T> waitForAllTasks() {
		PartialResult<T> ans = null;


		for (Task<T> task : tasks) {
			if (ans == null) {
				ans = task.waitForResult();
				publish("firstresult", ans);
			}
			else {
				PartialResult<T> oldAns = ans;
				PartialResult<T> next = task.waitForResult();
				publish("nextresult", next);
				ans = ans.reduce(next);
				publish("reducedresult", oldAns, ans);
			}
		}
		publish("finalresult", ans);
		return ans;
	}

	public String toString() {
		return "Job";
	}

}
