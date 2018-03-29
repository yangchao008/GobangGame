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

    public static Context sApplicationContext;
    public static int sScreenWidth;
    public static int sScreenHeight;
    public static float sDensity;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplicationContext = getApplicationContext();

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        sScreenWidth = dm.widthPixels;
        sScreenHeight = dm.heightPixels;

        sDensity = getResources().getDisplayMetrics().density;
    }
}
