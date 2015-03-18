package hawup.examples.sum;

import hawup.core.Task;
import hawup.core.TasksProvider;

import java.util.HashSet;
import java.util.Set;

public class SumFrom1ToN implements TasksProvider<Integer> {
	
	private int N;

	public SumFrom1ToN(int N) {
		this.N = N;
	}

	@Override
	public Set<Task<Integer>> getTasks(int numNodes) {
		int numTasks = numNodes*4;
		int numsPerTask = this.N / numTasks;
		Set<Task<Integer>> ans = new HashSet<Task<Integer>>();
		int lo = 0;
		for (int i=0; i < numTasks; ++i) {
			int hi = Math.min(lo+numsPerTask,this.N);
			Task<Integer> t = new SumLoToHi(lo, hi);
			ans.add(t);
			lo = hi + 1;
		}
		
		return ans;
	}
	
	public String toString() {
		return "Sum from 1 to " + this.N;
	}

}
