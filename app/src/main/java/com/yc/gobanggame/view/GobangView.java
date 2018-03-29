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

import com.yc.gobanggame.R;
import com.yc.gobanggame.bean.Step;
import com.yc.gobanggame.manager.GameStepManger;
import com.yc.gobanggame.manager.GameUIManger;
import com.yc.gobanggame.util.AppHelperUtil;

import java.util.List;

/**
 * Author: yangchao
 * Date: 2018-03-26 17:04
 * Comment: //TODO
 */
public class GobangView extends View{
    final String TAG = getClass().getName();

    GameStepManger mGameStepManger;
    GameUIManger mGameUIManger;
    Paint mBackgroundLinePaint,mPaint;
    int left,top;
    Bitmap mWhiteBitmap,mBlackBitmap,mNullBitmap;
    Context mContext;
    ImageView mNextStep;
    Step mLastStep;

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

    private void init(Context context) {
        mContext = context;
        mBlackBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.btn_red_pre);
        mWhiteBitmap = BitmapFactory.decodeResource(context.getResources(),R.mipmap.btn_green_pre);

        mGameStepManger = GameStepManger.getInstance();
        mGameUIManger = GameUIManger.getInstance();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
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
        if (getWidth() > 0 && mGameUIManger.beanSpaceHeight < 1) {
            mGameUIManger.initData(getWidth(), getHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        showNextStep();
        drawAllStep(canvas);
    }

    private void drawAllStep(Canvas canvas) {
        List<Step> steps = mGameStepManger.getAllStep();
        for (Step step : steps){
            canvas.drawBitmap(Step.UserType.BLACK  == step.userType ? mBlackBitmap : mWhiteBitmap ,
                    step.rectF.left,step.rectF.top,mPaint);
        }
    }

    private void showNextStep(){
        if (mNextStep != null)
            mNextStep.setImageResource(Step.UserType.BLACK  == mGameStepManger.mCurrentUserType
                    ? R.mipmap.btn_red_pre : R.mipmap.btn_green_pre);
    }

    private void drawBackground(Canvas canvas) {
        if (left < 1)   return;
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
            pts[ii + 2] = backgroundBeanLine.endPoint.x ;
            AppHelperUtil.debug(TAG,"drawBackground --pts[ii + 2] = " + pts[ii + 2]);
            pts[ii + 3] = backgroundBeanLine.endPoint.y;
            AppHelperUtil.debug(TAG,"drawBackground --pts[ii + 3] = " + pts[ii + 3]);
        }
        canvas.drawLines(pts,mBackgroundLinePaint);
//        for (int i = 0; i < 1; i++) {
//            GameUIManger.BackgroundBeanLine backgroundBeanLine = lines.get(i);
//            float startX  = backgroundBeanLine.startPoint.x + left;
//            float startY = backgroundBeanLine.startPoint.y + top;
//            float stopX = backgroundBeanLine.endPoint.x + left;
//            float stopY = backgroundBeanLine.endPoint.y+ top;
//            canvas.drawLine(startX,startY,stopX,stopY,mBackgroundLinePaint);
//        }
    }

    public void reSet() {
        mGameStepManger.reSet();
        mGameUIManger.reSet();
        invalidate();
    }

    public void backStep() {
        if (mGameStepManger.mStepWhiteUser.size() > 0 || mGameStepManger.mStepBlackUser.size() > 0) {
            mGameStepManger.backStep();
            invalidate();
        }
    }

    public boolean isEmpty() {
        return 0 == mGameStepManger.mStepBlackUser.size() && 0 == mGameStepManger.mStepWhiteUser.size();
    }

    public void destroy(){
        mGameStepManger.reSet();
        recycleBitmap(mBlackBitmap);
        recycleBitmap(mWhiteBitmap);
        recycleBitmap(mNullBitmap);
    }

    private void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled())
            bitmap.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Step step = mGameUIManger.getStep(event.getX(),event.getY(),mGameStepManger.mCurrentUserType);
                if (step != null && mGameStepManger.addStep(step)){
                    mLastStep = step;
                    if (mGameStepManger.isComplete()){
                        Toast.makeText(mContext,(Step.UserType.BLACK == mGameStepManger.getWinUserType() ?
                                "红方" : "绿方") + "赢了" ,Toast.LENGTH_LONG).show();
                    }
                    invalidate();
                    return true;
                }
        }
        return super.onTouchEvent(event);
    }

}
