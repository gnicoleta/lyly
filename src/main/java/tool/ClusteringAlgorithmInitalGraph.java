package tool;
/*
import edu.uci.ics.jung.graph.Graph;

import java.util.ArrayList;
import java.util.Collection;

public class ClusteringAlgorithmInitalGraph extends ClusteringAlgorithm {

    public ClusteringAlgorithmInitalGraph(Graph g) {
        super(g);
    }
    @Override
    public void checkIsolatedNodes(Graph graph, Collection<Node> visited_nodes, Collection<Edge> mst_edges) {
        ArrayList<Edge> edges_from_isolated_nodes = new ArrayList<>();
        for(Object n : graph.getVertices()) {
            if(!visited_nodes.contains((Node)n)) {
                Graph aux = generateMST(graph, (Node)n);
                for(Object o: aux.getEdges()) {
                    //edges_from_isolated_nodes.add((Edge) o);
                    mst_edges.add((Edge)o);
                }
            }
        }
        //return edges_from_isolated_nodes;
    }
}
*/