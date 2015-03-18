package hawup.testing;

import static org.junit.Assert.*;

import java.time.Duration;

import hawup.examples.sum.SumFrom1ToN;
import hawup.examples.sum.SumLoToHi;
import hawup.util.TimedRunnable;

import org.junit.Test;

public class TestSumTakesTime {

	@Test
	public void test() {
		SumLoToHi summer = new SumLoToHi(1,10000);
		//
		// change the repeatFactor below so that
		//   the computation takes between 45 and 60 seconds
		//
		SumLoToHi.repeatFactor = 6000L;
		Duration time = timeSum(summer);
		
		System.out.println("Time is " + time);
		
		assertTrue("Needs to take at least 45 seconds, but took only " + time + " seconds",time.compareTo(Duration.ofSeconds(45)) >= 0);
		assertTrue("Needs to take at most 60 seconds, but took " + time + " seconds",time.compareTo(Duration.ofSeconds(60)) <= 0);
	}
	
	private Duration timeSum(final SumLoToHi summer) {
		TimedRunnable tr = new TimedRunnable(
				new Runnable() {
					public void run() {
						summer.taskWork();
					}
				}
				);
		tr.run();
		return tr.getTime();
	}

}
