package com.buaa.tezlikai.appmarket.fragment;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.buaa.tezlikai.appmarket.adapter.AppItemAdapter;
import com.buaa.tezlikai.appmarket.base.BaseFragment;
import com.buaa.tezlikai.appmarket.base.LoadingPager.LoadedResult;
import com.buaa.tezlikai.appmarket.bean.AppInfoBean;
import com.buaa.tezlikai.appmarket.bean.HomeBean;
import com.buaa.tezlikai.appmarket.factory.ListViewFactory;
import com.buaa.tezlikai.appmarket.holder.PictureHolder;
import com.buaa.tezlikai.appmarket.protocol.HomeProtocol;

import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 */
public class HomeFragment extends BaseFragment {

    private List<AppInfoBean> mDatas;  //listView的数据源
    private List<String> mPicutures;//轮播图
    private HomeProtocol mProtocol;

    @Override
    public LoadedResult initData() {//真正加载数据
        //发送网络请求
       /* try {
            HttpUtils httpUtils = new HttpUtils();
            // http://localhost:8080/GooglePlayServer/image?name=
            String url = Constants.URLS.BASEURL + "home";
            RequestParams parmas = new RequestParams();
            parmas.addQueryStringParameter("index", "0");
            ResponseStream responseStream = httpUtils.sendSync(HttpMethod.GET, url, parmas);
            String result = responseStream.readString();

            //Gson
            Gson gson = new Gson();
            homeBean = gson.fromJson(result, HomeBean.class);

            LoadedResult state = checkState(homeBean);

            if (state != LoadedResult.SUCCESS){//如果不成功，就直接返回，走到这里说明homeBean是OK的
                return state;
            }

            state = checkState(homeBean.list);
            if (state != LoadedResult.SUCCESS){//如果不成功，就直接返回，走到这里说明homeBean.list是OK得
                return state;
            }
            mDatas = homeBean.list;
            picutures = homeBean.picture;
            return LoadedResult.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return LoadedResult.ERROR;
        }*/
        /*====================  协议简单封装之后  ========================*/
        mProtocol = new HomeProtocol();
        try {
            HomeBean homeBean = mProtocol.loadData(0);
            LoadedResult state = checkState(homeBean);

            if (state != LoadedResult.SUCCESS){//如果不成功，就直接返回，走到这里说明homeBean是OK的
                return state;
            }
            state = checkState(homeBean.list);
            if (state != LoadedResult.SUCCESS){//如果不成功，就直接返回，走到这里说明homeBean.list是OK得
                return state;
            }
            mDatas = homeBean.list;
            mPicutures = homeBean.picture;
            return LoadedResult.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return LoadedResult.ERROR;
        }
    }

    @Override
    protected View initSuccessView() {
        //返回成功视图
        ListView listView = ListViewFactory.createListView();

        // 创建一个PictureHolder
        PictureHolder pictureHolder = new PictureHolder();
        // 触发加载数据
        pictureHolder.setDataAndRefreshHolderView(mPicutures);

        View headView = pictureHolder.getmHolderView();
        listView.addHeaderView(headView);//添加图片轮播

        //设置Adapter
        listView.setAdapter(new HomeAdapter(listView,mDatas));

        return listView;
    }

    class HomeAdapter extends AppItemAdapter{


        public HomeAdapter(AbsListView absListView, List<AppInfoBean> dataSource) {
            super(absListView, dataSource);
        }

        @Override
        public List<AppInfoBean> onLoadMore() throws Exception {
            //SystemClock.sleep(2000);
            return loadMore(mDatas.size());
        }
        @Nullable
        public List<AppInfoBean> loadMore(int index) throws Exception {
            //真正加载更多的数据
            //发送网络请求
           /* HttpUtils httpUtils = new HttpUtils();
            // http://localhost:8080/GooglePlayServer/image?name=
            String url = Constants.URLS.BASEURL + "home";
            RequestParams parmas = new RequestParams();
            parmas.addQueryStringParameter("index", index+"");//20
            ResponseStream responseStream = httpUtils.sendSync(HttpMethod.GET, url, parmas);
            String result = responseStream.readString();

            //Gson
            Gson gson = new Gson();
            HomeBean homeBean = gson.fromJson(result, HomeBean.class);

            if (homeBean == null){
                return null;
            }
            if (homeBean.list == null || homeBean.list.size() == 0){
                return null;
            }
            return homeBean.list;
        }*/
           /*====================  协议简单封装之后  ========================*/

            HomeBean homeBean = mProtocol.loadData(mDatas.size());
            if (homeBean == null) {
                return null;
            }
            if (homeBean.list == null || homeBean.list.size() == 0) {
                return null;
            }
            return homeBean.list;
        }
        }

}
