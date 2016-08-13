package com.example.zhanghongqiang.databindingsample.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;


public class ToastUtil {

    private static Toast toast = null;

    public static void show(Context context, int res) {
        if (context == null) {
            return;
        }
        show(context, context.getString(res));
    }

    public static void show(Context context, String message) {
        if (context == null) {
            return;
        }
        if (TextUtils.isEmpty(message)) {
            return;
        }
        if (message.startsWith("htt")) {
            return;
        }

        if (message.startsWith("<") && message.endsWith(">")) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

}
