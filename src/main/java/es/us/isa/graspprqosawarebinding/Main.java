package es.us.isa.graspprqosawarebinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import es.us.isa.graspprqosawarebinding.QoSProperty.QoSPropertyType;
import es.us.isa.graspprqosawarebinding.algorithms.GRASPwithPathRelinking;
import es.us.isa.graspprqosawarebinding.objectivefunctions.ObjectiveFunction;
import es.us.isa.graspprqosawarebinding.objectivefunctions.WeightedSumObjectiveFunction;
import es.us.isa.graspprqosawarebinding.problem.Constraint;
import es.us.isa.graspprqosawarebinding.problem.QoSAwareBindingProblem;
import es.us.isa.graspprqosawarebinding.utilityfuncions.NegativeUtilityFunction;
import es.us.isa.graspprqosawarebinding.utilityfuncions.PositiveUtilityFunction;
import es.us.isa.graspprqosawarebinding.utilityfuncions.UtilityFunction;

public class Main {
	
	Random random;
	
	QoSProperty cost;	
	
	QoSProperty availability;
	
	QoSProperty storage;
	
	Map<QoSProperty,Double> max;
	Map<QoSProperty,Double> min;
	
	Task cloudStorageService;
	Task paymentAndBillingService;
	Task notificationService;
	
	public static void main(String[] params) {
		Main main=new Main();
		main.run();
	}

	private void run() {
		Random random=new Random();
		QoSAwareBindingProblem problem=buildSampleProblem();
		GRASPwithPathRelinking graspWithPR=new GRASPwithPathRelinking(10,0.2,new Random(), 2,3);
		Binding optimalSolution=graspWithPR.solve(problem);
		System.out.println(optimalSolution);		
	}

	private QoSAwareBindingProblem buildSampleProblem() {
		Set<QoSProperty> qoSProperties=buildQoSProperties();
		Application application=buildApplication();
		Map<Task,Set<Plan>> market=buildMarket(qoSProperties);
		ObjectiveFunction objFunc=buildObjectiveFunction(qoSProperties);
		List<Constraint> constraints=buildConstraints();
		
		QoSAwareBindingProblem problem=new QoSAwareBindingProblem(application, market, constraints, objFunc,new ArrayList<QoSProperty>(qoSProperties),random);
		return problem;
	}

	private List<Constraint> buildConstraints() {
		List<Constraint> c=new ArrayList<Constraint>();
		return c;
	}

	private Map<Task, Set<Plan>> buildMarket(Set<QoSProperty> qoSProperties) {
		HashMap<Task,Set<Plan>> result=new HashMap<Task, Set<Plan>>();
		result.put(cloudStorageService, buildStorageMarket());
		result.put(paymentAndBillingService, buildPaymentAndBillingMarket());
		result.put(notificationService, buildNotificationMarket());
		computeMaxAndMins(result);
		return result;
	}

	private void computeMaxAndMins(HashMap<Task, Set<Plan>> result) {
		max=new HashMap<QoSProperty, Double>();
		min=new HashMap<QoSProperty, Double>();
		QoSProperty[] props= {cost,availability,storage};
		double minValue;
		double maxValue;
		for(QoSProperty q:props) {
			for(Task t:result.keySet()) {
				minValue=Double.MAX_VALUE;
				maxValue=Double.MIN_VALUE;
				for(Plan p:result.get(t)) {
					if(p.getQoSValue(q)>maxValue) 
						maxValue=p.getQoSValue(q);											
					if(p.getQoSValue(q)<minValue)
						minValue=p.getQoSValue(q);
				}
				if(min.get(q)==null)
					min.put(q, minValue);
				else if(minValue<min.get(q))
					min.put(q, minValue);
				if(max.get(q)==null)
					max.put(q, maxValue);
				else
					max.put(q, maxValue+max.get(q));
			}			
		}		
	}

	private Set<Plan> buildNotificationMarket() {
		Set<Plan> market=new HashSet<Plan>();
		ServiceProvider twilio=new ServiceProvider("Twilio");
		
		Map<QoSProperty,Double> values=new HashMap<QoSProperty, Double>();
		values.put(cost,0.075);
		values.put(availability, 99.95);
		values.put(storage, 0.0);
		Plan payAsYouGo=new Plan("Pay as you go",twilio, values);
		market.add(payAsYouGo);
		
		values=new HashMap<QoSProperty, Double>();
		values.put(cost,0.05);
		values.put(availability, 99.99);
		values.put(storage, 0.0);
		Plan perVolume=new Plan("Per volume",twilio,values);
		market.add(perVolume);
		
		values=new HashMap<QoSProperty,Double>();
		values.put(cost,0.03);
		values.put(availability, 99.99);
		values.put(storage, 0.0);
		Plan commitedUse=new Plan("Commited Use",twilio,values);
		market.add(commitedUse);
		
		
		return market;
	}

