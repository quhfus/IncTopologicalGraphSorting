package algorithm;

public class Edge {
	
	private Vertex target;
	
	public Edge(Vertex target) {
		super();
		this.target = target;
	}
	
	Vertex getTarget() {
		return this.target;
	}
}