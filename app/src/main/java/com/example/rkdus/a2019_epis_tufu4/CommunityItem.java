package com.example.rkdus.a2019_epis_tufu4;

public class CommunityItem {
    String title, written;
    int index;

    public CommunityItem(String title, String written, int index) {
        this.title = title;
        this.written = written;
        this.index = index;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setWritten(String time) {
        this.written = written;
    }

    public String getWritten() {
        return written;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

}
