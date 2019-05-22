package tool;

import com.sun.xml.internal.bind.v2.TODO;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class InitialSystemStructure {
    ArrayList<Graph> packages_g;
    ClusteringAlgorithm clusteringAlgorithm = new ClusteringAlgorithm();

    //TODO: Something is wrong, check to see where is the null pointer exception coming from
    public ArrayList<Graph> getInitialStructureOfTheSystem(HashMap<String, HashSet<String>> packages_classes_structure, Graph tool_graph) {


        System.out.println("GG" + tool_graph.getEdges());

        for (Map.Entry<String, HashSet<String>> e : packages_classes_structure.entrySet()) {
            String key = e.getKey();
            HashSet<String> values = e.getValue();
            ArrayList<Node> nodes = new ArrayList<>();
            ArrayList<Edge> edges = new ArrayList<>();

            Graph package_cluster_graph = new UndirectedSparseMultigraph();
            System.out.println("ASTA E:" + key);
            System.out.println("ASTA E values:" + values);

            for(String s : values) {
                String node_className = s.substring(0, s.length() - ".class".length());
                System.out.println("WWW: " + node_className);
                Node foundNode = clusteringAlgorithm.getNode(tool_graph, new Node(node_className));
                System.out.println("OOO"+ foundNode);
                if(foundNode!=null) {
                    System.out.println("GOOO"+ foundNode);
                    nodes.add(foundNode);
                }
            }
            System.out.println("AAL N: " + nodes);
            for(Node n : nodes) {
                System.out.println("EACH: " + tool_graph.getIncidentEdges(n));
                edges.addAll(tool_graph.getIncidentEdges(n));
                System.out.println("NNN: " + n);
                package_cluster_graph.addVertex(n);
            }

            System.out.println("EEDE: " + edges);
            for(Edge edge : edges) {
                System.out.println("2EEDE: " + edge);
                if(nodes.contains(edge.getEdgeSourceNode())&& nodes.contains(edge.getEdgeDestiantionNode())) {
                    package_cluster_graph.addEdge(edge, edge.getEdgeSourceNode(), edge.getEdgeDestiantionNode(), EdgeType.UNDIRECTED);
                }
            }


            System.out.println("package_cluster_graph: "+ package_cluster_graph);

           // packages_g.add(package_cluster_graph);

            //tool_graph.containsVertex()


        }
        System.out.println("PACK SIZE: " + packages_g.size());
        return packages_g;
    }
}
