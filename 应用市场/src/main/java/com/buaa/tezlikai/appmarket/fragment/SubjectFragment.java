package com.buaa.tezlikai.appmarket.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.buaa.tezlikai.appmarket.base.BaseFragment;
import com.buaa.tezlikai.appmarket.base.BaseHolder;
import com.buaa.tezlikai.appmarket.base.LoadingPager;
import com.buaa.tezlikai.appmarket.base.LoadingPager.LoadedResult;
import com.buaa.tezlikai.appmarket.base.SuperBaseAdapter;
import com.buaa.tezlikai.appmarket.bean.SubjectInfoBean;
import com.buaa.tezlikai.appmarket.factory.ListViewFactory;
import com.buaa.tezlikai.appmarket.holder.SubjectHolder;
import com.buaa.tezlikai.appmarket.protocol.SubjectProtocol;

import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 */
public class SubjectFragment extends BaseFragment {

    private List<SubjectInfoBean> mDatas;

    @Override
    public LoadingPager.LoadedResult initData() {//真正加载数据
        SubjectProtocol protocol = new SubjectProtocol();
        try {
            mDatas = protocol.loadData(0);
            return checkState(mDatas);
        } catch (Exception e) {
            e.printStackTrace();
            return LoadedResult.ERROR;
        }
    }

    @Override
    protected View initSuccessView() {
        //返回视图
        ListView listView = ListViewFactory.createListView();
        listView.setAdapter(new SubjectAdapter(listView,mDatas));
        return listView;
    }
    private class SubjectAdapter extends SuperBaseAdapter<SubjectInfoBean> {

        public SubjectAdapter(AbsListView absListView, List<SubjectInfoBean> dataSource) {
            super(absListView, dataSource);
        }

        @Override
        public BaseHolder<SubjectInfoBean> getSpecialHolder(int positon) {
            return new SubjectHolder();
        }
    }
}
