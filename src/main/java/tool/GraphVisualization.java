package tool;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import oracle.jrockit.jfr.JFR;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GraphVisualization {

    public Graph<Node, Edge> graph;
    private int zoomed_in = 0;
    double tresshold_val = 1;
    JTextField tresshold = new JTextField(10);
    JTextField minNodesInCluster = new JTextField(10);
    String[] tresHoldBoxValues = {"default", "25%", "50%", "75%", "125%", "150%", "175%", "200%"};
    JComboBox treshodComboBox = new JComboBox(tresHoldBoxValues);
    ClusteringAlgorithm clusteringAlgorithm = new ClusteringAlgorithm();
    int contor = 0;

    //public void visualizeGraph(Graph graph) {

    //this method is for visualizing any graph
    public JFrame visualizeGraph(Graph graph, String window_title, boolean visible) {
        System.out.println("2- " + graph.getVertices());
        // The Layout<V, E> is parameterized by the vertex and edge types
        Layout<Node, Edge> layout = new FRLayout<>(graph);
        layout.setSize(new Dimension(1000, 1000)); // sets the initial size of the space

        // The BasicVisualizationServer<V,E> is parameterized by the edge types
        VisualizationViewer<Node, Edge> vv =
                new VisualizationViewer<Node, Edge>(layout, new Dimension(800, 800));

        Transformer<Node, Paint> vertexColor = new Transformer<Node, Paint>() {
            public Paint transform(Node i) {
                return i.getColor();
            }
        };

        Transformer<Node, String> vertexLabelTransformer = new Transformer<Node, String>() {
            public String transform(Node vertex) {
                return (String) vertex.Node_Property();
            }
        };

        Transformer<Node, Shape> vertexSize = new Transformer<Node, Shape>() {
            public Shape transform(Node i) {
                Ellipse2D circle = new Ellipse2D.Double(-15, -15, 30, 30);
                // in this case, the vertex is twice as large
                AffineTransform.getScaleInstance(2, 2).createTransformedShape(circle);
                return circle;
            }
        };

        Transformer<Graph<Context<String, String>, String>, Shape> edgeTransformer = new Transformer<Graph<Context<String, String>, String>, Shape>() {
            @Override
            public Shape transform(Graph<Context<String, String>, String> graphStringContext) {
                return (new Line2D.Double());
            }
        };

        Transformer<Edge, String> edgeLabelTransformer = new Transformer<Edge, String>() {
            public String transform(Edge edge) {
                //return "[ "+edge.Edge_Property()+" ]: Wt = "+edge.weight;
                return "" + edge.weight;
            }
        };

        vv.getRenderContext().setEdgeLabelTransformer(edgeLabelTransformer);
        vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
        //vv.getRenderContext().setEdgeLabelClosenessTransformer(edgeTransformer);
        vv.getRenderContext().setLabelOffset(15);
        //vv.getRenderContext().setVertexShapeTransformer(vertexSize);
        vv.setPreferredSize(new Dimension(800, 800)); //Sets the viewing area size
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        //vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setVertexLabelTransformer(vertexLabelTransformer);
        //vv.getRenderingHints().remove(RenderingHints.KEY_ANTIALIASING);

        //JFrame frame = new JFrame("Complex Graph View");
        contor++;
        JFrame frame = new JFrame(window_title + contor);
        Container content = frame.getContentPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();

        final AbstractModalGraphMouse graphMouse = new DefaultModalGraphMouse<String, Number>();
        vv.setGraphMouse(graphMouse);
        vv.addKeyListener(graphMouse.getModeKeyListener());
        //vv.setToolTipText("<html><center>Type 'p' for Pick mode<p>Type 't' for Transform mode");
        vv.setToolTipText("<html><center>Type 'p' for Pick mode<p>");

/*
        vv.setPreferredSize(new Dimension(800, 800)); //Sets the viewing area size
        Container content = frame.getContentPane();
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        content.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final AbstractModalGraphMouse graphMouse = new DefaultModalGraphMouse<String, Number>();
        vv.setGraphMouse(graphMouse);

        vv.addKeyListener(graphMouse.getModeKeyListener());
        vv.setToolTipText("<html><center>Type 'p' for Pick mode<p>Type 't' for Transform mode");

        final ScalingControl scaler = new CrossoverScalingControl();

        JButton zoom_in = new JButton("zoom in");
        zoom_in.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, (float)2.0, vv.getCenter());
                zoomed_in++;
            }
        });

        JButton zoom_out = new JButton("zoom out");
        zoom_out.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(vv, (float)1/(float)2.0, vv.getCenter());
                zoomed_in--;
            }
        });

        JButton reset = new JButton("reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MutableTransformer layout = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
                layout.setToIdentity();
                MutableTransformer view = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);
                view.setToIdentity();
                if (zoomed_in > 0) {
                    for (int i = 0; i < zoomed_in; i++) {
                        scaler.scale(vv, (float)1/(float)2.0, vv.getCenter());
                    }
                    zoomed_in = 0;
                }
                if (zoomed_in < 0) {
                    zoomed_in = zoomed_in * -1;
                    for (int i = 0; i < zoomed_in; i++) {
                        scaler.scale(vv, (float)2.0, vv.getCenter());
                    }
                }
                zoomed_in = 0;
            }
        });

        JPanel controls = new JPanel();
        controls.add(zoom_in);
        controls.add(zoom_out);
        controls.add(reset);
        content.add(controls, BorderLayout.NORTH);
        content.add(vv);*/

        //jframe interfcae
       /* tresshold.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tresshold.setText("Tresshold: " + tresshold.getText().replaceAll("\\D+", ""));
                System.out.println(tresshold.getText());
            }
        });

        minNodesInCluster.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                minNodesInCluster.setText("No. of nodes: " + minNodesInCluster.getText().replaceAll("\\D+", ""));
                System.out.println(minNodesInCluster.getText());
            }
        });
        tresshold.setToolTipText("By default is the average of graph weights");

        JPanel controls = new JPanel();
        //controls.add(objectList);
        //controls.add(tresshold);
        //controls.add(minNodesInCluster);

        clusteringAlgorithm.setGraph(graph);

        JButton generateMST = new JButton("generate MST");
        generateMST.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MSTFrame(clusteringAlgorithm.generateMST(graph, null, true), "MST");
            }
        });

        controls.add(generateMST);
        if (window_title.contains("Graph")) {
            generateMST.setVisible(true);
        } else {
            generateMST.setVisible(false);
        }
        content.add(controls, BorderLayout.SOUTH);*/

        //frame.setVisible(true);
        frame.setVisible(visible);
        return frame;
    }

    /**
     * This method visualize a graph step by step from its initial form all the way to the is clustered form
     * first step is the complex graph visualization, the graph form of the initial system
     * second step is the MST, a maximum spanning tree of the system, with input for tresshold and minmum no. of nodes in a cluster
     * third step is the clustered form, after applying the clustering algorithm on the system, we obtain the clusters
     * also the MQ is computed based on the clusters
     */
    public JFrame visualizeComplexGraphSteps(Graph graph, String window_title) {
        //this.graph = graph;
        System.out.println(graph.getVertices());
        JFrame frame = visualizeGraph(graph, window_title, false);
        frame.setVisible(true);
        Container content = frame.getContentPane();
        tresshold.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tresshold.setText("Tresshold: " + tresshold.getText().replaceAll("\\D+", ""));
                System.out.println(tresshold.getText());
            }
        });

        minNodesInCluster.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                minNodesInCluster.setText("No. of nodes: " + minNodesInCluster.getText().replaceAll("\\D+", ""));
                System.out.println(minNodesInCluster.getText());
            }
        });
        tresshold.setToolTipText("By default is the average of graph weights");

        JPanel controls = new JPanel();
        //controls.add(objectList);
        //controls.add(tresshold);
        //controls.add(minNodesInCluster);

        clusteringAlgorithm.setGraph(graph);

        JButton generateMST = new JButton("generate MST");
        generateMST.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MSTFrame(clusteringAlgorithm.generateMST(graph, null, true), "MST");
            }
        });

        controls.add(generateMST);
        if (window_title.contains("Graph")) {
            generateMST.setVisible(true);
        } else {
            generateMST.setVisible(false);
        }
        content.add(controls, BorderLayout.SOUTH);
        return frame;
    }

    /**
     * Step (2) frame for visualizing the maximum spanning tree of the system
     * The tresshold by default has the value of the average of all the weights on the edges of the graph
     * The minimum number of nodes in a cluster can be given by the user
     */
    public void MSTFrame(Graph graph, String str) {
        JFrame newFrame = visualizeComplexGraphSteps(graph, str);

        JPanel controls = new JPanel();
        double init_treshold = clusteringAlgorithm.computeTheTressHold(graph);
        tresshold_val = init_treshold;
        setTressholdInputField(tresshold_val);
        setMinNodesInCluster(2);


        JButton clusterFrameBtn = new JButton("Clusters");
        clusterFrameBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clusterFrame(tresshold_val, 2, "CLUSTERS", graph);
            }
        });

        JLabel treshold_label = new JLabel("Treshold: " + tresshold_val);

        treshodComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = treshodComboBox.getSelectedIndex();
                System.out.println(selectedIndex + ": " + tresHoldBoxValues[selectedIndex]);
                if (selectedIndex == 0) {
                    tresshold_val = 1 * init_treshold;
                    treshold_label.setText("Treshold: " + tresshold_val);
                }
                if (selectedIndex == 1) {
                    tresshold_val = 0.25 * init_treshold;
                    treshold_label.setText("Treshold: " + tresshold_val);
                }
                if (selectedIndex == 2) {
                    tresshold_val = 0.50 * init_treshold;
                    treshold_label.setText("Treshold: " + tresshold_val);
                }
                if (selectedIndex == 3) {
                    tresshold_val = 0.75 * init_treshold;
                    treshold_label.setText("Treshold: " + tresshold_val);
                }
                if (selectedIndex == 4) {
                    tresshold_val = 1.25 * init_treshold;
                    treshold_label.setText("Treshold: " + tresshold_val);
                }
                if (selectedIndex == 5) {
                    tresshold_val = 1.50 * init_treshold;
                    treshold_label.setText("Treshold: " + tresshold_val);
                }
                if (selectedIndex == 6) {
                    tresshold_val = 1.75 * init_treshold;
                    treshold_label.setText("Treshold: " + tresshold_val);
                }
                if (selectedIndex == 7) {
                    tresshold_val = 2 * init_treshold;
                    treshold_label.setText("Treshold: " + tresshold_val);
                }
            }
        });

        controls.add(treshodComboBox);
        controls.add(minNodesInCluster);
        controls.add(clusterFrameBtn);
        if (str.contains("MST")) {
            clusterFrameBtn.setVisible(true);
            minNodesInCluster.setVisible(true);
            treshodComboBox.setVisible(true);
        } else {
            clusterFrameBtn.setVisible(false);
            minNodesInCluster.setVisible(false);
        }
        newFrame.add(treshold_label, BorderLayout.NORTH);
        Container content = newFrame.getContentPane();
        content.add(controls, BorderLayout.SOUTH);
    }

    /**
     * Step (3) frame for visualizing the clusters of the system after applying the clustering algorithm
     * The tresshold was and the minum number of nodes in a cluster was given in the second step
     * The MQ is computed
     */
    public void clusterFrame(Double tresshold_value, Integer kids, String str, Graph g) {
        //JFrame newFrame = visualizeGraph(graph, str);
        clusteringAlgorithm.setGraph(g);
        Graph clusters = clusteringAlgorithm.generateClusters(tresshold_value, kids);
        JFrame newFrame = visualizeComplexGraphSteps(clusters, "CLUSTERS");
        JLabel label = new JLabel("MQ: " + clusteringAlgorithm.computeMQ(g, clusteringAlgorithm.getClusters()));
        newFrame.add(label, BorderLayout.NORTH);
        System.out.println("TOOL CLUSTER SIZE: " + clusteringAlgorithm.getClusters().size());
    }

    public void setTressholdInputField(double value) {
        tresshold.setText("Tresshold: " + value);
    }

    public void setMinNodesInCluster(double value) {

        minNodesInCluster.setText("No. of nodes: " + value);
    }
}
