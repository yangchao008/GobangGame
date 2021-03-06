package com.yc.gobanggame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yc.gobanggame.view.GobangView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: yangchao
 * Date: 2018-03-26 15:34
 * Comment: 游戏区的fragment
 */
public class GameAreaFragment extends Fragment implements ILeft {
    public static final String TAG = GameAreaFragment.class.getName();
    ImageView mNextStep;//下一个玩家的icon视图
    View mContentView;//内容视图
    @BindView(R.id.gobang_view)
    GobangView mGobangView;
    Unbinder mUnBinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_game_area, container, false);
        //初始化视图
        mUnBinder = ButterKnife.bind(this, mContentView);

        mGobangView.setImageView(mNextStep);
        return mContentView;
    }

    @Override
    public void reSet() {
        mGobangView.reSet();
    }

    @Override
    public void backStep() {
        mGobangView.backStep();
    }

    public void nextStep(ImageView imageView) {
        mNextStep = imageView;
    }

    @Override
    public boolean isEmpty() {
        return mGobangView.isEmpty();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //释放游戏区view的资源
        mGobangView.destroy();
        //视图解绑
        mUnBinder.unbind();
    }
}
