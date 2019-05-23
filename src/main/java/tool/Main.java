package tool;

import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n\n\n");
        CSVGraphHandler complexCSVGraph = new CSVGraphHandler();
        CSVGraphHandler complexCSVGraph2 = new CSVGraphHandler();
        //Graph graph = complexCSVGraph.buildGraphFromCsvFile("src\\main\\resources\\csv_files\\bank.jar_edges.csv");
        Graph graph = complexCSVGraph.buildGraphFromCsvFile("src\\main\\resources\\csv_files\\manta-1.9.jar_edges.csv");
        ClusteringAlgorithm clusteringAlgorithm = new ClusteringAlgorithm(graph);
        //ClusteringAlgorithmInitalGraph clusteringAlgorithmInitalGraph = new ClusteringAlgorithmInitalGraph(graph);

        complexCSVGraph.visualizeGraph(graph, "Complex Graph View");
        Graph result = clusteringAlgorithm.generateMST(graph, null, true);


        InitialSystemStructure initialSystemStructure = new InitialSystemStructure();
        InitialPackageStructure initialPackageStructure = new InitialPackageStructure();
        Set<Graph> package_clusters = initialSystemStructure.getInitialStructureOfTheSystem(initialPackageStructure.getPackageStructureOfTheSystem(), graph);

//        for(Graph g: package_clusters) {
//            System.out.println(g);
//        }

        //double tresshold = clusteringAlgorithm.computeTheTressHold(result);
        //complexCSVGraph.setTressholdInputField(tresshold);
        //complexCSVGraph.setMinNodesInCluster(2);
        //complexCSVGraph.visualizeGraph(result, "MST");
        //complexCSVGraph.newFrame(result, "MST");

        //tresshold = 1.1 * tresshold;
        //System.out.println("TRESHOLD: " + tresshold);
        //Graph clusters = clusteringAlgorithm.generateClusters(tresshold, 2);
        //complexCSVGraph.visualizeGraph(clusters, "CLUSTERS");
        Set<Graph> all_clusters = clusteringAlgorithm.getClusters();
        //clusteringAlgorithm.colorClusters(clusteringAlgorithm.getClusters());
        //System.out.println("Clusters size: " + clusteringAlgorithm.getClusters().size());

        clusteringAlgorithm.computeMQ(graph, all_clusters); //result?
        System.out.println(all_clusters.size());
        clusteringAlgorithm.computeMQ(graph, package_clusters);

        complexCSVGraph.visualizeGraph(initialSystemStructure.getPackageClustersGraph(), "PACKAGES");
        //result.getVertices().stream().forEach(System.out::println);
        //System.out.println(clusters);
    }
}
