package tool;

import com.google.common.collect.Streams;
import com.sun.org.apache.xpath.internal.operations.Bool;
import edu.uci.ics.jung.algorithms.cluster.BicomponentClusterer;
import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.*;

import com.google.common.collect.MoreCollectors;

public class ClusteringAlgorithm {

    Graph graph;
    Graph result;

    public ClusteringAlgorithm() {

    }

    public ClusteringAlgorithm(Graph graph) {
        this.graph = graph;
    }

/*
    public void visitIsolatedNodes(Graph graph, Collection<Node> visited_nodes, Collection<Edge> the_mst_edges) {
        for(Object n : graph.getVertices())  {
            if(!visited_nodes.contains(getNode(graph, (Node)n))) {
                System.out.println("AAAAAAAAAAAAAa " + visited_nodes.size());
                visited_nodes.add((Node)n);
                Graph result = generateMST(graph, (Node)n);
                for(Object e: result.getEdges()) {
                    the_mst_edges.add((Edge)e);
                }
            }
        }
    }*/

    public Graph generateMST(Object... args) {
        Graph graph = null;
        Node current_node = null;
        boolean isolated_nodes = false;

        int conto = 0;
        if (args.length == 0) {
            graph = this.graph;
            current_node = (Node) Streams.findLast(this.graph.getVertices().stream()).get();
        } else {
            //System.out.println("CEAU" + args[0]);
            if (args[0] instanceof Graph) {
                graph = (Graph) args[0];
            } else {
                //System.out.println("CEAU3" + args[0]);
                graph = this.graph;
            }
                if (args[1] instanceof Node) {
                    current_node = getNode(graph, (Node) args[1]);
                } else {
                    current_node = (Node) Streams.findLast(this.graph.getVertices().stream()).get();
                }

            }

        try {
            if (args[2] != null) {
                System.out.println(" AMU " + args[2]);
                isolated_nodes = true;
            }
        } catch (Exception e) {

        }
        Collection<Node> visited_nodes = new ArrayList<>();
        Graph<Node, Edge> result = new UndirectedSparseMultigraph<Node, Edge>() {
        };

        Collection<Edge> possible_edges_from_visited_nodes = new ArrayList<>();
        Collection<Edge> the_mst_edges = new ArrayList<>();
        Collection<Edge> edges_of_node;

        while (!visited_nodes.contains(current_node)) {
            conto++;
            //System.out.println("CURENT" + conto + " -- " + current_node);
            visited_nodes.add(current_node);
            edges_of_node = graph.getIncidentEdges(current_node);
            //System.out.println("EDE" + edges_of_node);
            //System.out.println(" CCCCCCCCCC     " + current_node.Node_Property());

            //System.out.println("WWWW");
            possible_edges_from_visited_nodes.forEach(System.out::println);
            //System.out.println("WWWW");

            //we add all the edges of the current_nod into the array with all the edges of ALL the visited nodes
            ClusteringAlgorithm.addEdgesFromVisitedNodes(edges_of_node, possible_edges_from_visited_nodes);

            Node current_node_parent = new Node();
            double MAX = 0;    //the max value will end up being stored in here
            for (Edge ii : possible_edges_from_visited_nodes) {

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
        the_mst_edges.remove(the_mst_edges.size() - 2);
        System.out.println("ABANANAANA" + the_mst_edges.size());
        if(the_mst_edges.size() == 1) {
            System.out.println(the_mst_edges);
            System.out.println(((Edge)the_mst_edges.toArray()[0]).getEdgeDestiantionNode());
            result.addVertex(((Edge)the_mst_edges.toArray()[0]).getEdgeDestiantionNode());
            //the_mst_edges.add(new Edge())
        }
        /*for (Edge tt : the_mst_edges) {
            System.out.println(the_mst_edges.size());
            System.out.println(" -- " + tt.getEdgeSourceNode() + " - " + tt.getEdgeDestiantionNode() + ": " + tt.getWeight());
            //result.addEdge(tt, tt.getEdgeSourceNode(), tt.getEdgeDestiantionNode(), EdgeType.UNDIRECTED);
            //System.out.println(result.getVertices());
        }*/
        for (int i = 0; i < the_mst_edges.size() - 1; i++) {
            result.addEdge(((ArrayList<Edge>) the_mst_edges).get(i), ((ArrayList<Edge>) the_mst_edges).get(i).getEdgeSourceNode(), ((ArrayList<Edge>) the_mst_edges).get(i).getEdgeDestiantionNode(), EdgeType.UNDIRECTED);
        }
        //System.out.println(result.getVertices());
        //System.out.println(result.getEdges());

        System.out.println("INIT: " + graph.getVertexCount());
        System.out.println("VIZITATE: " + visited_nodes.size());


        System.out.println("KKKKKKK " + isolated_nodes);
        if(isolated_nodes == true) {
            //visitIsolatedNodes(graph, visited_nodes, the_mst_edges);
            for(Object o : graph.getVertices()) {
                if(!visited_nodes.contains((Node)o)) {
                    System.out.println("ASTA E NEVIZ: " + graph.getIncidentEdges(o));
                    System.out.println("AAAAAAAAAAAAAa " + visited_nodes.size());
                    visited_nodes.add((Node)o);
                    System.out.println("BBBBBB  " + visited_nodes.size());
                    Graph aux = generateMST(graph, (Node)o);
                    for(Object e: aux.getEdges()) {
                        the_mst_edges.add((Edge)e);
                    }
                }
            }
        }

        System.out.println("INIT2: " + graph.getVertexCount());
        System.out.println("VIZITATE2: " + visited_nodes.size());

        return result;
    }


    public Graph generateClusters(Double tresshold) {
        this.graph = graph;
        Graph result = generateMST();
        Collection<Edge> edges = result.getEdges();
        Set<Node> edges_to_remove_nodesDest = new HashSet<>();
        Collection<Edge> edges_to_remove = new ArrayList<>();
        ArrayList<Graph> clusters = new ArrayList<>();

        ArrayList<Edge> edges_to_remake = new ArrayList<>();

        //int contor = 0;
        for (Edge e : edges) {
            System.out.println("--------------" + e);
            if (e.getWeight() > tresshold) {
                System.out.println("~~~~~~~~~~ taiat " + e);
                edges_to_remove_nodesDest.add(e.getEdgeDestiantionNode());
                edges_to_remove_nodesDest.add(e.getEdgeSourceNode());
                edges_to_remove.add(e);
            }
        }

        for (Edge ed : edges_to_remove) {
            result.removeEdge(ed);
        }

        for(Node n : edges_to_remove_nodesDest) {
            System.out.println("~~~~~~" + n);
           clusters.add(generateMST(result, getNode(result, n)));
        }

        System.out.println("CLUSTERS SIZE" + clusters.size());
        for(Graph ge : clusters) {
            System.out.println(" *****  " + ge.getVertices());
        }


        for(Graph ge : clusters) {
           if(ge.getEdgeCount() <= 1) {
               Collection<Node> vertices  = ge.getVertices();
               Node source = (Node)vertices.toArray()[0];
               Node destination = null;
               try {
                   destination = (Node) vertices.toArray()[1];
               }catch (ArrayIndexOutOfBoundsException e) {

               }
               if(destination == null) {
                   destination = source;
               }

               System.out.println("MAMA " + source);
               System.out.println("TATA " + destination);
               for(Edge e: edges_to_remove) {
                   System.out.println("WHAHA " + e.getEdgeDestiantionNode() + " -- " + e.getEdgeSourceNode() + " ~~~~ " + source + " qq"+ e.getEdgeSourceNode().equals(source));
                   if(e.getEdgeSourceNode().equals(source)|| e.getEdgeDestiantionNode().equals(destination) ) {
                       result.addEdge(e, e.getEdgeSourceNode(), e.getEdgeDestiantionNode(), EdgeType.UNDIRECTED);
                       edges_to_remake.add(e);
                       //edges_to_remove.remove(e);
                       if(source!= null) {
                           edges_to_remove_nodesDest.remove(source);
                       }
                       if(destination!= null) {
                           edges_to_remove_nodesDest.remove(destination);
                       }
                   }
               }
           }
        }

        for(Edge e : edges_to_remake) {
            edges_to_remove.remove(e);
        }

        clusters.clear();


        for(Node n : edges_to_remove_nodesDest) {
            System.out.println("^^^^^" + n);
            clusters.add(generateMST(result, getNode(result, n)));
        }

        System.out.println("CLUSTERS SIZE" + clusters.size());
        Integer contor = 1;
        for(Graph ge : clusters) {
            System.out.println(" $$$$$$  " + ge.getVertices());

            for(Object n : ge.getVertices()) {
                ((Node)n).cluster_id = contor;
            }
        }

        return result;
    }


    public void colorNode(String color, Node n) {
        //n.setCluster_id(color);
    }

    private void cutTresshold(Edge e, Graph g, Double tresshold) {
        if (e.getWeight() > tresshold) {
            g.getEdges().remove(e);
        }
    }


    public static void addEdgesFromVisitedNodes(Collection<Edge> source, Collection<Edge> destination) {
        for (Edge t : source) {
            destination.add(new Edge(t.getEdgeDestiantionNode(), t.getEdgeSourceNode(), t.getWeight()));
            destination.add(t);
        }
    }


    public static Node getNode(Graph g, Node source) {
        Collection<Node> all_nodes = g.getVertices();

        for (Node i : all_nodes) {
            System.out.println(i.Node_Property() + " - " + source.Node_Property());
            System.out.println(i.Node_Property().equals(source.Node_Property()));
            if (i.Node_Property().equals(source.Node_Property())) {
                return i;
            }
        }
        return null;
    }

}
