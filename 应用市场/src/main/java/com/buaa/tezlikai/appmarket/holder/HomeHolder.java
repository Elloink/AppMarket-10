package com.buaa.tezlikai.appmarket.holder;

import android.view.View;
import android.widget.TextView;

import com.buaa.tezlikai.appmarket.R;
import com.buaa.tezlikai.appmarket.base.BaseHolder;
import com.buaa.tezlikai.appmarket.utils.UIUtils;

/**
 * Created by Administrator on 2016/4/24.
 */
public class HomeHolder extends BaseHolder<String>{

    private TextView mTvTmp1;
    private TextView mTvTmp2;

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_tmp, null);
        //初始化孩子
        mTvTmp1 = (TextView)view.findViewById(R.id.tmp_tv_1);
        mTvTmp2 = (TextView)view.findViewById(R.id.tmp_tv_2);
        return view;
    }

    @Override
    public void refreshHolderView(String data) {
        mTvTmp1.setText("北京航空航天大学本部"+data);
        mTvTmp2.setText("北京航空航天大学软件学院"+data);
    }

}
