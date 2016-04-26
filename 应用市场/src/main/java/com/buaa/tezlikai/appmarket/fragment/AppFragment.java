package com.buaa.tezlikai.appmarket.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.buaa.tezlikai.appmarket.base.BaseFragment;
import com.buaa.tezlikai.appmarket.base.BaseHolder;
import com.buaa.tezlikai.appmarket.base.LoadingPager;
import com.buaa.tezlikai.appmarket.base.SuperBaseAdapter;
import com.buaa.tezlikai.appmarket.bean.AppInfoBean;
import com.buaa.tezlikai.appmarket.factory.ListViewFactory;
import com.buaa.tezlikai.appmarket.holder.AppItemHolder;
import com.buaa.tezlikai.appmarket.protocol.AppProtocol;

import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 */
public class AppFragment extends BaseFragment {

    private List<AppInfoBean> mDatas;
    private AppProtocol mProtocol;

    @Override
    public LoadingPager.LoadedResult initData() {//真正加载数据
//        SystemClock.sleep(2000);//延迟的一种方式
        //执行耗时操作
        mProtocol = new AppProtocol();
        try {
            mDatas = mProtocol.loadData(0);
            return checkState(mDatas);
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }
    }

    @Override
    protected View initSuccessView() {
        //返回视图
        ListView listView = ListViewFactory.createListView();

        //设置adapter
        listView.setAdapter(new AppAdapter(listView,mDatas));

        //返回listview
        return listView;
    }

    private class AppAdapter extends SuperBaseAdapter {

        public AppAdapter(AbsListView absListView, List dataSource) {
            super(absListView, dataSource);
        }

        @Override
        public BaseHolder getSpecialHolder(int positon) {
            return new AppItemHolder();
        }

        @Override
        public List<AppInfoBean> onLoadMore() throws Exception {
            return mProtocol.loadData(mDatas.size());
        }
    }
}
