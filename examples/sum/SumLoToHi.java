package hawup.examples.sum;

import hawup.core.PartialResult;
import hawup.core.Task;
import hawup.util.RepeatRunnable;
import hawup.util.SumOfInts;

/**
 * Manage the inclusive sum of integers from low to high
 * @author roncytron
 *
 */
public class SumLoToHi extends Task<Integer> {

	//
	// To illustrate how this lab works, we need this computation
	//   to slow down by a specified factor
	// Change the value below as needed and as directed by
	//   the lab's instructions
	// Be sure to add the L at the end of the value, so that it is
	//   represented as a long data type
	//
	public static long repeatFactor = 16000000L;
	
	final private int low;    // start at low
	final private int high;   // end at high, inclusively
	private int ans;

	public SumLoToHi(int low, int high) {
		this.low = low;
		this.high = high;
		this.ans = 0;
	}

	@Override
	public void taskWork() {
		//
		// Capture the work in a Runnable
		//   so we can repeat it some number of times, below
		//
		Runnable mypart = new Runnable() {
			public void run() {
				ans = 0;
				for (int i=low; i <= high; ++i) {
					ans = ans + i;
				}				
			}
		};
		//
		// Doing this sum is too fast on almost all computers
		// So let's do it some number of times to slow things down
		// We want the computer to be busy working on this problem,
		//   not sleeping
		//
		//  16,000,000 may or may not be the right number of times
		//    to repeat this computation on your computer to
		//    take the proper amount of time
		//
		//  Experiment and find a repeat count that takes about
		//    one minute (between 45 and 60 seconds)
		//    on your computer
		//
		new RepeatRunnable(repeatFactor, mypart).run();
	}

	@Override
	public PartialResult<Integer> getTaskPartialResult() {
		return new SumOfInts(ans);
	}

	public String toString() {
		return "Task sum from " + low + " to " + high; 
	}

}
