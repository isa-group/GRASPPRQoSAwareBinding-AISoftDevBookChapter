package es.us.isa.graspprqosawarebinding.algorithms;

import java.util.Random;

import es.us.isa.graspprqosawarebinding.Binding;
import es.us.isa.graspprqosawarebinding.problem.QoSAwareBindingProblem;

public class GRASPwithPathRelinking extends GRASP {

	int maxPRIterations;
	PathRelinking pr;
	public GRASPwithPathRelinking(int maxGRASPIterations, double alpha, Random random, int maxNeighboursExplored, int maxPRIterations) {
		super(maxGRASPIterations, alpha, random);		
		pr=new PathRelinking(null,maxNeighboursExplored,random);
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
