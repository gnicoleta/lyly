package tool;

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
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
public class ComplexCSVGraph {

    private Graph<Node, Edge> graph;

    Map nodesMap = new HashMap();



    public void removeEdge(){}

    public void addVertexToHashMap(String vertex) {
        if (!nodesMap.containsKey(vertex)) {
            nodesMap.put(vertex, new Node(vertex));
        }
    }

    //public Graph getGraphFromCsvFile(String csvFile) {
    public void buildGraphFromCsvFile(String csvFile) {
        Graph<Node, Edge> graph = new DirectedSparseMultigraph<Node, Edge>() {
        };
        String line = "";
        String cvsSplitBy = ",";

        ArrayList<Integer> weight_properties = new ArrayList<>();

        int weight = 0;


        //the first line in the csv file has to be skipped
        int count_line = 0;


        int cnt = 0;

        //try with resources
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

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
                graph.addEdge(new Edge(weight, edge), (Node) nodesMap.get(source), (Node) nodesMap.get(destination), EdgeType.DIRECTED);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("aaaa" + graph.getVertexCount());
        this.graph = graph;
        System.out.println("bbbb:" + this.graph.getVertexCount());
        //return graph;
    }

    //public void visualizeGraph(Graph graph) {
    public void visualizeGraph() {
        // The Layout<V, E> is parameterized by the vertex and edge types
        Layout<Integer, String> layout = new CircleLayout(this.graph);
        layout.setSize(new Dimension(1000, 1000)); // sets the initial size of the space
        // The BasicVisualizationServer<V,E> is parameterized by the edge types
        BasicVisualizationServer<Integer, String> vv =
                new BasicVisualizationServer<Integer, String>(layout);
        vv.setPreferredSize(new Dimension(500, 500)); //Sets the viewing area size

        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

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


    public static void MST_Prim(Graph graph, Node source) {
        ArrayList<Node> visited_nodes = new ArrayList<>();

    }
}
