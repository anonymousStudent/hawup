package hawup.util;

public class RepeatRunnable implements Runnable {

	private Runnable runnable;
	private long factor;

	/**
	 * Takes time proportional the specified factor.
	 * It does this by running the Runnable r repeatedly, factor
	 *    number of times.
	 * @param factor the factor by which the computation should be repeated
	 * @param r the computation to be repeated
	 */
	public RepeatRunnable(long factor, Runnable r) {
		this.runnable = r;
		this.factor = factor;
	}

	public void run() {
		for (long i=0; i < factor; ++i) {
			runnable.run();
		}
	}

}
