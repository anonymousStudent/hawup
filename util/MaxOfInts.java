package hawup.util;

import hawup.core.PartialResult;

/**
 * A result from maxing integer values
 * @author roncytron
 *
 */
public class MaxOfInts implements PartialResult<Integer>{
	
	private int value;
	
	public MaxOfInts(int value) {
		this.value = value;
	}

	@Override
	public PartialResult<Integer> reduce(PartialResult<Integer> other) {
		return new MaxOfInts(Math.max(this.value,other.getValue()));
	}

	@Override
	public Integer getValue() {
		return this.value;
	}
	
	public String toString() {
		return ""+this.value;
	}

}
