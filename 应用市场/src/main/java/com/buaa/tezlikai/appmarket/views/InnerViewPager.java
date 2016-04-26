package com.buaa.tezlikai.appmarket.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * InnerViewPager自定义的一个实现左右滑自己控制，上下滑父控件控制
 * 解决版本低的不兼容问题
 */
public class InnerViewPager extends ViewPager {

	private float	mDownX;
	private float	mDownY;
	private float	mMoveX;
	private float	mMoveY;

	public InnerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InnerViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		// 左右滑动-->自己处理
		// 上下滑动-->父亲处理
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = ev.getRawX();
			mDownY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			mMoveX = ev.getRawX();
			mMoveY = ev.getRawY();

			int diffX = (int) (mMoveX - mDownX);
			int diffY = (int) (mMoveY - mDownY);
			// 左右滚动的绝对值 > 上下滚动的绝对值
			if (Math.abs(diffX) > Math.abs(diffY)) {// 左右
				// 左右滑动-->自己处理
				// getParent()(父亲).request(请求)Disallow(不)Intercept(拦截)TouchEvent(touch事件)(true(同意));
				// 请求父亲不拦截事件
				getParent().requestDisallowInterceptTouchEvent(true);//一定要记住这个方法，请求父控件不拦截
			} else {// 上下
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		case MotionEvent.ACTION_UP:

			break;
		case MotionEvent.ACTION_CANCEL:

			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

}
