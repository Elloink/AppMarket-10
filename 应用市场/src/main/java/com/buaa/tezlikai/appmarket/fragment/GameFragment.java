package com.buaa.tezlikai.appmarket.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.buaa.tezlikai.appmarket.adapter.AppItemAdapter;
import com.buaa.tezlikai.appmarket.base.BaseFragment;
import com.buaa.tezlikai.appmarket.base.LoadingPager;
import com.buaa.tezlikai.appmarket.bean.AppInfoBean;
import com.buaa.tezlikai.appmarket.factory.ListViewFactory;
import com.buaa.tezlikai.appmarket.protocol.GameProtocol;

import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 */
public class GameFragment extends BaseFragment {

    private GameProtocol mGameProtocol;
    private List<AppInfoBean> mDatas;

    @Override
    public LoadingPager.LoadedResult initData() {//真正加载数据
        mGameProtocol = new GameProtocol();
        try {
            mDatas = mGameProtocol.loadData(0);
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

        listView.setAdapter(new GameAdapter(listView,mDatas));
        return listView;
    }

    private class GameAdapter extends AppItemAdapter {

        public GameAdapter(AbsListView absListView, List<AppInfoBean> dataSource) {
            super(absListView, dataSource);
        }

        @Override
        public List onLoadMore() throws Exception {
            return mGameProtocol.loadData(mDatas.size());
        }
    }
}
