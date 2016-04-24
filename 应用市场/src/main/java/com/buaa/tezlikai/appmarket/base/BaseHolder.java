package com.buaa.tezlikai.appmarket.base;

import android.view.View;

/**
 * BaseHolder
 */
public abstract class BaseHolder<HODLERBEANTYPE> {

	public View	mHolderView;

	// 做holder需要持有孩子对象
	private HODLERBEANTYPE	mData;

	public BaseHolder() {
		// 初始化根布局
		mHolderView = initHolderView();
		// 绑定tag
		mHolderView.setTag(this);
	}

	/**
	 * @deprecated 设置数据刷新视图
	 *  需要设置数据和刷新数据的时候调用
	 *  HODLERBEANTYPE :自己定义的一个类型，别的也可以
	 */
	public void setDataAndRefreshHolderView(HODLERBEANTYPE data) {
		// 保存数据
		mData = data;
		// 刷新显示
		refreshHolderView(data);
	}

	/**
	 * @deprecated  初始化holderView/根视图
	 *  BaseHolder 初始化的被调用
	 */
	public abstract View initHolderView();

	/**
	 * @deprecated 刷新holder视图
	 * setDataAndRefreshHolderView()方法被调用的时候就被调用了吧
	 */
	public abstract void refreshHolderView(HODLERBEANTYPE data);

}
