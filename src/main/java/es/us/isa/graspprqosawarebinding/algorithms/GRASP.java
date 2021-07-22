package es.us.isa.graspprqosawarebinding.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import es.us.isa.graspprqosawarebinding.Binding;
import es.us.isa.graspprqosawarebinding.Plan;
import es.us.isa.graspprqosawarebinding.QoSProperty;
import es.us.isa.graspprqosawarebinding.QoSProperty.QoSPropertyType;
import es.us.isa.graspprqosawarebinding.Task;
import es.us.isa.graspprqosawarebinding.objectivefunctions.WeightedSumObjectiveFunction;
import es.us.isa.graspprqosawarebinding.problem.QoSAwareBindingProblem;

public class GRASP implements OptimizationAlgorithm {

	int maxIterations;
	int currentIteration;
	Random random;
	double alpha;
	Binding optimalSolution;
	Set<Binding> builtSolutions;
	
	public GRASP(int maxIterations, double alpha,Random random) {
		super();
		this.maxIterations = maxIterations;
		this.alpha=alpha;
		this.random = random;
	}

	public Binding solve(QoSAwareBindingProblem p) {
		Binding result=null;
		currentIteration=0;
		optimalSolution=null;
		builtSolutions=new HashSet<Binding>();
		while(!terminationCriterion()) {
			result=buildSolution(p);
			result=improveSoluton(result,p);
			builtSolutions.add(result);
			currentIteration++;
			if(optimalSolution == null || p.getObjectiveFunction().evaluate(result)>p.getObjectiveFunction().evaluate(optimalSolution))
				optimalSolution=result;
		}
		return result;
	}

	protected Binding buildSolution(QoSAwareBindingProblem p) {
		Binding result=new Binding();
		for(Task t:p.getMarket().keySet()) 
		{
			Set<Plan> validFeatures=identifyValidFeatures(result,p,p.getMarket().get(t));
			Set<Plan> restrictedCandidates=selectRestrictedCandidates(validFeatures,p);
			Plan plan=chooseRandom(restrictedCandidates);
			result.setProvider(t, plan);
		}
		return result;
	}

	private Plan chooseRandom(Set<Plan> restrictedCandidates) {
		int size = restrictedCandidates.size();
		int item = random.nextInt(size); 
		int i = 0;
		for(Plan obj : restrictedCandidates)
		{
		    if (i == item)
		        return obj;
		    i++;
		}
		return null;
	}

	private Set<Plan> selectRestrictedCandidates(Set<Plan> validFeatures, QoSAwareBindingProblem p) {
		double gMin=computeGMin(validFeatures,p);
		double gMax=computeGMax(validFeatures,p);
		double threshold=gMin+ alpha * (gMax-gMin);
		Set<Plan> result=new HashSet<Plan>();
		for(Plan plan:validFeatures) {
			if(greedy(plan,p) >= threshold)
				result.add(plan);
		}
		return result;
	}

	private double computeGMax(Set<Plan> validFeatures, QoSAwareBindingProblem p) {
		double max=Double.MIN_VALUE;
		for(Plan plan:validFeatures)
			if(greedy(plan,p)>max)
				max=greedy(plan,p);
		return max;
	}

	private double computeGMin(Set<Plan> validFeatures,QoSAwareBindingProblem p) {
		double min=Double.MAX_VALUE;
		for(Plan plan:validFeatures)
			if(greedy(plan,p)<min)
				min=greedy(plan,p);
		return min;
	}

	private double greedy(Plan plan,QoSAwareBindingProblem p) {
		double totalUtility=0;
		for(QoSProperty q:p.getQualityProperties()) {
			totalUtility += greedy(plan,q,p);
		}
		return totalUtility;
	}
	
	private double greedy(Plan plan,QoSProperty q,QoSAwareBindingProblem p) {
		double result=0;
		if(p.getObjectiveFunction() instanceof WeightedSumObjectiveFunction) {
			result=((WeightedSumObjectiveFunction)p.getObjectiveFunction()).getUtiltyFunction(q).getUtility(plan.getQoSValue(q));
		}else {
			result= (q.getType() == QoSPropertyType.POSITIVE) ? plan.getQoSValue(q) : -plan.getQoSValue(q);
		}
		return result;
	}

	private Set<Plan> identifyValidFeatures(Binding result, QoSAwareBindingProblem p, Set<Plan> set) {
		return set;
	}

	protected boolean terminationCriterion() {
		return currentIteration>=maxIterations;
	}	
	
	protected Binding improveSoluton(Binding base,QoSAwareBindingProblem p) {
		Binding result=new Binding(base);
		Binding candidate;
		Double optimalValue=p.getObjectiveFunction().evaluate(base);
		Double candidateValue=null;
		for(Task t:p.getMarket().keySet()) {
			for(Plan plan:p.getMarket().get(t)) {
				if(base.getProvider(t)!=plan) {
					candidate=new Binding(base);
					candidate.setProvider(t, plan);
					candidateValue=p.getObjectiveFunction().evaluate(candidate);
					if(candidateValue>optimalValue) {
						optimalValue=candidateValue;
						result=candidate;
					}
				}
			}
		}
		return result;
	}

	
	
}
