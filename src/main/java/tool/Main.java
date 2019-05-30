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
        /*
        azureblob-1.5.0-rc.1
        bobo-browse-3.0.0
        bursa
        car-rental-system-core-3.2-client
        manta-1.9 //?
        rice-api
        yahoo_search-2.0.1
        yui-1.4.7
        zypr-api
        facebook
        nadron
        yank
        zel

         */
        String name_of_file = "yank";
        Graph graph = complexCSVGraph.buildGraphFromCsvFile("src\\main\\resources\\csv_files\\" + name_of_file + ".jar_edges.csv");
        ClusteringAlgorithm clusteringAlgorithm = new ClusteringAlgorithm(graph);
        //ClusteringAlgorithmInitalGraph clusteringAlgorithmInitalGraph = new ClusteringAlgorithmInitalGraph(graph);

        //complexCSVGraph.visualizeGraph(graph, "Complex Graph View");
        Graph result = clusteringAlgorithm.generateMST(graph, null, true);

        GraphVisualization graphVisualization = new GraphVisualization();
        graphVisualization.visualizeComplexGraphSteps(graph, "Complex Graph View");

        ModularizationQuality mq = new ModularizationQuality();
        //System.out.println(graph.getVertices());



       /* InitialSystemStructure initialSystemStructure = new InitialSystemStructure(graph);
        InitialPackageStructure initialPackageStructure = new InitialPackageStructure();
        Set<Graph> package_clusters =
                initialSystemStructure.
                        getInitialStructureOfTheSystem(initialPackageStructure.
                                getPackageStructureOfTheSystem("src\\main\\resources\\jars\\" + name_of_file +".jar"), graph, 0);*/


        //remake some edges
        //Set<Graph> remade_pack_clst = initialSystemStructure.makePackageEdges(package_clusters);
        //initialSystemStructure.setPackages_graph_clusters(remade_pack_clst);
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
        System.out.println("ALL CLOST: " + all_clusters.size());
        for(Graph g : all_clusters) {
            System.out.println("ALLL MIN NODES: " + g.getVertexCount());
        }
        System.out.println("TOOL CLUSTER SIZE: " + clusteringAlgorithm.getClusters().size());
        //clusteringAlgorithm.colorClusters(clusteringAlgorithm.getClusters());
        //System.out.println("Clusters size: " + clusteringAlgorithm.getClusters().size());

        System.out.println("MQ of TOOL: " + mq.computeMQ(graph, all_clusters)); //result?
        //System.out.println("MQ of PACKAGES: " + mq.computeMQ(graph, package_clusters));

        //System.out.println("PACKAGE CLUSTER SIZE: " + package_clusters.size());

        //graphVisualization.visualizeGraph(initialSystemStructure.getPackageClustersGraph(), "PACKAGES", true);
        //graphVisualization.visualizeGraphMQ(initialSystemStructure.getPackageClustersGraph(remade_pack_clst), "PACKAGES", mq.computeMQ(graph, package_clusters));

        //graphVisualization.visualizeGraphMQ(initialSystemStructure.getPackageClustersGraph(package_clusters), "PACKAGES init", mq.computeMQ(graph, package_clusters));

        //initialSystemStructure.getPackageClustersMQ();

        //complexCSVGraph.visualizeGraph(initialSystemStructure.getPackageClustersGraph(), "PACKAGES");
        //result.getVertices().stream().forEach(System.out::println);
        //System.out.println(clusters);
    }
}
