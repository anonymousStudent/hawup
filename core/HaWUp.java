package hawup.core;

import java.util.HashSet;
import java.util.Set;

import hawup.util.Wrappers;
import hawup.viz.HaWUpViz;

/** @author Meagan Provencher,
 * Lab Section C
 * mprovencher@wustl.edu
 * CSE 132 Lab 3a
 * HaWUP.java
 * 
 */

public class HaWUp {

	final private Node[] nodes;

	/**
	 * Nothing for you to change in this constructor, but
	 *   there is work for you to do in a method below.
	 * 
	 * 1) Create an array of Nodes
	 * 2) Start each of those Nodes in its own thread  
	 * 3) Optionally, start a visualization.
	 * @param numNodes the number of Nodes for this HaWUp
	 * @param maxQueueSize the maximum number of Tasks queued at each Node
	 * @param viz should the visualization be launched?
	 */
	public HaWUp(int numNodes, int maxQueueSize, boolean viz) {
		this.nodes = new Node[numNodes];          // (1)
		for (int i=0; i < numNodes; ++i) {
			nodes[i] = new Node(i, maxQueueSize); // (1)
			new Thread(nodes[i]).start();         // (2)
		}
		if (viz) {                                // (3)
			HaWUpViz hv = new HaWUpViz(nodes, maxQueueSize);
			hv.setVisible(true);
		}
	}

	/**
	 * Nothing to change here, keep looking below
	 * Constructor that launches the viz by default
	 */
	public HaWUp(int numNodes, int maxQueueSize) {
		this(numNodes, maxQueueSize, true);
	}

	/**
	 * You must modify this method as described below.  Read this
	 * entire description before starting on your modifications!
	 * 
	 * Generates code to run a set of Tasks on the cluster.  
	 * The Tasks are placed and run on the cluster when the
	 *    returned Runnable's .run() is called.
	 * The software design name for the returned object is a "future"
	 * It is a computation that can be invoked later to accomplish
	 * its goals.
	 * 
	 * This code is given to you BUT our analysts have uncovered an
	 * insidious race that could cause this code to fail.  
	 * 
	 * Suppose somebody
	 *   1) Calls genJob and receives the resulting ResultRunnable
	 *   2) Then calls getResult() BEFORE the run() method was able
	 *         to set the job instance variable (*)
	 *   The result is a NullPointerException issued from getResult()
	 *     because job is null
	 *   
	 *   To fix this, you must use what you have learned in class so
	 *   that getResult() waits until the instance variable has been set.
	 *   The HaWUpTester tests for this!
	 *   ==============================================================
	 *   
	 *   (*) This can happen in one of two ways:
	 *     a)  getResult() is called but the run() method has not been called
	 *     b)  run() is called, but has not yet reached the assignment
	 *         to the instance variable.  There is a narrow window in
	 *         which getResult() could be called after run(), but the instance
	 *         variable has not been assigned.  To make that window more likely,
	 *         there is a sleep() call.
	 * 
	 * @param tp provides the tasks for the job
	 * @return a Runnable that when .run() actually enqueues the Tasks
	 */
	public ResultRunnable genJob(TasksProvider<?> tp) {
		return new ResultRunnable() {
			private Job<?> job = null;

			public void run() {
				//
				// Do not remove the sleep!  It opens the window wider for
				//    unacceptable behavior
				//
				Wrappers.sleep(200);

				job = Job.genJob(tp, HaWUp.this);
				job.run();
				
				Wrappers.notifyAll(this);

			}

			@Override
			public PartialResult<?> getResult() {
				//
				// Must wait until job is not null
				//
				while (job == null)
					Wrappers.wait(this);
				return job.waitForAllTasks();
			}
		};
	}

	/**
	 * Nothing for you to change here
	 * Accessor, returns the Nodes as a Set.
	 * For security reasons, we do not return the array directly.
	 * Do you see why?
	 * @return the set of Nodes
	 */
	public Set<Node> getCluster() {
		Set<Node> ans = new HashSet<Node>();
		for (Node n : this.nodes) {
			ans.add(n);
		}
		return ans;
	}

	/**
	 * Nothing for you to change here
	 * 
	 * @return the number of Nodes in this HaWUp
	 */
	public int getNumNodes() {
		return this.nodes.length;
	}

}
