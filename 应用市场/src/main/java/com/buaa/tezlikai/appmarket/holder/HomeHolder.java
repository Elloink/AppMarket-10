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
 * Created by Administrator on 2016/4/24.
 */
public class HomeHolder extends BaseHolder<AppInfoBean>{

    @ViewInject(R.id.item_appinfo_iv_icon)
    private ImageView mIvIcon;
    @ViewInject(R.id.item_appinfo_rb_stars)
    private RatingBar mRbStars;
    @ViewInject(R.id.item_appinfo_tv_des)
    private TextView mTvDes;
    @ViewInject(R.id.item_appinfo_tv_size)
    private TextView mTvSize;
    @ViewInject(R.id.item_appinfo_tv_title)
    private TextView mTvTitle;

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_app_info, null);
        //注入
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void refreshHolderView(AppInfoBean data) {
        mTvTitle.setText(data.name);
        mTvDes.setText(data.des);
        mTvSize.setText(StringUtils.formatFileSize(data.size));//将数据大小改成Kb/M显示

        mIvIcon.setImageResource(R.drawable.ic_default);//设置默认图片
        //BitmapUtils bitmapUtils = new BitmapUtils(UIUtils.getContext());

        // http://localhost:8080/GooglePlayServer/image?name=
        // data.iconUrl
        String url = Constants.URLS.IMAGEBASEURL + data.iconUrl;
        BitmapHelper.display(mIvIcon,url);//把BitmapUtils封装了一下，以免重复加载bitmapUtils

        mRbStars.setRating(data.stars);
    }
}
