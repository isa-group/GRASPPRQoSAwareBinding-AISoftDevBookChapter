package es.us.isa.graspprqosawarebinding.objectivefunctions;

import java.util.List;

import javax.swing.SpringLayout.Constraints;

import es.us.isa.graspprqosawarebinding.Binding;
import es.us.isa.graspprqosawarebinding.problem.Constraint;
import es.us.isa.graspprqosawarebinding.problem.QoSAwareBindingProblem;

public class ConstrainedObjectiveFunction implements ObjectiveFunction {
	Double contraintsWeight;
	ObjectiveFunction of;
	QoSAwareBindingProblem problem;
	
	public ConstrainedObjectiveFunction(Double contraintsWeight, ObjectiveFunction of, QoSAwareBindingProblem problem) {
		super();
		this.contraintsWeight = contraintsWeight;
		this.of = of;
		this.problem = problem;
	}

	public Double evaluate(Binding g) {
		return of.evaluate(g)-contraintsWeight*meetingDistance(g);
	}

	private Double meetingDistance(Binding g) {
		List<Constraint> constraints=problem.getConstraints();
		if(constraints.isEmpty())
			return 0.0;
		Double value=0.0;
		for(Constraint c:constraints) {
			value+=c.meetingDistance(g);
		}
		value=value/(double)constraints.size();
		return value;
	}
	
}
