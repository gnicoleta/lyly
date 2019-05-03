package tool;

public class Edge{
    Node source;
    Node destination;
    double weight;
    String Label;
    int id;

    public Edge(double weight, String Label) {
        this.weight = weight;
        this.Label = Label;
    }

    public Edge(Node source, Node destination, double weight, String Label) {
        this.weight = weight;
        this.Label = Label;
        this.source = source;
        this.destination = destination;
    }
    public Edge(Node source, Node destination, double weight) {
        this.weight = weight;
        this.source = source;
        this.destination = destination;
    }

    public Node getEdgeSourceNode() {
        return this.source;
    }

    public Node getEdgeDestiantionNode() {
        return this.destination;
    }

    public double getWeight() {
        return this.weight;
    }

    public String toString() {
        //return Label;
        //return "E" + id;
        //return " " + weight;
        return " " + this.source.Node_Property() + " - " + weight + " - " + this.destination.Node_Property();
    }

    public String getEdges() {
        //return Label;
        //return "E" + id;
        return " " + this.source.Node_Property() + " - " + weight + " - " + this.destination.Node_Property();
    }

    public String Edge_Property() {
        String Link_prop = Label;
        return (Link_prop);
    }
}
