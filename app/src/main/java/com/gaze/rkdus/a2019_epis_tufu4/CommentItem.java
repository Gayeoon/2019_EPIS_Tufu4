package com.gaze.rkdus.a2019_epis_tufu4;

/*
*  CommentItem class
*  Copyright 2019, 김가연. All rights reserved.
*/

public class CommentItem {
    String id, date, comment;
    int index;

    public CommentItem(String id, String date, String comment) {
        this.id = id;
        this.date = date;
        this.comment = comment;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getid() {
        return id;
    }

    public void setdate(String time) {
        this.date = date;
    }

    public String getdate() {
        return date;
    }

    public void setcomment(String comment) {
        this.comment = comment;
    }

    public String getcomment() {
        return comment;
    }

}
