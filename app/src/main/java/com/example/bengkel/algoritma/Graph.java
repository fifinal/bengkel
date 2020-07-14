package com.example.bengkel.algoritma;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private int nNodes;
    private List<Edge> edges;
    private List<Node> nodeList;
    private Map<String, List<Edge>> nodes;

    public Graph(int nNodes) {
        this.nNodes = nNodes;
        this.nodes = new HashMap();
    }

    public Graph() {
        this.nodes = new HashMap();
    }

    public Graph(List<Node> nodeList, List<Edge> edges) {
        this.nodeList=nodeList;
        this.nNodes=nodeList.size();
        this.nodes = new HashMap();
        this.edges=edges;
    }

    public void init(){
        for (Node n:nodeList){
            putEmptyEdge(n.getNode());
        }
        for (Edge edge:edges){
            addEdge(edge);
        }
    }

    public void addNodeList(Node node) {
        this.nodeList.add(node);
        putEmptyEdge(node.getNode());
    }

    public void addEdge(Edge edge) {
        String node=edge.getNode().split("-")[0];
        if(nodes.containsKey(node)){
            nodes.get(node).add(edge);
        }
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges =new ArrayList<>(edges);
    }

    public void setNodeList(List<Node> nodeList) {
        this.nodeList = new ArrayList<>(nodeList);
        this.nNodes=nodeList.size();
    }

    public List<Edge> neighbours(String start) {
        return nodes.get(start);
    }

    public int getnNodes() {
        return nodes.size();
    }

    public Map<String, List<Edge>> getNodes() {
        return nodes;
    }

    public void putEmptyEdge(String name) {
        nodes.put(name, new ArrayList());
    }
}
