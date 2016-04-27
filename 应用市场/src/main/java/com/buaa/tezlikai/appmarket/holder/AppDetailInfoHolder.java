package com.buaa.tezlikai.appmarket.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.buaa.tezlikai.appmarket.R;
import com.buaa.tezlikai.appmarket.base.BaseHolder;
import com.buaa.tezlikai.appmarket.bean.AppInfoBean;
import com.buaa.tezlikai.appmarket.conf.Constants;
import com.buaa.tezlikai.appmarket.utils.BitmapHelper;
import com.buaa.tezlikai.appmarket.utils.StringUtils;
import com.buaa.tezlikai.appmarket.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/4/27.
 * 下载页面的信息holder
 */
public class AppDetailInfoHolder extends BaseHolder<AppInfoBean> {

    @ViewInject(R.id.app_detail_info_iv_icon)
    ImageView	mIvIcon;

    @ViewInject(R.id.app_detail_info_rb_star)
    RatingBar mRbStar;

    @ViewInject(R.id.app_detail_info_tv_downloadnum)
    TextView mTvDownLoadNum;

    @ViewInject(R.id.app_detail_info_tv_name)
    TextView	mTvName;

    @ViewInject(R.id.app_detail_info_tv_time)
    TextView	mTvTime;

    @ViewInject(R.id.app_detail_info_tv_version)
    TextView	mTvVersion;

    @ViewInject(R.id.app_detail_info_tv_size)
    TextView	mTvSize;



    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_info, null);
        //注入
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void refreshHolderView(AppInfoBean data) {

        String date = UIUtils.getString(R.string.detatil_date, data.date);
        String downloadNum = UIUtils.getString(R.string.detatil_downloadnum, data.downloadNum);
        String size = UIUtils.getString(R.string.detatil_size, StringUtils.formatFileSize(data.size));
        String version = UIUtils.getString(R.string.detatil_version, data.version);


        mTvName.setText(data.name);

        mTvDownLoadNum.setText(downloadNum);
        mTvTime.setText(date);
        mTvVersion.setText(version);
        mTvSize.setText(size);//格式化好了

        mIvIcon.setImageResource(R.drawable.ic_default);
        BitmapHelper.display(mIvIcon, Constants.URLS.IMAGEBASEURL + data.iconUrl);

        mRbStar.setRating(data.stars);
    }
}
