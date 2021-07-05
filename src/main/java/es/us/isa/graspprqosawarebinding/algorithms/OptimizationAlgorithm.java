package es.us.isa.graspprqosawarebinding.algorithms;

import es.us.isa.graspprqosawarebinding.Binding;
import es.us.isa.graspprqosawarebinding.problem.QoSAwareBindingProblem;

public interface OptimizationAlgorithm {
	public Binding solve(QoSAwareBindingProblem p);
}
