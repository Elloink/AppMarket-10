package com.buaa.tezlikai.appmarket.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.buaa.tezlikai.appmarket.activity.DetailActivity;
import com.buaa.tezlikai.appmarket.base.BaseHolder;
import com.buaa.tezlikai.appmarket.base.SuperBaseAdapter;
import com.buaa.tezlikai.appmarket.bean.AppInfoBean;
import com.buaa.tezlikai.appmarket.holder.AppItemHolder;
import com.buaa.tezlikai.appmarket.utils.UIUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 */
public class AppItemAdapter extends SuperBaseAdapter<AppInfoBean> {

    public AppItemAdapter(AbsListView absListView, List<AppInfoBean> dataSource) {
        super(absListView, dataSource);
    }

    @Override
    public BaseHolder getSpecialHolder(int position) {
        return new AppItemHolder();
    }
    @Override
    public void onNormalItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(UIUtils.getContext(), mDataSource.get(position).packageName, Toast.LENGTH_SHORT).show();
        goToDetailActivity(mDataSource.get(position).packageName);//跳转到详情页面
    }

    private void goToDetailActivity(String packageName) {

        Intent intent = new Intent(UIUtils.getContext(),DetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName",packageName);
        UIUtils.getContext().startActivity(intent);
    }
}
