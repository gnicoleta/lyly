package tool;

public class Edge{
    double weight;
    String Label;
    int id;

    public Edge(double weight, String Label) {
        this.weight = weight;
        this.Label = Label;
    }

    public String toString() {
        //return Label;
        //return "E" + id;
        return " " + weight;
    }

    public String Edge_Property() {
        String Link_prop = Label;
        return (Link_prop);
    }
}
