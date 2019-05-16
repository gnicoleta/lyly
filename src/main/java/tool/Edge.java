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

    @Override
    public boolean equals(Object otherObject) {
        // a quick test to see if the objects are identical
        if (this == otherObject) return true;
        // must return false if the explicit parameter is null
        if (otherObject == null) return false;
        // if the classes don't match, they can't be equal
        if (!getClass().equals(otherObject.getClass()))
            return false;
        // now we know otherObject is a non-null Employee
        Edge other = (Edge) otherObject;
        // test whether the fields have identical values
        return this.source==other.getEdgeSourceNode() &&  this.destination==other.getEdgeDestiantionNode() && this.weight==other.getWeight() ;
    }
}
