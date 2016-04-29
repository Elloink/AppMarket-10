package com.buaa.tezlikai.appmarket.holder;

import android.view.View;

import com.buaa.tezlikai.appmarket.R;
import com.buaa.tezlikai.appmarket.base.BaseHolder;
import com.buaa.tezlikai.appmarket.utils.UIUtils;


/**
 * MenuHolder:侧边栏
 */
public class MenuHolder extends BaseHolder<Object> {

	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.menu_view, null);
		return view;
	}

	@Override
	public void refreshHolderView(Object data) {

	}
}
