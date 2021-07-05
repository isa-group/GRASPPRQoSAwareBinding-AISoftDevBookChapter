package es.us.isa.graspprqosawarebinding;

import java.util.Map;

public class Plan {
	
	String name;
	ServiceProvider provider;
	Map<QoSProperty, Double> values;
	
		
	
	public Plan(String name,ServiceProvider provider, Map<QoSProperty, Double> values) {
		super();
		this.name=name;
		this.provider = provider;
		this.values = values;
	}

	public ServiceProvider getProvider() {
		return provider;
	}
	
	public Double  getQoSValue(QoSProperty property) {
		return values.get(property);
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
	
	
	
}
