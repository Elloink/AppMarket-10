package com.buaa.tezlikai.appmarket.factory;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ListView;

import com.buaa.tezlikai.appmarket.utils.UIUtils;

/**
 * Created by Administrator on 2016/4/26.
 * ListView的简单抽取，方便管理listView
 */
public class ListViewFactory {
    public static ListView createListView(){

        //返回成功的视图
        ListView listView = new ListView(UIUtils.getContext());

        //listview的基本设置
        listView.setCacheColorHint(Color.TRANSPARENT);//把分割线设置成透明
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));//把点击的条目的默认颜色改成透明
        listView.setFastScrollEnabled(true);//开启快速滑动模块

        return listView;
    }
}
