package hawup.testingnode;

import static org.junit.Assert.*;
import hawup.core.Node;
import hawup.core.PartialResult;
import hawup.core.Task;
import hawup.util.Wrappers;

import java.util.Random;

import org.junit.Test;

public class TestNode {

	private Random r = new Random();

	@Test
	public void testEnqueuesProperly() {
		int maxQueueSize = r.nextInt(100)+1;
		genNode(maxQueueSize);

	}

	private Node genNode(int maxQueueSize) {
		Node n = new Node(0, maxQueueSize);
		assertFalse("Node is idle, but isBusy() returned true", n.isBusy());
		for (int i=0; i < maxQueueSize; ++i) {
			assertEquals(i, n.getNumWaiting());
			n.addTask(genIncrTask(0));
			assertTrue("Queued work, but isBusy() retrurned false", n.isBusy());
		}
		assertEquals(maxQueueSize, n.getNumWaiting());
		return n;
	}

	private volatile int blockTestVar = 0;
	@Test
	public void testBlocksWhenFull() {
		int maxQueueSize = r.nextInt(100)+1;
		final Node n = genNode(maxQueueSize);
		this.blockTestVar = 0;
		//
		// This Thread should block forever
		//
		new Thread() {
			public void run() {
				blockTestVar = blockTestVar + 1;
				n.addTask(genIncrTask(0));
				blockTestVar = blockTestVar + 1;
			}
		}.start();
		Wrappers.sleep(1000);
		assertEquals("You did not block when queue is full", maxQueueSize, n.getNumWaiting());
		assertEquals("You did not block when queue was full",
				1, blockTestVar);
	}

	@Test
	public void testRun() {
		Node n = new Node(0, 1);
		final int val = r.nextInt(100);
		Task<Integer> t = genIncrTask(val);
		n.addTask(t);
		new Thread(n).start();
		//
		// Following should block until the value is ready
		//
		assertEquals(new Integer(val+1),t.waitForResult().getValue());
		assertFalse("You are done but still think you are busy", n.isBusy());
		assertEquals("Queue should be empty", 0, n.getNumWaiting());
	}

	@Test
	public void testIsBusyRace() {
		Node n = new Node(0,1);
		new Thread(n).start();
		Wrappers.sleep(1000);
		Task<Integer> sleeper = genIncrTask(0);
		n.addTask(sleeper);
		Wrappers.sleep(25);
		boolean b = n.isBusy();
		assertTrue(
				"You should be busy.\n"
						+ "Check for atomicity of assignment to currentTask\n"
						+"in the run() method of Node",b);

	}

	private Task<Integer> genIncrTask(final int v) {
		return new Task<Integer>() {

			private int val = v;

			@Override
			public void taskWork() {
				Wrappers.sleep(1000);
				val = val + 1;
			}

			@Override
			public PartialResult<Integer> getTaskPartialResult() {
				return new PartialResult<Integer>() {

					@Override
					public PartialResult<Integer> reduce(
							PartialResult<Integer> other) {
						return this;
					}

					@Override
					public Integer getValue() {
						return new Integer(val);
					}

				};
			}

		};
	}

}
