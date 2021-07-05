package es.us.isa.graspprqosawarebinding.problem;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import es.us.isa.graspprqosawarebinding.Application;
import es.us.isa.graspprqosawarebinding.Binding;
import es.us.isa.graspprqosawarebinding.Plan;
import es.us.isa.graspprqosawarebinding.QoSProperty;
import es.us.isa.graspprqosawarebinding.Task;
import es.us.isa.graspprqosawarebinding.objectivefunctions.ObjectiveFunction;

public class QoSAwareBindingProblem {
	
	List<QoSProperty> qualityProperties;
	
	Application application;
	
	Map<Task,Set<Plan>> market;
	
	List<Constraint> constraints;
	
	ObjectiveFunction objectiveFunction;
	
	Random random;

	public QoSAwareBindingProblem(Application application, Map<Task, Set<Plan>> market, List<Constraint> constraints,
			ObjectiveFunction objectiveFunction,List<QoSProperty> properties, Random random) {
		super();
		this.qualityProperties=properties;
		this.application = application;
		this.market = market;
		this.constraints = constraints;
		this.objectiveFunction = objectiveFunction;
		this.random=random;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public List<Constraint> getConstraints() {
		return constraints;
	}

	public void setConstraints(List<Constraint> constraints) {
		this.constraints = constraints;
	}

	public ObjectiveFunction getObjectiveFunction() {
		return objectiveFunction;
	}

	public void setObjectiveFunction(ObjectiveFunction objectiveFunction) {
		this.objectiveFunction = objectiveFunction;
	}

	public Map<Task, Set<Plan>> getMarket() {
		return market;
	}	
	
	public Set<Plan> getAvailablePlans(Task t){
		return market.get(t);
	}
	
	public List<QoSProperty> getQualityProperties() {
		return qualityProperties;
	}
	
	public Binding generateRandomBinding() {
		Binding result=new Binding();
		for(Task t:market.keySet()) {
			result.setProvider(t, chooseRandomPlan(t));
		}
		return result;
	}
	
	
	private Plan chooseRandomPlan(Task t) {
		Set<Plan> restrictedCandidates=market.get(t);
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
	
	
}
