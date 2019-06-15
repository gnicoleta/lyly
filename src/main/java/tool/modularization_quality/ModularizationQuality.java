package tool.modularization_quality;

import edu.uci.ics.jung.graph.Graph;
import tool.graph_builder.Edge;
import tool.graph_builder.Node;

import java.util.*;

public class ModularizationQuality {

    private Graph initial_Graph;

    public ModularizationQuality() {

    }

    public ModularizationQuality(Graph graph) {
        this.initial_Graph = graph;
    }
    public void setGraph(Graph graph) {
        this.initial_Graph = graph;
    }

    public double computeMQ(Graph initialGraph, Set<Graph> clusters) {
        Collection<Edge> intraedges = new ArrayList<>(); //intern edges in a cluster
        HashMap<Graph, Double> clusters_mq = new HashMap<>();
        double system_mq = 0;
        for (Graph cluster : clusters) {
            intraedges = cluster.getEdges();
            Collection<Node> nodes = cluster.getVertices();
            Collection<Edge> interedges = new ArrayList<>(); //external edges between this cluster and other clusters

            //for each node in the cluster, we get its inital state from the initial graph
            //and add all of its edges to the interedges
            //interedges at first will contain all the edges of the cluster from the initial place in the initial graph
            for (Node n : nodes) {
                Node node = getNode(initialGraph, n);
                interedges.addAll(initialGraph.getIncidentEdges(node));
            }

            //we now remove the intraedges (internal edges) from the initial interedges (tthat contains ALL the edges)
            //after this the interedges will contain only the external edges of a cluster
            for (Edge e : intraedges) {
                interedges.remove(e);
            }

            double internal_edges_sum = 0;
            double external_edges_sum = 0;
            for (Edge e : intraedges) {
                internal_edges_sum += e.getWeight();
            }
            for (Edge e : interedges) {
                external_edges_sum += e.getWeight();
            }
            double mq = 2 * internal_edges_sum / (2 * internal_edges_sum + external_edges_sum);
            clusters_mq.put(cluster, mq);
        }
        Iterator<Graph> keySetIterator = clusters_mq.keySet().iterator();

        while (keySetIterator.hasNext()) {
            Graph key = keySetIterator.next();
            //System.out.println("key: " + key.hashCode() + " value: " + clusters_mq.get(key));
            double cluster_CF = clusters_mq.get(key);
            if(Double.isNaN(cluster_CF)) cluster_CF = 0.0;
            //system_mq += clusters_mq.get(key);
            system_mq += cluster_CF;
        }
        System.out.println("TOTAL MQ: " + system_mq);
        return system_mq;
    }


    public static Node getNode(Graph g, Node source) {
        Collection<Node> all_nodes = g.getVertices();

        for (Node i : all_nodes) {
            if (i.Node_Property().equals(source.Node_Property())) {
                return i;
            }
        }
        return null;
    }
}
