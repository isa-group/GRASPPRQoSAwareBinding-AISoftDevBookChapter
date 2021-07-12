package es.us.isa.graspprqosawarebinding.utilityfuncions;

public class NegativeUtilityFunction extends BoundedUtilityFunction<Double> {
	
	
	
	public NegativeUtilityFunction(double min, double max) {
		super(min,max);		
	}



	public Double getUtility(Double value) {
			return 1.0 - ((value-min)/(max-min));
	}
	
	public Object clone() throws CloneNotSupportedException
    {
		return new NegativeUtilityFunction(min, max);		
    }

}
