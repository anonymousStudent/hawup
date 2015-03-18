package hawup.util;

import hawup.core.PartialResult;

/**
 * A result from summing integer values
 * @author roncytron
 *
 */
public class SumOfInts implements PartialResult<Integer>{
	
	private int value;
	
	public SumOfInts(int value) {
		this.value = value;
	}

	@Override
	public PartialResult<Integer> reduce(PartialResult<Integer> other) {
		return new SumOfInts(this.value + other.getValue());
	}

	@Override
	public Integer getValue() {
		return this.value;
	}
	
	public String toString() {
		return ""+this.value;
	}

}
