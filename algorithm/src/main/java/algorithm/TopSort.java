package algorithm;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

public class TopSort {

	private DirectedGraph<Vertex, Edge> graph;

	private Vertex[] stack;

	private Map<Vertex, Integer> stack_map;
	// private LinkedList<Vertex> stack;

	private Set<Vertex> visited;

	private Set<Vertex> unvisited;

	private Set<Vertex> encounter;

	private Set<Vertex> deque_set;

	private int insertCounter;

	private LinkedList<Vertex> stack_list;

	public TopSort(DirectedGraph<Vertex, Edge> graph) {
		super();
		this.graph = graph;

	}

	private void updateStackMap_Shift(ListIterator<Vertex> it, int changes, int intial_ChangePosition) {
		while (changes > 0) {
			Vertex x = it.next();
			stack_map.put(x, intial_ChangePosition++);
			changes--;
		}
	}

	private void updateStackMap(Vertex x, int position) {
		stack_map.put(x, position);
	}

	void printStackMap() {
		for (Map.Entry<Vertex, Integer> entry : stack_map.entrySet()) {
			System.out.println("StackMap Key: " + entry.getKey().getText() + " StackMap Entry: " + entry.getValue());
		}
	}

	public void topologicalSort() {
		long time = System.currentTimeMillis();
		this.insertCounter = 1;
		this.stack = new Vertex[graph.getVertexCount()];
		this.stack_map = new HashMap<Vertex, Integer>();
		this.visited = new HashSet<Vertex>();
		this.unvisited = new HashSet<Vertex>();
		this.unvisited.addAll(graph.getVertices());
		this.encounter = new HashSet<Vertex>();
		this.deque_set = new HashSet<Vertex>();
		while (unvisited.size() > 0) {
			iteration();
		}

		System.out.println("Processing Time: " + (System.currentTimeMillis() - time));
		this.stack_list = new LinkedList<Vertex>(Arrays.asList(stack));
	}

	private void iteration() {
		// Select unvisited Node
		Vertex start = selectNewNode();
		// Iterate over all outgoing edges
		Deque<Vertex> deque = new ArrayDeque<Vertex>();
		deque.push(start);
		// System.out.println("New Iteration");
		iterateOutgoingEdgeNonRecursivDFS(deque);
	}

	private void iterateOutgoingEdgeNonRecursiv(Deque<Vertex> deque) {
		while (!deque.isEmpty()) {
			Vertex x = deque.peek();

			Set<Edge> edges = x.getOutgoingEdges();
			boolean push = false;
			for (Edge e : edges) {
				Vertex target = e.getTarget();
				if (!visited.contains(target) && !deque_set.contains(target)) {
					deque.push(target);
					deque_set.add(target);
					// System.out.println("Push: " + target.getText());
					push = true;
				}
			}
			// System.out.println("---------------------------------------------");
			if (!push) {
				// System.out.println(x.getText());
				// System.out.println(stack.length);
				// System.out.println(insertCounter);
				// System.out.println("------------------------" + x.getText());
				stack[stack.length - insertCounter] = x;
				stack_map.put(x, stack.length - insertCounter);
				insertCounter++;
				deque.pop();
			}
			visited.add(x);
			unvisited.remove(x);
		}
	}
	
	private void iterateOutgoingEdgeNonRecursivDFS(Deque<Vertex> deque) {
		while (!deque.isEmpty()) {
			Vertex x = deque.peek();

			Set<Edge> edges = x.getOutgoingEdges();
			boolean push = false;
			for (Edge e : edges) {
				Vertex target = e.getTarget();
				if (!visited.contains(target)) {
					deque.push(target);
//					deque_set.add(target);
					push = true;
					break;
				}
			}
			if (!push) {
				stack[stack.length - insertCounter] = x;
				stack_map.put(x, stack.length - insertCounter);
				insertCounter++;
				deque.pop();
			}
			visited.add(x);
			unvisited.remove(x);
		}
	}

