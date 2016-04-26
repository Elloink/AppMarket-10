package com.buaa.tezlikai.appmarket.holder;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.TextView;

import com.buaa.tezlikai.appmarket.base.BaseHolder;
import com.buaa.tezlikai.appmarket.bean.CategoryInfoBean;
import com.buaa.tezlikai.appmarket.utils.UIUtils;

/**
 * CategoryTitleHolder
 */
public class CategoryTitleHolder extends BaseHolder<CategoryInfoBean> {

	private TextView	mTvTitle;

	@Override
	public View initHolderView() {
		mTvTitle = new TextView(UIUtils.getContext());
		mTvTitle.setTextSize(15);
		int padding = UIUtils.dip2Px(5);
		mTvTitle.setPadding(padding, padding, padding, padding);
		mTvTitle.setBackgroundColor(Color.WHITE);

		AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		mTvTitle.setLayoutParams(params);

		return mTvTitle;
	}

	@Override
	public void refreshHolderView(CategoryInfoBean data) {
		mTvTitle.setText(data.title);
	}

}
