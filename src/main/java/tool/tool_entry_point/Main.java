package tool.tool_entry_point;

import edu.uci.ics.jung.graph.Graph;
import tool.clustering_algorithm.ClusteringAlgorithm;
import tool.graph_builder.CSVGraphHandler;
import tool.graph_visualization.GraphVisualization;
import tool.modularization_quality.ModularizationQuality;
import tool.package_system_structure.InitialPackageStructure;
import tool.package_system_structure.InitialSystemStructure;

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
        int-planning

         */
        String name_of_file = "yui-1.4.7"; //yank looks gud,
        Graph graph = complexCSVGraph.buildGraphFromCsvFile("src\\main\\resources\\csv_files\\" + name_of_file + ".jar_edges.csv");
        Graph graph2 = complexCSVGraph.buildGraphFromCsvFile("src\\main\\resources\\csv_files\\" + name_of_file + ".jar_edges.csv");
        ClusteringAlgorithm clusteringAlgorithm = new ClusteringAlgorithm(graph);
        //ClusteringAlgorithmInitalGraph clusteringAlgorithmInitalGraph = new ClusteringAlgorithmInitalGraph(graph);
        GraphVisualization graphVisualization = new GraphVisualization();

        ClusteringAlgorithm csnw = new ClusteringAlgorithm(graph2);
        double tressHold = csnw.computeTheTressHold(graph2);
        ModularizationQuality mm = new ModularizationQuality();
        double tres_25 = 0.25 * tressHold;
        double tres_50 = 0.5 * tressHold;
        double tres_75 = 0.75 * tressHold;
        double tres_125 = 1.25 * tressHold;
        double tres_150 = 1.5 * tressHold;

        csnw.generateClusters(tressHold,  3);
        System.out.println("MACA normal: " + csnw.getClusters().size());
        System.out.println("---------MQ MACA normal: " + mm.computeMQ(graph2, csnw.getClusters()));
        System.out.println("------------------MQ MACA normal tresshold: " + tressHold);
        //graphVisualization.visualizeGraphMQ(graph2, csnw.getClusters(), "Default tresshold GRAPH", mm.computeMQ(graph2, csnw.getClusters()));

        csnw.generateClusters(tres_25,  3);
        System.out.println("MACA 25: " + csnw.getClusters().size());
        System.out.println("---------MQ MACA 25: " + mm.computeMQ(graph2, csnw.getClusters()));
        System.out.println("------------------MQ MACA 25 tresshold: " + tres_25);
        //graphVisualization.visualizeGraphMQ(graph2, csnw.getClusters(), "25 tresshold GRAPH", mm.computeMQ(graph2, csnw.getClusters()));

        csnw.generateClusters(tres_50,  3);
        System.out.println("MACA 50: " + csnw.getClusters().size());
        System.out.println("-----MQ MACA 50: " + mm.computeMQ(graph2, csnw.getClusters()));
        System.out.println("-----------MQ MACA 50 tresshold: " + tres_50);
        csnw.generateClusters(tres_75,  3);
        //graphVisualization.visualizeGraphMQ(graph2, csnw.getClusters(), "50 tresshold GRAPH", mm.computeMQ(graph2, csnw.getClusters()));

        System.out.println("MACA 75: " + csnw.getClusters().size());
        System.out.println("--------MQ MACA 75: " + mm.computeMQ(graph2, csnw.getClusters()));
        System.out.println("-----------MQ MACA 75 tresshold: " + tres_75);
        csnw.generateClusters(tres_125,  3);
        //graphVisualization.visualizeGraphMQ(graph2, csnw.getClusters(), "75 tresshold GRAPH", mm.computeMQ(graph2, csnw.getClusters()));

        System.out.println("MACA 125: " + csnw.getClusters().size());
        System.out.println("-------MQ MACA 125: " + mm.computeMQ(graph2, csnw.getClusters()));
        System.out.println("-----------------MQ MACA 125 tresshold: " + tres_125);

        csnw.generateClusters(tres_150,  3);
        System.out.println("MACA 150: " + csnw.getClusters().size());
        System.out.println("-----------MQ MACA 150: " + mm.computeMQ(graph2, csnw.getClusters()));
        System.out.println("----------------------MQ MACA 150 tresshold: " + tres_150);
        //graphVisualization.visualizeGraphMQ(graph2, csnw.getClusters(), "125 tresshold GRAPH", mm.computeMQ(graph2, csnw.getClusters()));

        //complexCSVGraph.visualizeGraph(graph, "Complex Graph View");
        Graph result = clusteringAlgorithm.generateMST(graph, null, true);


        graphVisualization.visualizeComplexGraphSteps(graph2, "Complex Graph View", tressHold);

        ModularizationQuality mq = new ModularizationQuality();
        //System.out.println(graph.getVertices());



        InitialSystemStructure initialSystemStructure = new InitialSystemStructure(graph);
        InitialPackageStructure initialPackageStructure = new InitialPackageStructure();
        Set<Graph> package_clusters =
                initialSystemStructure.
                        getInitialStructureOfTheSystem2(initialPackageStructure.
                                getPackageStructureOfTheSystem("src\\main\\resources\\jars\\" + name_of_file +".jar"), graph, 0);


        //remake some edges
        boolean remake = false;
        if(remake) {
            Set<Graph> remade_pack_clst = initialSystemStructure.makePackageEdges(package_clusters);
            initialSystemStructure.setPackages_graph_clusters(remade_pack_clst);
            for (Graph g : package_clusters) {
                System.out.println(g);
            }
        }

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
        System.out.println("PACKAGE CLUSTER SIZE: " + package_clusters.size());
        //clusteringAlgorithm.colorClusters(clusteringAlgorithm.getClusters());
        //System.out.println("Clusters size: " + clusteringAlgorithm.getClusters().size());

        System.out.println("MQ of TOOL: " + mq.computeMQ(graph, all_clusters)); //result?
        //System.out.println("MQ of PACKAGES: " + mq.computeMQ(graph, package_clusters));

        //System.out.println("PACKAGE CLUSTER SIZE: " + package_clusters.size());

        //graphVisualization.visualizeGraph(initialSystemStructure.getPackageClustersGraph(), "PACKAGES", true);
        //graphVisualization.visualizeGraphMQ(initialSystemStructure.getPackageClustersGraph(remade_pack_clst), "PACKAGES", mq.computeMQ(graph, package_clusters));

       graphVisualization.visualizeGraphMQ(initialSystemStructure.getPackageClustersGraph(package_clusters), package_clusters,"PACKAGES init", mq.computeMQ(graph, package_clusters));

        initialSystemStructure.getPackageClustersMQ();

        //complexCSVGraph.visualizeGraph(initialSystemStructure.getPackageClustersGraph(), "PACKAGES");
        //result.getVertices().stream().forEach(System.out::println);
        //System.out.println(clusters);
    }
}
