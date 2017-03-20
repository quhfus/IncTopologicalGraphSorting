package algorithm;

import dagGen.DAGSmith;
import dagGen.DAGTools;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

public class MainTest {

	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		DAGSmith smith = new DAGSmith();
		boolean[][] dag = smith.generateRandomDAG(10, 2);
		System.out.println(DAGTools.printDAG(dag));
		System.out.println(
				"Generated a " + dag.length + "x" + dag[0].length + " DAG with " + DAGTools.getEdges(dag) + " edges.");

		DirectedGraph<Vertex, Edge> graph = new DirectedSparseGraph<Vertex, Edge>();
		Vertex[] vertexes = new Vertex[dag.length];
		for (int i = 0; i < vertexes.length; i++) {
			vertexes[i] = new Vertex(i);
			vertexes[i].setText(String.valueOf(i));
			graph.addVertex(vertexes[i]);
		}

		for (int i = 0; i < dag.length; i++) {
			for (int j = 0; j < dag[i].length; j++) {
				if (dag[i][j] == true) {
					vertexes[i].addOutGoingEdge(new Edge(vertexes[j]));
				}
			}
		}

		System.out.println("Generation Time: " + (System.currentTimeMillis() - time));
		TopSort ts = new TopSort(graph);
		ts.topologicalSort();
		ts.print();
//		System.out.println("-------------------------------");
//		ts.addBackwardEdge(vertexes[0], vertexes[3]);
//		ts.print();
//		ts.printStackMap();
		// ts.print();
	}

}
