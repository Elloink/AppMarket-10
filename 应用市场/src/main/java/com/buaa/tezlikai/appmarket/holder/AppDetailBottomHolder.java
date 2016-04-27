package com.buaa.tezlikai.appmarket.holder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.buaa.tezlikai.appmarket.R;
import com.buaa.tezlikai.appmarket.base.BaseHolder;
import com.buaa.tezlikai.appmarket.bean.AppInfoBean;
import com.buaa.tezlikai.appmarket.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * AppDetailBottomHolder
 */
public class AppDetailBottomHolder extends BaseHolder<AppInfoBean> implements OnClickListener {
	@ViewInject(R.id.app_detail_download_btn_share)
	Button	mBtnShare;
	@ViewInject(R.id.app_detail_download_btn_favo)
	Button	mBtnFavo;
	@ViewInject(R.id.app_detail_download_btn_download)
	Button	mBtnDownLoad;

	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_bottom, null);
		ViewUtils.inject(this, view);
		mBtnShare.setOnClickListener(this);
		mBtnFavo.setOnClickListener(this);
		mBtnDownLoad.setOnClickListener(this);
		return view;
	}

	@Override
	public void refreshHolderView(AppInfoBean data) {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.app_detail_download_btn_download:
			Toast.makeText(UIUtils.getContext(), "app_detail_download_btn_download", 0).show();
			break;
		case R.id.app_detail_download_btn_share:
			Toast.makeText(UIUtils.getContext(), "app_detail_download_btn_share", 0).show();
			
			break;
		case R.id.app_detail_download_btn_favo:
			Toast.makeText(UIUtils.getContext(), "app_detail_download_btn_favo", 0).show();
			
			break;

		default:
			break;
		}
	}

}
