package es.us.isa.graspprqosawarebinding.utilityfuncions;

public abstract class BoundedUtilityFunction<T> implements UtilityFunction<T> {

	T max;
	T min;
	
	public BoundedUtilityFunction(T min,T max){
		this.min=min;
		this.max=max;
	}
	
	public T getMin() {
		return min;
	}
	
	public void setMin(T min) {
		this.min = min;
	}
	
	public T getMax() {
		return max;
	}
	
	public void setMax(T max) {
		this.max = max;
	}
	
	
	public abstract Object clone() throws CloneNotSupportedException;
}
