package com.buaa.tezlikai.appmarket.factory;


import android.support.v4.util.SparseArrayCompat;

import com.buaa.tezlikai.appmarket.base.BaseFragment;
import com.buaa.tezlikai.appmarket.fragment.AppFragment;
import com.buaa.tezlikai.appmarket.fragment.CategoryFragment;
import com.buaa.tezlikai.appmarket.fragment.GameFragment;
import com.buaa.tezlikai.appmarket.fragment.HomeFragment;
import com.buaa.tezlikai.appmarket.fragment.HotFragment;
import com.buaa.tezlikai.appmarket.fragment.RecommendFragment;
import com.buaa.tezlikai.appmarket.fragment.SubjectFragment;

/**
 * Fragment工厂类
 */
public class FragmentFactory {
	/**
	<string-array name="main_titles">
	<item>首页</item>
	<item>应用</item>
	<item>游戏</item>
	<item>专题</item>
	<item>推荐</item>
	<item>分类</item>
	<item>排行</item>
	</string-array>
	 */
	public static final int					FRAGMENT_HOME		= 0;
	public static final int					FRAGMENT_APP		= 1;
	public static final int					FRAGMENT_GAME		= 2;
	public static final int					FRAGMENT_SUBJECT	= 3;
	public static final int					FRAGMENT_RECOMMEND	= 4;
	public static final int					FRAGMENT_CATEGORY	= 5;
	public static final int					FRAGMENT_HOT		= 6;
	//替换map的使用（推荐使用）
	public static SparseArrayCompat<BaseFragment> cachesFragment = new SparseArrayCompat<BaseFragment>();

	public static BaseFragment getFragment(int position){
		BaseFragment fragment = null;

		//如果缓存里面有对应的fragment，就直接取出返回
		BaseFragment temFragment = cachesFragment.get(position);
		if (temFragment != null){
			fragment = temFragment;
			return fragment;
		}

	/*	//如果缓存里面有对应的fragment，就直接取出返回
		if (cachesFragmentMap.containsKey(position)){
			fragment = cachesFragmentMap.get(position);
			return fragment;
		}*/

		switch (position){
			case FRAGMENT_HOME:// 主页
				fragment = new HomeFragment();
				break;
			case FRAGMENT_APP:// 应用
				fragment = new AppFragment();
				break;
			case FRAGMENT_GAME:// 游戏
				fragment = new GameFragment();
				break;
			case FRAGMENT_SUBJECT:// 专题
				fragment = new SubjectFragment();
				break;
			case FRAGMENT_RECOMMEND:// 推荐
				fragment = new RecommendFragment();
				break;
			case FRAGMENT_CATEGORY:// 分类
				fragment = new CategoryFragment();
				break;
			case FRAGMENT_HOT:// 排行
				fragment = new HotFragment();
				break;
			default:
				break;
		}
		//保存对应的fragment
		cachesFragment.put(position,fragment);
		return fragment;
	}

}