	private Vertex selectNewNode() {
		Vertex x = unvisited.iterator().next();
		// System.out.println("Newnode: " + x.getText());
		return x;
	}

	void print() {
		for (Vertex x : stack_list) {
			System.out.println(x.getText());
		}
	}

	void addBackwardEdge(Vertex a, Vertex b) {
		long time = System.currentTimeMillis();
		encounter.add(a);
//		actionNewEdge(a, b, encounter);
		actionNewEdgeNonRecursive(a, b);
		System.out.println("Edge Add Time: " + (System.currentTimeMillis() - time));
	}

//	private void actionNewEdge(Vertex a, Vertex b, Set<Vertex> encounter) {
//		// System.out.println("Vertex: " + a.getText() + " Vertex: " +
//		// b.getText());
//		if (encounter.contains(b)) {
//			System.out.println("Cycle detected!!!");
//		}
//		encounter.add(b);
//
//		if (!isBefore(a, b)) {
//			setBehind(a, b);
//			Set<Edge> edges = b.getOutgoingEdges();
//			for (Edge e : edges) {
//				Vertex target = e.getTarget();
//				actionNewEdge(b, target, encounter);
//			}
//		}
//		encounter.remove(b);
//	}

	private void actionNewEdgeNonRecursive(Vertex a, Vertex b) {
		encounter.add(a);
		Deque<VertexPair> deque = new ArrayDeque<VertexPair>();
		deque.add(new VertexPair(a, b));
		while (!deque.isEmpty()) {
			VertexPair p = deque.peek();
			Vertex a1 = p.getLeft();
			Vertex b1 = p.getRight();
			if (encounter.contains(b1)) {
				System.out.println("Cycle detected!!!");
			}
			encounter.add(b1);
			
			if (!isBefore(a1, b1)) {
				setBehind(a1, b1);
				Set<Edge> edges = b.getOutgoingEdges();
				for (Edge e : edges) {
					Vertex target = e.getTarget();
					deque.add(new VertexPair(b, target));
				}
			} else {
				deque.pop();
			}
			encounter.remove(b1);
		}
	}

	private boolean isBefore(Vertex a, Vertex b) {
		return stack_map.get(a) < stack_map.get(b) ? true : false;
	}

	private void setBehind(Vertex a, Vertex b) {
		// Wir schauen uns die stack List Order jetzt an
		int b_position = (int) stack_map.get(b);
		int a_position = (int) stack_map.get(a);

		// stack_list.remove(b_position);
		ListIterator<Vertex> it_b = stack_list.listIterator(b_position);
		it_b.next();
		it_b.remove();
		// Update Map
		updateStackMap_Shift(it_b, a_position - b_position, b_position);
		it_b.add(b);

		updateStackMap(b, a_position);
		// print();
		// System.out.println("SET BEHIND!");
	}

	void setTopOrder(LinkedList<Vertex> order, Map<Vertex, Integer> map) {
		this.stack_map = map;
		this.stack_list = order;
	}

	class VertexPair extends Pair<Vertex, Vertex> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1004740480914508535L;

		private Vertex a;
		private Vertex b;

		VertexPair(Vertex a, Vertex b) {
			super();
			this.a = a;
			this.b = b;
		}

		public Vertex setValue(Vertex value) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Vertex getLeft() {
			return a;
		}

