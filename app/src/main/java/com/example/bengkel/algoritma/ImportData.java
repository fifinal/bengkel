package com.example.bengkel.algoritma;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImportData {
    List<Node> nodeList;
    Map<String,Node> nodeMap;
    List<Edge> edgeList;
    private String[][] dataEdge={{"12","13", "576.8646521910252"},
            {"13","12", "576.8646521910252"},
            {"13","0", "2071.6716431546424"},
            {"0","13", "2071.6716431546424"},
            {"12","1", "2251.421204089918"},
            {"1","12", "2251.421204089918"},
            {"1","0", "1166.0676231429675"},
            {"0","1", "1166.0676231429675"},
            {"1","8", "1014.7060166558107"},
            {"8","1", "1014.7060166558107"},
            {"8","5", "505.2664901014702"},
            {"5","8", "505.2664901014702"},
            {"0","9", "1163.7001945386514"},
            {"9","0", "1163.7001945386514"},
            {"5","2", "278.45275111636795"},
            {"2","5", "278.45275111636795"},
            {"5","6", "521.1502381055346"},
            {"6","5", "521.1502381055346"},
            {"2","6", "515.0825723038049"},
            {"6","2", "515.0825723038049"},
            {"2","3", "1342.3051897275398"},
            {"3","2", "1342.3051897275398"},
            {"9","3", "721.4321381960151"},
            {"3","9", "721.4321381960151"},
            {"9","10", "1691.8368257089646"},
            {"10","9", "1691.8368257089646"},
            {"10","11", "1179.52318337405"},
            {"11","10", "1179.52318337405"},
            {"3","11", "1629.4966245702485"},
            {"11","3", "1629.4966245702485"},
            {"7","8", "1155.8910378075407"},
            {"8","7", "1155.8910378075407"},
            {"7","6", "459.5854005529718"},
            {"6","7", "459.5854005529718"}};

    public ImportData(Map<String,Node> nodeMap) {
        this.nodeMap=nodeMap;
        this.edgeList=new ArrayList<>();
    }

    public List<Node> getNodeList() {



        return nodeList;
    }

    public void setNodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    public List<Edge> getEdgeList() {

        Log.d("data edge :", dataEdge.length+"");
            for(String[] d:dataEdge) {
                Node start = nodeMap.get(d[0]);
                Node end = nodeMap.get(d[1]);
                Edge edge = new Edge(d[0]+"-"+d[1], Float.parseFloat(d[2]));
                edgeList.add(edge);
            }
        return edgeList;
    }

    public void setEdgeList(List<Edge> edgeList) {
        this.edgeList = edgeList;
    }
}
