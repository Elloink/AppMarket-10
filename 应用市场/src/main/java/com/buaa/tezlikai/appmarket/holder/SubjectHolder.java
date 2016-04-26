package com.buaa.tezlikai.appmarket.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.buaa.tezlikai.appmarket.R;
import com.buaa.tezlikai.appmarket.base.BaseHolder;
import com.buaa.tezlikai.appmarket.bean.SubjectInfoBean;
import com.buaa.tezlikai.appmarket.conf.Constants;
import com.buaa.tezlikai.appmarket.utils.BitmapHelper;
import com.buaa.tezlikai.appmarket.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/4/26.
 */
public class SubjectHolder extends BaseHolder<SubjectInfoBean> {

    @ViewInject(R.id.item_subject_iv_icon)
    ImageView mIvIcon;

    @ViewInject(R.id.item_subject_tv_title)
    TextView mTvTitle;

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_subject, null);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void refreshHolderView(SubjectInfoBean data) {
        mTvTitle.setText(data.des);
        mIvIcon.setImageResource(R.drawable.ic_default);

        BitmapHelper.display(mIvIcon, Constants.URLS.IMAGEBASEURL+data.url);

    }
}
