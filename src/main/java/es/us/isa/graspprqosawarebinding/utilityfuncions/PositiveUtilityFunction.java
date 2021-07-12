package es.us.isa.graspprqosawarebinding.utilityfuncions;

public class PositiveUtilityFunction extends BoundedUtilityFunction<Double>{

	
	

	public PositiveUtilityFunction(double min, double max) {
		super(min,max);		
	}



	public Double getUtility(Double value) {
		return (value-min)/(max-min);
	}
	
	public Object clone() throws CloneNotSupportedException
    {
		return new PositiveUtilityFunction(min, max);		
    }

			
}
