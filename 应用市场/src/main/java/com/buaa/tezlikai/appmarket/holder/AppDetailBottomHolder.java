package com.buaa.tezlikai.appmarket.holder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.buaa.tezlikai.appmarket.R;
import com.buaa.tezlikai.appmarket.base.BaseHolder;
import com.buaa.tezlikai.appmarket.bean.AppInfoBean;
import com.buaa.tezlikai.appmarket.manager.DownLoadInfo;
import com.buaa.tezlikai.appmarket.manager.DownloadManager;
import com.buaa.tezlikai.appmarket.utils.CommonUtils;
import com.buaa.tezlikai.appmarket.utils.PrintDownLoadInfo;
import com.buaa.tezlikai.appmarket.utils.UIUtils;
import com.buaa.tezlikai.appmarket.views.ProgressButton;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;

/**
 * AppDetailBottomHolder
 */
public class AppDetailBottomHolder extends BaseHolder<AppInfoBean> implements OnClickListener ,DownloadManager.DownLoadObserver{
	@ViewInject(R.id.app_detail_download_btn_share)
	Button	mBtnShare;
	@ViewInject(R.id.app_detail_download_btn_favo)
	Button	mBtnFavo;
	@ViewInject(R.id.app_detail_download_btn_download)
	ProgressButton mProgressButton;
	private AppInfoBean mData;

	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_bottom, null);
		ViewUtils.inject(this, view);
		mBtnShare.setOnClickListener(this);
		mBtnFavo.setOnClickListener(this);
		mProgressButton.setOnClickListener(this);
		return view;
	}

	@Override
	public void refreshHolderView(AppInfoBean data) {
		mData = data;
		/*------------根据不同的状态给用户提示------------------*/
		DownLoadInfo info = DownloadManager.getInstance().getDownLoadInfo(data);
		refreshProgressBtnUI(info);
	}

	public void refreshProgressBtnUI(DownLoadInfo info) {
		mProgressButton.setBackgroundResource(R.drawable.selector_app_detail_bottom_normal);
		switch (info.state){
			case DownloadManager.STATE_UNDOWNLOAD://未下载
				mProgressButton.setText("下载");
				break;
			case DownloadManager.STATE_DOWNLOADING:// 下载中
				mProgressButton.setBackgroundResource(R.drawable.selector_app_detail_bottom_downloading);
				mProgressButton.setProgressEnable(true);
				mProgressButton.setMax(info.max);
				mProgressButton.setProgress(info.curProgress);
				int progress = (int) (info.curProgress * 100.f / info.max +.5f);
				mProgressButton.setText(progress + "%");
				break;
			case DownloadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
				mProgressButton.setText("继续下载");
				break;
			case DownloadManager.STATE_WAITINGDOWNLOAD:// 等待下载
				mProgressButton.setText("等待中");
				break;
			case DownloadManager.STATE_DOWNLOADFAILED:// 下载失败
				mProgressButton.setText("重试");
				break;
			case DownloadManager.STATE_DOWNLOADED:// 下载完成
				mProgressButton.setProgressEnable(false);
				mProgressButton.setText("安装");
				break;
			case DownloadManager.STATE_INSTALLED:// 已安装
				mProgressButton.setText("打开");
				break;
			default:
				break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.app_detail_download_btn_download:
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
		CommonUtils.openApp(UIUtils.getContext(),info.packageName);
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
				refreshProgressBtnUI(info);
			}
		});
	}

	public void addObserverAndRerefresh(){
		DownloadManager.getInstance().addObserver(this);

		//手动刷新
		DownLoadInfo downLoadInfo = DownloadManager.getInstance().getDownLoadInfo(mData);
		/*--------------可以发送消息，也可以直接调用refreshProgressBtnUI()方法-------------*/
//		DownloadManager.getInstance().notifyObservers(downLoadInfo);//方式一
		refreshProgressBtnUI(downLoadInfo);//方式二

	}

}
