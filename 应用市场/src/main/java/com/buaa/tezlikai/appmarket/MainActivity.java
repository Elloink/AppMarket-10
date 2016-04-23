package com.buaa.tezlikai.appmarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStripExtends;
import com.buaa.tezlikai.appmarket.base.BaseFragment;
import com.buaa.tezlikai.appmarket.factory.FragmentFactory;
import com.buaa.tezlikai.appmarket.utils.LogUtils;
import com.buaa.tezlikai.appmarket.utils.UIUtils;

public class MainActivity extends ActionBarActivity {

    private PagerSlidingTabStripExtends mTabs;
    private ViewPager mViewPager;
    private String[] mMainTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVIew();
        initActionBar();
        initData();
        initListener();
    }



    /**
     * 初始化View
     */
    private void initVIew() {
        mTabs = (PagerSlidingTabStripExtends) findViewById(R.id.main_tabs);
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setLogo(R.drawable.ic_launcher);//设置logo
        actionBar.setTitle("应用市场");//设置Titile

    }

    /**
     * 初始化Data
     */
    private void initData() {

        mMainTitles = UIUtils.getStringArr(R.array.main_titles);

//        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        MainFragmentStatePagerAdapter adapter = new MainFragmentStatePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);

        // Bind the tabs to the ViewPager
        // viewpager和tabs的一个绑定
        mTabs.setViewPager(mViewPager);
    }
    private void initListener() {
        mTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //完成触发加载
                BaseFragment fragment = FragmentFactory.getFragment(position);
                if (fragment!=null){
                    fragment.getLoadingPager().loadData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 第一种pageView适配的方式
     */
    private class MainPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (mMainTitles != null){
                return mMainTitles.length;
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView tv = new TextView(UIUtils.getContext());
            tv.setText(mMainTitles[position]);
            container.addView(tv);
            return tv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        /**
         * 必须重写此方法，否则会报空指针异常
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mMainTitles[position];
        }


    }

    /**
     * 第二种pageView适配的方式（有缓存）
     */
    private class MainFragmentPagerAdapter extends FragmentPagerAdapter{

        public MainFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //常见fragment
            Fragment fragment = FragmentFactory.getFragment(position);
            LogUtils.sf("初始化"+mMainTitles[position]);
            return fragment;
        }

        @Override
        public int getCount() {
            if (mMainTitles != null){
                return mMainTitles.length;
            }
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mMainTitles[position];
        }
    }
    /**
     * 第三种pageView适配的方式（没有缓存，数据量大的话，推荐使用，本项目用这种适配方式）
     */
    private class MainFragmentStatePagerAdapter extends FragmentStatePagerAdapter{

        public MainFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = FragmentFactory.getFragment(position);
            LogUtils.sf("初始化"+mMainTitles[position]);
            return fragment;
        }

        @Override
        public int getCount() {
            if (mMainTitles != null){
                return mMainTitles.length;
            }
            return 0;
        }

        /**
         * 此方法必须实现，否则报指针异常
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mMainTitles[position];
        }
    }
}
