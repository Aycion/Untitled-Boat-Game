package engine;

public class ResourceNotFound extends Throwable {
	
	String ResourceName;
	
	public ResourceNotFound(String name) {
		this.ResourceName = name;
	}
	@Override
	public String getLocalizedMessage() {
		return (ResourceName + " Not loaded");
	}
}
