package es.us.isa.graspprqosawarebinding.problem;

import es.us.isa.graspprqosawarebinding.Binding;
import es.us.isa.graspprqosawarebinding.QoSProperty;
import es.us.isa.graspprqosawarebinding.Task;

public class SimpleConstraint extends Constraint{
	QoSAwareBindingProblem problem;
	QoSProperty property;
	Double threshold;
	Operator operator;
	
	public static final double MINIMUM_DISTANCE_NOT_MEET=0.01;
	
	
	

	public SimpleConstraint(QoSAwareBindingProblem problem, QoSProperty property, Double threshold, Operator operator) {
		super();
		this.problem = problem;
		this.property = property;
		this.threshold = threshold;
		this.operator = operator;
	}

	@Override
	public Double meetingDistance(Binding b) {
		Double result=0.0;
		Double value=0.0;
		for(Task t:b.getTasks()) {
			value+=b.getProvider(t).getQoSValue(property);
		}
		switch(operator) {
		case GREATER:
			result=Math.max(threshold-value,0.0);
			if(result==0)
				result=MINIMUM_DISTANCE_NOT_MEET;
			break;
		case GREATER_OR_EQUAL:
			result=Math.max(threshold-value,0.0);
			break;
		case EQUAL:
			result=Math.abs(threshold-value);
			break;
		case LOWER_OR_EQUAL:
			result=Math.max(value-threshold, 0);
			break;
		case LOWER:
			result=Math.max(value-threshold, 0);
			if(result==0)
				result=MINIMUM_DISTANCE_NOT_MEET;
			break;
		}
		
		return result;
	}
	
	public enum Operator{GREATER,GREATER_OR_EQUAL,EQUAL,LOWER_OR_EQUAL,LOWER};
}
