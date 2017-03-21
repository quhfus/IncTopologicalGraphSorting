package algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Vertex implements Comparable<Vertex> {
	private List<String> uris;
	private int entityQuery;
	private double score;
	private boolean isCandidate;
	private String description;
	private String text;
	private String context;
	private double occurrences;

	private Set<Edge> outgoingEdges;
	private double sumOutGoing;

	private int nr;
	
	Vertex(int nr) {
		super();
		this.uris = new ArrayList<String>();
		this.outgoingEdges = new HashSet<Edge>();
		this.entityQuery = -1;
		this.isCandidate = false;
		this.sumOutGoing = 0;
		this.text = "";
		this.context = "";
		this.nr = nr;
	}

	void addOutGoingEdge(Edge e) {
		outgoingEdges.add(e);
	}
	
	void removeAllOutgoingEdges() {
		this.outgoingEdges.clear();
	}

	String getContext() {
		return context;
	}

	void setContext(String context) {
		this.context = context;
	}

	double getSumOutGoingEdges() {
		return sumOutGoing;
	}

	Set<Edge> getOutgoingEdges() {
		return this.outgoingEdges;
	}
	
	List<String> getUris() {
		return uris;
	}

	void addUri(String uri) {
		this.uris.add(uri);
	}

	boolean isCandidate() {
		return isCandidate;
	}

	void setCandidate(boolean isCandidate) {
		this.isCandidate = isCandidate;
	}

	int getEntityQuery() {
		return entityQuery;
	}

	void setEntityQuery(int entityQuery) {
		this.entityQuery = entityQuery;
	}

	void setGraphValue(double val) {
		this.score = val;
	}

	double getScore() {
		return this.score;
	}

	void setScore(double score) {
		this.score = score;
	}

	String getDescription() {
		return description;
	}

	void setDescription(String description) {
		this.description = description;
	}

	String getText() {
		return text;
	}

	void setText(String text) {
		this.text = text;
	}

	double getOccurrences() {
		return occurrences;
	}

	void setOccurrences(int occurrences) {
		this.occurrences = Math.log(occurrences + 1);
	}
	
	boolean hasOutgoingEdges() {
		return outgoingEdges.size() > 1 ? true : false;
	}

	@Override
	public boolean equals(Object obj) {
		Vertex comp = (Vertex) obj;
		boolean isEqual = true;
		if (!this.text.equalsIgnoreCase(comp.getText())) {
			return false;
		}
		return isEqual;
	}

	@Override
	public int hashCode() {
		return this.text.hashCode();
	}

	public int compareTo(Vertex o) {
		return this.nr < o.nr ? 1 : this.nr > o.nr ? -1 :0;
	}

//	/**
//	 * The return values are switched to provide a descending order when using
//	 * Collections.sort(), which generally provides an ascending sort order.
//	 * 
//	 */
//	public int compareTo(Vertex o) {
//		if (this.text < o.getOccurrences()) {
//			return 1;
//		} else if (this.getOccurrences() > o.getOccurrences()) {
//			return 1;
//		} else {
//			return 0;
//		}
//	}
}