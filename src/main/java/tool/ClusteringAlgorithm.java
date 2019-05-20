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

    public void setGraph(Graph g) {
        this.graph = g;
    }

    public Graph generateMST(Object... args) {
        Graph graph = null;
        Node current_node = null;
        boolean isolated_nodes = false;

        if (args.length == 0) {
            graph = this.graph;
            current_node = (Node) Streams.findLast(this.graph.getVertices().stream()).get();
        } else {
            if (args[0] instanceof Graph) {
                graph = (Graph) args[0];
            } else {
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
                isolated_nodes = true;
            }
        } catch (Exception e) {

        }
        try {
            if (args[3] != null) {
                //System.out.println(" Name of MST " + args[3]);
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
            visited_nodes.add(current_node);
            edges_of_node = graph.getIncidentEdges(current_node);
            //possible_edges_from_visited_nodes.forEach(System.out::println);

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
        the_mst_edges.remove(the_mst_edges.size() - 2); //don't add the null value ( node - null )
        if (the_mst_edges.size() == 1) {
            result.addVertex(((Edge) the_mst_edges.toArray()[0]).getEdgeDestiantionNode());
        }
        for (int i = 0; i < the_mst_edges.size() - 1; i++) {
            result.addEdge(((ArrayList<Edge>) the_mst_edges).get(i), ((ArrayList<Edge>) the_mst_edges).get(i).getEdgeSourceNode(), ((ArrayList<Edge>) the_mst_edges).get(i).getEdgeDestiantionNode(), EdgeType.UNDIRECTED);
        }

        //System.out.println("INIT: " + graph.getVertexCount());
        //System.out.println("VIZITATE: " + visited_nodes.size());

        //visit all the node, including the isolated ones, only if 'true' selected in method call
        if (isolated_nodes == true) {
            //visitIsolatedNodes(graph, visited_nodes, the_mst_edges);
            for (Object o : graph.getVertices()) {
                if (!visited_nodes.contains((Node) o)) {
                    visited_nodes.add((Node) o);
                    Graph aux = generateMST(graph, (Node) o);
                    for (Object e : aux.getEdges()) {
                        the_mst_edges.add((Edge) e);
                    }
                }
            }
        }
        //System.out.println("MST: " + result);
        return result;
    }

/*
    public Graph generateClusters(Double tresshold, int no_kid_cluster) {
        this.graph = graph;//inital graph
        Graph result = generateMST();
        Collection<Edge> edges = result.getEdges();
        Set<Node> edges_to_remove_nodesDest = new HashSet<>();
        Collection<Edge> edges_to_remove = new CopyOnWriteArrayList<>();
        Set<Edge> edges_to_remake = new HashSet<>();

        //based on the tresshold I add the edges that need to be cut to the edges_to_remove list
        //everywhere you cut the tree, 2 pieces will remain from the source and destination node of the cut edge
        //those nodes will be added to a list of nodes that should be raparsed in order to get the clusters
        for (Edge e : edges) {
            if (e.getWeight() > tresshold) {
                edges_to_remove_nodesDest.add(e.getEdgeDestiantionNode());
                edges_to_remove_nodesDest.add(e.getEdgeSourceNode());
                edges_to_remove.add(e);
            }
        }

        //we remove the edges from the MST
        for (Edge ed : edges_to_remove) {
            result.removeEdge(ed);
        }

        //revisit the tree from the splitted ends of the cut edge
        //add those clusters to clusters list
        for (Node n : edges_to_remove_nodesDest) {
            clusters.add(generateMST(result, getNode(result, n)));
        }

        //now I check for baby clusters
        //in the method signature you can give a limit number of nodes that a cluster should have
        //parse the clusters list and check if the clusters have the no of nodes > no limit given
        //System.out.println("CLUSTERS: " + clusters);
        for (Graph ge : clusters) {
            if (ge.getVertexCount() <= no_kid_cluster) {
                Collection<Node> vertices = ge.getVertices();

                //contains all the edges of the current baby cluster that has no of nodes <= no of nodes limit (given by user)
                Collection<Edge> edgs = ge.getEdges();
                Node source = null;
                Node destination = null;

                //from all the edges of the baby cluster we choose the veaviest one
                if (edgs.size() > 0) {
                    double MAX = Integer.MIN_VALUE;
                    Edge heaviest_edge = null;
                    for (Edge em : edgs) {
                        if (em.getWeight() > MAX) {
                            MAX = em.getWeight();
                            heaviest_edge = em;
                        }
                    }
                    try {
                        source = heaviest_edge.getEdgeSourceNode();
                        if (source == null) {
                            source = (Node) vertices.toArray()[0];
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                    destination = heaviest_edge.getEdgeDestiantionNode();
                    if (destination == null) {
                        destination = source;
                    }
                    remakeEdges(edges_to_remove, edges_to_remake, source, destination, edges_to_remove_nodesDest);
                }

                //parse the list of edges of the current baby cluster
                /*for (Edge em : edgs) {
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
                    remakeEdges(edges_to_remove, edges_to_remake, source, destination, edges_to_remove_nodesDest);
                }*/

    //meaning the cluster contains only one node
                /*if (edgs.size() == 0) { //aici
                    try {
                        if (source == null) {
                            source = (Node) vertices.toArray()[0];
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                    if (destination == null) {
                        destination = source;
                    }
                    remakeEdges(edges_to_remove, edges_to_remake, source, destination, edges_to_remove_nodesDest);
                }
            }
        }

        //clear the clusters list because I need to redo it
        clusters.clear();
        Set<Node> nodes_remade = new HashSet<>();
        //Set<Node> nodes_to_revisit = new HashSet<>();

        /**
         * parse the edges to remake, and remove the edges that need to be redone from the edges to remove list
         * add those edge to the graph itself
         * check the size of the new cluster to be done
         * if the size is right I need to keep track of the nodes I should revisit
         * */
        /*for (Edge e : edges_to_remake) { //aici
            if (!(edges_to_remove_nodesDest.contains(e.getEdgeDestiantionNode())) && !(nodes_remade.contains(e.getEdgeSourceNode()) || nodes_remade.contains(e.getEdgeDestiantionNode()))) {
                edges_to_remove.remove(e);
                result.addEdge(e, e.getEdgeSourceNode(), e.getEdgeDestiantionNode());
                int new_clst_size = generateMST(result, e.getEdgeSourceNode()).getVertexCount();
                if (new_clst_size <= no_kid_cluster) {
                    edges_to_remove.remove(e);
                    result.addEdge(e, e.getEdgeSourceNode(), e.getEdgeDestiantionNode());
                    nodes_remade.add(e.getEdgeSourceNode());
                    //nodes_to_revisit.add(e.getEdgeDestiantionNode());
                } else if (new_clst_size > no_kid_cluster) {
                    //System.out.println("CNAD e MAI MARE: " + edges_to_remove + " EDGEUL CURENT " + e);
                }
            }
        }

        //since I already removed the edges that need to be redone,
        //the edges that are still in this list mean the cut edges of differemt clusters, and I need to revisit them
        for (Edge e : edges_to_remove) {
            edges_to_remove_nodesDest.add(e.getEdgeSourceNode());
            edges_to_remove_nodesDest.add(e.getEdgeDestiantionNode());
        }

        edges_to_remove_nodesDest.addAll(nodes_remade);

        //based on the nodes that should be revist, do the clusters
        for (Node n : edges_to_remove_nodesDest) {
            clusters.add(generateMST(result, getNode(result, n), null, "MACAROANE"));
        }

        //check for baby clusters again
        //redo any remaining edges in the edges_to_remove that will make the baby cluster abide to the given node limit
        for (Graph ge : clusters) {
            if (ge.getVertexCount() <= no_kid_cluster) {
                for (Edge e : edges_to_remove) {
                    if (generateMST(result, e.getEdgeSourceNode()).getVertexCount() <= no_kid_cluster || generateMST(result, e.getEdgeDestiantionNode()).getVertexCount() <= no_kid_cluster) {
                        result.addEdge(e, e.getEdgeSourceNode(), e.getEdgeDestiantionNode());
                        edges_to_remove.remove(e);
                        //edges_to_remove_nodesDest.remove(n);
                    }
                }
            }
        }

        //clear the clusters because they need to be redone
        clusters.clear();

        //do the clusters
        for (Node n : edges_to_remove_nodesDest) {
            clusters.add(generateMST(result, getNode(result, n), null, "MACAROANE CU AVIOANE"));
        }

        //make sure I do not have duplicate clusters (made by same tree but visited from different nodes
        // the path remains the same, the order is different, their hash will be different but the content of the object is identical)
        //if there are such clusters, I must remove them
        ArrayList<Graph> to_remove_clst = new ArrayList<>();
        for (int i = 0; i < clusters.size(); i++) {
            for (int j = i + 1; j < clusters.size(); j++) {
                to_remove_clst.add(removeDupl((Graph) clusters.toArray()[i], (Graph) clusters.toArray()[j]));
            }
        }
        for (Graph g : to_remove_clst) {
            if (g == null) continue;
            clusters.remove(g);
        }
        colorNodesInClusters(clusters);

        return result;
    }*/


    public Graph generateClusters(Double tresshold, int no_kid_cluster) {
        this.graph = graph;//inital graph
        Graph result = generateMST();
        Collection<Edge> edges = result.getEdges();
        Set<Node> edges_to_remove_nodesDest = new HashSet<>();
        Collection<Edge> edges_to_remove = new CopyOnWriteArrayList<>();
        Set<Edge> edges_to_remake = new HashSet<>();

        //based on the tresshold I add the edges that need to be cut to the edges_to_remove list
        //everywhere you cut the tree, 2 pieces will remain from the source and destination node of the cut edge
        //those nodes will be added to a list of nodes that should be raparsed in order to get the clusters
        for (Edge e : edges) {
            if (e.getWeight() > tresshold) {
                edges_to_remove_nodesDest.add(e.getEdgeDestiantionNode());
                edges_to_remove_nodesDest.add(e.getEdgeSourceNode());
                edges_to_remove.add(e);
            }
        }

        //we remove the edges from the MST
        for (Edge ed : edges_to_remove) {
            result.removeEdge(ed);
        }

        //revisit the tree from the splitted ends of the cut edge
        //add those clusters to clusters list
        for (Node n : edges_to_remove_nodesDest) {
            clusters.add(generateMST(result, getNode(result, n)));
        }

        //now I check for baby clusters
        //in the method signature you can give a limit number of nodes that a cluster should have
        //parse the clusters list and check if the clusters have the no of nodes > no limit given
        //System.out.println("CLUSTERS: " + clusters);
        for (Graph ge : clusters) {
            if (ge.getVertexCount() <= no_kid_cluster) {
                Collection<Node> vertices = ge.getVertices();

                //contains all the edges of the current baby cluster that has no of nodes <= no of nodes limit (given by user)

                Collection<Node> all_nodes_from_baby_cluster = ge.getVertices();
                Collection<Edge> all_edged_from_baby_cluster = ge.getEdges(); //edges from within the cluster
                //Collection<Edge> edgs = ge.getEdges();
                Collection<Edge> edgs = new ArrayList<>();

                for (Node n : all_nodes_from_baby_cluster) {
                    edgs.addAll(this.graph.getIncidentEdges(getNode(this.graph, n)));
                }

                for (Edge e : all_edged_from_baby_cluster) {
                    edgs.remove(e); //edges from outside the cluster, those who were previous broken
                }

                Node source = null;
                Node destination = null;

                //from all the edges (that were broken) of the baby cluster we choose the heaviest one
                if (edgs.size() > 0) {
                    double MAX = Integer.MIN_VALUE;
                    Edge heaviest_edge = null;
                    for (Edge em : edgs) {
                        if (em.getWeight() > MAX) {
                            MAX = em.getWeight();
                            heaviest_edge = em;
                        }
                    }
                    try {
                        source = heaviest_edge.getEdgeSourceNode();
                        if (source == null) {
                            source = (Node) vertices.toArray()[0];
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                    destination = heaviest_edge.getEdgeDestiantionNode();
                    if (destination == null) {
                        destination = source;
                    }
                    remakeEdges(edges_to_remove, edges_to_remake, source, destination, edges_to_remove_nodesDest);
                }

                //parse the list of edges of the current baby cluster
                /*for (Edge em : edgs) {
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
                    remakeEdges(edges_to_remove, edges_to_remake, source, destination, edges_to_remove_nodesDest);
                }*/

                //meaning the cluster contains only one node
                /*if (edgs.size() == 0) {
                    System.out.println("ODATA e AICI");
                    try {
                        if (source == null) {
                            source = (Node) vertices.toArray()[0];
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                    if (destination == null) {
                        destination = source;
                    }
                    remakeEdges(edges_to_remove, edges_to_remake, source, destination, edges_to_remove_nodesDest);
                }*/
            }
        }

        //clear the clusters list because I need to redo it
        clusters.clear();
        Set<Node> nodes_remade = new HashSet<>();
        //Set<Node> nodes_to_revisit = new HashSet<>();

        /**
         * parse the edges to remake, and remove the edges that need to be redone from the edges to remove list
         * add those edge to the graph itself
         * check the size of the new cluster to be done
         * if the size is right I need to keep track of the nodes I should revisit
         * */
        for (Edge e : edges_to_remake) {
            if (!(edges_to_remove_nodesDest.contains(e.getEdgeDestiantionNode())) && !(nodes_remade.contains(e.getEdgeSourceNode()) || nodes_remade.contains(e.getEdgeDestiantionNode()))) {
                edges_to_remove.remove(e);
                result.addEdge(e, e.getEdgeSourceNode(), e.getEdgeDestiantionNode());
                int new_clst_size = generateMST(result, e.getEdgeSourceNode()).getVertexCount();
                if (new_clst_size <= no_kid_cluster) {
                    edges_to_remove.remove(e);
                    result.addEdge(e, e.getEdgeSourceNode(), e.getEdgeDestiantionNode());
                    nodes_remade.add(e.getEdgeSourceNode());
                    //nodes_to_revisit.add(e.getEdgeDestiantionNode());
                } else if (new_clst_size > no_kid_cluster) {
                    //System.out.println("CNAD e MAI MARE: " + edges_to_remove + " EDGEUL CURENT " + e);
                }
            }
        }

        //since I already removed the edges that need to be redone,
        //the edges that are still in this list mean the cut edges of differemt clusters, and I need to revisit them
        for (Edge e : edges_to_remove) {
            edges_to_remove_nodesDest.add(e.getEdgeSourceNode());
            edges_to_remove_nodesDest.add(e.getEdgeDestiantionNode());
        }

        edges_to_remove_nodesDest.addAll(nodes_remade);

        //based on the nodes that should be revist, do the clusters
        for (Node n : edges_to_remove_nodesDest) {
            clusters.add(generateMST(result, getNode(result, n), null, "MACAROANE"));
        }

        //check for baby clusters again
        //redo any remaining edges in the edges_to_remove that will make the baby cluster abide to the given node limit
        for (Graph ge : clusters) {
            if (ge.getVertexCount() <= no_kid_cluster) {
                for (Edge e : edges_to_remove) {
                    if (generateMST(result, e.getEdgeSourceNode()).getVertexCount() <= no_kid_cluster || generateMST(result, e.getEdgeDestiantionNode()).getVertexCount() <= no_kid_cluster) {
                        result.addEdge(e, e.getEdgeSourceNode(), e.getEdgeDestiantionNode());
                        edges_to_remove.remove(e);
                        //edges_to_remove_nodesDest.remove(n);
                    }
                }
            }
        }

        //clear the clusters because they need to be redone
        clusters.clear();

        //do the clusters
        for (Node n : edges_to_remove_nodesDest) {
            clusters.add(generateMST(result, getNode(result, n), null, "MACAROANE CU AVIOANE"));
        }

        //make sure I do not have duplicate clusters (made by same tree but visited from different nodes
        // the path remains the same, the order is different, their hash will be different but the content of the object is identical)
        //if there are such clusters, I must remove them
        ArrayList<Graph> to_remove_clst = new ArrayList<>();
        for (int i = 0; i < clusters.size(); i++) {
            for (int j = i + 1; j < clusters.size(); j++) {
                to_remove_clst.add(removeDupl((Graph) clusters.toArray()[i], (Graph) clusters.toArray()[j]));
            }
        }
        for (Graph g : to_remove_clst) {
            if (g == null) continue;
            clusters.remove(g);
        }
        colorNodesInClusters(clusters);

        return result;
    }

    /**
     * we have to add the baby cluster to the closest cluster
     * we add it by remaking the previous cut edge.
     * If there is such a edge that was remove we search it by the baby current edge source and destination node
     * I add that edge that needs to be remade to a list that will contain all the edges that need to me redone
     * I do not need the visit that cluster from the split ends since I added it to another one, that already exits
     * so I remove the node from the edges_to_remove_nodesDest (containg nodes that I should revisit)
     */
    public void remakeEdges(Collection<Edge> edges_to_remove, Set<Edge> edges_to_remake, Node source, Node destination, Set<Node> edges_to_remove_nodesDest) {
        for (Edge e : edges_to_remove) {
            //if (e.getEdgeSourceNode().equals(source) || e.getEdgeDestiantionNode().equals(destination) || e.getEdgeSourceNode().equals(destination) || e.getEdgeDestiantionNode().equals(source)) {
            if (e.getEdgeSourceNode().equals(source) || e.getEdgeDestiantionNode().equals(destination)) {
                edges_to_remake.add(e);
                if (source != null) {
                    edges_to_remove_nodesDest.remove(source);
                }
                if (destination != null) {
                    edges_to_remove_nodesDest.remove(destination);
                }
            }
        }
    }

    /**
     * each cluster will have an id
     * every node of that cluster will have the same cluster id
     * I do this so I can color the nodes of the same cluster with the same cluster
     */
    public void colorNodesInClusters(Set<Graph> clusters) {
        int clst_id = 0;
        initiazeColorMap(clusters.size());
        for (Graph ge : clusters) {
            clst_id++;
            for (Object n : ge.getVertices()) {
                ((Node) n).setCluster_id(clst_id);
                colorNode(((Node) n).getCluster_id(), (Node) n);
            }
        }
    }

    public void colorClusters(Set<Graph> clusters) {
        initiazeColorMap(clusters.size());
        int contor = 0;
        for (Graph g : clusters) {
            for (Object n : g.getVertices()) {
                ((Node) n).setCluster_id(contor);
                colorNode(((Node) n).getCluster_id(), (Node) n);
            }
            contor++;
        }
    }

    public Set<Graph> getClusters() {
        return this.clusters;
    }

    public void colorNode(int cluster_id, Node n) {
        n.setColor((Color) colorMap.get(cluster_id));
    }

    /**
     * I initialize the colorMap with different colors for each cluster
     */
    public void initiazeColorMap(Integer no_clusters) {
        Random rand = new Random();
        for (int i = 1; i <= no_clusters; i++) {
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            Color randomColor = new Color(r, g, b);
            colorMap.put(i, randomColor);
        }
    }

    public double computeTheTressHold(Graph graph) {
        Collection<Edge> edges = graph.getEdges();
        double threshold = 0;
        for (Edge e : edges) {
            threshold += e.getWeight();
        }
        threshold = threshold / edges.size();
        return threshold;
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
                interedges.addAll(graph.getIncidentEdges(node));
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
            System.out.println("key: " + key.hashCode() + " value: " + clusters_mq.get(key));
            system_mq += clusters_mq.get(key);
        }
        System.out.println("TOTAL MQ: " + system_mq);
        return system_mq;
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
