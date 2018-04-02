package com.yc.gobanggame;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Author: yangchao
 * Date: 2018-03-27 10:30
 * Comment: //TODO
 */
public class MyApplication extends Application{

    public static Context sApplicationContext;//应用上下文
    public static int sScreenWidth;//屏幕宽度
    public static int sScreenHeight;//屏幕高度
    public static float sDensity;//屏幕密度

    @Override
    public void onCreate() {
        super.onCreate();
        sApplicationContext = getApplicationContext();

        //初始化变量
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        sScreenWidth = dm.widthPixels;
        sScreenHeight = dm.heightPixels;

        sDensity = getResources().getDisplayMetrics().density;
    }
}
