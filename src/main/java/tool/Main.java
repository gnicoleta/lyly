package tool;

import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;

import java.util.ArrayList;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n\n\n");
        CSVGraphHandler complexCSVGraph = new CSVGraphHandler();
        CSVGraphHandler complexCSVGraph2 = new CSVGraphHandler();
        //Graph graph = complexCSVGraph.buildGraphFromCsvFile("src\\main\\resources\\csv_files\\bank.jar_edges.csv");
        Graph graph = complexCSVGraph.buildGraphFromCsvFile("src\\main\\resources\\csv_files\\bursa.jar_edges.csv");
        ClusteringAlgorithm clusteringAlgorithm = new ClusteringAlgorithm(graph);
        //ClusteringAlgorithmInitalGraph clusteringAlgorithmInitalGraph = new ClusteringAlgorithmInitalGraph(graph);

        complexCSVGraph.visualizeGraph(graph, "Complex Graph View");

        //complexCSVGraph.showAllVertices();

        Graph result = clusteringAlgorithm.generateMST(graph, null, true);
        complexCSVGraph.visualizeGraph(result, "MST");

        Graph clusters = clusteringAlgorithm.generateClusters(56.0);
        complexCSVGraph.visualizeGraph(clusters, "CLUSTERS");
        clusteringAlgorithm.colorClusters(clusteringAlgorithm.getClusters());
        int contor = 0;
        for (Graph g : clusteringAlgorithm.getClusters()) {
            complexCSVGraph.visualizeGraph(g, "CLUSTERS" + contor++);

        }

        System.out.println(clusters);

    }
}
