package com.example.zhanghongqiang.databindingsample;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhanghongqiang.databindingsample.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;


/**
 * Created by liangyaotian on 1/20/16.
 */
public final class MyBindingAdapter {

    /**
     * 显示图片
     * xml写法:app:url="@{url}"
     *
     * @param simpleDraweeView fresco的图片
     * @param url              图片地址
     */
    @BindingAdapter({"url"})
    public static void showloadImage(SimpleDraweeView simpleDraweeView, String url) {
        simpleDraweeView.setImageURI("http://img.sibumbg.com/G1/M00/20/A8/CixGgVgQDoiAXmpXAADaMDjvSRE094.jpg");
    }

    @BindingAdapter({"url"})
    public static void showloadImage(ImageView simpleDraweeView, String url) {

    }

    @BindingAdapter({"text"})
    public static void bindEditText(EditText editText, CharSequence oldValue, CharSequence newValue) {
        editText.setText(newValue);
        if (newValue != null) {
            editText.setSelection(newValue.length());
        }
    }

    /**
     * 字符串数据,防止textView出现显示null的字段
     * xml写法:app:text="@{****}"
     */
    @BindingAdapter({"text"})
    public static void bindTextView(TextView textView, CharSequence newValue) {
        if (newValue != null) {
            textView.setText(newValue + "");
        }
    }

    /**
     * 显示整数数据,防止res.Resources$NotFoundException
     * xml写法:app:text="@{2222}"
     */
    @BindingAdapter({"text"})
    public static void bindTextView(TextView textView, int newValue) {
        textView.setText(newValue + "");
    }

    /**
     * 截取小数值,保留几位
     * xml写法:app:text="@{2.2200077778}"
     *
     * @param textView
     * @param doubleValue 小数值
     */
    @BindingAdapter({"text"})
    public static void bindTextView(TextView textView, double doubleValue) {
        textView.setText(StringUtils.getStringFromFloatKeep2(doubleValue));
    }


    @BindingAdapter("selected")
    public static void setPaddingLeft(View view, boolean selected) {
        view.setSelected(selected);
    }


}
