package es.us.isa.graspprqosawarebinding.algorithms;

import java.util.Random;
import java.util.Set;

import es.us.isa.graspprqosawarebinding.Binding;
import es.us.isa.graspprqosawarebinding.Plan;
import es.us.isa.graspprqosawarebinding.Task;
import es.us.isa.graspprqosawarebinding.problem.QoSAwareBindingProblem;

public class PathRelinking implements OptimizationAlgorithm {

	Set<Binding> eliteSolutions;
	int neighboursVisited;
	int maxNeighbours;
	Random random;
	Binding initialSolution;
	Binding guidingSolution;

	public PathRelinking(Set<Binding> eliteSolutions, int maxNeighbours, Random random) {
		this.maxNeighbours = maxNeighbours;
		this.eliteSolutions = eliteSolutions;
		this.random = random;
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

		Binding optimalSolution = p.getObjectiveFunction().evaluate(initialSolution) > p.getObjectiveFunction()
				.evaluate(guidingSolution) ? initialSolution : guidingSolution;
		Binding currentSolution = new Binding(initialSolution);
		while (!currentSolution.equals(guidingSolution) && neighboursVisited < maxNeighbours) {
			for (Task t : p.getApplication().getTasks()) {
				if (!currentSolution.getProvider(t).equals(guidingSolution.getProvider(t))) {
					currentSolution.setProvider(t, guidingSolution.getProvider(t));
					if (p.getObjectiveFunction().evaluate(initialSolution) > p.getObjectiveFunction()
							.evaluate(guidingSolution))
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
