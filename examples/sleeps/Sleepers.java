package hawup.examples.sleeps;

import hawup.core.Task;
import hawup.core.TasksProvider;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Sleepers implements TasksProvider<Integer> {
	
	final private int maxSleep;
	public Sleepers(int maxSleep) {
		this.maxSleep = maxSleep;
	}

	@Override
	public Set<Task<Integer>> getTasks(int numNodes) {
		int numTasks = numNodes*4;
		Random r = new Random();
		Set<Task<Integer>> ans = new HashSet<Task<Integer>>();
		for (int i=0; i < numTasks; ++i) {
			ans.add(new Sleeper(r.nextInt(maxSleep)));
		}
		return ans;
	}
	
	public String toString() {
		return "Sleepers with max sleep of " + maxSleep +"ms";
	}

}
