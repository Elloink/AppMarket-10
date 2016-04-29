package com.buaa.tezlikai.appmarket.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.buaa.tezlikai.appmarket.R;
import com.buaa.tezlikai.appmarket.base.BaseHolder;
import com.buaa.tezlikai.appmarket.bean.AppInfoBean;
import com.buaa.tezlikai.appmarket.conf.Constants;
import com.buaa.tezlikai.appmarket.manager.DownLoadInfo;
import com.buaa.tezlikai.appmarket.manager.DownloadManager;
import com.buaa.tezlikai.appmarket.manager.DownloadManager.DownLoadObserver;
import com.buaa.tezlikai.appmarket.utils.BitmapHelper;
import com.buaa.tezlikai.appmarket.utils.CommonUtils;
import com.buaa.tezlikai.appmarket.utils.PrintDownLoadInfo;
import com.buaa.tezlikai.appmarket.utils.StringUtils;
import com.buaa.tezlikai.appmarket.utils.UIUtils;
import com.buaa.tezlikai.appmarket.views.CircleProgressView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;

/**
 * Created by Administrator on 2016/4/24.
 */
public class AppItemHolder extends BaseHolder<AppInfoBean> implements View.OnClickListener,DownLoadObserver{

    @ViewInject(R.id.item_appinfo_iv_icon)
    private ImageView mIvIcon;
    @ViewInject(R.id.item_appinfo_rb_stars)
    private RatingBar mRbStars;
    @ViewInject(R.id.item_appinfo_tv_des)
    private TextView mTvDes;
    @ViewInject(R.id.item_appinfo_tv_size)
    private TextView mTvSize;
    @ViewInject(R.id.item_appinfo_tv_title)
    private TextView mTvTitle;
    @ViewInject(R.id.item_appinfo_circleprogressview)
    private CircleProgressView mCircleProgressView;
    private AppInfoBean mData;


    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_app_info, null);
        //注入
        ViewUtils.inject(this,view);

        mCircleProgressView.setOnClickListener(this);
        return view;
    }

    @Override
    public void refreshHolderView(AppInfoBean data) {
        //  清除复用ConvertView之后的progress效果
        mCircleProgressView.setProgress(0);

        mData = data;
        mTvTitle.setText(data.name);
        mTvDes.setText(data.des);
        mTvSize.setText(StringUtils.formatFileSize(data.size));//将数据大小改成Kb/M显示

        mIvIcon.setImageResource(R.drawable.ic_default);//设置默认图片
        //BitmapUtils bitmapUtils = new BitmapUtils(UIUtils.getContext());

        // http://localhost:8080/GooglePlayServer/image?name=
        // data.iconUrl
        String url = Constants.URLS.IMAGEBASEURL + data.iconUrl;
        BitmapHelper.display(mIvIcon,url);//把BitmapUtils封装了一下，以免重复加载bitmapUtils

        mRbStars.setRating(data.stars);

        /*------------根据不同的状态给用户提示------------------*/
        DownLoadInfo info = DownloadManager.getInstance().getDownLoadInfo(data);
        refreshCircleProgressBtnUI(info);
    }
    public void refreshCircleProgressBtnUI(DownLoadInfo info) {
        switch (info.state){
            case DownloadManager.STATE_UNDOWNLOAD://未下载
                mCircleProgressView.setNote("下载");
                mCircleProgressView.setIcon(R.drawable.ic_download);
                break;
            case DownloadManager.STATE_DOWNLOADING:// 下载中
                mCircleProgressView.setBackgroundResource(R.drawable.selector_app_detail_bottom_downloading);
                mCircleProgressView.setProgressEnable(true);
                mCircleProgressView.setMax(info.max);
                mCircleProgressView.setProgress(info.curProgress);
                int progress = (int) (info.curProgress * 100.f / info.max +.5f);
                mCircleProgressView.setNote(progress + "%");
                mCircleProgressView.setIcon(R.drawable.ic_pause);
                break;
            case DownloadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                mCircleProgressView.setNote("继续下载");
                mCircleProgressView.setIcon(R.drawable.ic_resume);
                break;
            case DownloadManager.STATE_WAITINGDOWNLOAD:// 等待下载
                mCircleProgressView.setNote("等待中");
                mCircleProgressView.setIcon(R.drawable.ic_pause);
                break;
            case DownloadManager.STATE_DOWNLOADFAILED:// 下载失败
                mCircleProgressView.setNote("重试");
                mCircleProgressView.setIcon(R.drawable.ic_redownload);
                break;
            case DownloadManager.STATE_DOWNLOADED:// 下载完成
                mCircleProgressView.setProgressEnable(false);
                mCircleProgressView.setNote("安装");
                mCircleProgressView.setIcon(R.drawable.ic_install);
                break;
            case DownloadManager.STATE_INSTALLED:// 已安装
                mCircleProgressView.setNote("打开");
                mCircleProgressView.setIcon(R.drawable.ic_install);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_appinfo_circleprogressview:
                DownLoadInfo info = DownloadManager.getInstance().getDownLoadInfo(mData);
                switch (info.state){
                    case DownloadManager.STATE_UNDOWNLOAD://未下载
                        doDownLoad(info);
                        break;
                    case DownloadManager.STATE_DOWNLOADING:// 下载中
                        pauseDownLoad(info);
                        break;
                    case DownloadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                        doDownLoad(info);
                        break;
                    case DownloadManager.STATE_WAITINGDOWNLOAD:// 等待下载
                        cancelDownLoad(info);
                        break;
                    case DownloadManager.STATE_DOWNLOADFAILED:// 下载失败
                        doDownLoad(info);
                        break;
                    case DownloadManager.STATE_DOWNLOADED:// 下载完成
                        installApk(info);
                        break;
                    case DownloadManager.STATE_INSTALLED:// 已安装
                        openApk(info);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
    /**
     * 打开应用
     * @param info
     */
    private void openApk(DownLoadInfo info) {
        CommonUtils.openApp(UIUtils.getContext(), info.packageName);
    }

    /**
     * 安装应用
     * @param info
     */
    private void installApk(DownLoadInfo info) {
        File apkFile = new File(info.savePath);
        CommonUtils.installApp(UIUtils.getContext(), apkFile);
    }

    /**
     * 取消下载
     * @param info
     */
    private void cancelDownLoad(DownLoadInfo info) {
        DownloadManager.getInstance().cancel(info);

    }

    /**
     * 暂停应用
     * @param info
     */
    private void pauseDownLoad(DownLoadInfo info) {
        DownloadManager.getInstance().pause(info);

    }

    /**
     * 点击下载按钮
     */
    private void doDownLoad(DownLoadInfo info){
		 /*-------------根据不同的状态触发不用的操作 -----------------*/
        DownloadManager.getInstance().downLoad(info);
    }

    /*-----------收到数据改变，更新UI-------------*/
    @Override
    public void onDownLoadInfoChange(final DownLoadInfo info) {
        //过滤DownLoadInfo,防止DownLoadInfo混乱，
        // 解决问题:比如你点击一个下载，然后在打开另一个应用下载界面的时候它会直接下载的问题
        if (!info.packageName.equals(mData.packageName)){
            return;
        }
        PrintDownLoadInfo.printDownLoadInfo(info);

        UIUtils.postTaskSafely(new Runnable() {
            @Override
            public void run() {
                refreshCircleProgressBtnUI(info);
            }
        });
    }
}
