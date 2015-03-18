package hawup.examples.hack;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import hawup.core.Task;
import hawup.core.TasksProvider;

/**
 * See KeyManager for an example of the one-way function
 * @author roncytron
 *
 */
public class Hack implements TasksProvider<BigInteger>{
	
	private String pubkey;
	private BigInteger upperbound;

	/**
	 * Search for a private key whose corresponding public
	 *    key matches the specified pubkey
	 * @param pubkey the public key
	 * @param upperbound the largest integer we should try as the private key
	 */
	public Hack(String pubkey, BigInteger upperbound) {
		this.pubkey = pubkey;
		this.upperbound = upperbound;
	}

	@Override
	public Set<Task<BigInteger>> getTasks(int numNodes) {
		int numTasks = numNodes * 16;
		Set<Task<BigInteger>> tasks = new HashSet<Task<BigInteger>>();
		BigInteger numsPerTask = upperbound.divide(BigInteger.valueOf(numTasks));
		BigInteger priv = BigInteger.ZERO;
		while (priv.compareTo(upperbound) < 0) {
			tasks.add(new LookForKeyBetweenLoHi(pubkey, priv, priv.add(numsPerTask)));
			priv = priv.add(numsPerTask);
		}
		return tasks;
	}

}
