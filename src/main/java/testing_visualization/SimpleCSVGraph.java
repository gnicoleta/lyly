package testing_visualization;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@Data
public class SimpleCSVGraph {

    private ArrayList<String> nodes = new ArrayList<String>();
    private Graph graph;

    public void addVertexToGraph(String vertex, Graph graph) {
        //I want to put in the node array only the distinctive nodes,
        //that's why I check to see if I already got that node in my nodes array
        if (nodes.contains(vertex) == false) {

            //nodes is an ArrayList<String>, so their names are stored in an ArrayList
            nodes.add(vertex);
            graph.addVertex(vertex);
        }
    }

    //public Graph getGraphFromCsvFile(String csvFile) {
    public void buildGraphFromCsvFile(String csvFile) {
        Graph<String, String> graph = new DirectedSparseMultigraph<>();
        String line = "";
        String cvsSplitBy = ",";

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

                //the label
                String edge = "Edge = from: " + source + " , to: " + destination + "";

                addVertexToGraph(source, graph);
                addVertexToGraph(destination, graph);

                //aici verificand mai sus, in functie de config file, daca gaseste ceva din cvs (parcurgi bucata cu bucata),
                // insumezi si adaugi pe arc
                //...


                //here I add the edge, ed is the String I talked about above (which contains the source vertex and the destination vertex)
                //next we get from out map the source vertex, searching the map with the given key (source node),
                // and retrieving its value (an Integer)

                System.out.println(cnt++ + ": " + source + " TO " + destination);
                graph.addEdge(edge, source, destination, EdgeType.DIRECTED);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.graph = graph;
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

        JFrame frame = new JFrame("Simple Directed Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }
}

class SimpleCSVGraphMain {
    public static void main(String[] args) {

        System.out.println("\n\n\n");
        SimpleCSVGraph simpleCSVGraph = new SimpleCSVGraph();
        simpleCSVGraph.buildGraphFromCsvFile("D:\\LICENTA\\RawDataDump2\\outputs\\ByteCommunicationFinal.jar_edges.csv");
        simpleCSVGraph.visualizeGraph();

        System.out.println(simpleCSVGraph.getGraph().getVertexCount());
        System.out.println(simpleCSVGraph.getGraph().getEdges().size());
    }
}
