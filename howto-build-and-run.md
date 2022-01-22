# How to build, run, and customize the project
## Authors

The authors of project are:
 * José Antonio Parejo Maestre

## Building

This is a maven-java project, you will need the following pre-requisites to build this project:
  * A valid JAVA JDK installation. The authors have used JDK 11 to develop this project, but any version later tha 1.8 should be enough. You can download the openjdk from: https://openjdk.java.net/install/ 

Once you have installed both tools, you should be able to build the project using the following command: 
```
	.\mvnw install
```

after executing suh command, you should see a message stating that the installation has been successfull.

## Running

First, you must ensure that your environment meets the requirements stated above. The main example of this project can be executed by typing the following command:
```
.\mvnw compile exec:java
```
You should see a result similar to this:
```
[INFO] --- exec-maven-plugin:3.0.0:java (default-cli) @ graspprqosawarebinding ---
Binding [chosenProviders={Notfication Sevice=Commited Use, Cloud Storage Service=Google Drive base paid plan, Payment & Billing Service=Business card}]:0.7640731503791482
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```
Such output provides bot the best binding found by the search algorithm ([chosenProviders={Notfication Sevice=Commited Use, Cloud Storage Service=Google Drive base paid plan, Payment & Billing Service=Business card}]), and the value of the objective function for such solution: 0.7640731503791482.

## How to adapt this example to other scenarios

The details of the specific problem instance to be solved are available at the Main class. Specifically, the input data that defines the problem and scenario to be solved are:

 ### The set of tasks for which we must find a binding:
 
 Any application that requires a set of external services much choose a binding (provider and plan) for each service. In the context of the project, the services are named tasks, and the set of tasks to bind for the application is defined in the following method of the main class:
 
 ```
 Application buildApplication() {
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
 ```
 
 In your application /scenario involves a different set of services, you should start by changing the set of tasks defined in the methods shown above.
 
 ### The market of available providers:
 
 One of the main components of the scenario and problem solved by this project is the maket of available services for each task. This market is created in the following function:
 ```
 Map<Task, Set<Plan>> buildMarket(Set<QoSProperty> qoSProperties) {
		HashMap<Task,Set<Plan>> result=new HashMap<Task, Set<Plan>>();
		result.put(cloudStorageService, buildStorageMarket());
		result.put(paymentAndBillingService, buildPaymentAndBillingMarket());
		result.put(notificationService, buildNotificationMarket());
		computeBindingMaxAndMins(result);
		computeGlobalMaxAndMins(result);
		return result;
	}
 ```

This function returns a Map, that provides for each Task, the set available plans in the market (remember that one provided can support several plans with different QoS guarantees). In the implementation shown above, each task has a specific function responsible of building its set of available plans.

 ### The Objective function
 
 One of the crucial elements in this project is the formulation of the objective function. We have defined an interface thad specifies how to invoke the objective function given a specific Binding:
``` 
public interface ObjectiveFunction {
	 public Double evaluate(Binding g);
}
```
Additionally, we have created an implementation of such interface for a computing a weighted sum of the global QoS properties of the binding in the class WeightedSumObjectiveFunction:

```
public class WeightedSumObjectiveFunction implements ObjectiveFunction{

	private Map<QoSProperty,Double> weights;
	private Map<QoSProperty,UtilityFunction<Double>> utilityFunctions;
	
	public WeightedSumObjectiveFunction(Map<QoSProperty, Double> weights,
			Map<QoSProperty, UtilityFunction<Double>> utilityFunctions) {
		super();
		this.weights = weights;
		this.utilityFunctions = utilityFunctions;
	}

	public Double evaluate(Binding b) {
		Double result=0.0;
		Double value;
		UtilityFunction<Double> utility;
		Plan p;
		for(QoSProperty q:weights.keySet()) {
			value=0.0;
			for(Task t:b.getTasks()) {
				p=b.getProvider(t);
				value+=p.getQoSValue(q);
			}
			utility=utilityFunctions.get(q);
			result+=weights.get(q)*utility.getUtility(value);
		}
		return result;
	}
	
	public UtilityFunction<Double> getUtiltyFunction(QoSProperty q){
		return utilityFunctions.get(q);
	}
	
	public Map<QoSProperty, Double> getWeights() {
		return weights;
	}

}
```
This class contains a weight per QoS property, and an UtilityFunction per QoS property, whose responsibility is to compute the utility of the global QoS of the bindings.
In the main class of the project, the default objective function to be used is defined in the following method:

```
ObjectiveFunction buildObjectiveFunction(Set<QoSProperty> qoSProperties	) {
		ObjectiveFunction result=null;
		Map<QoSProperty,Double> weights=new HashMap<QoSProperty, Double>();
		weights.put(cost, 0.5);
		weights.put(availability,0.3);
		weights.put(storage, 0.2);
		
		Map<QoSProperty,UtilityFunction<Double>> utilities=new HashMap<QoSProperty, UtilityFunction<Double>>();
		utilities.put(cost, new NegativeUtilityFunction(bindingMin.get(cost), bindingMax.get(cost)));
		utilities.put(availability, new PositiveUtilityFunction(bindingMin.get(availability), bindingMax.get(availability)));
		utilities.put(storage, new PositiveUtilityFunction(bindingMin.get(storage), bindingMax.get(storage)));
		
		result=new WeightedSumObjectiveFunction(weights, utilities);
		
		return  result;
	}
```

I such method, two specific utility functions are used, one for positive properties (the higher the QoS value, the better) and another for negative properties (the lower the QoS value, the better).
