package com.example.bengkel.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Bengkel implements Parcelable {
    String bengkelId,imgBengkel,namaBengkel, telp, alamatBengkel, buka, tutup;
    boolean penuh;

    public Bengkel(String imgBengkel, String namaBengkel, String alamatBengkel) {
        this.imgBengkel = imgBengkel;
        this.namaBengkel = namaBengkel;
        this.alamatBengkel = alamatBengkel;
    }

    public Bengkel(String bengkelId, String download_uri, String namaBengkel, String alamatBengkel) {
        this.bengkelId = bengkelId;
        this.imgBengkel = download_uri;
        this.namaBengkel = namaBengkel;
        this.alamatBengkel = alamatBengkel;
    }

    public Bengkel(String bengkelId, String imgBengkel, String namaBengkel, String telp, String alamatBengkel, String buka, String tutup) {
        this.bengkelId = bengkelId;
        this.imgBengkel = imgBengkel;
        this.namaBengkel = namaBengkel;
        this.telp = telp;
        this.alamatBengkel = alamatBengkel;
        this.buka = buka;
        this.tutup = tutup;
    }

    public Bengkel(){}

    public String getBengkelId() {
        return bengkelId;
    }

    public void setBengkelId(String bengkelId) {
        this.bengkelId = bengkelId;
    }

    public String getImgBengkel() {
        return imgBengkel;
    }

    public void setImgBengkel(String imgBengkel) {
        this.imgBengkel = imgBengkel;
    }

    public String getNamaBengkel() {
        return namaBengkel;
    }

    public void setNamaBengkel(String namaBengkel) {
        this.namaBengkel = namaBengkel;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getAlamatBengkel() {
        return alamatBengkel;
    }

    public void setAlamatBengkel(String alamatBengkel) {
        this.alamatBengkel = alamatBengkel;
    }

    public String getBuka() {
        return buka;
    }

    public void setBuka(String buka) {
        this.buka = buka;
    }

    public String getTutup() {
        return tutup;
    }

    public void setTutup(String tutup) {
        this.tutup = tutup;
    }

    public boolean isPenuh() {
        return penuh;
    }

    public void setPenuh(boolean penuh) {
        this.penuh = penuh;
    }

    protected Bengkel(Parcel in) {
        bengkelId = in.readString();
        imgBengkel = in.readString();
        namaBengkel = in.readString();
        telp = in.readString();
        alamatBengkel = in.readString();
        buka = in.readString();
        tutup = in.readString();
        penuh = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bengkelId);
        dest.writeString(imgBengkel);
        dest.writeString(namaBengkel);
        dest.writeString(telp);
        dest.writeString(alamatBengkel);
        dest.writeString(buka);
        dest.writeString(tutup);
        dest.writeByte((byte) (penuh ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Bengkel> CREATOR = new Creator<Bengkel>() {
        @Override
        public Bengkel createFromParcel(Parcel in) {
            return new Bengkel(in);
        }

        @Override
        public Bengkel[] newArray(int size) {
            return new Bengkel[size];
        }
    };
}
