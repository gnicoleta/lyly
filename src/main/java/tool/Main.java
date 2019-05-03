package tool;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n\n\n");
        ComplexCSVGraph complexCSVGraph = new ComplexCSVGraph();
        complexCSVGraph.buildGraphFromCsvFile("D:\\LICENTA\\JBUGGER_outputs\\vehicule.jar_edges.csv");
        complexCSVGraph.visualizeGraph2();

        complexCSVGraph.showAllVertices();
        complexCSVGraph.MST_Prim("Boeing");
    }
}
