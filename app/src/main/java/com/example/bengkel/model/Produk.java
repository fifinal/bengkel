package com.example.bengkel.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Produk implements Parcelable {

    String namaProduk,id;
    int stok, harga;

    public Produk(String id, String namaProduk, int stok, int harga) {
        this.id = id;
        this.namaProduk = namaProduk;
        this.stok = stok;
        this.harga = harga;
    }

    public Produk() {}
    public Produk(Parcel source) {
        id=source.readString();
        namaProduk=source.readString();
        stok=source.readInt();
        harga=source.readInt();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public static final Creator<Produk> CREATOR =new Creator<Produk>() {
        @Override
        public Produk createFromParcel(Parcel source) {
            return new Produk(source);
        }

        @Override
        public Produk[] newArray(int size) {
            return new Produk[size];
        }
    };
    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(namaProduk);
        dest.writeInt(stok);
        dest.writeInt(harga);
    }
}