	private Set<Plan> buildPaymentAndBillingMarket() {
		Set<Plan> market=new HashSet<Plan>();
		ServiceProvider paypal=new ServiceProvider("Paypal");
		
		Map<QoSProperty,Double> values=new HashMap<QoSProperty, Double>();
		values.put(cost,0.42);
		values.put(availability, 0.0);
		values.put(storage, 0.0);
		Plan businessCard=new Plan("Business card",paypal, values);
		market.add(businessCard);

		values=new HashMap<QoSProperty, Double>();
		values.put(cost,12.25);
		values.put(availability, 0.0);
		values.put(storage, 0.0);
		Plan businessPhone=new Plan("Business phone",paypal, values);
		market.add(businessPhone);
		
		return market;
	}

	private Set<Plan> buildStorageMarket() {
		Set<Plan> market=new HashSet<Plan>();
		
		ServiceProvider gDrive=new ServiceProvider("Google Drive");
		Map<QoSProperty,Double> values=new HashMap<QoSProperty, Double>();
		values.put(cost,0.0);
		values.put(availability, 99.95);
		values.put(storage, 15.0);
		Plan gDriveFree=new Plan("Google Drive Free plan",gDrive, values);
		market.add(gDriveFree);

		values=new HashMap<QoSProperty, Double>();
		values.put(cost,1.99);
		values.put(availability, 99.95);
		values.put(storage, 100.0);
		Plan gDrivePaid=new Plan("Google Drive base paid plan",gDrive, values);
		market.add(gDrivePaid);		
		
		ServiceProvider dropbox=new ServiceProvider("Dropbox");
		values=new HashMap<QoSProperty, Double>();
		values.put(cost,0.0);
		values.put(availability, 99.63);
		values.put(storage, 2.0);
		Plan dropboxFree=new Plan("Dropbox Free plan",dropbox, values);
		market.add(dropboxFree);
		
		values=new HashMap<QoSProperty, Double>();
		values.put(cost,9.99);
		values.put(availability, 99.63);
		values.put(storage, 2048.0);
		Plan dropboxPaid=new Plan("Dropbox paid plan",dropbox, values);
		market.add(dropboxPaid);
		
		ServiceProvider box=new ServiceProvider("Box");
		values=new HashMap<QoSProperty, Double>();
		values.put(cost,0.0);
		values.put(availability, 99.00);
		values.put(storage, 10.0);
		Plan boxFree=new Plan("Box Free plan",box, values);
		market.add(boxFree);
		
		values=new HashMap<QoSProperty, Double>();
		values.put(cost,9.0);
		values.put(availability, 99.00);
		values.put(storage, 100.0);
		Plan boxPaid=new Plan("Box paid plan",box, values);
		market.add(boxPaid);
		return market;
	}

	private Application buildApplication() {
		Application app=new Application();
		List<Task> tasks=new ArrayList<Task>();
		
		cloudStorageService=new Task("Cloud Storage Service");
		tasks.add(cloudStorageService);
		
		paymentAndBillingService=new Task("Payment & Billing Service");
		tasks.add(paymentAndBillingService);
		
		notificationService=new Task("Notfication Sevice");
		tasks.add(notificationService);
		
		app.setTasks(tasks);
		return app;
	}

	private Set<QoSProperty> buildQoSProperties() {
		cost=new QoSProperty("Cost",QoSPropertyType.NEGATIVE);
		availability=new QoSProperty("Availability",QoSPropertyType.POSITIVE);
		storage=new QoSProperty("Storage capacity",QoSPropertyType.POSITIVE);
		Set<QoSProperty> result=new HashSet<QoSProperty>();
		result.add(cost);
		result.add(availability);
		result.add(storage);
		return result;
	}

	private ObjectiveFunction buildObjectiveFunction(Set<QoSProperty> qoSProperties	) {
		ObjectiveFunction result=null;
		Map<QoSProperty,Double> weights=new HashMap<QoSProperty, Double>();
		weights.put(cost, 0.5);
		weights.put(availability,0.3);
		weights.put(storage, 0.2);
		
		Map<QoSProperty,UtilityFunction<Double>> utilities=new HashMap<QoSProperty, UtilityFunction<Double>>();
		utilities.put(cost, new NegativeUtilityFunction(min.get(cost), max.get(cost)));
		utilities.put(availability, new PositiveUtilityFunction(min.get(availability), max.get(availability)));
		utilities.put(storage, new PositiveUtilityFunction(min.get(storage), max.get(storage)));
		
		result=new WeightedSumObjectiveFunction(weights, utilities);
		
		return  result;
	}
	
	
}
