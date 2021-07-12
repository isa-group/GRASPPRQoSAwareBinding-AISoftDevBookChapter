package es.us.isa.graspprqosawarebinding.algorithms;

import java.util.Map;
import java.util.Random;

import es.us.isa.graspprqosawarebinding.Binding;
import es.us.isa.graspprqosawarebinding.QoSProperty;
import es.us.isa.graspprqosawarebinding.problem.QoSAwareBindingProblem;

public class GRASPwithPathRelinking extends GRASP {

	int maxPRIterations;
	PathRelinking pr;
	public GRASPwithPathRelinking(int maxGRASPIterations, double alpha, int maxNeighboursExplored, int maxPRIterations, Map<QoSProperty, Double> globalMin,
			Map<QoSProperty, Double> globalMax, Random random) {
		super(maxGRASPIterations, alpha, random);		
		pr=new PathRelinking(null,maxNeighboursExplored, globalMin, globalMax, random);
	}

	public Binding solve(QoSAwareBindingProblem p) {
		super.solve(p);
		Binding prOptimal=null;
		pr.setEliteSolutions(builtSolutions);
		for(int i=0;i<maxPRIterations;i++) {
			prOptimal=pr.solve(p);
			if(p.getObjectiveFunction().evaluate(prOptimal)>p.getObjectiveFunction().evaluate(optimalSolution))
				this.optimalSolution=prOptimal;
		}
		return optimalSolution;
	}
	

}
