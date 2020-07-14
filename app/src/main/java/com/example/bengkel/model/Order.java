package com.example.bengkel.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Order implements Parcelable {

    private @ServerTimestamp Date timestamp;
    private String id, nama,status,idMekanik;
    private GeoPoint geoPoint;

    public Order( String idMekanik, String nama, String status, GeoPoint geoPoint) {
        this.idMekanik = idMekanik;
        this.nama = nama;
        this.status = status;
        this.geoPoint = geoPoint;
    }

    public Order() {

    }

    protected Order(Parcel in) {
        id = in.readString();
        nama = in.readString();
        status = in.readString();
        idMekanik = in.readString();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdMekanik() {
        return idMekanik;
    }

    public void setIdMekanik(String idMekanik) {
        this.idMekanik = idMekanik;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nama);
        dest.writeString(status);
        dest.writeString(idMekanik);
    }
}
