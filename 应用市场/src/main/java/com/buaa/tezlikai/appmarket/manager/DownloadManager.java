package com.buaa.tezlikai.appmarket.manager;

import android.support.annotation.NonNull;

import com.buaa.tezlikai.appmarket.bean.AppInfoBean;
import com.buaa.tezlikai.appmarket.conf.Constants;
import com.buaa.tezlikai.appmarket.factory.ThreadPoolFactory;
import com.buaa.tezlikai.appmarket.utils.CommonUtils;
import com.buaa.tezlikai.appmarket.utils.FileUtils;
import com.buaa.tezlikai.appmarket.utils.IOUtils;
import com.buaa.tezlikai.appmarket.utils.UIUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/28.
 * 下载管理器，设置成单例模式
 * 需要时刻记住当前的状态，
 */
public class DownloadManager { //

    public static final int STATE_UNDOWNLOAD = 0;// 未下载
    public static final int STATE_DOWNLOADING = 1;// 下载中
    public static final int STATE_PAUSEDOWNLOAD = 2;// 暂停下载
    public static final int STATE_WAITINGDOWNLOAD = 3;// 等待下载
    public static final int STATE_DOWNLOADFAILED = 4;// 下载失败
    public static final int STATE_DOWNLOADED = 5;// 下载完成
    public static final int STATE_INSTALLED = 6;// 已安装

    public static DownloadManager instance;
    //记录正在下载的一些downLoadInfo
    public Map<String, DownLoadInfo> mDownLoadInfoMaps = new HashMap<String, DownLoadInfo>();

    private DownloadManager() {
    }

    public static DownloadManager getInstance() {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager();
                }
            }
        }
        return instance;
    }

    /**
     * 用户点击了下载按钮
     */
    public void downLoad(DownLoadInfo info) {
        mDownLoadInfoMaps.put(info.packageName, info);

        /*------------------- 当前状态 : 未下载 ----------------------*/
        info.state = STATE_UNDOWNLOAD;
        notifyObservers(info);

        /*------------------- 当前状态 : 等待状态----------------------*/
        info.state = STATE_WAITINGDOWNLOAD;
        notifyObservers(info);

        //得到线程池，执行任务
        ThreadPoolFactory.getDownLoadPool().execute(new DownLoadTask(info));
    }


    private class DownLoadTask implements Runnable {

        DownLoadInfo mInfo;

        public DownLoadTask(DownLoadInfo info) {
            super();
            mInfo = info;
        }

        @Override
        public void run() {
            //正在发起网络请求下载apk
            try {//进入的run()方法说明已经进入线程池中了

                //下载地址
                String url = Constants.URLS.DOWNLOADBASEURL;
                HttpUtils httpUtils = new HttpUtils();
                //相关参数
                RequestParams params = new RequestParams();
                params.addQueryStringParameter("name", mInfo.downloadUrl);
                params.addQueryStringParameter("range", 0 + "");

                ResponseStream responseStream = httpUtils.sendSync(HttpMethod.GET, url, params);

                if (responseStream.getStatusCode() == 200) {
                    InputStream in = null;
                    FileOutputStream out = null;
                    try {
                        in = responseStream.getBaseStream();
                        File saveFile = new File(mInfo.savePath);
                        out = new FileOutputStream(saveFile);

                        byte[] buffer = new byte[1024];
                        int len = -1;

                        while ((len = in.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                            mInfo.curProgress += len;
                            /*------------------- 当前状态 : 下载中状态----------------------*/
                            mInfo.state = STATE_DOWNLOADING;
                            notifyObservers(mInfo);
                        }
                         /*------------------- 当前状态 : 下载完成状态----------------------*/
                        mInfo.state = STATE_DOWNLOADED;
                        notifyObservers(mInfo);

                    } finally {
                        IOUtils.close(out);
                        IOUtils.close(in);
                    }
                } else {
                     /*------------------- 当前状态 : 下载失败----------------------*/
                    mInfo.state = STATE_DOWNLOADFAILED;
                    notifyObservers(mInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
                 /*------------------- 当前状态 : 下载失败----------------------*/
                mInfo.state = STATE_DOWNLOADFAILED;
                notifyObservers(mInfo);
            }
        }
    }

    /**
     * 暴露当前状态，也就是需要提供downLoadInfo
     * 外界需要知道最新的state的状态
     */
    public DownLoadInfo getDownLoadInfo(AppInfoBean data) {


        //已安装
        if (CommonUtils.isInstalled(UIUtils.getContext(), data.packageName)) {
            DownLoadInfo info = generateDownInfo(data);
            info.state = STATE_INSTALLED;//已安装
            return info;
        }

        //下载完成
        DownLoadInfo info = generateDownInfo(data);
        File saveApk = new File(info.savePath);
        if (saveApk.exists()) {//如果存在我们的下载目录
            if (saveApk.length() == data.size) {
                //初始化一个downLoadInfo
                info.state = STATE_DOWNLOADED;//下载完成
                return info;
            }
        }

        /**
         * 下载中
         * 暂停下载
         * 等待下载
         * 下载失败
         */
        DownLoadInfo downLoadInfo = mDownLoadInfoMaps.get(data.packageName);
        if (downLoadInfo != null) {
            return downLoadInfo;
        }

        //未下载
        DownLoadInfo tempInfo = generateDownInfo(data);
        tempInfo.state = STATE_UNDOWNLOAD;//未下载
        return tempInfo;
    }

    /**
     * 根据AppInfoBean生成一个DownLoadInfo,进行一些常规的赋值，也就是对一些常规属性赋值（除了state之外的属性）
     */
    @NonNull
    public DownLoadInfo generateDownInfo(AppInfoBean data) {
        //下载apk放置的目录
        String dir = FileUtils.getDir("download");
        File file = new File(dir, data.packageName + ".apk");
        //保存路径
        String savePath = file.getAbsolutePath();

        //初始化一个downLoadInfo
        DownLoadInfo info = new DownLoadInfo();
        info.downloadUrl = data.downloadUrl;
        info.savePath = savePath;
        info.packageName = data.packageName;
        info.max = data.size;
        info.curProgress = 0;
        return info;
    }



    /*=============== 自定义观察者设计模式  begin ===============*/
    public interface DownLoadObserver {
        void onDownLoadInfoChange(DownLoadInfo info);
    }

    List<DownLoadObserver>	downLoadObservers	= new LinkedList<DownLoadObserver>();

    /**添加观察者*/
    public void addObserver(DownLoadObserver observer) {
        if (observer == null) {
            throw new NullPointerException("observer == null");
        }
        synchronized (this) {
            if (!downLoadObservers.contains(observer))
                downLoadObservers.add(observer);
        }
    }

    /**删除观察者*/
    public synchronized void deleteObserver(DownLoadObserver observer) {
        downLoadObservers.remove(observer);
    }

    /**通知观察者数据改变*/
    public void notifyObservers(DownLoadInfo info) {
        for (DownLoadObserver observer : downLoadObservers) {
            observer.onDownLoadInfoChange(info);
        }
    }

	/*=============== 自定义观察者设计模式  end ===============*/
}
