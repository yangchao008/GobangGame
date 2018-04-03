package com.yc.gobanggame.manager;

import com.yc.gobanggame.bean.Step;
import com.yc.gobanggame.util.AppHelperUtil;

import java.util.ArrayList;
import java.util.List;

import static com.yc.gobanggame.bean.Step.Flags.OCCUPY;
import static com.yc.gobanggame.bean.Step.Flags.UN_OCCUPY;

/**
 * Author: yangchao
 * Date: 2018-03-26 19:37
 * Comment: 棋子落下的步数管理者
 */
public class GameStepManger {
    final String TAG = getClass().getName();

    volatile int count = 1;//一方连着的棋子计数
    boolean isComplete = false;//是否哪一方已经赢了
    public List<Step> mStepBlackUser;//黑方落子步数的集合
    public List<Step> mStepWhiteUser;//白方落子步数的集合
    public List<Step> mStepTemp;//临时落子步数的集合
    public Step.UserType mCurrentUserType;//当前哪一方下
    public Step.UserType mWinUserType;//保存赢的一方

    private static volatile GameStepManger mInstance;
    public static GameStepManger getInstance() {
        if (mInstance == null)
            synchronized (GameStepManger.class){
                if (mInstance == null)
                    mInstance = new GameStepManger();
        }
        return mInstance;
    }

    public GameStepManger() {
        //初始化对象
        mStepBlackUser = new ArrayList<>();
        mStepWhiteUser = new ArrayList<>();
        mStepTemp = new ArrayList<>();
        mCurrentUserType = Step.UserType.BLACK;
    }

    /**
     * 添加落子的步对象到集合中
     * @param step
     * @return false 这局棋已经结束；true 添加成功。
     */
    public boolean addStep(Step step){
        if (isComplete)  return false;
        return add(step);
    }

    /**
     * 添加落子的步对象到集合中
     * @param step
     * @return
     */
    private boolean add(Step step) {
        List<Step> currentSteps = getCurrentSteps();

        setNearStep(step,currentSteps);
        step.flag = OCCUPY;
        currentSteps.add(step);
        return true;
    }

    /**
     * 获取当前落子一方的数据集合
     * @return
     */
    private List<Step> getCurrentSteps() {
        return  Step.UserType.BLACK  == mCurrentUserType ? mStepBlackUser : mStepWhiteUser;
    }

    /**
     * 轮询落子一方的数据集合，添加邻居棋子
     * @param newStep
     * @param list
     */
    private void setNearStep(Step newStep, List<Step> list) {
       for (Step step : list){
           int fy = newStep.point.x - step.point.x;
           int fx = newStep.point.y - step.point.y;
           switch (fx){
               case -1:
                   if (0 == fy)  newStep.toRightNearStep = step;
                   else if (1 == fy)   newStep.toRightTopNearStep = step;
                   else if (-1 == fy)   newStep.toRightBottomNearStep = step;
                   break;
               case 0:
                   if (1 == fy)  step.toBottomNearStep = newStep;
                   else if (-1 == fy)  newStep.toBottomNearStep = step;
                   break;
               case 1:
                   if (0 == fy)  step.toRightNearStep = newStep;
                   else if (1 == fy)   step.toRightBottomNearStep = newStep;
                   else if (-1 == fy)   step.toRightTopNearStep = newStep;
                   break;
           }
       }
    }

    /**
     * 重开一局
     */
    public void reSet(){
        isComplete = false;
        mCurrentUserType = Step.UserType.BLACK;
        mStepBlackUser.clear();
        mStepTemp.clear();
        mStepWhiteUser.clear();
    }

    /**
     * 回退一步
     */
    public void backStep() {
        isComplete = false;
        mCurrentUserType = Step.UserType.BLACK  == mCurrentUserType ? Step.UserType.WHITE : Step.UserType.BLACK;
        List<Step> currentSteps = getCurrentSteps();
        //移除最后一步
        Step step = currentSteps.remove(currentSteps.size() - 1);
        for (Step s : currentSteps){
            if (s.toBottomNearStep == step) s.toBottomNearStep = null;
            if (s.toRightBottomNearStep == step) s.toRightBottomNearStep = null;
            if (s.toRightNearStep == step) s.toRightNearStep = null;
            if (s.toRightTopNearStep == step) s.toRightTopNearStep = null;
        }
        //清除和最后一步相关的邻居
        step.flag = UN_OCCUPY;
    }

    /**
     * 这局棋是否结束
     * @return
     */
    public boolean isComplete(){
        if (isComplete) return true;
        count = 1;
        List<Step> currentSteps = getCurrentSteps();
        mWinUserType = mCurrentUserType;
        mCurrentUserType = Step.UserType.BLACK  == mCurrentUserType ? Step.UserType.WHITE : Step.UserType.BLACK;
        if (currentSteps.size() > 4)
            for (Step step : currentSteps) {
                getCountToRightTopNearStep(step.toRightTopNearStep);
                if (count >= 5) {
                    isComplete = true;
                    break;
                } else count = 1;
                getCountToRightNearStep(step.toRightNearStep);
                if (count >= 5) {
                    isComplete = true;
                    break;
                } else count = 1;
                getCountToRightBottomNearStep(step.toRightBottomNearStep);
                if (count >= 5) {
                    isComplete = true;
                    break;
                } else count = 1;
                getCountToBottomNearStep(step.toBottomNearStep);
                if (count >= 5) {
                    isComplete = true;
                    break;
                }else count = 1;
            }
        AppHelperUtil.debug(TAG,"isComplete -- count = " + count);
        return isComplete;
    }

    /**
     * 获取赢的一方
     * @return
     */
    public Step.UserType getWinUserType() {
        return mWinUserType;
    }

    /**
     * 获取朝右上方直线方位的棋子的个数
     * @param root
     */
    private void getCountToRightTopNearStep(Step root) {
        AppHelperUtil.debug(TAG,"getCountToRightTopNearStep -- count = " + count);
        if (root == null || count >= 5)  return;
        else {
            count ++;
            getCountToRightTopNearStep(root.toRightTopNearStep);
        }
    }

    /**
     * 获取朝右方直线方位的棋子的个数
     * @param root
     */
    private void getCountToRightNearStep(Step root) {
        AppHelperUtil.debug(TAG,"getCountToRightNearStep -- count = " + count);
        if (root == null || count >= 5)  return;
        else {
            count ++;
            getCountToRightNearStep(root.toRightNearStep);
        }
    }

    /**
     * 获取朝右下方直线方位的棋子的个数
     * @param root
     */
    private void getCountToRightBottomNearStep(Step root) {
        AppHelperUtil.debug(TAG,"getCountToRightBottomNearStep -- count = " + count);
        if (root == null || count >= 5)  return;
        else {
            count ++;
            getCountToRightBottomNearStep(root.toRightBottomNearStep);
        }
    }

    /**
     * 获取朝下方直线方位的棋子的个数
     * @param root
     */
    private void getCountToBottomNearStep(Step root) {
        AppHelperUtil.debug(TAG,"getCountToBottomNearStep -- count = " + count);
        if (root == null || count >= 5)  return;
        else {
            count ++;
            getCountToBottomNearStep(root.toBottomNearStep);
        }
    }

    /**
     * 获取黑方和白方总的棋子数据集合
     * @return
     */
    public List<Step> getAllStep() {
        List<Step> list = new ArrayList<>();
        mStepTemp.clear();
        mStepTemp.addAll(mStepBlackUser);
        mStepTemp.addAll(mStepWhiteUser);
        for (Step step : mStepTemp)
            list.add(step);

        return list;
    }
}
