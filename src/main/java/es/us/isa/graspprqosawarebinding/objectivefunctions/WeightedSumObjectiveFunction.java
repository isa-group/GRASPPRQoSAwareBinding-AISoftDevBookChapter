package es.us.isa.graspprqosawarebinding.objectivefunctions;

import java.util.Map;

import es.us.isa.graspprqosawarebinding.Binding;
import es.us.isa.graspprqosawarebinding.Plan;
import es.us.isa.graspprqosawarebinding.QoSProperty;
import es.us.isa.graspprqosawarebinding.Task;
import es.us.isa.graspprqosawarebinding.utilityfuncions.UtilityFunction;

public class WeightedSumObjectiveFunction implements ObjectiveFunction{

	private Map<QoSProperty,Double> weights;
	private Map<QoSProperty,UtilityFunction<Double>> utilityFunctions;
	
	public WeightedSumObjectiveFunction(Map<QoSProperty, Double> weights,
			Map<QoSProperty, UtilityFunction<Double>> utilityFunctions) {
		super();
		this.weights = weights;
		this.utilityFunctions = utilityFunctions;
	}

	public Double evaluate(Binding b) {
		Double result=0.0;
		Double value;
		UtilityFunction<Double> utility;
		Plan p;
		for(QoSProperty q:weights.keySet()) {
			value=0.0;
			for(Task t:b.getTasks()) {
				p=b.getProvider(t);
				value+=p.getQoSValue(q);
			}
			utility=utilityFunctions.get(q);
			result+=weights.get(q)*utility.getUtility(value);
		}
		return result;
	}
	
	public UtilityFunction<Double> getUtiltyFunction(QoSProperty q){
		return utilityFunctions.get(q);
	}

}
