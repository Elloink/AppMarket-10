package com.buaa.tezlikai.appmarket.holder;

import android.view.View;
import android.widget.LinearLayout;

import com.buaa.tezlikai.appmarket.R;
import com.buaa.tezlikai.appmarket.base.BaseHolder;
import com.buaa.tezlikai.appmarket.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * LoadMoreHolder
 */
public class LoadMoreHolder extends BaseHolder<Integer> {
	@ViewInject(R.id.item_loadmore_container_loading)
	LinearLayout			mContainerLoading;

	@ViewInject(R.id.item_loadmore_container_retry)
	LinearLayout			mContainerRetry;

	public static final int	STATE_LOADING	= 0;
	public static final int	STATE_RETRY		= 1;
	public static final int	STATE_NONE		= 2;

	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_loadmore, null);

		ViewUtils.inject(this, view);

		return view;
	}

	@Override
	public void refreshHolderView(Integer state) {
		mContainerLoading.setVisibility(View.GONE);
		mContainerRetry.setVisibility(View.GONE);
		switch (state) {
		case STATE_LOADING://正在加载更多中...
			mContainerLoading.setVisibility(View.VISIBLE);
			break;
		case STATE_RETRY://加载更多失败
			mContainerRetry.setVisibility(View.VISIBLE);
			break;
		case STATE_NONE:
			break;

		default:
			break;
		}
	}

}
