package com.yc.gobanggame.manager;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

import com.yc.gobanggame.MyApplication;
import com.yc.gobanggame.bean.Step;
import com.yc.gobanggame.util.AppHelperUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: yangchao
 * Date: 2018-03-27 10:24
 * Comment: 棋盘UI管理者
 */
public class GameUIManger {
    final String TAG = getClass().getName();

    final int HORIZONTAL_LINE_COUNT = 11;//棋盘水平线条数
    final int VERTICAL_LINE_COUNT = 11;//棋盘竖直线条数
    final float AREA_BOUNDER = 20 * MyApplication.sDensity;//棋盘离view边上的距离
    public List<BackgroundBeanLine> mBackgroundLines;//棋盘背景线的集合
    public List<SpaceBeanRect> mSpaceBeanRect;//棋子落下的空间集合
    public float beanSpaceWidth;//棋子落下的空间的宽度
    public float beanSpaceHeight;//棋子落下的空间的高度
    RectF mGameRectF = new RectF();//棋盘的长方形区域

    private static volatile GameUIManger mInstance;
    public static GameUIManger getInstance() {
        if (mInstance == null)
            synchronized (GameUIManger.class){
                if (mInstance == null)
                    mInstance = new GameUIManger();
            }
        return mInstance;
    }

    /**
     * 初始化
     * @param width
     * @param height
     */
    public void initData(float width, float height) {
        beanSpaceWidth = (width - 2 * AREA_BOUNDER) / (VERTICAL_LINE_COUNT - 1);
        beanSpaceHeight = (height- 2 * AREA_BOUNDER)  / (HORIZONTAL_LINE_COUNT - 1);

        AppHelperUtil.debug(TAG," initData -- beanSpaceWidth = " + beanSpaceWidth
                + "-- beanSpaceHeight = " + beanSpaceHeight);
        initBackgroundLines();
        initSpaceBeanRect();
    }

    /**
     * 初始化棋子落下的空间集合
     */
    private void initSpaceBeanRect() {
        if (mSpaceBeanRect != null)   mSpaceBeanRect.clear();
        mSpaceBeanRect = new ArrayList<>();
        float fx = AREA_BOUNDER - beanSpaceWidth / 2;
        float fy = AREA_BOUNDER - beanSpaceHeight / 2;
        for (int i = 0; i < VERTICAL_LINE_COUNT; i++) {
            for (int j = 0; j < HORIZONTAL_LINE_COUNT; j++) {
                SpaceBeanRect spaceBeanRect = new SpaceBeanRect();
                spaceBeanRect.step = new Step();
                spaceBeanRect.step.point = new Point(i,j);

                float left = j * beanSpaceWidth + fx;
                float right = left +  beanSpaceWidth;
                float top = i * beanSpaceHeight + fy;
                float bottom = top + beanSpaceHeight;
                spaceBeanRect.rectF = new RectF(left,top,right,bottom);
                mSpaceBeanRect.add(spaceBeanRect);
                AppHelperUtil.debug(TAG,"spaceBeanRect.rectF = " +
                        spaceBeanRect.rectF.toString() + "--AREA_BOUNDER =" + AREA_BOUNDER );
            }
        }
        mGameRectF.left = mSpaceBeanRect.get(0).rectF.left;
        mGameRectF.top = mSpaceBeanRect.get(0).rectF.top;
        mGameRectF.right = mSpaceBeanRect.get(mSpaceBeanRect.size() -1).rectF.right;
        mGameRectF.bottom = mSpaceBeanRect.get(mSpaceBeanRect.size() -1).rectF.bottom;
    }

    /**
     *初始化棋盘背景线的集合
     */
    private void initBackgroundLines() {
        if (mBackgroundLines != null)   return;

        mBackgroundLines = new ArrayList<>();
        for (int i = 0; i < VERTICAL_LINE_COUNT; i++) {
            BackgroundBeanLine backgroundBeanLine = new BackgroundBeanLine();
            float fy = i * beanSpaceHeight + AREA_BOUNDER;
            backgroundBeanLine.startPoint = new PointF(0+ AREA_BOUNDER,fy);
            backgroundBeanLine.endPoint = new PointF((VERTICAL_LINE_COUNT - 1) * beanSpaceWidth + AREA_BOUNDER,fy);
            mBackgroundLines.add(backgroundBeanLine);
        }

        for (int i = 0; i < HORIZONTAL_LINE_COUNT; i++) {
            BackgroundBeanLine backgroundBeanLine = new BackgroundBeanLine();
            float fX = i * beanSpaceWidth+ AREA_BOUNDER;
            backgroundBeanLine.startPoint = new PointF(fX,0+ AREA_BOUNDER);
            backgroundBeanLine.endPoint = new PointF(fX,(HORIZONTAL_LINE_COUNT - 1) * beanSpaceHeight + AREA_BOUNDER);
            mBackgroundLines.add(backgroundBeanLine);
        }
    }

    /**
     * 根据当前手指点下的坐标，获取棋子当前步的对象，为null的话，说明当前步已经落过
     * @param x
     * @param y
     * @param userType
     * @return
     */
    public Step getStep(float x, float y, Step.UserType userType){
        Step step = null;
        if (isGameArea(x,y)){
            for (int i = 0; i < mSpaceBeanRect.size(); i++) {
                SpaceBeanRect spaceBeanRect = mSpaceBeanRect.get(i);
                RectF rectF = spaceBeanRect.rectF;
                if (rectF.left < x && x< rectF.right && rectF.top < y && y < rectF.bottom
                        && spaceBeanRect.step.flag.value < 0) {
                    spaceBeanRect.step.userType = userType;
                    spaceBeanRect.step.rectF = rectF;
                    step = spaceBeanRect.step;
                    break;
                }
            }
        }
        return step;
    }

    /**
     * 获取棋子的宽度
     * @return
     */
    public int getIconWidth(){
        return (int)(Math.min(beanSpaceHeight,beanSpaceWidth) - 2);
    }

    /**
     * 根据x,y坐标，判断当前点的位置是否在棋盘内
     * @param x
     * @param y
     * @return
     */
    private boolean isGameArea(float x,float y){
        if (mGameRectF.left < x && x< mGameRectF.right && mGameRectF.top < y && y < mGameRectF.bottom)
            return true;
        return false;
    }

    /**
     * 重开一局
     */
    public void reSet() {
        initSpaceBeanRect();
    }

    /**
     * 背景线对象
     */
    public static class BackgroundBeanLine {
        public PointF startPoint;//开始点
        public PointF endPoint;//结束点
    }

    /**
     *棋子落下的空间对象
     */
    public static class SpaceBeanRect{
        public Step step;//步
        public RectF rectF;//落子区域
    }
}
