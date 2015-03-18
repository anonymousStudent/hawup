package hawup.examples.sum;

import hawup.core.HaWUp;
import hawup.core.PartialResult;
import hawup.core.ResultRunnable;
import hawup.util.Publisher;
import hawup.util.TimedRunnable;


public class Main {

	public static void main(String[] args) {
		HaWUp h = new HaWUp(8,5);
		Publisher.LOGGING = true;
		ResultRunnable job = h.genJob(new SumFrom1ToN(10000));
		TimedRunnable tr = new TimedRunnable(
				new Runnable() {
					public void run() {
						//
						// Following line will return almost
						//   immediately.  It places the work
						//   on the Nodes' queues
						//
						job.run();
						//
						// Following line will block until
						//   the result is ready
						//
						PartialResult<?> result = job.getResult();
						System.out.println("Returned answer is " + result.getValue());
					}
				}
				);
		//
		// Time the entire process of putting work on the queues
		//   and waiting for the answer
		//
		tr.run();
		System.out.println("Time taken is " + tr.getTime());

	}

}
