package com.yc.gobanggame;

/**
 * Author: yangchao
 * Date: 2018-03-26 15:37
 * Comment: 游戏区的左部分用户操作接口
 */
public interface ILeft {

    void reSet();//重开

    void backStep();//悔棋

    boolean isEmpty();//是否没有任何棋子
}
