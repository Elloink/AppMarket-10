package com.buaa.tezlikai.appmarket.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.buaa.tezlikai.appmarket.R;
import com.buaa.tezlikai.appmarket.base.BaseActivity;
import com.buaa.tezlikai.appmarket.base.LoadingPager;
import com.buaa.tezlikai.appmarket.base.LoadingPager.LoadedResult;
import com.buaa.tezlikai.appmarket.bean.AppInfoBean;
import com.buaa.tezlikai.appmarket.holder.AppDetailBottomHolder;
import com.buaa.tezlikai.appmarket.holder.AppDetailDesHolder;
import com.buaa.tezlikai.appmarket.holder.AppDetailInfoHolder;
import com.buaa.tezlikai.appmarket.holder.AppDetailPicHolder;
import com.buaa.tezlikai.appmarket.holder.AppDetailSafeHolder;
import com.buaa.tezlikai.appmarket.manager.DownloadManager;
import com.buaa.tezlikai.appmarket.protocol.DetailProtocol;
import com.buaa.tezlikai.appmarket.utils.UIUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class DetailActivity extends BaseActivity {

    private String mPackageName;
    private AppInfoBean mDatas;

    @ViewInject(R.id.app_detail_bottom)
    FrameLayout mContainerBottom;

    @ViewInject(R.id.app_detail_des)
    FrameLayout	mContainerDes;

    @ViewInject(R.id.app_detail_info)
    FrameLayout	mContainerInfo;

    @ViewInject(R.id.app_detail_pic)
    FrameLayout	mContainerPic;

    @ViewInject(R.id.app_detail_safe)
    FrameLayout	mContainerSafe;
    private LoadingPager mLoadingPager;
    private AppDetailBottomHolder mAppDetailBottomHolder;

    @Override
    public void init() {
        mPackageName = getIntent().getStringExtra("packageName");
    }

    @NonNull
    @Override
    public void initView() {
        mLoadingPager = new LoadingPager(UIUtils.getContext()) {
            @Override
            protected LoadedResult initData() {
                return onInitData();
            }
            @Override
            protected View initSuccessView() {

                return onLoadSuccessView();
            }
        };
        setContentView(mLoadingPager);
    }

    @Override
    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("应用市场");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void initData() {
        mLoadingPager.loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private LoadedResult onInitData() {
        //发起网络请求
        DetailProtocol protocol = new DetailProtocol(mPackageName);
        try {
            mDatas = protocol.loadData(0);
            if (mDatas == null){
                return LoadedResult.ERROR;
            }
            return LoadedResult.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return LoadedResult.ERROR;
        }
    }

    private View onLoadSuccessView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.activity_detail, null);
        ViewUtils.inject(this, view);

        //填充内容
        //1.信息部分
        AppDetailInfoHolder appDetailInfoHolder = new AppDetailInfoHolder();
        mContainerInfo.addView(appDetailInfoHolder.getmHolderView());
        appDetailInfoHolder.setDataAndRefreshHolderView(mDatas);

        //2.安全部分
        AppDetailSafeHolder appDetailSafeHolder = new AppDetailSafeHolder();
        mContainerSafe.addView(appDetailSafeHolder.getmHolderView());
        appDetailSafeHolder.setDataAndRefreshHolderView(mDatas);

        //3.截图部分
        AppDetailPicHolder appDetailPicHolder = new AppDetailPicHolder();
        mContainerPic.addView(appDetailPicHolder.getmHolderView());
        appDetailPicHolder.setDataAndRefreshHolderView(mDatas);

        //4.描述部分
        AppDetailDesHolder appDetailDesHolder = new AppDetailDesHolder();
        mContainerDes.addView(appDetailDesHolder.getmHolderView());
        appDetailDesHolder.setDataAndRefreshHolderView(mDatas);

        //5.下载部分
        mAppDetailBottomHolder = new AppDetailBottomHolder();
        mContainerBottom.addView(mAppDetailBottomHolder.getmHolderView());
        mAppDetailBottomHolder.setDataAndRefreshHolderView(mDatas);

        DownloadManager.getInstance().addObserver(mAppDetailBottomHolder);
        return view;
    }

    @Override
    protected void onPause() {//界面不可见的时候移除观察者
        if (mAppDetailBottomHolder != null){
            DownloadManager.getInstance().deleteObserver(mAppDetailBottomHolder);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {//界面可见的时候添加观察者
        if (mAppDetailBottomHolder != null){
            //开启监听的时候，手动的去过去一下最新的状态
            mAppDetailBottomHolder.addObserverAndRerefresh();
        }
        super.onResume();
    }
}
