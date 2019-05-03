package tool;

public class Node {
    String id;

    public Node(String id) {
        this.id = id;
    }

    public String toString() {
        return "V: " + id;
    }

    public String Node_Property() {
        String node_prop = id;
        return (node_prop);
    }
}
