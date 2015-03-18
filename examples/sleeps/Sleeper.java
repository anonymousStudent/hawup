package hawup.examples.sleeps;

import hawup.core.PartialResult;
import hawup.core.Task;
import hawup.util.RepeatRunnable;
import hawup.util.SumOfInts;
import hawup.util.Wrappers;

/**
 * Manage the inclusive sume of integers from low to high
 * @author roncytron
 *
 */
public class Sleeper extends Task<Integer> {

	final private int ms; 

	public Sleeper(int ms) {
		this.ms = ms;
	}

	@Override
	public void taskWork() {
		Wrappers.sleep(ms);				
	}

	@Override
	public PartialResult<Integer> getTaskPartialResult() {
		return new PartialResult<Integer>() {

			@Override
			public PartialResult<Integer> reduce(PartialResult<Integer> other) {
				return this;
			}

			@Override
			public Integer getValue() {
				return new Integer(132);
			}
			
			public String toString() {
				return "Rested after " + ms + "ms of sleep";
			}

		};
	}

	public String toString() {
		return "Task sleeper for " + ms + " milliseconds"; 
	}

}
