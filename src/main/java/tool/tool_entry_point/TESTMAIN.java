package tool.tool_entry_point;

import edu.uci.ics.jung.graph.Graph;
import tool.clustering_algorithm.ClusteringAlgorithm;
import tool.graph_builder.CSVGraphHandler;
import tool.modularization_quality.ModularizationQuality;
import tool.package_system_structure.InitialPackageStructure;
import tool.package_system_structure.InitialSystemStructure;

public class TESTMAIN {
    public static void main(String[] args) {
        CSVGraphHandler complexCSVGraph = new CSVGraphHandler();
        String name_of_file = "manta-1.9";
        Graph graph = complexCSVGraph.buildGraphFromCsvFile("src\\main\\resources\\csv_files\\" + name_of_file + ".jar_edges.csv");
        ClusteringAlgorithm clusteringAlgorithm = new ClusteringAlgorithm(graph);

        InitialSystemStructure initialSystemStructure = new InitialSystemStructure(graph);
        InitialPackageStructure initialPackageStructure = new InitialPackageStructure();

         initialSystemStructure.reclusterBasedOnPackageLevel(0, initialPackageStructure.getPackageStructureOfTheSystem("src\\main\\resources\\jars\\" + name_of_file +".jar"));
        ModularizationQuality modularizationQuality = new ModularizationQuality();
    }
}
