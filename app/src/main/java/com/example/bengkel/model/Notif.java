package com.example.bengkel.model;

public class Notif {

    String icon, name, content, time;

    public Notif(String icon, String name, String content, String time) {
        this.icon = icon;
        this.name = name;
        this.content = content;
        this.time = time;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
