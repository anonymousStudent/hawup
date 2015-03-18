package hawup.core;

import java.time.Duration;
import java.time.Instant;

import hawup.util.Publisher;
import hawup.util.Wrappers;

/**
 * Captures the common parts of all Tasks:
 *   1) Sending PCS messages about activity
 *   2) Waiting for a result to be computed
 * @author roncytron
 *
 * @param <T>
 */
abstract public class Task<T> extends Publisher implements Runnable {

	private boolean complete;

	abstract public void taskWork();
	abstract public PartialResult<T> getTaskPartialResult();

	public Task() {
		this.complete = false;
	}

	/**
	 * Runs the supplied taskWork() method using a
	 *   before...after pattern.
	 *   before:  create a timestamp, publish task starts
	 *   after:   indicate completion, publish task completes and results
	 */
	final public void run() {
		Instant startTime = Instant.now();
		publish("starts", startTime);
		try {
			taskWork();
		} catch(Throwable t) {
			publish("error", t);
		} finally {

			this.complete = true;
			publish("completes", Duration.between(startTime, Instant.now()));
			publish("result", getPartialResult());

		}
	}

	/**
	 * Nothing for you to change here.  This method is OK, and can
	 *    return null if the Task is not yet complete
	 *    
	 * Returns the supplied result if taskWork() has finished, otherwise null
	 * @return the supplied result of the taskWork() computation
	 */
	final public PartialResult<T> getPartialResult() {
		if (!complete)
			return null;
		else
			return getTaskPartialResult();
	}

	/**
	 * The caller of this method blocks until the result of this
	 * task is ready.
	 * @return the result of this task when it is available
	 */
	final public PartialResult<T> waitForResult() {
		//
		// You must wait until the computation is complete
		// Fix this
		//
		return getPartialResult();
	}

}