		@Override
		public Vertex getRight() {
			return b;
		}

	}

	public static void main(String args[]) {
		// Todo Graph Generation
		DirectedGraph<Vertex, Edge> graph = new DirectedSparseGraph<Vertex, Edge>();
		Vertex a = new Vertex(0);
		a.setText("a");
		Vertex b = new Vertex(1);
		b.setText("b");
		Vertex c = new Vertex(2);
		c.setText("c");
		Vertex d = new Vertex(3);
		d.setText("d");
		Vertex e = new Vertex(4);
		e.setText("e");
		Vertex f = new Vertex(5);
		f.setText("f");

		a.addOutGoingEdge(new Edge(b));
		a.addOutGoingEdge(new Edge(f));
		b.addOutGoingEdge(new Edge(c));
		b.addOutGoingEdge(new Edge(d));
		d.addOutGoingEdge(new Edge(c));
		e.addOutGoingEdge(new Edge(d));
		f.addOutGoingEdge(new Edge(d));

		graph.addVertex(a);
		graph.addVertex(b);
		graph.addVertex(c);
		graph.addVertex(d);
		graph.addVertex(e);
		graph.addVertex(f);

		TopSort ex = new TopSort(graph);
		ex.topologicalSort();
		ex.print();
		
		ex.addBackwardEdge(d, b);
		ex.print();
		ex.printStackMap();
		
		// DirectedGraph<Vertex, Edge> graph = new DirectedSparseGraph<Vertex,
		// Edge>();
		// Vertex a = new Vertex(1);
		// a.setText("a");
		// Vertex b = new Vertex(2);
		// b.setText("b");
		// Vertex c = new Vertex(3);
		// c.setText("c");
		// Vertex d = new Vertex(4);
		// d.setText("d");
		// Vertex e = new Vertex(5);
		// e.setText("e");
		// Vertex f = new Vertex(6);
		// f.setText("f");
		//
		// a.addOutGoingEdge(new Edge(f));
		// b.addOutGoingEdge(new Edge(a));
		// b.addOutGoingEdge(new Edge(c));
		// b.addOutGoingEdge(new Edge(e));
		// d.addOutGoingEdge(new Edge(c));
		// d.addOutGoingEdge(new Edge(f));
		// d.addOutGoingEdge(new Edge(e));
		// e.addOutGoingEdge(new Edge(a));
		// e.addOutGoingEdge(new Edge(f));
		// f.addOutGoingEdge(new Edge(c));
		//
		// graph.addVertex(a);
		// graph.addVertex(b);
		// graph.addVertex(c);
		// graph.addVertex(d);
		// graph.addVertex(e);
		// graph.addVertex(f);

//		DirectedGraph<Vertex, Edge> graph = new DirectedSparseGraph<Vertex, Edge>();
//		Vertex a = new Vertex(1);
//		a.setText("a");
//		Vertex b = new Vertex(2);
//		b.setText("b");
//		Vertex c = new Vertex(3);
//		c.setText("c");
//		Vertex d = new Vertex(4);
//		d.setText("d");
//		Vertex e = new Vertex(5);
//		e.setText("e");
//		Vertex f = new Vertex(6);
//		f.setText("f");
//
//		a.addOutGoingEdge(new Edge(d));
//		// b.addOutGoingEdge(new Edge(f));
//		c.addOutGoingEdge(new Edge(d));
//		c.addOutGoingEdge(new Edge(b));
//		e.addOutGoingEdge(new Edge(f));
//
//		graph.addVertex(a);
//		graph.addVertex(b);
//		graph.addVertex(c);
//		graph.addVertex(d);
//		graph.addVertex(e);
//		graph.addVertex(f);
//		//
//		TopSort ex = new TopSort(graph);
//		ex.topologicalSort();
//
//		System.out.println("-------------------------------");
//
//		LinkedList<Vertex> l = new LinkedList<Vertex>();
//		l.add(e);
//		l.add(c);
//		l.add(b);
//		l.add(f);
//		l.add(a);
//		l.add(d);
//
//		Map<Vertex, Integer> map = new HashMap<Vertex, Integer>();
//		map.put(e, 0);
//		map.put(c, 1);
//		map.put(b, 2);
//		map.put(f, 3);
//		map.put(a, 4);
//		map.put(d, 5);
//
//		ex.setTopOrder(l, map);
//		ex.print();
//		ex.printStackMap();
//		//
//		ex.addBackwardEdge(f, c);
//		ex.print();
//		ex.printStackMap();
	}

}
