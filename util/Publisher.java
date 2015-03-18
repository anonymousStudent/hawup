package hawup.util;

import java.beans.PropertyChangeSupport;

/**
 * Classes that extend this class get a PCS
 *    object instantiated for them, and 
 *    have handy publish methods to push PCS messages out
 * Also, by setting LOGGING to true, all messages are also written
 *    to the console
 *    
 * @author roncytron
 *
 */
public class Publisher {
	
	private PropertyChangeSupport pcs;
	public static boolean LOGGING = false;

	public Publisher() {
		this.pcs = new PropertyChangeSupport(this);
	}
	
	public PropertyChangeSupport getPCS() {
		return this.pcs;
	}
	
	public void publish(String message) {
		publish(message, null, null);
	}
	
	public void publish(String message, Object newValue) {
		publish(message, null, newValue);
	}
	
	public void publish(String message, Object oldValue, Object newValue) {
		if (LOGGING) {
			System.out.println(
					blanks(stackDepth()) 
					+ this 
					+ " sends message \"" 
					+ message 
					+ "\" "
					+ (newValue != null ? (newValue) : "")
					+ (oldValue != null ? (" (was " + oldValue+")") : "")
					);
		}
		pcs.firePropertyChange(message, oldValue, newValue);
	}
	
	/**
	 * Return this Thread's current stack depth, but take away 1
	 *    for the stackDepth method itself.
	 * This is used for indenting output to show the dynamic call
	 *    structure of the application
	 * @return the current Thread's stack depth
	 */
	private static int stackDepth() {
		return Thread.currentThread().getStackTrace().length - 1;
	}
	
	/**
	 * 
	 * @param num
	 * @return num blanks
	 */
	private String blanks(int num) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < num; ++i) {
			sb.append(" ");
		}
		return sb.toString();
	}

}
