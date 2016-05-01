package com.buaa.tezlikai.appmarket.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.buaa.tezlikai.appmarket.conf.Constants;
import com.buaa.tezlikai.appmarket.factory.ThreadPoolFactory;
import com.buaa.tezlikai.appmarket.holder.LoadMoreHolder;
import com.buaa.tezlikai.appmarket.utils.LogUtils;
import com.buaa.tezlikai.appmarket.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * SuperBaseAdapter
 */
public abstract class SuperBaseAdapter<ITEMBEANTYPE> extends BaseAdapter implements OnItemClickListener {
	private final AbsListView mAbsListView;
	public List<ITEMBEANTYPE>	mDataSource	= new ArrayList<ITEMBEANTYPE>();

	public static final int		VIEWTYPE_LOADMORE	= 0;
	public static final int		VIEWTYPE_NORMAL		= 1;
	private LoadMoreHolder		mLoadMoreHolder;
	private LoadMoreTask		mLoadMoreTask;

	public SuperBaseAdapter(AbsListView absListView, List<ITEMBEANTYPE> dataSource) {
		super();
		absListView.setOnItemClickListener(this);
		mDataSource = dataSource;
		mAbsListView = absListView;//获取传入的listView
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

	/*=============== lisview里面可以显示几种viewType  begin ===============*/

	@Override
	public int getViewTypeCount() {
		return super.getViewTypeCount() + 1;// 2
	}

	// Integers must be in the range 0 to getViewTypeCount - 1.
	@Override
	public int getItemViewType(int position) {
		// 如果滑到底部,那么对于的ViewType是加载更多
		if (position == getCount() - 1) {// 滑到底部
			return VIEWTYPE_LOADMORE;
		}
		return getNormalViewType(position);
	}

	/**
	 * 子类其实可以复写该方法，添加更多的viewType
	 * @param position
	 * @return 返回普通view的一个viewType
	 */
	public int getNormalViewType(int position) {
		return VIEWTYPE_NORMAL;
	}

	/*=============== lisview里面可以显示几种viewType  end ===============*/

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseHolder<ITEMBEANTYPE> holder = null;
		/*=============== 初始化视图 决定根布局 ===============*/

		if (convertView == null) {
				if (getItemViewType(position) == VIEWTYPE_LOADMORE){
				holder = (BaseHolder<ITEMBEANTYPE>) getLoadMoreHolder();
				}else {
					holder = getSpecialHolder(position);
				}
			} else {
			holder = (BaseHolder) convertView.getTag();
		}

		/*=============== 数据展示 ===============*/
		if (getItemViewType(position) == VIEWTYPE_LOADMORE){
			//去开始加载更多
			if (hasLoadMore()){
				perFormLoadMore();
			}else {//没有加载更多的时候就把mLoadMoreHolder的状态置为空，就啥也显示
				mLoadMoreHolder.setDataAndRefreshHolderView(LoadMoreHolder.STATE_NONE);
			}

		}else {
			holder.setDataAndRefreshHolderView(mDataSource.get(position));
		}
		return holder.mHolderView;
	}

	/**
	 * @des 返回具体的Baseholder的子类
	 * @call getView方法中如果没有convertView的时候被创建
	 * @return
	 */
	public abstract BaseHolder<ITEMBEANTYPE> getSpecialHolder(int position);

	/**
	 * @return  返回一个加载更多的holder
	 */
	private LoadMoreHolder getLoadMoreHolder() {
		if (mLoadMoreHolder == null) {
			mLoadMoreHolder = new LoadMoreHolder();
		}
		return mLoadMoreHolder;
	}

	/**
	 *  滑到底之后,应该去拉取更多的数据
	 *  滑到底的时候被调用
	 */
	private void perFormLoadMore() {
		if (mLoadMoreTask == null) {// 1次
			// 修改loadmore当前的视图为加载中
			int state = LoadMoreHolder.STATE_LOADING;
			mLoadMoreHolder.setDataAndRefreshHolderView(state);
			LogUtils.sf("###开启线程,加载更多");
			mLoadMoreTask = new LoadMoreTask();//防止重复加载
			ThreadPoolFactory.getNormalPool().execute(mLoadMoreTask);
		}
	}


	class LoadMoreTask implements Runnable {
		@Override
		public void run() {
			// 真正开始请求网络加载更多数据
			List<ITEMBEANTYPE> loadMoreDatas = null;
			/*--------------- 根据加载更多的数据,处理 loadmore的状态 begin---------------*/
			int state = LoadMoreHolder.STATE_LOADING;
			try {
				loadMoreDatas = onLoadMore();
				// 得到返回数据,处理结果
				if (loadMoreDatas == null) {// 没有更多数据
					state = LoadMoreHolder.STATE_NONE;
				} else {
					// 假如规定每页返回20
					// 10<20==>没有加载更多
					if (loadMoreDatas.size() < Constants.PAGESIZE) {// 没有加载更多
						state = LoadMoreHolder.STATE_NONE;
					} else {
						state = LoadMoreHolder.STATE_LOADING;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				state = LoadMoreHolder.STATE_RETRY;
			}

			/*--------------- 定义一个中转的临时变量 ---------------*/
			final int tempState = state;
			final List<ITEMBEANTYPE> tempLoadMoreDatas = loadMoreDatas;
			/*--------------- 根据加载更多的数据,处理 loadmore的状态 end---------------*/
			UIUtils.postTaskSafely(new Runnable() {

				@Override
				public void run() {
					// 刷新loadmore视图
					mLoadMoreHolder.setDataAndRefreshHolderView(tempState);
					// 刷新listview视图 返回加载更多过后得到的数据 mDatas.addAll()
					if (tempLoadMoreDatas != null) {
						mDataSource.addAll(tempLoadMoreDatas);// listview数据源更新
						notifyDataSetChanged();// 刷新listview
					}
				}
			});
			mLoadMoreTask = null;//防止重复加载
		}
	}

	/**
	 *  可有可无的方法,定义成一个public方法,如果子类有加载更多.再去覆写就行了
	 *  真正开始加载更多数据的地方
	 *  滑到底的时候
	 */
	public  List<ITEMBEANTYPE> onLoadMore() throws Exception {
		return null;
	}
	/**
	 *  决定有没有加载更多,默认是返回true,但是子类可以覆写此方法,如果子类返回的是flase,就不会去加载更多
	 *  getView中滑动底的时候会调用
	 *
	 */
	private boolean hasLoadMore() {
		return true;
	}

	/*=============== 处理item的点击事件 ===============*/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mAbsListView instanceof ListView){
			//因为首页有轮播图，所有占一个position，所以索引的整体减去getHeaderViewsCount（）
			position = position - ((ListView) mAbsListView).getHeaderViewsCount();
		}
		if (getItemViewType(position) == VIEWTYPE_LOADMORE) {
			// 重新加载更多
			perFormLoadMore();
		} else {
			onNormalItemClick(parent, view, position, id);
		}
	}
	/**
	 * @des 点击普通条目对应的事件处理
	 * @call 如果子类需要处理item的点击事件,就直接覆写此方法
	 */
	public void onNormalItemClick(AdapterView<?> parent, View view, int position, long id) {

	}
}
