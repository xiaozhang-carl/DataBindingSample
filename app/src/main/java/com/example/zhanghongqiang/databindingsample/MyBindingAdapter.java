package com.example.zhanghongqiang.databindingsample;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


/**
 * Created by liangyaotian on 1/20/16.
 */
public final class MyBindingAdapter {


    @BindingAdapter({"bind:url"})
    public static void showloadImage(ImageView view, String url) {
        loadImage(view, url);
    }

    /**
     * @param view
     * @param url
     */
    @BindingAdapter({"bind:url"})
    public static void loadImage(ImageView view, String url) {


        int defaultImageRes;
        Context context = view.getContext();

        defaultImageRes = R.mipmap.ic_launcher;

        //空的图片地址的话就显示默认图片
        if (TextUtils.isEmpty(url)) {
            view.setImageResource(defaultImageRes);
            return;
        }
        if (url.startsWith("htt")) {
            Glide.with(context).load(url).into(view);
        }
        if (url.startsWith("/")) {
            url = "file://" + url;
            Glide.with(context).load(url).into(view);
        }

    }

    @BindingAdapter({"bind:text"})
    public static void bindEditText(EditText editText, CharSequence oldValue, CharSequence newValue) {
        editText.setText(newValue);
        if (newValue != null) {
            editText.setSelection(newValue.length());
        }
    }

    @BindingAdapter("bind:selected")
    public static void setPaddingLeft(View view, boolean selected) {
        view.setSelected(selected);
    }
}
