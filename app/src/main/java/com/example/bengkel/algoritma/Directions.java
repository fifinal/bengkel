package com.example.bengkel.algoritma;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Directions {
    private Boolean alternative;
    private Node origin, destination;
    private Graph graph;
    private BellmanFord bellmanFord;
    private Map<String, Node> nodes;
    private List<Edge> edgesResult;

    public Directions() {
    }

    public Directions(Graph graph) {
        graph.init();
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        graph.init();
        this.graph = graph;
    }

    public Boolean getAlternative() {
        return alternative;
    }

    public void setAlternative(Boolean alternative) {
        this.alternative = alternative;
    }

    public Node getOrigin() {
        return origin;
    }

    public void setOrigin(Node origin) {
        this.origin = origin;
        graph.addNodeList(origin);
        neighbour(origin);
    }

    private void neighbour(Node node) {
        GeoPoint start = node.getGeo();

        Location originLocation = new Location("");
        originLocation.setLatitude(start.getLatitude());
        originLocation.setLongitude(start.getLongitude());

        float[] distance = new float[2];
        for (int i = 0; i < distance.length; i++)
            distance[i] = Float.MAX_VALUE;

        List<Node> list = new ArrayList<>();
        boolean found = false;
        for (int i = 0; i < distance.length; i++) {
            list.add(graph.getNodeList().get(i));
            found = false;
            for (Node nd : graph.getNodeList()) {
                if (!list.contains(nd) && nd != node) {

                    Location otheLocation = new Location("");
                    otheLocation.setLatitude(nd.getGeo().getLatitude());
                    otheLocation.setLongitude(nd.getGeo().getLongitude());

                    float distanceResult = originLocation.distanceTo(otheLocation);

                    if (distance[i] > distanceResult) {
                        distance[i] = distanceResult;
                        if (i > 0) {

                            Node oldNode = list.get(i - 1);

                            Location oldLocation = new Location("");
                            oldLocation.setLatitude(oldNode.getGeo().getLatitude());
                            oldLocation.setLongitude(oldNode.getGeo().getLongitude());

                            float newLocationToOldLocation = otheLocation.distanceTo(oldLocation);
                            float newLocationToState = otheLocation.distanceTo(originLocation);

                            Log.d("index " + i, nd.getNode() + distanceResult + "| " + oldNode.getNode()
                                    + newLocationToOldLocation + " | " + newLocationToState);
                            if (newLocationToOldLocation > newLocationToState) {
                                Log.d("inside ", i + " " + newLocationToOldLocation + " | " + newLocationToState);
                                list.set(i, nd);
                                found = true;
                            }

                        } else {
                            found = true;
                            list.set(i, nd);
                        }
                    }
                }

            }
            if (found) {

                Log.d("index " + i, list.get(i).getNode() + "-" + node.getNode());
                graph.addEdge(new Edge(node.getNode() + "-" + list.get(i).getNode(), distance[i]));
                graph.addEdge(new Edge(list.get(i).getNode() + "-" + node.getNode(), distance[i]));
            }
        }
    }

    public void shortestRoutes() {
        nodes = new HashMap<>();
        for (Node node : graph.getNodeList()) {
            nodes.put(node.getNode(), node);
        }
        Log.d("direction", "shortes");

        bellmanFord = new BellmanFord(graph, nodes, origin.getNode());
    }

    public Node getDestination() {
        return destination;
    }

    public void setDestination(Node destination) {
        this.destination = destination;
        graph.addNodeList(destination);
        Log.d("Neighboar destination ", destination.getNode());
        neighbour(destination);
    }

    public List<Edge> getRoutes() {
        Map<String, Edge> resultMap = bellmanFord.result();
        String tujuan = destination.getNode();
        ArrayList<String> rutes = new ArrayList();
        for (int i = 0; i < resultMap.size(); i++) {
            String key = graph.getNodeList().get(i).getNode();
            Log.d("nodes ", key + "\t" + resultMap.get(key).getNode().split("-")[0] + "\t"
                    + resultMap.get(key).getWeight() + "\t\t" + resultMap.get(key).getNode().split("-")[1] + "\n");
        }
        for (int i = 0; i < resultMap.size(); i++) {
            if (i == 0) {
                rutes.add(tujuan);
                tujuan = resultMap.get(tujuan).getNode().split("-")[1];
            } else {
                if (tujuan.equals("start")) {
                    rutes.add("start");
                    break;
                } else {
                    rutes.add(tujuan);
                    tujuan = resultMap.get(tujuan).getNode().split("-")[1];
                }
            }
        }
        edgesResult = new ArrayList<>();
        for (int i = 0; i < rutes.size(); i++) {
            String node = rutes.get(i);
            Log.d("rute ", resultMap.get(node).getNode().split("-")[0]);
            edgesResult.add(resultMap.get(node));
        }
        return edgesResult;

    }
}
