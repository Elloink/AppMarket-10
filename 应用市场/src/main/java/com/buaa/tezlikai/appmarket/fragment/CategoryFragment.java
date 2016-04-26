package com.buaa.tezlikai.appmarket.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.buaa.tezlikai.appmarket.base.BaseFragment;
import com.buaa.tezlikai.appmarket.base.BaseHolder;
import com.buaa.tezlikai.appmarket.base.LoadingPager;
import com.buaa.tezlikai.appmarket.base.SuperBaseAdapter;
import com.buaa.tezlikai.appmarket.bean.CategoryInfoBean;
import com.buaa.tezlikai.appmarket.factory.ListViewFactory;
import com.buaa.tezlikai.appmarket.holder.CategoryItemHolder;
import com.buaa.tezlikai.appmarket.holder.CategoryTitleHolder;
import com.buaa.tezlikai.appmarket.protocol.CategoryProtocol;

import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 */
public class CategoryFragment extends BaseFragment {

    private List<CategoryInfoBean> mDatas;

    @Override
    public LoadingPager.LoadedResult initData() {//真正加载数据
        CategoryProtocol categoryProtocol = new CategoryProtocol();
        try {
            mDatas = categoryProtocol.loadData(0);
            return checkState(mDatas);
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }
    }

    @Override
    protected View initSuccessView() {
        //返回视图
        ListView listView = ListViewFactory.createListView();
        listView.setAdapter(new CategoryAdapter(listView,mDatas));

        return listView;
    }

    private class CategoryAdapter extends SuperBaseAdapter<CategoryInfoBean> {
        public CategoryAdapter(AbsListView absListView, List<CategoryInfoBean> dataSource) {
            super(absListView, dataSource);
        }

        @Override
        public BaseHolder<CategoryInfoBean> getSpecialHolder(int position) {
            CategoryInfoBean categoryInfoBean = mDatas.get(position);
            //如果是title --> titleHolder
            if (categoryInfoBean.isTitle){
                return new CategoryTitleHolder();
            }else {
                //如果不是title --> itemHolder
                return new CategoryItemHolder();
            }
        }

        @Override
        public int getViewTypeCount() {//因为有两种viewType
            return super.getViewTypeCount() + 1;
        }

        @Override
        public int getNormalViewType(int position) {// 0 1 2 range 0 - getViewTypeCount-1
            CategoryInfoBean categoryInfoBean = mDatas.get(position);
            if (categoryInfoBean.isTitle){
                return super.getNormalViewType(position) + 1;
            }else {
                return super.getNormalViewType(position);
            }
        }
    }
}
