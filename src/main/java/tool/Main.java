package tool;

import edu.uci.ics.jung.graph.Graph;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n\n\n");
        CSVGraphHandler complexCSVGraph = new CSVGraphHandler();
        CSVGraphHandler complexCSVGraph2 = new CSVGraphHandler();
        //Graph graph = complexCSVGraph.buildGraphFromCsvFile("D:\\LICENTA\\JBUGGER_outputs\\vehicule.jar_edges.csv");
        //D:\LICENTA\lyly\src\main\resources\csv_files
        //Graph graph = complexCSVGraph.buildGraphFromCsvFile("src\\main\\resources\\csv_files\\bank.jar_edges.csv");
        Graph graph = complexCSVGraph.buildGraphFromCsvFile("src\\main\\resources\\csv_files\\vehicule.jar_edges.csv");
        ClusteringAlgorithm clusters = new ClusteringAlgorithm(graph);
        complexCSVGraph.visualizeGraph(graph);

        complexCSVGraph.showAllVertices();
        //complexCSVGraph.MST_Prim("Boeing");

        Graph result = clusters.generateMST("Turisme");
        complexCSVGraph.visualizeGraph(result);

    }
}
