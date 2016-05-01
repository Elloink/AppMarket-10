package com.buaa.tezlikai.appmarket;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStripExtends;
import com.buaa.tezlikai.appmarket.base.BaseActivity;
import com.buaa.tezlikai.appmarket.base.BaseFragment;
import com.buaa.tezlikai.appmarket.factory.FragmentFactory;
import com.buaa.tezlikai.appmarket.holder.MenuHolder;
import com.buaa.tezlikai.appmarket.utils.LogUtils;
import com.buaa.tezlikai.appmarket.utils.UIUtils;

public class MainActivity extends BaseActivity {

    private PagerSlidingTabStripExtends mTabs;
    private ViewPager mViewPager;
    private String[] mMainTitles;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private FrameLayout mMain_menu;

    /**
     * 初始化View
     */
    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        mTabs = (PagerSlidingTabStripExtends) findViewById(R.id.main_tabs);
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawlayout);
        mMain_menu = (FrameLayout) findViewById(R.id.main_menu);
    }

    @Override
    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        actionBar.setLogo(R.drawable.ic_launcher);//设置logo
        actionBar.setTitle("应用市场");//设置Titile

        //显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer_am);
        initActionBarToggle();

    }
    private void initActionBarToggle() {
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout, R.drawable.ic_drawer_am,R.string.open,R.string.close);

        //同步状态的方法
        mToggle.syncState();
        //设置mDrawerLayout拖动的监听
        mDrawerLayout.setDrawerListener(mToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //mToggle控制打开关闭drawlayout
                mToggle.onOptionsItemSelected(item);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化Data
     */
    @Override
    public void initData() {

        mMainTitles = UIUtils.getStringArr(R.array.main_titles);

//        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        MainFragmentStatePagerAdapter adapter = new MainFragmentStatePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);

        // Bind the tabs to the ViewPager
        // viewpager和tabs的一个绑定
        mTabs.setViewPager(mViewPager);

        //设置viewpager的动画
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        MenuHolder menuHolder = new MenuHolder();
        mMain_menu.addView(menuHolder.getmHolderView());
        menuHolder.setDataAndRefreshHolderView(null);
    }
    @Override
    public void initListener() {
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
            LogUtils.sf("初始化" + mMainTitles[position]);
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

    /**
     * API提供的一种viewpager翻转特效：
     * 可以参考file:///D:/Android1/Android1/sdk/docs/training/animation/screen-slide.html实现
     */
    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

}
