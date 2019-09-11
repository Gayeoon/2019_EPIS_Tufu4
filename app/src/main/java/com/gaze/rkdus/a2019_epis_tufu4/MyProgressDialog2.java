package com.gaze.rkdus.a2019_epis_tufu4;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/*
 *  MessageView class
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class MyProgressDialog2 extends Dialog {


    public static MyProgressDialog2 show(Context context, CharSequence title,
                                         CharSequence message) {
        return show(context, title, message, false);
    }

    public static MyProgressDialog2 show(Context context, CharSequence title,
                                         CharSequence message, boolean indeterminate) {
        return show(context, title, message, indeterminate, false, null);
    }

    public static MyProgressDialog2 show(Context context, CharSequence title,
                                         CharSequence message, boolean indeterminate, boolean cancelable) {
        return show(context, title, message, indeterminate, cancelable, null);
    }


    public static MyProgressDialog2 show(Context context, CharSequence title,
                                         CharSequence message, boolean indeterminate,
                                         boolean cancelable, OnCancelListener cancelListener) {
        MyProgressDialog2 dialog = new MyProgressDialog2(context);
        dialog.setTitle(title);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        /* The next line will add the ProgressBar to the dialog. */
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#2eb8ff"), PorterDuff.Mode.MULTIPLY);
        progressBar.setIndeterminate(true);
        dialog.addContentView(progressBar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        dialog.show();

        return dialog;
    }

    public MyProgressDialog2(Context context) {
        super(context, R.style.NewDialog);
    }
}