package tool;

import edu.uci.ics.jung.graph.Graph;

import java.util.ArrayList;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n\n\n");
        CSVGraphHandler complexCSVGraph = new CSVGraphHandler();
        CSVGraphHandler complexCSVGraph2 = new CSVGraphHandler();
        //Graph graph = complexCSVGraph.buildGraphFromCsvFile("D:\\LICENTA\\JBUGGER_outputs\\vehicule.jar_edges.csv");
        //D:\LICENTA\lyly\src\main\resources\csv_files
        //Graph graph = complexCSVGraph.buildGraphFromCsvFile("src\\main\\resources\\csv_files\\bank.jar_edges.csv");
        Graph graph = complexCSVGraph.buildGraphFromCsvFile("src\\main\\resources\\csv_files\\vehicule.jar_edges.csv");
        ClusteringAlgorithm clusteringAlgorithm = new ClusteringAlgorithm(graph);
        //ClusteringAlgorithmInitalGraph clusteringAlgorithmInitalGraph = new ClusteringAlgorithmInitalGraph(graph);

        complexCSVGraph.visualizeGraph(graph, "Complex Graph View", null);

        //complexCSVGraph.showAllVertices();

        Graph result = clusteringAlgorithm.generateMST(graph, null, true);
        complexCSVGraph.visualizeGraph(result, "MST", null);

        Graph clusters = clusteringAlgorithm.generateClusters(14.0);
        complexCSVGraph.visualizeGraph(clusters, "CLUSTERS", clusteringAlgorithm.getCluster().size());

        System.out.println(clusters);

    }
}
