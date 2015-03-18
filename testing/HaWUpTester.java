package hawup.testing;

import static org.junit.Assert.*;
import hawup.core.HaWUp;
import hawup.core.ResultRunnable;
import hawup.examples.sleeps.Sleeper;
import hawup.examples.sleeps.Sleepers;
import hawup.util.Wrappers;

import org.junit.Test;

public class HaWUpTester {

	@Test
	public void callsGetResultBeforeRun() {
		final HaWUp h = new HaWUp(0, 2, false);
		final ResultRunnable r = h.genJob(new Sleepers(20));
		new Thread() {
			public void run() {
				Wrappers.sleep(1000);
				r.run();
			}
		}.start();
		try {
			r.getResult();
		}
		catch (NullPointerException npe) {
			fail("getResult() returned too soon, before the instance variable was set");
		}
	}
	@Test
	public void callsRunBeforeGetResult() {
		final HaWUp h = new HaWUp(0 ,2, false);
		final ResultRunnable r = h.genJob(new Sleepers(20));
		new Thread(r).start(); 
		try {
			r.getResult();
		}
		catch (NullPointerException npe) {
			fail("getResult() returned too soon, before the instance variable was set");
		}
	}
	
}
