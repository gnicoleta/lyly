package tool;

import com.sun.xml.internal.bind.v2.TODO;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import org.jcp.xml.dsig.internal.dom.DOMUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

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

    public Set<Graph> getInitialStructureOfTheSystem(HashMap<String, HashSet<String>> packages_classes_structur, Graph tool_graph, Integer level) {
        HashMap<String, HashSet<String>> packages_classes_structure = packages_classes_structur;
        if (level != 0) {
            packages_classes_structure = this.reclusterBasedOnPackageLevel(level, packages_classes_structur);
        }
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

    public HashMap<String, HashSet<String>> reclusterBasedOnPackageLevel(Integer level, HashMap<String, HashSet<String>> packages_classes_structure) {
        //level 1 -> deepest
        ArrayList<String> allKeys = new ArrayList<>();
        allKeys.addAll(packages_classes_structure.keySet());
        allKeys.stream().forEach(System.out::println);

        ArrayList<String> needs_to_be_redone = new ArrayList<>();
        ArrayList<String> parent_package = new ArrayList<>();

        HashMap<String, String> packageToBeRedon_parrent = new HashMap<>();

        for (Map.Entry<String, HashSet<String>> e : packages_classes_structure.entrySet()) {
            String key = e.getKey();
            HashSet<String> values = e.getValue();
            System.out.println("OLD Size: " + values.size() + " pentru cheia: " + key);
            String builder = "";
            if (values.size() <= level) {
                String[] split_key = key.split("\\.");
                System.out.println("Key: " + key);
                System.out.println(split_key.length);
                for (int i = 0; i < split_key.length - level; i++) {
                    System.out.println("Split: " + split_key[i]);
                    builder = builder + split_key[i] + ".";
                    System.out.println(builder);
                }

                needs_to_be_redone.add(key);
                //builder = builder.substring(0, builder.length()-1);
                parent_package.add(builder);
                System.out.println("DESPRE CE VORBIM: " + builder + " -- -si cheia: " + key);

                for (String s : allKeys) {
                    if (s.startsWith(builder)) {
                        System.out.println("SEEE: " + s);
                    }
                }
                packageToBeRedon_parrent.put(key, builder);
                //packageToBeRedon_parrent.put(builder, key);
            }
        }

        System.out.println("STAR");
        packageToBeRedon_parrent.entrySet().stream().forEach(System.out::println);
        System.out.println("END");


        System.out.println("KEYSTAR");
        packageToBeRedon_parrent.keySet().stream().forEach(System.out::println);
        System.out.println("END");


        for (Map.Entry<String, String> e : packageToBeRedon_parrent.entrySet()) {
            String key = e.getKey();
            String value = e.getValue();
            //System.out.println("HAHA: for value: " + value + "---- " + " key: " + key + " ---- "+ packages_classes_structure.get(value));
            System.out.println("Pentru e: : " + value + "---- " + " key: " + key);


            Set<String> the_level = new HashSet<>();
            for (String s : allKeys) {
                if (s.startsWith(value)) {
                    //System.out.println("INCEPE CU: " +  value + " ---- " + s);
                    the_level.add(s);
                }
            }
            HashSet<String> new_values = new HashSet<>();
            int contor = 0;
            System.out.println("Level size: " + the_level.size());
            for (String s : the_level) {
                contor++;
                System.out.println("Iteraria: " + contor);
                System.out.println("WTF: " + s);
                System.out.println("WHA: " + packages_classes_structure.get(s));
                if (packages_classes_structure.get(s) != null) {
                    new_values.addAll(packages_classes_structure.get(s));
                    System.out.println("S the level: " + s);
                }

                packages_classes_structure.remove(s);

                System.out.println("KAKA: " + new_values);
                System.out.println("S-o sters: " + s);
            }

            System.out.println("S-au adaugat: " + value + " --- " + new_values);
            System.out.println("HERE: " + value + " este in packages structure: " + packages_classes_structure.keySet().contains(value));
            if (!packages_classes_structure.keySet().contains(value)) {
                System.out.println("SE ADDA: " + value + " ------ " + new_values);
                packages_classes_structure.put(value, new_values);
            }

            System.out.println("MACARENA: " + value + " ----------- " + new_values);

//            if(allKeys.contains(e) && allKeys.contains(key)) {
//                System.out.println("HRHR: " + e + " -- " + key);
//            }


        }

        for (Map.Entry<String, HashSet<String>> e : packages_classes_structure.entrySet()) {
            String key = e.getKey();
            HashSet<String> values = e.getValue();
            System.out.println("NEW Sizes: " + values.size());

            if (values.size() <= level) {
                System.out.println("EEE: " + key);
            }
        }

        return packages_classes_structure;
    }

    public void packageClusterDimension(Integer level, HashMap<String, HashSet<String>> packages_classes_structure) {
    }

    public void setPackages_graph_clusters(Set<Graph> packages_graph_clusters) {
        this.packages_graph_clusters = packages_graph_clusters;
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

    public Set<Graph> makePackageEdges(Set<Graph> clustrs) {
        Set<Graph> clusters = clustrs;
        System.out.println("ASTA E GRAFUL: " + graph);
        //CopyOnWriteArrayList<Graph> clusters = (CopyOnWriteArrayList<Graph>) cluster;
        Collection<Node> pr_node = new HashSet<>();
        Collection<Edge> pr_edge = new HashSet<>();
        HashMap<Node, Edge> pr_edge_map = new HashMap<>();
        for (Graph h : clusters) {
            System.out.println(h.getEdgeCount() + " -- si noduri: " + h.getVertexCount());
            if (h.getVertexCount() != 0 && h.getEdgeCount() == 0) {
                System.out.println("AICI sunt min: " + h.getVertices());
                System.out.println("MACAR: " + graph.getVertices().containsAll(h.getVertices()));
                for (Object n : graph.getVertices()) {
                    for (Object nn : h.getVertices()) {
                        if (((Node) n).equals((Node) nn)) {
                            Collection<Edge> to_redo_edges = graph.getIncidentEdges((Node) n);
                            Collection<Node> node_reo = new HashSet<>();
                            for (Edge e : to_redo_edges) {
                                node_reo.add(e.getEdgeSourceNode());
                                node_reo.add(e.getEdgeDestiantionNode());
                            }

                            //System.out.println("CONTINE: " + node_reo.containsAll(h.getVertices()));
                            for (Node w : node_reo) {
                                System.out.println("LABA: " + h.getVertices().contains(w) + " ---- " + w);
                                if (h.getVertices().contains(w)) {
                                    pr_node.add(w);
                                    System.out.println("DE aSEMNEA: ");
                                    graph.getIncidentEdges(w).stream().forEach(System.out::println);
                                    double max = Integer.MIN_VALUE;

                                    Edge best_edge = null;
                                    for (Object e : graph.getIncidentEdges(w)) {
                                        if (((Edge) e).getWeight() > max) {
                                            max = ((Edge) e).getWeight();
                                            best_edge = (Edge) e;
                                        }
                                    }
                                    pr_edge_map.put(w, best_edge);
                                    pr_edge.add(best_edge);

                                }
                            }
                        }
                    }
                }
            }
        }

        CopyOnWriteArrayList<Node> link_nodes = new CopyOnWriteArrayList<>();

        Collection<Edge> link_edges = new HashSet<>();
        for (Graph t : clusters) {
//            for(Node rn : pr_node) {
//                for(Edge re : pr_edge) {
//
//                }
//            }
            for (Map.Entry<Node, Edge> e : pr_edge_map.entrySet()) {
                Node key = e.getKey();
                Edge values = e.getValue();

                //System.out.println("KAKA KEY: " + key);
                System.out.println("KEY contine: " +  pr_node.contains(key) + " -- -key" + key);
                Node to_change_Key = null;
                if (t.getVertices().contains(key) && pr_node.contains(key)) {
                    if(!values.getEdgeSourceNode().equals(key) && !link_nodes.contains(values.getEdgeSourceNode())) {
                        System.out.println("POPO: " + values.getEdgeSourceNode());
                        link_nodes.add(values.getEdgeSourceNode());
                        System.out.println("KK size: " + link_nodes.size());
                        to_change_Key = values.getEdgeSourceNode();
                        //pr_node.remove(key);
                    }
                    if(!values.getEdgeDestiantionNode().equals(key) && !link_nodes.contains(values.getEdgeDestiantionNode())) {

                        System.out.println("2KK size: " + link_nodes.size());
                        System.out.println("2POPO: " + values.getEdgeDestiantionNode());
                        link_nodes.add(values.getEdgeDestiantionNode());

                        to_change_Key = values.getEdgeDestiantionNode();
                        //pr_node.remove(key);
                    }
                    pr_node.remove(key);

                    t.removeVertex(key);
                    //key = to_change_Key;
                    System.out.println("VR sa schimb: " + to_change_Key + "---------- " + key);

                    link_edges.add(values);
                }

            }
        }
        //delete empty clusters?
//        for (Graph t : clusters) {
//            if (t.getVertexCount() == 0) {
//                System.out.println("ZERO: " + t);
//                clusters.remove(t);
//            }
//        }

        for (Graph t : clusters) {
            for(Node nl : link_nodes) {

                if(t.getVertices().contains(nl)) {
                    for(Edge e : link_edges) {
                        if(e.getEdgeDestiantionNode().equals(nl ) || e.getEdgeSourceNode().equals(nl ) ) {
                            System.out.println("REFACUT:" + e);
                            t.addEdge(e, e.getEdgeSourceNode(), e.getEdgeDestiantionNode());
                            link_nodes.remove(nl);
                        }
                    }
//                    System.out.println("NL: " + nl);
//                    System.out.println("REFACUT:" + pr_edge_map.containsKey(nl));
//                    Edge redone_for_good = pr_edge_map.get(nl);
//                    t.addEdge(redone_for_good, redone_for_good.getEdgeSourceNode(), redone_for_good.getEdgeDestiantionNode());
//                    link_nodes.remove(nl);
                    //pr_edge_map.get(nl);
                }
            }
        }

        for(Graph h : clusters) {
            System.out.println("CLS SIZE: " + h.getVertexCount() + " -------- " + h.getEdgeCount());
        }

        return clusters;
    }
}
