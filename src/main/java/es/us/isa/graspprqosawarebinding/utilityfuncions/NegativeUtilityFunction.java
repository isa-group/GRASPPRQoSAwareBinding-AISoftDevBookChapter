package es.us.isa.graspprqosawarebinding.utilityfuncions;

public class NegativeUtilityFunction implements UtilityFunction<Double> {

	private double min;
	private double max;
	
	
	
	public NegativeUtilityFunction(double min, double max) {
		super();
		this.min = min;
		this.max = max;
	}



	public Double getUtility(Double value) {
			return 1.0 - ((value-min)/(max-min));
	}

}
