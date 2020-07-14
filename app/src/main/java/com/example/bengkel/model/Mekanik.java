package com.example.bengkel.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Mekanik implements Parcelable {
    String id, idBengkel,imgProfile,nama, alamat, email, nohp;

    public Mekanik() {
    }

    public Mekanik(Parcel source) {
        id=source.readString();
        idBengkel=source.readString();
        imgProfile=source.readString();
        nama=source.readString();
        alamat=source.readString();
        email=source.readString();
        nohp=source.readString();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdBengkel() {
        return idBengkel;
    }

    public void setIdBengkel(String idBengkel) {
        this.idBengkel = idBengkel;
    }

    public String getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(String imgProfile) {
        this.imgProfile = imgProfile;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public Mekanik(String id, String idBengkel, String imgProfile, String nama, String alamat, String email, String nohp) {
        this.id = id;
        this.idBengkel = idBengkel;
        this.imgProfile = imgProfile;
        this.nama = nama;
        this.alamat = alamat;
        this.email = email;
        this.nohp = nohp;
    }

    public static final Creator<Mekanik> CREATOR = new Creator<Mekanik>() {
        @Override
        public Mekanik createFromParcel(Parcel source) {
            return new Mekanik(source);
        }

        @Override
        public Mekanik[] newArray(int size) {
            return new Mekanik[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(idBengkel);
        dest.writeString(imgProfile);
        dest.writeString(nama);
        dest.writeString(alamat);
        dest.writeString(email);
        dest.writeString(nohp);

    }
}
