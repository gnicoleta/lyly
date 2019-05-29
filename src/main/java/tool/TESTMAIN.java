package tool;

import edu.uci.ics.jung.graph.Graph;

import java.util.Set;

public class TESTMAIN {
    public static void main(String[] args) {
        CSVGraphHandler complexCSVGraph = new CSVGraphHandler();
        String name_of_file = "azureblob-1.5.0-rc.1";
        Graph graph = complexCSVGraph.buildGraphFromCsvFile("src\\main\\resources\\csv_files\\" + name_of_file + ".jar_edges.csv");
        ClusteringAlgorithm clusteringAlgorithm = new ClusteringAlgorithm(graph);

        InitialSystemStructure initialSystemStructure = new InitialSystemStructure(graph);
        InitialPackageStructure initialPackageStructure = new InitialPackageStructure();

         initialSystemStructure.reclusterBasedOnPackageLevel(1, initialPackageStructure.getPackageStructureOfTheSystem("src\\main\\resources\\jars\\" + name_of_file +".jar"));
    }
}
