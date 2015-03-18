package hawup.examples.hack;

import java.math.BigInteger;

import hawup.core.PartialResult;
import hawup.core.Task;
import hawup.util.RepeatRunnable;
import hawup.util.SumOfInts;

/**
 * Try private keys between low and high, looking for one
 *    whose public key matches the one specified
 * @author roncytron
 *
 */
public class LookForKeyBetweenLoHi extends Task<BigInteger> {

	final private BigInteger low;
	final private BigInteger high;
	private BigInteger ans;
	final private String pubkey;

	/**
	 * 
	 * @param pubkey the public key we want to match
	 * @param low the lowest private key we will try, inclusively
	 * @param high the highet private key we will try, inclusively
	 */
	public LookForKeyBetweenLoHi(String pubkey, BigInteger low, BigInteger high) {
		this.low = low;
		this.high = high;
		this.pubkey = pubkey;
	}

	@Override
	public void taskWork() {
		ans = BigInteger.ZERO;
		BigInteger i = low;
		//
		// Keep iterating if i is in the interval [low, high]
		//   until we find a match
		// When we find a match, ans is set to something other than 0.
		//
		while (ans.equals(BigInteger.ZERO) && i.compareTo(high)<= 0) {
			if (KeyManager.sqrAndTakeMiddle(i).equals(pubkey)) {
				//
				// Found match!
				//
				ans = i;
			}
			//
			// i = i + 1
			//
			i = i.add(BigInteger.ONE);
		}
	}


	@Override
	public PartialResult<BigInteger> getTaskPartialResult() {
		return new HackAnswer(ans);
	}

	public String toString() {
		return "Hack from " + low + " to " + high; 
	}

}
