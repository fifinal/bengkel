package com.example.bengkel.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Chatroom implements Parcelable {

    private String icon, title, content, chatroom_id;
    private @ServerTimestamp
    Date timestamp;

    public Chatroom() {
    }

    protected Chatroom(Parcel in) {
        icon = in.readString();
        title = in.readString();
        content = in.readString();
        chatroom_id = in.readString();
    }

    public static final Creator<Chatroom> CREATOR = new Creator<Chatroom>() {
        @Override
        public Chatroom createFromParcel(Parcel in) {
            return new Chatroom(in);
        }

        @Override
        public Chatroom[] newArray(int size) {
            return new Chatroom[size];
        }
    };

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChatroom_id() {
        return chatroom_id;
    }

    public void setChatroom_id(String chatroom_id) {
        this.chatroom_id = chatroom_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Chatroom(String icon, String title, String content, String chatroom_id, Date timestamp) {
        this.icon = icon;
        this.title = title;
        this.content = content;
        this.chatroom_id = chatroom_id;
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(icon);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(chatroom_id);
    }
}
