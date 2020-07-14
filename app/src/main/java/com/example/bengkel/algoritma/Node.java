package com.example.bengkel.algoritma;

import com.google.firebase.firestore.GeoPoint;

public class Node {
    private String node;
    private GeoPoint geo;

    public Node(String node, GeoPoint geo) {
        this.node = node;
        this.geo = geo;
    }

    public Node(String node) {
        this.node = node;
    }

    public Node() {
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public GeoPoint getGeo() {
        return geo;
    }

    public void setGeo(GeoPoint latLng) {
        this.geo = latLng;
    }
}
