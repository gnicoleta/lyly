package tool;

import java.awt.*;
import java.util.Collection;

public class Node {
    String id;
    //String cluster_id;
    Integer cluster_id = 0;
    Color color = Color.GREEN;

    public Node() {
    }

    public void setColor(Color color) {
        this.color = color;
    }
    public Color getColor() {
        return this.color;
    }

    public Node(String id) {
        this.id = id;
    }


    public String toString() {
        //return "V: " + id + " CLUSTER: " + cluster_id;
        return "V: " + id + " -- cluster id: " + cluster_id;
    }

    public String Node_Property() {
        String node_prop = id;
        return (node_prop);
    }

//    public void setCluster_id(String color) {
//        this.cluster_id = color;
//    }
//    public String getCluster_id() {
//        return cluster_id;
//    }
    public void setCluster_id(Integer color) {
        this.cluster_id = color;
    }
    public Integer getCluster_id() {
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
