package com.yc.gobanggame.bean;

import android.graphics.Point;
import android.graphics.RectF;

import static com.yc.gobanggame.bean.Step.Flags.UN_OCCUPY;

/**
 * Author: yangchao
 * Date: 2018-03-26 15:41
 * Comment: 每一步的对象
 */
public class Step {

    /**
     * 游戏方类型（黑方和白方）
     */
    public enum UserType{
        BLACK,
        WHITE
    }

    /**
     * 标志位（占用和未占用）
     */
    public enum Flags{
        UN_OCCUPY(-1),
        OCCUPY(1);

        public int value;
        Flags(int v){
            value = v;
        }
    }

    public RectF rectF;//长方形区域

    public UserType userType;//游戏方类型(哪一方用户)

    public Point point;//方位{[0,0],[0,1],[0,2],....[1,0],[1,1],[1,2],....}

    //四个方向处理，包含了所有（八个）方位的处理
    public Step toRightTopNearStep;//朝右上方的邻居
    public Step toRightNearStep;//朝右边的邻居
    public Step toRightBottomNearStep;//朝右下方的邻居
    public Step toBottomNearStep;//朝下方的邻居

    public Flags flag = UN_OCCUPY;//标志位

}
