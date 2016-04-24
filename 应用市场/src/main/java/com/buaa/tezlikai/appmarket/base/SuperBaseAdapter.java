package com.buaa.tezlikai.appmarket.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * SuperBaseAdapter
 */
public abstract class SuperBaseAdapter<ITEMBEANTYPE> extends BaseAdapter{
	public List<ITEMBEANTYPE>	mDataSource	= new ArrayList<ITEMBEANTYPE>();

	public SuperBaseAdapter(List<ITEMBEANTYPE> dataSource) {
		super();
		//absListView.setOnItemClickListener(this);
		mDataSource = dataSource;
	}

	@Override
	public int getCount() {
		if (mDataSource != null) {
			return mDataSource.size() + 1;
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mDataSource != null) {
			return mDataSource.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/*=============== lisview里面可以显示几种viewType  end ===============*/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseHolder<ITEMBEANTYPE> holder = null;
		/*=============== 初始化视图 决定根布局 ===============*/
		if (convertView == null) {
				holder = getSpecialHolder();
			} else {
			holder = (BaseHolder) convertView.getTag();
		}

		/*=============== 数据展示 ===============*/
		holder.setDataAndRefreshHolderView(mDataSource.get(position));
		return holder.mHolderView;
	}

	/**
	 * @des 返回具体的Baseholder的子类
	 * @call getView方法中如果没有convertView的时候被创建
	 * @return
	 */
	public abstract BaseHolder<ITEMBEANTYPE> getSpecialHolder();

}
