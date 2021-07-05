package es.us.isa.graspprqosawarebinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Binding {
	Map<Task, Plan> chosenProviders;
	
	public Binding() {
		chosenProviders=new HashMap<Task, Plan>();
	}
	
	public Binding(Binding initialSolution) {
		chosenProviders=new HashMap<Task, Plan>(initialSolution.chosenProviders);
	}

	public Plan getProvider(Task t) {
		return chosenProviders.get(t);
	}
	
	public void setProvider(Task task, Plan plan){
		chosenProviders.put(task, plan);
	}
	
	public Set<Task> getTasks(){
		return chosenProviders.keySet();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chosenProviders == null) ? 0 : chosenProviders.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Binding other = (Binding) obj;
		if (chosenProviders == null) {
			if (other.chosenProviders != null)
				return false;
		} else if (!chosenProviders.equals(other.chosenProviders))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Binding [chosenProviders=" + chosenProviders + "]";
	}
	
	
	
	
}
