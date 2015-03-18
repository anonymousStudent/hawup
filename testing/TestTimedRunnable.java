package hawup.testing;

import static org.junit.Assert.*;
import hawup.util.TimedRunnable;
import hawup.util.Wrappers;

import java.time.Duration;
import java.util.Random;

import org.junit.Test;

public class TestTimedRunnable {

	@Test
	public void test() {
		int NUMTESTS=10;
		System.out.println("Running " + NUMTESTS + " tests");
		Random r = new Random();
		for (int i=0; i < NUMTESTS; ++i) {
			int ms = r.nextInt(1000);
			TimedRunnable tr = genSleeper(ms);
			//
			// Start the runnable in another Thread
			//
			new Thread(tr).start();
			//
			// When properly implemented, the line below will
			//   block until the Runnable finished (sleeping)
			//
			try {
			Duration seen = tr.getTime();
			//
			// Check that the time is within a factor of 2 of what was expected
			//
			Duration expected = Duration.ofMillis(ms);
			Duration twice    = Duration.ofMillis(2*ms);
			System.out.println("Expected time: " + expected + ", time taken was " + seen);
			assertTrue("Expected at least " + expected + " time but you took " + seen + " time\nYou took too little time", seen.compareTo(expected) >=0);
			assertTrue("Expected " + expected + " time but you took " + seen + " time\nYou took over twice as much time", seen.compareTo(twice) <= 0);
			} catch (NullPointerException npe) {
				fail("Your getTime() method did not wait for this.end to be non-null");
			}
		}
	}
	
	private static TimedRunnable genSleeper(final int ms) {
		TimedRunnable ans = new TimedRunnable(
				new Runnable() {
					public void run() {
						Wrappers.sleep(ms);
					}
				}
				);
		return ans;
	}

}
