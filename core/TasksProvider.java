package hawup.core;

import java.util.Set;

/**
 * Nothing for you to change here
 * @author roncytron
 *
 * @param <T>
 */
public interface TasksProvider<T> {
	
	/**
	 * Generates a set of tasks for the specified number of nodes.
	 * The actual number of tasks can be less than, equal to, or greater than
	 *     the number of nodes.
	 * @param numNodes
	 * @return a Set of Tasks
	 */
	public Set<Task<T>> getTasks(int numNodes);

}
