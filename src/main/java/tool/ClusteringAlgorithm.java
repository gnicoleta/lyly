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

import java.awt.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.common.collect.MoreCollectors;
import sun.java2d.windows.GDIRenderer;

public class ClusteringAlgorithm {

    Graph graph;
    Graph result;
    Set<Graph> clusters = new HashSet<>();
    Map colorMap = new HashMap();

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
        try {
            if (args[3] != null) {
                System.out.println(" RORO " + args[3]);
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
        if (the_mst_edges.size() == 1) {
            System.out.println(the_mst_edges);
            System.out.println(((Edge) the_mst_edges.toArray()[0]).getEdgeDestiantionNode());
            result.addVertex(((Edge) the_mst_edges.toArray()[0]).getEdgeDestiantionNode());
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
        if (isolated_nodes == true) {
            //visitIsolatedNodes(graph, visited_nodes, the_mst_edges);
            for (Object o : graph.getVertices()) {
                if (!visited_nodes.contains((Node) o)) {
                    System.out.println("ASTA E NEVIZ: " + graph.getIncidentEdges(o));
                    System.out.println("AAAAAAAAAAAAAa " + visited_nodes.size());
                    visited_nodes.add((Node) o);
                    System.out.println("BBBBBB  " + visited_nodes.size());
                    Graph aux = generateMST(graph, (Node) o);
                    for (Object e : aux.getEdges()) {
                        the_mst_edges.add((Edge) e);
                    }
                }
            }
        }

        System.out.println("INIT2: " + graph.getVertexCount());
        System.out.println("VIZITATE2: " + visited_nodes.size());

        System.out.println("MST: " + result);

        return result;
    }


    public Graph generateClusters(Double tresshold, int no_kid_cluster) {
        this.graph = graph;
        Graph result = generateMST();
        Collection<Edge> edges = result.getEdges();
        Set<Node> edges_to_remove_nodesDest = new HashSet<>();
        Collection<Edge> edges_to_remove = new CopyOnWriteArrayList<>();
        //ArrayList<Graph> clusters = new ArrayList<>();

        Set<Edge> edges_to_remake = new HashSet<>();

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

        for (Node n : edges_to_remove_nodesDest) {
            System.out.println("~~~~~~" + n);
            clusters.add(generateMST(result, getNode(result, n)));
        }

        System.out.println("CLUSTERS SIZE" + clusters.size());
        for (Graph ge : clusters) {
            System.out.println(" *****  " + ge.getVertices());
        }

        System.out.println("ALLL REMOVED EDGES INITIAL: " + edges_to_remove);

/*
        for(Graph ge : clusters) {
            int found = 0;
           if(ge.getVertexCount() <= no_kid_cluster) {
               System.out.println("EU: " + ge);
               Collection<Node> vertices  = ge.getVertices();
               Collection<Edge> edgs  = ge.getEdges();




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
                   if(e.getEdgeSourceNode().equals(source)|| e.getEdgeDestiantionNode().equals(destination)||e.getEdgeSourceNode().equals(destination)|| e.getEdgeDestiantionNode().equals(source) ) {
                       //result.addEdge(e, e.getEdgeSourceNode(), e.getEdgeDestiantionNode(), EdgeType.UNDIRECTED);
                       edges_to_remake.add(e);
                       //edges_to_remove.remove(e);
                       found = 1;
                       if(source!= null) {
                           edges_to_remove_nodesDest.remove(source);
                       }
                       if(destination!= null) {
                           edges_to_remove_nodesDest.remove(destination);
                       }
                   } else {found = 0;}
               }
           }
        }*/

        for (Graph ge : clusters) {
            int found = 0;
            if (ge.getVertexCount() <= no_kid_cluster) {
                System.out.println("EU: " + ge + " -- " + ge.getVertexCount());
                Collection<Node> vertices = ge.getVertices();
                Collection<Edge> edgs = ge.getEdges();


                Node source = null;
                Node destination = null;

                System.out.println("EU TOO: " + edgs.size());


                for (Edge em : edgs) {
                    System.out.println(" %% " + ge + "  S_A LUAT EDGEUL: " + em);
                    try {
                        source = em.getEdgeSourceNode();
                        if (source == null) {
                            source = (Node) vertices.toArray()[0];
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                    destination = em.getEdgeDestiantionNode();
                    if (destination == null) {
                        destination = source;
                    }


                    System.out.println("MAMA " + source);
                    System.out.println("TATA " + destination);
                    for (Edge e : edges_to_remove) {
                        //System.out.println("WHAHA " + e.getEdgeDestiantionNode() + " -- " + e.getEdgeSourceNode() + " ~~~~ " + source + " qq" + e.getEdgeSourceNode().equals(source));
                        //if (e.getEdgeSourceNode().equals(source) || e.getEdgeDestiantionNode().equals(destination) || e.getEdgeSourceNode().equals(destination) || e.getEdgeDestiantionNode().equals(source)) {
                        if (e.getEdgeSourceNode().equals(source) || e.getEdgeDestiantionNode().equals(destination)) {
                            //result.addEdge(e, e.getEdgeSourceNode(), e.getEdgeDestiantionNode(), EdgeType.UNDIRECTED);
                            System.out.println(" DIN CARE SE VA REFACE: " + e);
                            edges_to_remake.add(e);
                            //edges_to_remove.remove(e);
                            found = 1;
                            if (source != null) {
                                System.out.println("REMOVED: soureced: " + source + " din edge: " + e);
                                edges_to_remove_nodesDest.remove(source);
                            }
                            if (destination != null) {
                                System.out.println("REMOVED: soureced: " + source + " din edge: " + e);
                                edges_to_remove_nodesDest.remove(destination);
                            }
                        } else {
                            found = 0;
                        }
                    }
                }
                if (edgs.size() == 0) {
                    try {
                        if (source == null) {
                            source = (Node) vertices.toArray()[0];
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                    if (destination == null) {
                        destination = source;
                    }

                    System.out.println("MAMA " + source);
                    System.out.println("TATA " + destination);
                    for (Edge e : edges_to_remove) {
                        System.out.println("WHAHA " + e.getEdgeDestiantionNode() + " -- " + e.getEdgeSourceNode() + " ~~~~ " + source + " qq" + e.getEdgeSourceNode().equals(source));
                        //if (e.getEdgeSourceNode().equals(source) || e.getEdgeDestiantionNode().equals(destination) || e.getEdgeSourceNode().equals(destination) || e.getEdgeDestiantionNode().equals(source)) {
                        if (e.getEdgeSourceNode().equals(source) || e.getEdgeDestiantionNode().equals(destination)) {
                            //result.addEdge(e, e.getEdgeSourceNode(), e.getEdgeDestiantionNode(), EdgeType.UNDIRECTED);
                            System.out.println(" DIN CARE SE VA REFACE: " + e);
                            edges_to_remake.add(e);
                            //edges_to_remove.remove(e);
                            found = 1;
                            if (source != null) {
                                System.out.println("REMOVED: soureced: " + source + " din edge: " + e);
                                edges_to_remove_nodesDest.remove(source);
                            }
                            if (destination != null) {
                                System.out.println("REMOVED: soureced: " + source + " din edge: " + e);
                                edges_to_remove_nodesDest.remove(destination);
                            }
                        } else {
                            found = 0;
                        }
                    }
                }
            }
        }


        clusters.clear();

        Node parent_node_mother_remake = null;
        Node parent_node_father_remake = null;

        Set<Node> nodes_remade = new HashSet<>();
        Set<Node> nodes_to_revisit = new HashSet<>();

        System.out.println(" EDEGES NODE DEST : " + edges_to_remove_nodesDest);
        System.out.println(" EDEGES REMOVE NOW : " + edges_to_remove);
        System.out.println(" EDEGES REMAKE NOW : " + edges_to_remake);

        System.out.println("NEDES " + nodes_remade);


        for (Edge e : edges_to_remake) {
            System.out.println("REMAKE: " + e);
            //edges_to_remove.remove(e);
            System.out.println("LLLL" + e + " --- " + !(edges_to_remove_nodesDest.contains(e.getEdgeDestiantionNode()) || edges_to_remove_nodesDest.contains(e.getEdgeSourceNode())) + " -- " + !(nodes_remade.contains(e.getEdgeSourceNode()) || nodes_remade.contains(e.getEdgeDestiantionNode())));
            //if (!(edges_to_remove_nodesDest.contains(e.getEdgeDestiantionNode()) || edges_to_remove_nodesDest.contains(e.getEdgeSourceNode())) && !(nodes_remade.contains(e.getEdgeSourceNode()) || nodes_remade.contains(e.getEdgeDestiantionNode()))) {

            if (!(edges_to_remove_nodesDest.contains(e.getEdgeDestiantionNode())) && !(nodes_remade.contains(e.getEdgeSourceNode()) || nodes_remade.contains(e.getEdgeDestiantionNode()))) {

                //if (!(nodes_remade.contains(e.getEdgeSourceNode()) || nodes_remade.contains(e.getEdgeDestiantionNode()))) {

                System.out.println("DE AICI: " + e.getEdgeSourceNode());
                edges_to_remove.remove(e);
                result.addEdge(e, e.getEdgeSourceNode(), e.getEdgeDestiantionNode());
                int new_clst_size = generateMST(result, e.getEdgeSourceNode()).getVertexCount();
                System.out.println("NEW CLST SIZE: " + new_clst_size + "  e: " + e);
                if (new_clst_size <= no_kid_cluster) {

                    System.out.println("ASTA SE ADUAGA: " + e + " ok: " + edges_to_remove.contains(e) + " sura: " + e.getEdgeSourceNode() + " det: " + e.getEdgeDestiantionNode());
                    edges_to_remove.remove(e);
                    result.addEdge(e, e.getEdgeSourceNode(), e.getEdgeDestiantionNode());
                    System.out.println("AVIOANE: " + e);
                    nodes_remade.add(e.getEdgeSourceNode());
                    nodes_to_revisit.add(e.getEdgeDestiantionNode());
                    //parent_node_mother_remake = e.getEdgeSourceNode();
                    //parent_node_father_remake = e.getEdgeSourceNode();
                } else if (new_clst_size > no_kid_cluster) {
                    System.out.println("CNAD e MAI MARE: " + edges_to_remove + " EDGEUL CURENT " + e);
                }
            }
        }

        System.out.println("REMADE: " + nodes_remade);
        System.out.println("REVISIT: " + nodes_to_revisit);

        System.out.println(" REMO " + edges_to_remove);

        for (Edge e : edges_to_remove) {
            //edges_to_remove_nodesDest.remove(e.getEdgeSourceNode());
            edges_to_remove_nodesDest.add(e.getEdgeSourceNode());
            edges_to_remove_nodesDest.add(e.getEdgeDestiantionNode());
            //edges_to_remove_nodesDest.remove(e.getEdgeDestiantionNode());
        }

        for (Node n : nodes_remade) {
            edges_to_remove_nodesDest.add(n);
        }

        System.out.println("EDEEE " + edges_to_remove);
        System.out.println("NEEE " + edges_to_remove_nodesDest);


        //clusters.clear();


        for (Node n : edges_to_remove_nodesDest) {
            System.out.println("^^^^^" + n);
            clusters.add(generateMST(result, getNode(result, n), null, "MACAROANE"));
        }


        System.out.println("CLUSTERS SIZE" + clusters.size());
        for (Graph ge : clusters) {
            System.out.println(" ))))   " + ge.getVertices());
        }


        for (Graph ge : clusters) {
            if (ge.getVertexCount() <= no_kid_cluster) {
                /*Iterator<Edge> e = edges_to_remove.iterator();
                while(e.hasNext()) {
                    for(Node n : vertices) {
                        System.out.println("PAPA: " + e.getEdgeSourceNode() + " cu "+ n + " -- ok: " + (e.getEdgeSourceNode() == n));
                        if(e.getEdgeSourceNode() == n || e.getEdgeDestiantionNode() == n) {

                            System.out.println("2PAPA: " + n);
                            result.addEdge(e, e.getEdgeSourceNode(), e.getEdgeDestiantionNode());
                            edges_to_remove.remove(e);
                            edges_to_remove_nodesDest.remove(n);
                        }
                    }
                }*/
                for (Edge e : edges_to_remove) {
                    //System.out.println("PAPA: " + e.getEdgeSourceNode() + " cu "+ n + " -- ok: " + (e.getEdgeSourceNode() == n));

                    if (generateMST(result, e.getEdgeSourceNode()).getVertexCount() <= no_kid_cluster || generateMST(result, e.getEdgeDestiantionNode()).getVertexCount() <= no_kid_cluster) {
                        //if(e.getEdgeSourceNode() == n) {

                        System.out.println("2PAPA: " + e);
                        result.addEdge(e, e.getEdgeSourceNode(), e.getEdgeDestiantionNode());
                        edges_to_remove.remove(e);
                        //edges_to_remove_nodesDest.remove(n);
                    }
                }
            }
        }

        System.out.println(" REMO " + edges_to_remove);

        clusters.clear();


        for (Node n : edges_to_remove_nodesDest) {
            System.out.println("2^^^^^" + n);
            clusters.add(generateMST(result, getNode(result, n), null, "MACAROANE CU AVIOANE"));
        }


     /*   System.out.println("CLUSTERS SIZE" + clusters.size());
        for(Graph ge : clusters) {
            System.out.println(" $$$$$$  " + ge.hashCode()+ " -- " + ge.getVertices());
        }*/

        //colorClusters(clusters);
        ArrayList<Graph> to_remove_clst = new ArrayList<>();
        for (int i = 0; i < clusters.size(); i++) {
            for (int j = i + 1; j < clusters.size(); j++) {
                to_remove_clst.add(removeDupl((Graph) clusters.toArray()[i], (Graph) clusters.toArray()[j]));
            }
        }

        for (Graph g : to_remove_clst) {
            if (g == null) continue;
            clusters.remove(g);
            System.out.println(" HASH: " + g.hashCode() + " --0 " + g);
        }


        int clst_id = 0;
        initiazeColorMap(clusters.size());
        System.out.println("CLUSTERS SIZE" + clusters.size());
        for (Graph ge : clusters) {
            System.out.println(" $$$$$$  " + ge.hashCode() + " -- " + ge.getVertices());
            clst_id++;
            for (Object n : ge.getVertices()) {
                ((Node) n).setCluster_id(clst_id);
                System.out.println("MACAROANE : " + ((Node) n).getCluster_id());
                colorNode(((Node) n).getCluster_id(), (Node) n);
            }
        }


        return result;
        //return clusters;
    }

//    public void checkForDuplicates() {
//
//    }

    public void colorClusters(Set<Graph> clusters) {
        initiazeColorMap(clusters.size());
        int contor = 1;

        System.out.println("MAPA : " + colorMap);

        for (Graph g : clusters) {
            for (Object n : g.getVertices()) {
                ((Node) n).setCluster_id(contor);
                colorNode(((Node) n).getCluster_id(), (Node) n);
                System.out.println("CASTANE: " + ((Node) n).getCluster_id());
            }
            contor++;
        }
    }

    public Set<Graph> getClusters() {
        return this.clusters;
    }


    public void colorNode(int cluster_id, Node n) {
        //n.setCluster_id(color);
        System.out.println("COCO" + colorMap.get(cluster_id));
        n.setColor((Color) colorMap.get(cluster_id));
    }


    public void initiazeColorMap(Integer no_clusters) {
        Random rand = new Random();
        System.out.println("CULORI: " + no_clusters);
        for (int i = 1; i <= no_clusters; i++) {
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color randomColor = new Color(r, g, b);
            System.out.println("COCOS " + randomColor);
            colorMap.put(i, randomColor);
        }
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


    public Graph removeDupl(Graph one, Graph two) {

        Collection<Node> nodes_one = new ArrayList();
        Collection<Node> nodes_two = new ArrayList();

        nodes_one = one.getVertices();
        nodes_two = two.getVertices();

        int contor = 0;

        if (nodes_one.size() == nodes_two.size()) {
            for (Node n : nodes_one) {
                if (nodes_two.contains(n)) {
                    contor++;
                }
            }
            if (contor == nodes_one.size()) {
                return one;
            } else return null;
        }
        return null;

    }

}
