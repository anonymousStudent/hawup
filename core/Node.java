package hawup.core;

import hawup.util.Publisher;
import hawup.util.Wrappers;

import java.util.LinkedList;
import java.util.List;

/**
 * A Node executes a queue of Tasks, one after the other.
 * Each Node processes its Task queue independently of the other Nodes.
 * @author roncytron
 *
 */
public class Node extends Publisher implements Runnable {

	final private List<Task<?>> queue;
	final private int maxQueueSize;
	private int id;
	private Task<?> currentTask;

	/**
	 * Construct a Node whose maximum queue occupancy is maxQueueSize
	 * @param id The identifying integer for this Node
	 * @param maxQueueSize The maximum number of Tasks that can be waiting to execute on this Node
	 */
	public Node(int id, int maxQueueSize) {
		this.queue = new LinkedList<Task<?>>();
		this.maxQueueSize  = maxQueueSize;
		this.id = id;
	}

	/**
	 * How many Tasks are waiting to run?  None of these will have
	 * been started yet.
	 * @return the number of tasks waiting to run
	 */
	public int getNumWaiting() {
		//
		// We want to be sure a thread executing this method
		//   enjoys exclusive access to the queue
		// Otherwise, while looking up the queue size, 
		//   another thread may be putting something on the queue
		//   or taking something off of it
		// You must fix this
		//
		while (this.getRunningTask().getPartialResult()==null)
			Wrappers.wait(queue);		
		return this.queue.size();
	}

	/**
	 * Is this Node busy?   A Node is busy if it is executing
	 *   some current task or it has Tasks waiting in its queue.
	 * @return whether this Node is busy
	 */
	public boolean isBusy() {
		//
		// Insidious race, see the comments in findAndRemoveTask()
		// A node is busy if its currentTask is not null, or
		//    if it has something in its queue
		//
		return currentTask != null || this.getNumWaiting() > 0;
	}

	/**
	 * What is the currently running Task?
	 * @return the currently running Task, or null if there is none
	 */
	public Task<?> getRunningTask() {
		return currentTask;
	}

	/**
	 * Add the specified Task to this Node's queue, if there is room
	 * Otherwise the caller waits until the Node's queue has space for
	 * the specified Task.
	 * 
	 * FIXME:  You must wait until the queue size is such that
	 *    it can accommodate the specified task
	 *    
	 * @param task
	 */
	public void addTask(Task<?> task) {
		this.queue.add(task);
		publish("taskadded", task);
	}

	/**
	 * This method should wait until there is something on the queue
	 *   so that queue.remove(0) can actually remove something
	 * @return
	 */
	private Task<?> findAndRemoveTask() {
		Task<?> ans = queue.remove(0);
		//
		// Insidious race at this point
		//   The queue could now be empty
		//   But the Node has not yet started running the returned
		//      Task, because this method has not yet returned.
		//   In this small window, it could look like this Node is not
		//      busy!   See isBusy()
		//   You must eliminate that race to receive
		//      full credit for this lab
		//
		//   Leave the sleep in so you can see this problem
		//
		Wrappers.sleep(200);
		return ans;
	}

	/**
	 * It is the life of a Node to indefinitely
	 *    1) Find and remove a Task from its queue
	 *    2) Run that Task
	 * This must be done in such a way that if the Task
	 * fails for some reason, the Node does not die but continues
	 * to service its queue
	 */
	public void run() {
		while (true) {
			publish("idle");

			//
			// We want the following statement to appear to
			//   execute atomically.  See comments under
			//   findAndRemoveTask() and isBusy()
			// As is, it does not execute atomically
			//
			currentTask = findAndRemoveTask();

			//
			// We do not want any locks held at this point
			// Otherwise, while the task is running on this Node,
			//   no other threads could make queue requests on this
			//   Node
			//
			try {
				publish("taskstarted", currentTask);
				publish("busy");
				currentTask.run();
			}
			finally {
				publish("taskended", currentTask);
				//
				// Is the following a problem in terms of atomicity
				//   of determining whether this node is busy?
				//
				currentTask = null;
				Wrappers.sleep(500);
			}
		}

	}

	public String toString() {
		return "Node " + id;
	}

}
