package com.example.bengkel.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;


public class ChatMessage implements Parcelable {

    private Account account;
    private String message;
    private String message_id;
    private @ServerTimestamp
    Date timestamp;

    public ChatMessage(Account account, String message, String message_id, Date timestamp) {
        this.account=account;
        this.message = message;
        this.message_id = message_id;
        this.timestamp = timestamp;
    }

    public ChatMessage() {

    }

    protected ChatMessage(Parcel in) {
        account = in.readParcelable(Account.class.getClassLoader());
        message = in.readString();
        message_id = in.readString();
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel in) {
            return new ChatMessage(in);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
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
        dest.writeParcelable(account, flags);
        dest.writeString(message);
        dest.writeString(message_id);
    }
}
