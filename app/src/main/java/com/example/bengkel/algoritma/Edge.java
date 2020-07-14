package com.example.bengkel.algoritma;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class Edge implements Parcelable {
    private String node;
    private float weight;
    private List<GeoPoint> latLng;


    protected Edge(Parcel in) {
        node = in.readString();
        weight = in.readFloat();
    }

    public static final Creator<Edge> CREATOR = new Creator<Edge>() {
        @Override
        public Edge createFromParcel(Parcel in) {
            return new Edge(in);
        }

        @Override
        public Edge[] newArray(int size) {
            return new Edge[size];
        }
    };

    public List<GeoPoint> getLatLng() {
        return latLng;
    }

    public void setLatLng(List<GeoPoint> latLng) {
        this.latLng = latLng;
    }


    public Edge(String node, float weight) {
        this.node=node;
        this.weight = weight;
    }

    public Edge(String node,List<GeoPoint> latLng, float weight) {
        this.node=node;
        this.latLng = latLng;
        this.weight = weight;
    }
    public Edge() {
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(node);
        dest.writeFloat(weight);
    }
}
