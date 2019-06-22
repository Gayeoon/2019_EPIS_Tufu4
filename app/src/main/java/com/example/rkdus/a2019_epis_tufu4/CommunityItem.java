package com.example.rkdus.a2019_epis_tufu4;

import android.graphics.Bitmap;

public class CommunityItem {
    String name, time, content, comment;
    Bitmap profile;


    public CommunityItem(String name, String time, String content, String comment) {
        this.name = name;
        this.time = time;
        this.content = content;
        this.comment = comment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }


}
