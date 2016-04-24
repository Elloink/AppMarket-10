package com.buaa.tezlikai.appmarket.fragment;

import android.graphics.Color;
import android.os.SystemClock;
import android.view.View;
import android.widget.ListView;

import com.buaa.tezlikai.appmarket.base.BaseFragment;
import com.buaa.tezlikai.appmarket.base.BaseHolder;
import com.buaa.tezlikai.appmarket.base.LoadingPager.LoadedResult;
import com.buaa.tezlikai.appmarket.base.SuperBaseAdapter;
import com.buaa.tezlikai.appmarket.holder.HomeHolder;
import com.buaa.tezlikai.appmarket.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 */
public class HomeFragment extends BaseFragment {

    private List<String> mDatas;

    @Override
    public LoadedResult initData() {//真正加载数据
        mDatas = new ArrayList<String>();
        for (int i= 0; i < 100;i++){
            mDatas.add(i+ "");
        }
        SystemClock.sleep(1000);

        return LoadedResult.SUCCESS;
    }

    @Override
    protected View initSuccessView() {
        //返回成功视图
        ListView listView = new ListView(UIUtils.getContext());
        //简单的设置
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setFastScrollEnabled(true);

        //设置Adapter
        listView.setAdapter(new HomeAdapter(mDatas));

        return listView;
    }

    class HomeAdapter extends SuperBaseAdapter<String>{

        public HomeAdapter(List<String> dataSource) {
            super(dataSource);
        }

        @Override
        public BaseHolder<String> getSpecialHolder() {

            return new HomeHolder();
        }
    }
}
