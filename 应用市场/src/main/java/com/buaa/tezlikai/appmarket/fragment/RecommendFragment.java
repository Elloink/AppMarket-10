package com.buaa.tezlikai.appmarket.fragment;

import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.buaa.tezlikai.appmarket.base.BaseFragment;
import com.buaa.tezlikai.appmarket.base.LoadingPager;
import com.buaa.tezlikai.appmarket.utils.UIUtils;

import java.util.Random;

/**
 * Created by Administrator on 2016/4/22.
 */
public class RecommendFragment extends BaseFragment {
    @Override
    public LoadingPager.LoadedResult initData() {//真正加载数据
        SystemClock.sleep(2000);

        LoadingPager.LoadedResult[] arr = {LoadingPager.LoadedResult.ERROR, LoadingPager.LoadedResult.SUCCESS, LoadingPager.LoadedResult.EMPTY};
        Random random = new Random();
        int index = random.nextInt(arr.length);
        return arr[index];
    }

    @Override
    protected View initSuccessView() {
        //返回视图
        TextView tv = new TextView(UIUtils.getContext());
        tv.setText(this.getClass().getSimpleName()+"北京航空航天大学");
        return tv;
    }
}
