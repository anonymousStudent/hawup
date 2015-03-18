package hawup.core;

/**
 * Nothing for you to change here
 * 
 * A characterization of the kinds of partial results that can be
 * returned by a Task when its taskWork() is complete.
 * @author roncytron
 *
 * @param <T>
 */
public interface PartialResult<T> {
	
	/**
	 * Combine this PartialResult with the supplied other PartialResult.
	 * This must be an associative operation, because the order in
	 *   which these partial results are produced or combined is not specified.
	 * @param other
	 * @return the reduction of this PartialResult with the other PartialResult
	 */
	public PartialResult<T> reduce(PartialResult<T> other);
	public T getValue();

}
