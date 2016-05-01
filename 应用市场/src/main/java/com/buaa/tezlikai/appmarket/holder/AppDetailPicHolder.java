package com.buaa.tezlikai.appmarket.holder;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.buaa.tezlikai.appmarket.R;
import com.buaa.tezlikai.appmarket.base.BaseHolder;
import com.buaa.tezlikai.appmarket.bean.AppInfoBean;
import com.buaa.tezlikai.appmarket.conf.Constants;
import com.buaa.tezlikai.appmarket.utils.BitmapHelper;
import com.buaa.tezlikai.appmarket.utils.UIUtils;
import com.buaa.tezlikai.appmarket.views.RatioLayout;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * AppDetailPicHolder
 */
public class AppDetailPicHolder extends BaseHolder<AppInfoBean> {
	@ViewInject(R.id.app_detail_pic_iv_container)
	LinearLayout	mContainerPic;

	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_pic, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void refreshHolderView(AppInfoBean data) {
		List<String> picUrls = data.screen;
		for (int i = 0; i < picUrls.size(); i++) {
			String url = picUrls.get(i);
			ImageView ivPic = new ImageView(UIUtils.getContext());
			ivPic.setImageResource(R.drawable.ic_default);// 默认图片
			BitmapHelper.display(ivPic, Constants.URLS.IMAGEBASEURL + url);
			
			// 控件宽度等于屏幕的1/3
			int widthPixels = UIUtils.getResource().getDisplayMetrics().widthPixels;
			widthPixels = widthPixels - mContainerPic.getPaddingLeft() - mContainerPic.getPaddingRight();

			int childWidth = widthPixels / 3;
			// 已知控件的宽度,和图片的宽高比,去动态的计算控件的高度
			RatioLayout rl = new RatioLayout(UIUtils.getContext());

			rl.setPicRatio(150 / 250);// 图片的宽高比
			rl.setRelative(RatioLayout.RELATIVE_WIDTH);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(childWidth, LayoutParams.WRAP_CONTENT);

			rl.addView(ivPic);

			if (i != 0) {// 不处理第一张图片
				params.leftMargin = UIUtils.dip2Px(5);
			}
			mContainerPic.addView(rl, params);

		}
	}
}
