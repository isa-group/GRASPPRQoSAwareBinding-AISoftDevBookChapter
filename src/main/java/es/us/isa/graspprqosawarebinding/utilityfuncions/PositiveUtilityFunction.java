package es.us.isa.graspprqosawarebinding.utilityfuncions;

public class PositiveUtilityFunction implements UtilityFunction<Double>{

	double min;
	double max;

	

	public PositiveUtilityFunction(double min, double max) {
		super();
		this.min = min;
		this.max = max;
	}



	public Double getUtility(Double value) {
		return (value-min)/(max-min);
	}

	
}
