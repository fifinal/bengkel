package com.example.bengkel.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.bengkel.algoritma.Node;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;


public class BengkelLocation implements Parcelable {

    private Bengkel bengkel;
    private GeoPoint geoPoint;
    private @ServerTimestamp Date timestamp;

    public BengkelLocation() {
    }

    protected BengkelLocation(Parcel in) {
        bengkel = in.readParcelable(Bengkel.class.getClassLoader());
    }

    public BengkelLocation(Bengkel bengkel, GeoPoint geoPoint, Date timestamp) {
        this.bengkel = bengkel;
        this.geoPoint = geoPoint;
        this.timestamp = timestamp;
    }

    public static final Creator<BengkelLocation> CREATOR = new Creator<BengkelLocation>() {
        @Override
        public BengkelLocation createFromParcel(Parcel in) {
            return new BengkelLocation(in);
        }

        @Override
        public BengkelLocation[] newArray(int size) {
            return new BengkelLocation[size];
        }
    };

    public Bengkel getBengkel() {
        return bengkel;
    }

    public void setBengkel(Bengkel bengkel) {
        this.bengkel = bengkel;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(bengkel, flags);
    }
}
