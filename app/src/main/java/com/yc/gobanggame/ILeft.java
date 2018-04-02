package com.yc.gobanggame;

import android.widget.ImageView;

/**
 * Author: yangchao
 * Date: 2018-03-26 15:37
 * Comment: 游戏区的左部分用户操作接口
 */
public interface ILeft {

    void reSet();//重开

    void backStep();//悔棋

    void nextStep(ImageView imageView);//下一个玩家的icon

    boolean isEmpty();//是否没有任何棋子
}
