package com.example.zhanghongqiang.databindingsample;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;


/**
 * Created by liangyaotian on 1/20/16.
 */
public final class MyBindingAdapter {

    /**
     * 显示图片
     * xml写法:app:url="@{url}"
     * @param simpleDraweeView  fresco的图片
     * @param url  图片地址
     */
    @BindingAdapter({"bind:url"})
    public static void showloadImage(SimpleDraweeView simpleDraweeView, String url) {
        simpleDraweeView.setImageURI(url);
    }

    @BindingAdapter({"bind:text"})
    public static void bindEditText(EditText editText, CharSequence oldValue, CharSequence newValue) {
        editText.setText(newValue);
        if (newValue != null) {
            editText.setSelection(newValue.length());
        }
    }

    @BindingAdapter({"bind:text"})
    public static void bindTextView(TextView textView, CharSequence newValue) {
        if (newValue != null) {
            textView.setText(newValue);
        }
    }


    /**
     * 截取小数值,保留几位
     * xml写法:app:text="@{2.22}"
     * @param textView
     * @param doubleValue 小数值
     */
    @BindingAdapter({"bind:text"})
    public static void bindTextView(TextView textView, double doubleValue) {
        textView.setText("doubleValue:" + doubleValue);
    }


    @BindingAdapter("bind:selected")
    public static void setPaddingLeft(View view, boolean selected) {
        view.setSelected(selected);
    }
}
