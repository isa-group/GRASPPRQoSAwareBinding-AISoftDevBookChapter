package es.us.isa.graspprqosawarebinding;

public class QoSProperty {
	String name;
	QoSPropertyType type;
	
	
	public QoSProperty(String name, QoSPropertyType type) {
		super();
		this.name = name;
		this.type=type;
	}

	public String getName() {
		return name;
	}
	
	public QoSPropertyType getType() {
		return type;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		QoSProperty other = (QoSProperty) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	public enum QoSPropertyType {POSITIVE,NEGATIVE};
}
