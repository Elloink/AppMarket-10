package com.buaa.tezlikai.appmarket.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.buaa.tezlikai.appmarket.MainActivity;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseActivity extends ActionBarActivity {
    //公共属性
    //公共方法

    private List<ActionBarActivity> activities = new LinkedList<ActionBarActivity>();

    private long mPreTime;
    private Activity mCurActivity;

    /**
     * 得到最上层的activity
     * @return
     */
    public Activity getmCurActivity() {
        return mCurActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initView();
        initActionBar();
        initData();
        initListener();
        activities.add(this);
    }

    @Override
    protected void onDestroy() {
        activities.remove(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        mCurActivity = this;// 最上层的一个activity
        super.onResume();
    }

    public void init() {

    }

    public abstract void initView();

    public void initActionBar() {

    }

    public void initData() {

    }

    public void initListener() {

    }

    /**
     * 完全退出
     */
    public void exit(){
        for (ActionBarActivity activity : activities){
            activity.finish();
        }
    }

    /**
     * 统一的退出控制
     */
    @Override
    public void onBackPressed() {
        if (this instanceof MainActivity){
            if (System.currentTimeMillis() - mPreTime > 2000){//两次点击间隔大于2s
                Toast.makeText(getApplicationContext(),"再按一次，退出应用市场",Toast.LENGTH_LONG).show();
                mPreTime = System.currentTimeMillis();
                return;
            }
        }
        super.onBackPressed();//finish();
    }
}
