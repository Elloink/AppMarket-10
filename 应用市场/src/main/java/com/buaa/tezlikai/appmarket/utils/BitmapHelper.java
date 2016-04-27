package com.buaa.tezlikai.appmarket.utils;

import android.view.View;

import com.lidroid.xutils.BitmapUtils;

/**
 * BitmapHelper:加载网络图片的方法
 */
public class BitmapHelper {
	public static BitmapUtils	mBitmapUtils;
	static {
		mBitmapUtils = new BitmapUtils(UIUtils.getContext());
	}

	public static <T extends View> void display(T container, String uri) {
		mBitmapUtils.display(container, uri);
	}

}
