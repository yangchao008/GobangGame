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
 * Comment: //TODO
 */
public class GameUIManger {
    final String TAG = getClass().getName();

    final int HORIZONTAL_SPACE_COUNT = 11;
    final int VERTICAL_SPACE_COUNT = 11;
    final float AREA_BOUNDER = 20 * MyApplication.sDensity;
    public List<BackgroundBeanLine> mBackgroundLines;
    public List<SpaceBeanRect> mSpaceBeanRect;
    public float beanSpaceWidth;
    public float beanSpaceHeight;
    RectF mGameRectF = new RectF();

    private static volatile GameUIManger mInstance;
    public static GameUIManger getInstance() {
        if (mInstance == null)
            synchronized (GameUIManger.class){
                if (mInstance == null)
                    mInstance = new GameUIManger();
            }
        return mInstance;
    }

    public void initData(float width, float height) {
        beanSpaceWidth = (width - 2 * AREA_BOUNDER) / (VERTICAL_SPACE_COUNT - 1);
        beanSpaceHeight = (height- 2 * AREA_BOUNDER)  / (HORIZONTAL_SPACE_COUNT - 1);

        AppHelperUtil.debug(TAG," initData -- beanSpaceWidth = " + beanSpaceWidth
                + "-- beanSpaceHeight = " + beanSpaceHeight);
        initBackgroundLines();
        initSpaceBeanRect();
    }

    private void initSpaceBeanRect() {
        if (mSpaceBeanRect != null)   mSpaceBeanRect.clear();
        mSpaceBeanRect = new ArrayList<>();
        float fx = AREA_BOUNDER - beanSpaceWidth / 2;
        float fy = AREA_BOUNDER - beanSpaceHeight / 2;
        for (int i = 0; i < VERTICAL_SPACE_COUNT; i++) {
            for (int j = 0; j <HORIZONTAL_SPACE_COUNT; j++) {
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

    private void initBackgroundLines() {
        if (mBackgroundLines != null)   return;

        mBackgroundLines = new ArrayList<>();
        for (int i = 0; i < VERTICAL_SPACE_COUNT; i++) {
            BackgroundBeanLine backgroundBeanLine = new BackgroundBeanLine();
            float fy = i * beanSpaceHeight + AREA_BOUNDER;
            backgroundBeanLine.startPoint = new PointF(0+ AREA_BOUNDER,fy);
            backgroundBeanLine.endPoint = new PointF((VERTICAL_SPACE_COUNT - 1) * beanSpaceWidth + AREA_BOUNDER,fy);
            mBackgroundLines.add(backgroundBeanLine);
        }

        for (int i = 0; i < HORIZONTAL_SPACE_COUNT; i++) {
            BackgroundBeanLine backgroundBeanLine = new BackgroundBeanLine();
            float fX = i * beanSpaceWidth+ AREA_BOUNDER;
            backgroundBeanLine.startPoint = new PointF(fX,0+ AREA_BOUNDER);
            backgroundBeanLine.endPoint = new PointF(fX,(HORIZONTAL_SPACE_COUNT- 1) * beanSpaceHeight + AREA_BOUNDER);
            mBackgroundLines.add(backgroundBeanLine);
        }
    }

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

    public int getIconWidth(){
        return (int)(Math.min(beanSpaceHeight,beanSpaceWidth) - 2);
    }

    private boolean isGameArea(float x,float y){
        if (mGameRectF.left < x && x< mGameRectF.right && mGameRectF.top < y && y < mGameRectF.bottom)
            return true;
        return false;
    }

    public void reSet() {
        initSpaceBeanRect();
    }

    public static class BackgroundBeanLine {
        public PointF startPoint;
        public PointF endPoint;
    }

    public static class SpaceBeanRect{
        public Step step;
        public RectF rectF;
    }
}
