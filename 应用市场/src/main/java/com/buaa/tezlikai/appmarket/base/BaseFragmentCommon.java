package com.buaa.tezlikai.appmarket.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/4/23.
 */
public abstract class BaseFragmentCommon extends Fragment {

    //共有属性
    //共有方法

    @Override
    public void onCreate(Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initData();
        initListener();
        super.onActivityCreated(savedInstanceState);
    }



    /**
     * 初始化View，而且是必须实现，但是不知道具体实现，定义成抽象方法
     * @return ：view
     */
    public abstract View initView();

    public void init() {

    }
    public void initListener() {
    }

    public void initData() {
        
    }
}
