package com.example.bengkel.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Account implements Parcelable {
    String id,email,role,aktif;

    public Account() {
    }

    protected Account(Parcel in) {
        id = in.readString();
        email = in.readString();
        role = in.readString();
        aktif = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAktif() {
        return aktif;
    }

    public void setAktif(String aktif) {
        this.aktif = aktif;
    }

    public Account(String id, String email, String role, String aktif) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.aktif = aktif;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(role);
        dest.writeString(aktif);
    }
}
