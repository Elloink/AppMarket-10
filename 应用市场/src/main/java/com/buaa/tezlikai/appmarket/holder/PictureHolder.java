package com.buaa.tezlikai.appmarket.holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.buaa.tezlikai.appmarket.R;
import com.buaa.tezlikai.appmarket.base.BaseHolder;
import com.buaa.tezlikai.appmarket.conf.Constants;
import com.buaa.tezlikai.appmarket.utils.BitmapHelper;
import com.buaa.tezlikai.appmarket.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * PictureHolder
 */
public class PictureHolder extends BaseHolder<List<String>> {

	@ViewInject(R.id.item_home_picture_pager)
	ViewPager mViewPager;

	@ViewInject(R.id.item_home_picture_container_indicator)
	LinearLayout mContainerIndicator;

	private List<String> mDatas;

	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_home_picture, null);
		// 注入找孩子
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void refreshHolderView(List<String> datas) {
		mDatas = datas;
		mViewPager.setAdapter(new PictureAdapter());
		mViewPager.setPageTransformer(true,new DepthPageTransformer());

		// 添加小点
		for (int i = 0; i < datas.size(); i++) {
			View indicatorView = new View(UIUtils.getContext());
			indicatorView.setBackgroundResource(R.drawable.indicator_normal);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dip2Px(5), UIUtils.dip2Px(5));// dp-->px
			// 左边距
			params.leftMargin = UIUtils.dip2Px(5);
			// 下边距
			params.bottomMargin = UIUtils.dip2Px(5);
			mContainerIndicator.addView(indicatorView, params);

			// 默认选中效果
			if (i == 0) {
				indicatorView.setBackgroundResource(R.drawable.indicator_selected);
			}
		}
		// 监听滑动事件
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				position = position % mDatas.size();

				for (int i = 0; i < mDatas.size(); i++) {
					View indicatorView = mContainerIndicator.getChildAt(i);
					// 还原背景
					indicatorView.setBackgroundResource(R.drawable.indicator_normal);

					if (i == position) {
						indicatorView.setBackgroundResource(R.drawable.indicator_selected);
					}
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		// 设置curItem为count/2
		int diff = Integer.MAX_VALUE / 2 % mDatas.size();//减去余数，保证初始化第一个图片是第一个位置
		int index = Integer.MAX_VALUE / 2;
		mViewPager.setCurrentItem(index - diff);

		// 自动轮播
		final AutoScrollTask autoScrollTask = new AutoScrollTask();
		autoScrollTask.start();
		// 用户触摸的时候移除自动轮播的任务
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						autoScrollTask.stop();
						break;
					case MotionEvent.ACTION_MOVE:

						break;
					case MotionEvent.ACTION_UP:
						autoScrollTask.start();
						break;

					default:
						break;
				}
				return false;
			}
		});

	}
	class AutoScrollTask implements Runnable {
		/**开始轮播*/
		public void start() {
			UIUtils.postTaskDelay(this, 2000);
		}

		public void stop() {
			UIUtils.removeTask(this);
		}

		/**结束轮播*/
		@Override
		public void run() {
			int item = mViewPager.getCurrentItem();
			item++;
			mViewPager.setCurrentItem(item);
			// 结束-->再次开始
			start();
		}
	}
	class PictureAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if (mDatas != null) {
				return Integer.MAX_VALUE;
				//return mDatas.size();
			}
			return 0;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			position = position % mDatas.size();//处理无限循环越界问题，防止越界

			ImageView iv = new ImageView(UIUtils.getContext());

			iv.setScaleType(ScaleType.FIT_XY);//对图片进行拉伸处理
			iv.setImageResource(R.drawable.ic_default);//设置默认的图片

			BitmapHelper.display(iv, Constants.URLS.IMAGEBASEURL + mDatas.get(position));

			container.addView(iv);
			return iv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	/**
	 * API提供的一种viewpager翻转特效：
	 * 可以参考file:///D:/Android1/Android1/sdk/docs/training/animation/screen-slide.html实现
	 */
	public class DepthPageTransformer implements ViewPager.PageTransformer {
		private static final float MIN_SCALE = 0.75f;

		public void transformPage(View view, float position) {
			int pageWidth = view.getWidth();

			if (position < -1) { // [-Infinity,-1)
				// This page is way off-screen to the left.
				view.setAlpha(0);

			} else if (position <= 0) { // [-1,0]
				// Use the default slide transition when moving to the left page
				view.setAlpha(1);
				view.setTranslationX(0);
				view.setScaleX(1);
				view.setScaleY(1);

			} else if (position <= 1) { // (0,1]
				// Fade the page out.
				view.setAlpha(1 - position);

				// Counteract the default slide transition
				view.setTranslationX(pageWidth * -position);

				// Scale the page down (between MIN_SCALE and 1)
				float scaleFactor = MIN_SCALE
						+ (1 - MIN_SCALE) * (1 - Math.abs(position));
				view.setScaleX(scaleFactor);
				view.setScaleY(scaleFactor);

			} else { // (1,+Infinity]
				// This page is way off-screen to the right.
				view.setAlpha(0);
			}
		}
	}
}
