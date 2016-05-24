package gdatastructure;

public class Vertex {
	private Object value;
	private boolean isVisited;
	public Link link;

	Vertex(Object value) {
		link = new Link();
		this.value = value;
		isVisited = false;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	void visit() {
		isVisited = true;
	}

	boolean isVisited() {
		return isVisited;
	}
}