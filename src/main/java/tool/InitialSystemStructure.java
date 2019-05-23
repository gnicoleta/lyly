package tool;

import com.sun.xml.internal.bind.v2.TODO;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.*;

public class InitialSystemStructure {

    Graph graph;

    public InitialSystemStructure(Graph graph) {
        this.graph = graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return this.graph;
    }

    //this will contain all the clusters formed based on the package structure
    Set<Graph> packages_graph_clusters = new HashSet<>();

    //clustering algorithm instance for coloring the package clusters
    //computing the MQ
    ClusteringAlgorithm clusteringAlgorithm = new ClusteringAlgorithm();

    public Set<Graph> getInitialStructureOfTheSystem(HashMap<String, HashSet<String>> packages_classes_structure, Graph tool_graph) {
        for (Map.Entry<String, HashSet<String>> e : packages_classes_structure.entrySet()) {
            String key = e.getKey();
            HashSet<String> values = e.getValue();
            ArrayList<Node> nodes = new ArrayList<>();
            ArrayList<Edge> edges = new ArrayList<>();
            Graph package_cluster_graph = new UndirectedSparseMultigraph();

            for (String s : values) {
                String node_className = s.substring(0, s.length() - ".class".length());
                Node foundNode = clusteringAlgorithm.getNode(tool_graph, new Node(node_className));
                if (foundNode != null) {
                    nodes.add(foundNode);
                }
            }
            for (Node n : nodes) {
                edges.addAll(tool_graph.getIncidentEdges(n));
                package_cluster_graph.addVertex(n);
            }

            for (Edge edge : edges) {
                if (nodes.contains(edge.getEdgeSourceNode()) && nodes.contains(edge.getEdgeDestiantionNode())) {
                    package_cluster_graph.addEdge(edge, edge.getEdgeSourceNode(), edge.getEdgeDestiantionNode(), EdgeType.UNDIRECTED);
                }
            }
            packages_graph_clusters.add(package_cluster_graph);
        }
        //clusteringAlgorithm.colorClusters(packages_graph_clusters);
        clusteringAlgorithm.colorNodesInClusters(packages_graph_clusters);
        return packages_graph_clusters;
    }

    public Graph getPackageClustersGraph() {
        Graph g = new UndirectedSparseMultigraph();
        for (Graph ge : packages_graph_clusters) {
            Collection<Node> nodes = ge.getVertices();
            Collection<Edge> edges = ge.getEdges();
            for (Node n : nodes) {
                g.addVertex(n);
            }

            if (edges.size() > 0) {
                for (Edge e : edges) {
                    g.addEdge(e, e.getEdgeSourceNode(), e.getEdgeDestiantionNode(), EdgeType.UNDIRECTED);
                }
            }
        }
       // clusteringAlgorithm.colorClusters(g, "package");
        return g;
    }

    public void getPackageClustersMQ() {
        Double packageSystemMQ = clusteringAlgorithm.computeMQ(this.graph, this.packages_graph_clusters);
        System.out.println("PACKAGE SYSTEM MQ: " + packageSystemMQ);
    }
}
