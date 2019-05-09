package tool;

public class Node {
    String id;
    String cluster_id;

    public Node() {
    }

    public Node(String id) {
        this.id = id;
    }


    public String toString() {
        return "V: " + id + " CLUSTER: " + cluster_id;
    }

    public String Node_Property() {
        String node_prop = id;
        return (node_prop);
    }

    public void setCluster_id(String color) {
        this.cluster_id = color;
    }

    public String getCluster_id() {
        return cluster_id;
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
        Node other = (Node) otherObject;
        // test whether the fields have identical values
        return this.id.equals(((Node) other).Node_Property());
    }
}
