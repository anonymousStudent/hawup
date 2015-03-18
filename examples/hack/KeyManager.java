package hawup.examples.hack;

import java.math.BigInteger;
import java.util.Random;

/**
 * A public / private key scheme that operates as follows:
 *   The private key is some integer priv
 *   The public key is the middle digits of priv*priv
 *   KeyManager.length specifies how many digits are taken
 *     from the middle.
 *   Example, with length 5
 *   If the private key priv is 132132132
 *     then priv*priv = 17458900306865424
 *     and the middle 5 characters from that string are:
 *     00306, which would be the public key for 132132132
 *     Notice that the leading 0s are required, so we use
 *     a String data type for the pubkey, not an int
 * It is easy to go from a private key to a public key:
 *    that logic is embodied by sqrAndTakeMiddle
 * But determining the private key that corresponds to a given
 *    public key is not straightforward.
 */

public class KeyManager {
	
	public final static int length = 7;
	
	/**
	 * The public method always relies on the final length specified above
	 * @param priv the private key
	 * @return the public key, as a String, that corresponds to the specified private key
	 */
	public static String sqrAndTakeMiddle(BigInteger priv) {
		return sqrAndTakeMiddle(priv, length);
	}
	
	/**
	 * For internal testing we allow length to be specified
	 * @param priv the private key
	 * @param length the number of characters taken from the middle of priv*priv
	 * @return the public key, as a String
	 */
	private static String sqrAndTakeMiddle(BigInteger priv, int length) {
		BigInteger sqr = priv.multiply(priv);
		String s = sqr.toString();
		int start = s.length()/2-length/2;
		String ans = s.substring(start, start+length);
		return ans;		
	}
	
	public static void main(String[] args) {
		BigInteger t = new BigInteger(34, new Random());
		System.out.println(sqrAndTakeMiddle(t, 7));
		System.out.println(sqrAndTakeMiddle(BigInteger.valueOf(132132132),5));
	}

}
