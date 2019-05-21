package tool;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;
import lombok.Data;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

@Data
public class CSVGraphHandler {

    public Graph<Node, Edge> graph;

    Map nodesMap = new HashMap();
    private int zoomed_in = 0;

    double tresshold_val = 1;

    JTextField tresshold = new JTextField(10);
    JTextField minNodesInCluster = new JTextField(10);

    String[] tresHoldBoxValues = {"25%", "50%", "75%", "100%", "125%", "150%", "175%", "200%"};
    JComboBox treshodComboBox = new JComboBox(tresHoldBoxValues);

    ClusteringAlgorithm clusteringAlgorithm = new ClusteringAlgorithm();

    public void setTressholdInputField(double value) {
        tresshold.setText("Tresshold: " + value);
    }



    public void setMinNodesInCluster(double value) {

        minNodesInCluster.setText("No. of nodes: " + value);
    }

    public void removeEdge() {
    }

    public void addVertexToHashMap(String vertex) {
        if (!nodesMap.containsKey(vertex)) {
            nodesMap.put(vertex, new Node(vertex));
        }
    }

    //public Graph getGraphFromCsvFile(String csvFile) {
    public Graph buildGraphFromCsvFile(String csvFile) {
        /*Graph<Node, Edge> graph = new DirectedSparseMultigraph<Node, Edge>() {
        };*/
        Graph<Node, Edge> graph = new UndirectedSparseMultigraph<Node, Edge>() {
        };
        String line = "";
        String cvsSplitBy = ",";

        //Path path = Paths.get(csvFile);
        File file = new File(csvFile);
        //Path path = file.getAbsolutePath();
        System.out.println("Path : " + file.getAbsolutePath());
        //System.out.println("CCCC" + path.toString());
        System.out.println("CCCC" + file.getAbsolutePath());

        ArrayList<Integer> weight_properties = new ArrayList<>();

        int weight = 0;


        //the first line in the csv file has to be skipped
        int count_line = 0;


        int cnt = 0;

        //try with resources
        //try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            //reading the file, line by line
            while ((line = br.readLine()) != null) {

                if (count_line == 0) {
                    count_line++;
                    continue;
                }

                // splitting the line after comma, eg: ana, bia, maria => edg[0] = ana; edg[1] = bia; edg[2] = maria
                String[] edges = line.split(cvsSplitBy);

                String source = edges[0];
                String destination = edges[1];

                addVertexToHashMap(source);
                addVertexToHashMap(destination);

                //the label
                String edge = "Edge = from: " + source + " , to: " + destination + "";

                //aici verificand mai sus, in functie de config file, daca gaseste ceva din cvs (parcurgi bucata cu bucata),
                // insumezi si adaugi pe arc
                //inca sunt hardcodate, ind reprezinta indicile de care incep parametrii care influenteaza ponderea
                //pana la 13
                //o reprezinta indicele din vectorul de proprietati, ce corespunde parametrului de mai sus
                weight_properties = ConfigHelper.getPropertiesValues();
                int ind = 6;
                int o = 0;
                while (ind != 13) {
                    weight += weight_properties.get(o) * Integer.parseInt(edges[ind]);
                    ind++;
                }


                //System.out.println(cnt++ + ": " + nodesMap.get(source).toString() + " TO " + nodesMap.get(destination).toString());
                //graph.addEdge(new Edge(weight, edge), (Node) nodesMap.get(source), (Node) nodesMap.get(destination), EdgeType.DIRECTED);
                graph.addEdge(new Edge((Node) nodesMap.get(source), (Node) nodesMap.get(destination), weight, edge), (Node) nodesMap.get(source), (Node) nodesMap.get(destination), EdgeType.UNDIRECTED);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("aaaa" + graph.getVertexCount());
        this.graph = graph;
        System.out.println("bbbb:" + this.graph.getVertexCount());
        return graph;
    }

    //public void visualizeGraph(Graph graph) {
    public JFrame visualizeGraph(Graph graph, String window_title) {
//        if(nr_clusters!=null) {
//            initiazeColorMap(nr_clusters.get());
//        }
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
            //return Color.GREEN;}
                /*if(i.getCluster_id().equals("green")) {
                    return Color.GREEN;
                } else return Color.RED;
            }*/
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
        JFrame frame = new JFrame(window_title);
        Container content = frame.getContentPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();

        final AbstractModalGraphMouse graphMouse = new DefaultModalGraphMouse<String, Number>();
        vv.setGraphMouse(graphMouse);
        vv.addKeyListener(graphMouse.getModeKeyListener());
        vv.setToolTipText("<html><center>Type 'p' for Pick mode<p>Type 't' for Transform mode");

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

        String[] objectStrings = {"Triangle", "Box", "Done"};
        JComboBox objectList = new JComboBox(objectStrings);


        //JButton plus = new JButton("+");
        objectList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = objectList.getSelectedIndex();
                System.out.println(selectedIndex);
                if (selectedIndex == 2) {
                    System.exit(0);
                }
            }
        });

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

        frame.setVisible(true);

        return frame;
    }

    public void MSTFrame(Graph graph, String str) {
        JFrame newFrame = visualizeGraph(graph, str);

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

        treshodComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = treshodComboBox.getSelectedIndex();
                System.out.println(selectedIndex + ": " +  tresHoldBoxValues[selectedIndex]);
                if (selectedIndex == 0) {
                    tresshold_val = 0.25 * init_treshold;
                }
                if (selectedIndex == 1) {
                    tresshold_val = 0.50 * init_treshold;
                }
                if (selectedIndex == 2) {
                    tresshold_val = 0.75 * init_treshold;
                }
                if (selectedIndex == 3) {
                    tresshold_val = 1 * init_treshold;
                }
                if (selectedIndex == 4) {
                    tresshold_val = 1.25 * init_treshold;
                }
                if (selectedIndex == 5) {
                    tresshold_val = 1.50 * init_treshold;
                }
                if (selectedIndex == 6) {
                    tresshold_val = 1.75 * init_treshold;
                }
                if (selectedIndex == 7) {
                    tresshold_val = 2 * init_treshold;
                }
            }
        });

        controls.add(tresshold);
        controls.add(treshodComboBox);
        controls.add(minNodesInCluster);
        controls.add(clusterFrameBtn);

        if (str.contains("MST")) {
            clusterFrameBtn.setVisible(true);
            tresshold.setVisible(true);
            minNodesInCluster.setVisible(true);
            treshodComboBox.setVisible(true);
        } else {
            clusterFrameBtn.setVisible(false);
            tresshold.setVisible(false);
            minNodesInCluster.setVisible(false);
        }

        Container content = newFrame.getContentPane();
        content.add(controls, BorderLayout.SOUTH);
    }

    public void clusterFrame(Double tresshold_value, Integer kids, String str, Graph g) {
        //JFrame newFrame = visualizeGraph(graph, str);
        clusteringAlgorithm.setGraph(g);
        Graph clusters = clusteringAlgorithm.generateClusters(tresshold_value, kids);
        JFrame newFrame = visualizeGraph(clusters, "CLUSTERS");
        JLabel label = new JLabel("MQ: " + clusteringAlgorithm.computeMQ(graph, clusteringAlgorithm.getClusters()));
        newFrame.add(label, BorderLayout.NORTH);
    }


    public static void addEdgesFromVisitedNodes(Collection<Edge> source, Collection<Edge> destination) {
        for (Edge t : source) {
            destination.add(t);
        }
    }

    public void showAllVertices() {
        Collection<Node> all_nodes = this.graph.getVertices();

        all_nodes.forEach(System.out::println);
    }
}
