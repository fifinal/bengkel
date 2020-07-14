package com.example.bengkel.algoritma;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BellmanFord {
    Map<String, Float> distances;
    Map<String, String> predecessors;
    Map<String, Node> nodes;
    Graph graph;
    String startVertex;

    public BellmanFord(Graph graph, Map<String, Node> nodes, String startVertex) {
        distances = new HashMap<>();
        predecessors = new HashMap<>();
        this.nodes = nodes;
        this.graph = graph;
        this.startVertex = startVertex;

        initialize();
        AllEdge();
    }

    private void initialize() {
        // initialize arrays
        for (String node : nodes.keySet()) {
            distances.put(node, Float.MAX_VALUE);
            predecessors.put(node, "me");
        }
        distances.put(startVertex, 0f);
    }

    public Map<String, Edge> result() {
        Map<String, Edge> resultMap = new HashMap<>();
        for (int i = 0; i < graph.getnNodes(); i++) {
            String key = graph.getNodeList().get(i).getNode();
            String end = predecessors.get(key).equals("me") ? "start" : predecessors.get(key);
            Edge edge = new Edge();
            edge.setNode(nodes.get(key).getNode() + "-" + nodes.get(end).getNode());
            edge.setWeight(distances.get(key));
            resultMap.put(key, edge);
        }
        return resultMap;
    }

    private void cariRute() {
        int tujuan = 9;
        String rute = "9";
        ArrayList<String> rutes = new ArrayList();
        for (int i = 0; i < graph.getnNodes(); i++) {
            Log.d("nodes ", nodes.get(i + "").getNode() + "\t" + distances.get(nodes.get(i + "").getNode()) + "\t\t"
                    + predecessors.get(nodes.get(i + "").getNode()) + "\n");

            if (i == 0) {
                rutes.add(String.valueOf(tujuan));
                rute = predecessors.get(nodes.get(tujuan + "").getNode());
            } else {
                if (rute.equals("0")) {
                    rutes.add(String.valueOf(0));
                    break;
                } else {
                    rutes.add(rute);
                    rute = predecessors.get(nodes.get(rute).getNode());
                }
            }
        }
        for (int i = rutes.size() - 1; i >= 0; i--) {
            String node = rutes.get(i);
            Log.d("rute ", nodes.get(node).getNode());
        }
    }

    private void checkNegativeWeight() {
        // check for negative-weight circles
        for (int i = 0; i < graph.getnNodes(); i++) {
            Iterator<Edge> it = graph.neighbours(nodes.get(i + "").getNode()).iterator();
            while (it.hasNext()) {
                Edge edge = it.next();
                String[] getNodes = edge.getNode().split("-");
                if (distances.get(getNodes[0]) + edge.getWeight() < distances.get(getNodes[1])) {
                    Log.d("Graph negative", "Graph contains negative-weight circle!");

                    distances.put(getNodes[1], Float.NEGATIVE_INFINITY);
                    predecessors.put(getNodes[1], getNodes[0]);
                }
            }
        }
    }

    private void AllEdge() {
        for (int i = 1; i < graph.getnNodes() - 1; i++) {
            for (int j = 0; j < graph.getnNodes(); j++) {
                String node = graph.getNodeList().get(j).getNode();
                Iterator<Edge> it = graph.neighbours(node).iterator();
                while (it.hasNext()) {
                    Edge edge = it.next();
                    String[] getNodes = edge.getNode().split("-");
                    if (distances.get(getNodes[0]) + edge.getWeight() < distances.get(getNodes[1])) {
//                        Log.d("while ",edge.getNode()+" - "+edge.getWeight());
//                        Log.d("if ",getNodes[1]+" = "+Math.round(distances.get(getNodes[0]) + edge.getWeight()) +" < "+ Math.round(distances.get(getNodes[1])));
                        distances.put(getNodes[1], distances.get(getNodes[0]) + edge.getWeight());
                        predecessors.put(getNodes[1], getNodes[0]);
                    }
                }
//                System.out.print(node+" \n "+distances.get(node)+" \n "+predecessors.get(node)+" | ");
            }
           tampilPerhitungan(i);
        }
        // checkNegativeWeight();
    }
    private void tampilPerhitungan(int i){
        System.out.println("Iterasi ke "+i);
        for (Node node: graph.getNodeList()){
            System.out.print(node.getNode()+" \t\t| ");
        }
        System.out.println();
        for (Node node: graph.getNodeList()){
            System.out.print(Math.round(distances.get(node.getNode())/1000)+" \t\t| ");
        }
        System.out.println();
        for (Node node: graph.getNodeList()){
            System.out.print(predecessors.get(node.getNode())+" \t\t| ");
        }
        System.out.println();
        System.out.println();
    }
}
