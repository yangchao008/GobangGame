package com.yc.gobanggame.bean;

import android.graphics.Point;
import android.graphics.RectF;

import static com.yc.gobanggame.bean.Step.Flags.UN_OCCUPY;

/**
 * Author: yangchao
 * Date: 2018-03-26 15:41
 * Comment: //TODO
 */
public class Step {

    public enum UserType{
        BLACK,
        WHITE
    }

    public enum Flags{
        UN_OCCUPY(-1),
        OCCUPY(1);

        public int value;
        Flags(int v){
            value = v;
        }
    }

    public RectF rectF;

    public UserType userType;

    public Point point;

    public Step toRightTopNearStep;

    public Step toRightNearStep;

    public Step toRightBottomNearStep;

    public Step toBottomNearStep;

    public Flags flag = UN_OCCUPY;

}
