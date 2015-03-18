package hawup.examples.hack;

import hawup.core.PartialResult;

import java.math.BigInteger;

public class HackAnswer implements PartialResult<BigInteger> {

	private BigInteger ans;

	public HackAnswer(BigInteger ans) {
		this.ans = ans;
	}
	
	/**
	 * 0 indicates that there is no match yet for the private key
	 * Thus, to reduce, we take either answer that is non-zero
	 */
	@Override
	public PartialResult<BigInteger> reduce(PartialResult<BigInteger> other) {
		if (!this.getValue().equals(BigInteger.ZERO))
			return this;
		if (!other.getValue().equals(BigInteger.ZERO))
			return other;
		return this;
	}

	@Override
	public BigInteger getValue() {
		return ans;
	}
	
	public String toString() {
		return "" + ans;
	}

}
