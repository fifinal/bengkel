package com.example.bengkel.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Layanan implements Parcelable {
    String id,nama;

    public Layanan() {
    }

    public Layanan(String id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    protected Layanan(Parcel in) {
        id = in.readString();
        nama = in.readString();
    }

    public static final Creator<Layanan> CREATOR = new Creator<Layanan>() {
        @Override
        public Layanan createFromParcel(Parcel in) {
            return new Layanan(in);
        }

        @Override
        public Layanan[] newArray(int size) {
            return new Layanan[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nama);
    }
}
