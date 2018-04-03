package com.yc.gobanggame.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.yc.gobanggame.MyApplication;
import com.yc.gobanggame.R;
import com.yc.gobanggame.bean.Step;
import com.yc.gobanggame.manager.GameStepManger;
import com.yc.gobanggame.manager.GameUIManger;
import com.yc.gobanggame.util.AppHelperUtil;

import java.util.List;

/**
 * Author: yangchao
 * Date: 2018-03-26 17:04
 * Comment: 游戏区的view
 */
public class GobangView extends View{
    final String TAG = getClass().getName();

    GameStepManger mGameStepManger;//步数管理者
    GameUIManger mGameUIManger;//UI管理者
    Paint mBackgroundLinePaint, mStepPaint;
    int left,top;//距离屏幕左边和顶部的距离（没有用处）
    Bitmap mWhiteBitmap,mBlackBitmap;
    Context mContext;
    ImageView mNextStep;

    public void setImageView(ImageView imageView) {
        mNextStep = imageView;
    }

    public GobangView(Context context) {
        super(context);
        init(context);
    }

    public GobangView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GobangView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public GobangView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * 初始化
     * @param context
     */
    private void init(Context context) {
        mContext = context;
        mBlackBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.btn_red_pre);
        mWhiteBitmap = BitmapFactory.decodeResource(context.getResources(),R.mipmap.btn_green_pre);

        mGameStepManger = GameStepManger.getInstance();
        mGameUIManger = GameUIManger.getInstance();

        mStepPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundLinePaint = new Paint();

        /*
         * 设置画笔样式为描边，圆环嘛……当然不能填充不然就么意思了
         * 画笔样式分三种：
         * 1.Paint.Style.STROKE：描边
         * 2.Paint.Style.FILL_AND_STROKE：描边并填充
         * 3.Paint.Style.FILL：填充
         */
        mBackgroundLinePaint.setStyle(Paint.Style.STROKE);
        mBackgroundLinePaint.setColor(Color.BLACK);

        /*
         * 设置描边的粗细，单位：像素px
         * 注意：当setStrokeWidth(0)的时候描边宽度并不为0而是只占一个像素
         */
        mBackgroundLinePaint.setStrokeWidth(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int[] mFloats = new int[2];
        getLocationOnScreen(mFloats);
        left = mFloats[0];
        top = mFloats[1];
        AppHelperUtil.debug(TAG,"onMeasure left = " + left + "-- top = " + top);

        int width = getWidth();
        if (width > 0) {
            mGameUIManger.initData(width, getHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mGameUIManger.initData(getWidth(), getHeight());//手机平台的onMeasure方法中getWidth()值为0
        drawBackground(canvas);
        showNextStep();
        drawAllStep(canvas);
    }

    /**
     * 绘画走的所有棋子
     * @param canvas
     */
    private void drawAllStep(Canvas canvas) {
        List<Step> steps = mGameStepManger.getAllStep();
        for (Step step : steps){
            canvas.drawBitmap(Step.UserType.BLACK  == step.userType ? mBlackBitmap : mWhiteBitmap ,
                    step.rectF.left,step.rectF.top, mStepPaint);
        }
    }

    /**
     * 展示当前落子的一方
     */
    private void showNextStep(){
        if (mNextStep != null)
            mNextStep.setImageResource(Step.UserType.BLACK  == mGameStepManger.mCurrentUserType
                    ? R.mipmap.btn_red_pre : R.mipmap.btn_green_pre);
    }

    /**
     * 绘画棋盘的背景线条
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        List<GameUIManger.BackgroundBeanLine> lines = mGameUIManger.mBackgroundLines;
        int size = lines.size();
        AppHelperUtil.debug(TAG,"drawBackground -- left = " + left + "-- top = " + top+ "-- size = " + size);
        float pts[] = new float[size * 4];
        for (int i = 0; i < size; i++) {
            GameUIManger.BackgroundBeanLine backgroundBeanLine = lines.get(i);
            int ii = 4 * i;
            pts[ii] = backgroundBeanLine.startPoint.x;
            AppHelperUtil.debug(TAG,"drawBackground --pts[ii] = " + pts[ii]);
            pts[ii + 1] = backgroundBeanLine.startPoint.y;
            AppHelperUtil.debug(TAG,"drawBackground --pts[ii + 1] = " + pts[ii + 1]);
            pts[ii + 2] = backgroundBeanLine.endPoint.x;
            AppHelperUtil.debug(TAG,"drawBackground --pts[ii + 2] = " + pts[ii + 2]);
            pts[ii + 3] = backgroundBeanLine.endPoint.y;
            AppHelperUtil.debug(TAG,"drawBackground --pts[ii + 3] = " + pts[ii + 3]);
        }
        canvas.drawLines(pts,mBackgroundLinePaint);
    }

    /**
     * 重开一局
     */
    public void reSet() {
        mGameStepManger.reSet();
        mGameUIManger.reSet();
        invalidate();
    }

    /**
     * 悔棋（回退到上一步）
     */
    public void backStep() {
        if (mGameStepManger.mStepWhiteUser.size() > 0 || mGameStepManger.mStepBlackUser.size() > 0) {
            mGameStepManger.backStep();
            invalidate();
        }
    }

    /**
     * 棋盘是不是没有落子
     * @return
     */
    public boolean isEmpty() {
        return 0 == mGameStepManger.mStepBlackUser.size() && 0 == mGameStepManger.mStepWhiteUser.size();
    }

    /**
     * 释放资源
     */
    public void destroy(){
        mGameStepManger.reSet();
        mGameUIManger.clear();
        recycleBitmap(mBlackBitmap);
        recycleBitmap(mWhiteBitmap);
    }

    /**
     * 回收bitmap
     * @param bitmap
     */
    private void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled())
            bitmap.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //根据当前手指点下的坐标，获取棋子当前步的对象，为null的话，说明当前步已经落过
                Step step = mGameUIManger.getStep(event.getX(),event.getY(),mGameStepManger.mCurrentUserType);
                if (step != null && mGameStepManger.addStep(step)){
                    if (mGameStepManger.isComplete()){//判断是不是当前方已经赢了
                        Toast.makeText(mContext,(Step.UserType.BLACK == mGameStepManger.getWinUserType() ?
                                "红方" : "绿方") + "赢了" ,Toast.LENGTH_LONG).show();
                    }
                    //重绘
                    invalidate();
                    return true;
                }
        }
        return super.onTouchEvent(event);
    }

}
