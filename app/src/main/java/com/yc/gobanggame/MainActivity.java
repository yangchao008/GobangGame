package com.yc.gobanggame;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Author: yangchao
 * Date: 2018-03-26 15:33
 * Comment: 主页
 */
public class MainActivity extends FragmentActivity {
    final String TAG = getClass().getName();

    Unbinder mUnBinder;
    @BindView(R.id.iv_next)
    ImageView mIvNext;//下一个玩家的icon视图
    AlertDialog.Builder mAlertDialogBuilder;//对话框

    ILeft mILeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化视图
        mUnBinder = ButterKnife.bind(this);

        //加载fragment
        GameAreaFragment fragment = new GameAreaFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.game_area, fragment, GameAreaFragment.TAG).commit();

        mILeft = fragment;

        if (mILeft != null)
            fragment.nextStep(mIvNext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //视图解绑
        mUnBinder.unbind();
    }

    @OnClick({R.id.btn_reset, R.id.btn_back})
    public void onViewClicked(final View view) {
        if (mILeft.isEmpty())   return;
        String msg;
        switch (view.getId()) {
            case R.id.btn_reset:
            default:
//                msg = "确定重开？";
//                view.setTag(0);
                mILeft.reSet();
                break;
            case R.id.btn_back:
                msg = "确定悔棋？";
                view.setTag(1);
                initDialog(view, msg);
                break;
        }
    }

    /**
     * 初始化对话框，如果已经初始化过，就直接展示
     * @param view
     * @param msg
     */
    private void initDialog(final View view, String msg) {
        if (mAlertDialogBuilder == null) {
            mAlertDialogBuilder = new AlertDialog.Builder(this);
            mAlertDialogBuilder.setMessage(msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int type = Integer.parseInt(view.getTag().toString());
                    if (0 == type && mILeft != null) mILeft.reSet();
                    else if (1 == type && mILeft != null) mILeft.backStep();

                }
            }).setNegativeButton("取消", null);
        }
        mAlertDialogBuilder.show();
    }

    @OnClick(R.id.btn_exit)
    public void onViewClicked() {
        finish();//退出按钮
    }
}
