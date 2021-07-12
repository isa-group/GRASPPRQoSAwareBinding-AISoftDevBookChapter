package es.us.isa.graspprqosawarebinding.algorithms;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import es.us.isa.graspprqosawarebinding.Binding;
import es.us.isa.graspprqosawarebinding.Plan;
import es.us.isa.graspprqosawarebinding.QoSProperty;
import es.us.isa.graspprqosawarebinding.Task;
import es.us.isa.graspprqosawarebinding.objectivefunctions.ObjectiveFunction;
import es.us.isa.graspprqosawarebinding.objectivefunctions.WeightedSumObjectiveFunction;
import es.us.isa.graspprqosawarebinding.problem.QoSAwareBindingProblem;
import es.us.isa.graspprqosawarebinding.utilityfuncions.BoundedUtilityFunction;
import es.us.isa.graspprqosawarebinding.utilityfuncions.UtilityFunction;

public class PathRelinking implements OptimizationAlgorithm {

	Set<Binding> eliteSolutions;
	int neighboursVisited;
	int maxNeighbours;
	Random random;
	Binding initialSolution;
	Binding guidingSolution;
	ObjectiveFunction func;
	Map<QoSProperty, Double> globalMin;
	Map<QoSProperty, Double> globalMax;

	public PathRelinking(Set<Binding> eliteSolutions, int maxNeighbours,Map<QoSProperty, Double> globalMin,	Map<QoSProperty, Double> globalMax, Random random) {
		this.maxNeighbours = maxNeighbours;
		this.eliteSolutions = eliteSolutions;
		this.random = random;
		this.globalMin = globalMin;
		this.globalMax = globalMax; 
	}
	
	public Binding getGuidingSolution() {
		return guidingSolution;
	}
	
	public void setGuidingSolution(Binding guidingSolution) {
		this.guidingSolution = guidingSolution;
	}
	
	public Set<Binding> getEliteSolutions() {
		return eliteSolutions;
	}
	
	public void setEliteSolutions(Set<Binding> eliteSolutions) {
		this.eliteSolutions = eliteSolutions;
	}

	public Binding solve(QoSAwareBindingProblem p) {
		if (initialSolution == null)
			initialSolution = getRandomElement(eliteSolutions);
		if (guidingSolution == null)
			guidingSolution = getRandomElement(eliteSolutions);

		neighboursVisited = 0;
		if(func==null) {
			Map<QoSProperty,UtilityFunction<Double>> utilityFunctions=new HashMap<QoSProperty, UtilityFunction<Double>>();
			WeightedSumObjectiveFunction of=(WeightedSumObjectiveFunction)p.getObjectiveFunction();
			BoundedUtilityFunction<Double> uFunc=null;
			for(QoSProperty q:p.getQualityProperties()) {
				try {
					uFunc=(BoundedUtilityFunction<Double>)((BoundedUtilityFunction<Double>) of.getUtiltyFunction(q)).clone();
				} catch (CloneNotSupportedException e) {					
					e.printStackTrace();
				}
				uFunc.setMin(globalMin.get(q));
				uFunc.setMax(globalMax.get(q));
			}
			func=new WeightedSumObjectiveFunction(of.getWeights(),
													utilityFunctions);
		}

		Binding optimalSolution = func.evaluate(initialSolution) > func.evaluate(guidingSolution) 
									? initialSolution : guidingSolution;
		Binding currentSolution = new Binding(initialSolution);
		while (!currentSolution.equals(guidingSolution) && neighboursVisited < maxNeighbours) {
			for (Task t : p.getApplication().getTasks()) {
				if (!currentSolution.getProvider(t).equals(guidingSolution.getProvider(t))) {
					currentSolution.setProvider(t, guidingSolution.getProvider(t));
					if (func.evaluate(initialSolution) > func.evaluate(guidingSolution))
						optimalSolution = currentSolution;
				}
			}
		}
		return optimalSolution;
	}

	private Binding getRandomElement(Set<Binding> restrictedCandidates) {
		int size = restrictedCandidates.size();
		int item = random.nextInt(size);
		int i = 0;
		for (Binding obj : restrictedCandidates) {
			if (i == item)
				return obj;
			i++;
		}
		return null;
	}

}
