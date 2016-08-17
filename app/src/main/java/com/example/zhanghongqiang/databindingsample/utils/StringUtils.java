package com.example.zhanghongqiang.databindingsample.utils;

import java.math.BigDecimal;

/**
 * Created by zhanghongqiang on 16/8/17  下午4:58
 * ToDo:字符串工具类
 */
public class StringUtils {

    public static String getStringFromFloatKeep2(double value) {
        if (value == 0) {
            return "0";
        }

        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "";
    }

}
