package com.buaa.tezlikai.appmarket.holder;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buaa.tezlikai.appmarket.R;
import com.buaa.tezlikai.appmarket.base.BaseHolder;
import com.buaa.tezlikai.appmarket.bean.AppInfoBean;
import com.buaa.tezlikai.appmarket.bean.AppInfoBean.AppInfoSafeBean;
import com.buaa.tezlikai.appmarket.conf.Constants;
import com.buaa.tezlikai.appmarket.utils.BitmapHelper;
import com.buaa.tezlikai.appmarket.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * AppDetailSafeHolder
 */
public class AppDetailSafeHolder extends BaseHolder<AppInfoBean> implements OnClickListener {
	@ViewInject(R.id.app_detail_safe_pic_container)
	LinearLayout	mContainerPic;

	@ViewInject(R.id.app_detail_safe_des_container)
	LinearLayout	mContainerDes;

	@ViewInject(R.id.app_detail_safe_iv_arrow)
	ImageView		mIvArrow;

	private boolean	isOpen	= true;

	@Override
	public View initHolderView() {

		View view = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_safe, null);
		ViewUtils.inject(this, view);

		view.setOnClickListener(this);
		return view;
	}

	@Override
	public void refreshHolderView(AppInfoBean data) {
		List<AppInfoSafeBean> safeBeans = data.safe;
		for (AppInfoSafeBean appInfoSafeBean : safeBeans) {
			ImageView ivIcon = new ImageView(UIUtils.getContext());
			BitmapHelper.display(ivIcon, Constants.URLS.IMAGEBASEURL + appInfoSafeBean.safeUrl);
			mContainerPic.addView(ivIcon);

			LinearLayout ll = new LinearLayout(UIUtils.getContext());

			// 描述图标
			ImageView ivDes = new ImageView(UIUtils.getContext());
			BitmapHelper.display(ivDes, Constants.URLS.IMAGEBASEURL + appInfoSafeBean.safeDesUrl);
			// 描述内容
			TextView tvDes = new TextView(UIUtils.getContext());
			tvDes.setText(appInfoSafeBean.safeDes);
			if (appInfoSafeBean.safeDesColor == 0) {
				tvDes.setTextColor(UIUtils.getColor(R.color.app_detail_safe_normal));
			} else {
				tvDes.setTextColor(UIUtils.getColor(R.color.app_detail_safe_warning));
			}
			
			tvDes.setGravity(Gravity.CENTER);
			// 加点间距
			int padding = UIUtils.dip2Px(5);
			ll.setPadding(padding, padding, padding, padding);

			ll.addView(ivDes);
			ll.addView(tvDes);

			mContainerDes.addView(ll);

		}
		// 默认折叠
		toggle(false);
	}

	@Override
	public void onClick(View v) {
		toggle(true);
	}

	private void toggle(boolean isAnimation) {
		if (isOpen) {// 折叠
			/**
			 mContainerDes高度发生变化
			 应有的高度-->0
			 */
			mContainerDes.measure(0, 0);
			int measuredHeight = mContainerDes.getMeasuredHeight();//获取mContainerDes的高度
			int start = measuredHeight;// 动画的开始高度
			int end = 0;// 动画的结束高度
			if (isAnimation) {
				doAnimation(start, end);
			} else {// 直接修改高度
				LayoutParams params = mContainerDes.getLayoutParams();
				params.height = end;
				mContainerDes.setLayoutParams(params);
			}
		} else {// 展开
			/**
			 mContainerDes高度发生变化
			 0-->应有的高度
			 */
			mContainerDes.measure(0, 0);
			int measuredHeight = mContainerDes.getMeasuredHeight();
			int end = measuredHeight;// 动画的开始高度
			int start = 0;// 动画的结束高度
			if (isAnimation) {
				doAnimation(start, end);
			} else {
				LayoutParams params = mContainerDes.getLayoutParams();
				params.height = end;
				mContainerDes.setLayoutParams(params);
			}
		}
		// 箭头的旋转动画
		if (isAnimation) {// 有折叠动画的时候
			if (isOpen) {
				ObjectAnimator.ofFloat(mIvArrow, "rotation", 180, 0).start();
			} else {
				ObjectAnimator.ofFloat(mIvArrow, "rotation", 0, 180).start();
			}
		}

		isOpen = !isOpen;
	}

	public void doAnimation(int start, int end) {
		ValueAnimator animator = ValueAnimator.ofInt(start, end);
		animator.start();// 开始动画
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator value) {

				int height = (Integer) value.getAnimatedValue();
				// 通过layoutParams,修改高度
				LayoutParams params = mContainerDes.getLayoutParams();
				params.height = height;
				mContainerDes.setLayoutParams(params);
			}
		});
	}

}
