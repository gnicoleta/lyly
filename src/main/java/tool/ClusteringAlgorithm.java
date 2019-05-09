package tool;

import com.google.common.collect.Streams;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.ArrayList;
import java.util.Collection;
import com.google.common.collect.MoreCollectors;
public class ClusteringAlgorithm {

    Graph graph;


    Graph result;

    public ClusteringAlgorithm() {

    }
    public ClusteringAlgorithm(Graph graph) {
        this.graph = graph;
    }


    //this is  a maximum spanning tree
    //public static void MST_Prim(Graph graph, Node source) {
    //public void generateMST(String source,double tresshold) {
    //public Graph generateMST(String source) {
    public Graph generateMST() {

        System.out.println("GRAPH NODES TOTAL: " + this.graph.getVertices().size());
        //System.out.println("Edges of the graph" + graph.getEdges());
        //Node source_node = new Node(source);
        Collection<Node> visited_nodes = new ArrayList<>();

        Graph<Node, Edge> result = new UndirectedSparseMultigraph<Node, Edge>() {
        };

        Collection<Edge> possible_edges_from_visited_nodes = new ArrayList<>();
        Collection<Edge> the_mst_edges = new ArrayList<>();

        Collection<Node> all_nodes = this.graph.getVertices();
        //Node current_node = ClusteringAlgorithm.getNode(this.graph, source_node);
        Node current_node = (Node)Streams.findLast(this.graph.getVertices().stream()).get();

        Collection<Edge> edges_of_node;

        //while (current_node != destination) {
        while (!visited_nodes.contains(current_node)) {
            visited_nodes.add(current_node);
            edges_of_node = this.graph.getIncidentEdges(current_node);
            System.out.println(" CCCCCCCCCC     " + current_node.Node_Property());


            System.out.println("WWWW");
            possible_edges_from_visited_nodes.forEach(System.out::println);
            System.out.println("WWWW");

            //we add all the edges of the current_nod into the array with all the edges of ALL the visited nodes
            ClusteringAlgorithm.addEdgesFromVisitedNodes(edges_of_node, possible_edges_from_visited_nodes);

            Node current_node_parent = new Node();
            double MAX = 0;    //the max value will end up being stored in here
            for (Edge ii : possible_edges_from_visited_nodes) {

                //System.out.println("EDGE-ul" + ii);
                //the edge is from a visited node to an unvisited one
                //from the array with all edges of all the visited nodes, the smallest edge will be chosen
                if (visited_nodes.contains(ii.getEdgeSourceNode()) && ii.getWeight() > MAX && !visited_nodes.contains(ii.getEdgeDestiantionNode())) {
                    MAX = ii.getWeight();
                    current_node_parent = ii.getEdgeSourceNode();
                    current_node = ii.getEdgeDestiantionNode();
                }
            }

            //we add the the edges as they are parsed, in order to obtain the mst
            the_mst_edges.add(new Edge(current_node_parent, current_node, MAX));

            //to make sure we don't get on the same edge twice, we remove it from the array with all the edges of all visited nodes
            // the way I implemented a edge is ex: A - B, but also B - A (for undirected graphs)
            possible_edges_from_visited_nodes.remove(new Edge(current_node_parent, current_node, MAX));
            possible_edges_from_visited_nodes.remove(new Edge(current_node, current_node_parent, MAX));
        }

        the_mst_edges.remove(the_mst_edges.size()-2);
        for (Edge tt : the_mst_edges) {

            System.out.println(the_mst_edges.size());
            System.out.println(" -- " + tt.getEdgeSourceNode() + " - " + tt.getEdgeDestiantionNode() + ": " + tt.getWeight());
            //result.addEdge(tt, tt.getEdgeSourceNode(), tt.getEdgeDestiantionNode(), EdgeType.UNDIRECTED);
            //System.out.println(result.getVertices());
        }

        for (int i = 0; i < the_mst_edges.size()-1; i++) {
            result.addEdge(((ArrayList<Edge>) the_mst_edges).get(i), ((ArrayList<Edge>) the_mst_edges).get(i).getEdgeSourceNode(), ((ArrayList<Edge>) the_mst_edges).get(i).getEdgeDestiantionNode(), EdgeType.UNDIRECTED);
        }
        System.out.println(result.getVertices());
        System.out.println(result.getEdges());

        return result;

    }


    public static void addEdgesFromVisitedNodes(Collection<Edge> source, Collection<Edge> destination) {
        for (Edge t : source) {
            destination.add(new Edge(t.getEdgeDestiantionNode(), t.getEdgeSourceNode(), t.getWeight()));
            destination.add(t);
        }
    }


    public static Node getNode(Graph g, Node source) {
        Collection<Node> all_nodes = g.getVertices();

        for(Node i : all_nodes) {
            System.out.println(i.Node_Property() + " - " + source.Node_Property());
            System.out.println(i.Node_Property().equals(source.Node_Property()));
            if(i.Node_Property().equals(source.Node_Property())) {
                return i;
            }
        }
        return null;
    }

}
