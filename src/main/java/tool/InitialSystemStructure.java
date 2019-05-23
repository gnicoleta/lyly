package tool;

import com.sun.xml.internal.bind.v2.TODO;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.*;

public class InitialSystemStructure {
    Set<Graph> packages_g = new HashSet<>();
    ClusteringAlgorithm clusteringAlgorithm = new ClusteringAlgorithm();


    int contor = 0;

    //TODO: Something is wrong, check to see where is the null pointer exception coming from
    public Set<Graph>getInitialStructureOfTheSystem(HashMap<String, HashSet<String>> packages_classes_structure, Graph tool_graph) {

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

            packages_g.add(package_cluster_graph);

            //tool_graph.containsVertex()


        }
        try {
            //contor ++;
            System.out.println("PACK SIZE: " + packages_g.size());
        } catch (NullPointerException e) {
            System.out.println("cocos: " + contor);
        }

        for(Graph g: packages_g) {
            contor++;
            System.out.println("AAA:" + contor + "  --------    " + g);
        }
        return packages_g;
    }

    public Graph getPackageClustersGraph() {
        Graph g = new UndirectedSparseMultigraph();

        for(Graph ge : packages_g) {
            Collection<Node> nodes = ge.getVertices();
            Collection<Edge> edges = ge.getEdges();
            for(Node n : nodes) {
                g.addVertex(n);
            }

            if(edges.size() > 0) {
                for(Edge e : edges) {
                    g.addEdge(e, e.getEdgeSourceNode(), e.getEdgeDestiantionNode(), EdgeType.UNDIRECTED);
                }
            }
        }

        return g;
    }
}
