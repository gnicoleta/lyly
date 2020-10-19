package tool.graph_builder;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import lombok.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Data
public class CSVGraphHandler {

    public Graph<Node, Edge> graph;
    Map nodesMap = new HashMap();

    public void addVertexToHashMap(String vertex) {
        if (!nodesMap.containsKey(vertex)) {
            nodesMap.put(vertex, new Node(vertex));
        }
    }

    //public Graph getGraphFromCsvFile(String csvFile) {
    public Graph buildGraphFromCsvFile(String csvFile) {
        /*Graph<Node, Edge> graph = new DirectedSparseMultigraph<Node, Edge>() {
        };*/
        Graph<Node, Edge> graph = new UndirectedSparseMultigraph<Node, Edge>() {
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
                graph.addEdge(new Edge((Node) nodesMap.get(source), (Node) nodesMap.get(destination), weight, edge), (Node) nodesMap.get(source), (Node) nodesMap.get(destination), EdgeType.UNDIRECTED);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("aaaa" + graph.getVertexCount());
        this.graph = graph;
        System.out.println("bbbb:" + this.graph.getVertexCount());
        return graph;
    }

    public void showAllVertices() {
        Collection<Node> all_nodes = this.graph.getVertices();

        all_nodes.forEach(System.out::println);
    }
}
