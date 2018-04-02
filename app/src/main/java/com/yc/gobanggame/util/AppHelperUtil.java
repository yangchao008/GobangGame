package com.yc.gobanggame.util;

import android.util.Log;

/**
 * Author: yangchao
 * Date: 2018-03-26 17:09
 * Comment: 应用帮助类（代码复用）
 */
public class AppHelperUtil {

    final static String TAG = AppHelperUtil.class.getName();

    final static String DEBUG = "deBug";
    final static boolean isDebug = true;

    /**
     * DEBUG
     * @param tag
     * @param content
     */
    public static void debug(String tag,String content){
        if (isDebug)    Log.d(DEBUG,tag + "---" + content);
    }
}
