package tool.clustering_algorithm;
/*
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import lombok.Data;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ComplexCSVGraph {

    public  Graph<Node, Edge> graph;

    Map nodesMap = new HashMap();



    public void removeEdge(){}

    public void addVertexToHashMap(String vertex) {
        if (!nodesMap.containsKey(vertex)) {
            nodesMap.put(vertex, new Node(vertex));
        }
    }

    //public Graph getGraphFromCsvFile(String csvFile) {
    public Graph buildGraphFromCsvFile(String csvFile) {
        Graph<Node, Edge> graph = new DirectedSparseMultigraph<Node, Edge>() {
        };
        String line = "";
        String cvsSplitBy = ",";

        //Path path = Paths.get(csvFile);
        File file = new File(csvFile);
        //Path path = file.getAbsolutePath();
        System.out.println("Path : " + file.getAbsolutePath());
        //System.out.println("CCCC" + path.toString());
        System.out.println("CCCC" + file.getAbsolutePath());

        ArrayList<Integer> weight_properties = new ArrayList<>();

        int weight = 0;


        //the first line in the csv file has to be skipped
        int count_line = 0;


        int cnt = 0;

        //try with resources
        //try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            //reading the file, line by line
            while ((line = br.readLine()) != null) {

                if (count_line == 0) {
                    count_line++;
                    continue;
                }

                // splitting the line after comma, eg: ana, bia, maria => edg[0] = ana; edg[1] = bia; edg[2] = maria
                String[] edges = line.split(cvsSplitBy);

                String source = edges[0];
                String destination = edges[1];

                addVertexToHashMap(source);
                addVertexToHashMap(destination);

                //the label
                String edge = "Edge = from: " + source + " , to: " + destination + "";

                //aici verificand mai sus, in functie de config file, daca gaseste ceva din cvs (parcurgi bucata cu bucata),
                // insumezi si adaugi pe arc
                //inca sunt hardcodate, ind reprezinta indicile de care incep parametrii care influenteaza ponderea
                //pana la 13
                //o reprezinta indicele din vectorul de proprietati, ce corespunde parametrului de mai sus
                weight_properties = ConfigHelper.getPropertiesValues();
                int ind = 6;
                int o = 0;
                while (ind != 13) {
                    weight += weight_properties.get(o) * Integer.parseInt(edges[ind]);
                    ind++;
                }


                //System.out.println(cnt++ + ": " + nodesMap.get(source).toString() + " TO " + nodesMap.get(destination).toString());
                //graph.addEdge(new Edge(weight, edge), (Node) nodesMap.get(source), (Node) nodesMap.get(destination), EdgeType.DIRECTED);
                graph.addEdge(new Edge((Node) nodesMap.get(source), (Node) nodesMap.get(destination), weight, edge), (Node) nodesMap.get(source), (Node) nodesMap.get(destination), EdgeType.DIRECTED);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("aaaa" + graph.getVertexCount());
        this.graph = graph;
        System.out.println("bbbb:" + this.graph.getVertexCount());
        return graph;
    }

    //public void visualizeGraph(Graph graph) {
    public void visualizeGraph() {
        // The Layout<V, E> is parameterized by the vertex and edge types
        Layout<Integer, String> layout = new CircleLayout(this.graph);
        layout.setSize(new Dimension(1000, 1000)); // sets the initial size of the space
        // The BasicVisualizationServer<V,E> is parameterized by the edge types
        BasicVisualizationServer<Integer, String> vv =
                new BasicVisualizationServer<Integer, String>(layout);
        vv.setPreferredSize(new Dimension(800, 800)); //Sets the viewing area size

        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

        vv.getRenderingHints().remove(RenderingHints.KEY_ANTIALIASING);
        JFrame frame = new JFrame("Complex Directed Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }

    //public void visualizeGraph(Graph graph) {
    public void visualizeGraph2() {
        // The Layout<V, E> is parameterized by the vertex and edge types
        Layout<Node, Edge> layout = new FRLayout<>(this.graph);
        layout.setSize(new Dimension(1000, 1000)); // sets the initial size of the space

        // The BasicVisualizationServer<V,E> is parameterized by the edge types
        VisualizationViewer<Node, Edge> vv =
                new VisualizationViewer<Node, Edge>(layout);


        Transformer<Node, Paint> vertexColor = new Transformer<Node, Paint>() {
            public Paint transform(Node i) {
                return Color.GREEN;
            }
        };

        Transformer<Node, String> vertexLabelTransformer = new Transformer<Node, String>() {
            public String transform(Node vertex) {
                return (String) vertex.Node_Property();
            }
        };
//
//        Transformer<Node, Shape> vertexSize = new Transformer<Node, Shape>() {
//            public Shape transform(Node i) {
//                Ellipse2D circle = new Ellipse2D.Double(-15, -15, 30, 30);
//                // in this case, the vertex is twice as large
//                AffineTransform.getScaleInstance(2, 2).createTransformedShape(circle);
//                return circle;
//            }
//        };

        Transformer<Edge, String> edgeLabelTransformer = new Transformer<Edge, String>() {
            public String transform(Edge edge) {
                //return "[ "+edge.Edge_Property()+" ]: Wt = "+edge.weight;
                return "" + edge.weight;
            }
        };

        vv.getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer);

        vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
        //vv.getRenderContext().setVertexShapeTransformer(vertexSize);


        vv.setPreferredSize(new Dimension(500, 500)); //Sets the viewing area size

        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        //vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setVertexLabelTransformer(vertexLabelTransformer);



        JFrame frame = new JFrame("Complex Directed Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }

/*
    //public static void MST_Prim(Graph graph, Node source) {
    public void MST_Prim(String source) {
        Node source_node = new Node(source);
        Collection<Node> visited_nodes = new ArrayList<>();

        Collection<Edge> possible_edges_from_visited_nodes = new ArrayList<>();
        Collection<Edge> the_mst_edges = new ArrayList<>();

        Collection<Node> all_nodes = this.graph.getVertices();
        Node current_node = ComplexCSVGraph.getNode(this.graph, source_node);
        System.out.println("BBBB" + current_node.Node_Property());
        System.out.println("AAAAA" + source_node.Node_Property());

        Collection<Edge> edges_of_node = this.graph.getIncidentEdges(current_node);
        edges_of_node.forEach(System.out::println);

        //while (current_node != destination) {
        while (!visited_nodes.contains(current_node)) {
            visited_nodes.add(current_node);

            //we add all the edges of the current_nod into the array with all the edges of ALL the visited nodes
            ComplexCSVGraph.addEdgesFromVisitedNodes(edges_of_node, possible_edges_from_visited_nodes);

            Node current_node_parent = new Node();
            double MIN = Integer.MAX_VALUE;    //the minimum value will end up being stored in here
            for (Edge ii : possible_edges_from_visited_nodes) {

                //the edge is from a visited node to an unvisited one
                //from the array with all edges of all the visited nodes, the smallest edge will be chosen
                if (visited_nodes.contains(ii.getEdgeSourceNode()) && ii.getWeight() < MIN && !visited_nodes.contains(ii.getEdgeDestiantionNode())) {
                    MIN = ii.getWeight();
                    current_node_parent = ii.getEdgeSourceNode();
                    current_node = ii.getEdgeDestiantionNode();
                }
            }

            //we add the the edges as they are parsed, in order to obtain the mst
            the_mst_edges.add(new Edge(current_node_parent, current_node, MIN));

            //to make sure we don't get on the same edge twice, we remove it from the array with all the edges of all visited nodes
            // the way I implemented a edge is ex: A - B, but also B - A (for undirected graphs)
            possible_edges_from_visited_nodes.remove(new Edge(current_node_parent, current_node, MIN));
            possible_edges_from_visited_nodes.remove(new Edge(current_node, current_node_parent, MIN));
        }


        for (Edge tt : the_mst_edges) {
            System.out.println(" " + tt.getEdgeSourceNode() + " - " + tt.getEdgeDestiantionNode() + ": " + tt.getWeight());
        }
    }*/

/*
    public static void addEdgesFromVisitedNodes(Collection<Edge> source, Collection<Edge> destination) {
        for (Edge t : source) {
            destination.add(t);
        }
    }

    public void showAllVertices() {
        Collection<Node> all_nodes = this.graph.getVertices();

        all_nodes.forEach(System.out::println);
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
*/