package com.example.zhanghongqiang.databindingsample.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * User: Yaotian Leung
 * Date: 2013-11-13
 * Time: 17:39
 */
public class ToastUtil {

    private static Toast toast=null;

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
        //授权证书de的返回是message,不需要吐司
        if (message.startsWith("htt")) {
            return;
        }
        if (message.startsWith("<")) {
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
