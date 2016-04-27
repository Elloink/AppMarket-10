package com.buaa.tezlikai.appmarket.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.buaa.tezlikai.appmarket.base.BaseFragment;
import com.buaa.tezlikai.appmarket.base.LoadingPager;
import com.buaa.tezlikai.appmarket.protocol.RecommendProtocol;
import com.buaa.tezlikai.appmarket.utils.UIUtils;
import com.buaa.tezlikai.appmarket.views.flyoutin.ShakeListener;
import com.buaa.tezlikai.appmarket.views.flyoutin.StellarMap;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/4/22.
 */
public class RecommendFragment extends BaseFragment {

    private List<String> mDatas;
    private RecommendAdapter adapter;
    private ShakeListener mShakeListener;

    @Override
    public LoadingPager.LoadedResult initData() {//真正加载数据
        RecommendProtocol protocol = new RecommendProtocol();
        try {
            mDatas = protocol.loadData(0);
            return checkState(mDatas);
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }
    }

    @Override
    protected View initSuccessView() {
        //返回成功视图
        final StellarMap stellarMap = new StellarMap(UIUtils.getContext());
        //设置adapter
        adapter = new RecommendAdapter();
        stellarMap.setAdapter(adapter);
        //设置第一页的时候显示
        stellarMap.setGroup(0, true);
        //设置把屏幕拆分成多个格子
        stellarMap.setRegularity(15, 20);

        // 加入摇一摇切换
        mShakeListener = new ShakeListener(UIUtils.getContext());
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                int groupIndex = stellarMap.getCurrentGroup();
                if (groupIndex == adapter.getGroupCount() - 1) {//防止越界问题
                    groupIndex = 0;
                } else {
                    groupIndex++;
                }
                stellarMap.setGroup(groupIndex, true);
            }
        });

        return stellarMap;
    }

    @Override
    public void onResume() {
        if (mShakeListener != null){
            mShakeListener.resume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mShakeListener != null){
            mShakeListener.pause();
        }
        super.onPause();
    }

    private class RecommendAdapter implements StellarMap.Adapter {
        private static final int PAGER_SIZE = 15;//每页显示多少条数据

        @Override
        public int getGroupCount() {//有多少组
            int groupCount = mDatas.size() / PAGER_SIZE;
            //如果不能整除，还有余数的情况
            if (mDatas.size() % PAGER_SIZE >0){//有余数
                groupCount++;
            }
            return groupCount;
        }

        @Override
        public int getCount(int group) {//每组有多少条数据
            if (group == getGroupCount()-1){//来到最后一组
                if (mDatas.size() % PAGER_SIZE > 0 ){//有余数
                    return mDatas.size() % PAGER_SIZE;//返回具体的余数值就可以了
                }
            }
            return PAGER_SIZE;//0-15
        }

        @Override
        public View getView(int group, int position, View convertView) {
            TextView tv = new TextView(UIUtils.getContext());
            //group:代表第几组
            //position:几组中的第几个位置
            int index = group * PAGER_SIZE + position;
            tv.setText(mDatas.get(index));

            //random对象
            Random random = new Random();
            //随机大小
            tv.setTextSize(random.nextInt(12)+25);//设置字体大小
            //随机颜色
            int alpha = 255;
            int red = random.nextInt(180)+30;
            int green = random.nextInt(180)+30;
            int blue = random.nextInt(180)+30;
            int argb = Color.argb(alpha, red, green, blue);
            tv.setTextColor(argb);

            //设置一下间距
            int padding = UIUtils.dip2Px(5);
            tv.setPadding(padding, padding, padding, padding);

            return tv;
        }

        @Override
        public int getNextGroupOnPan(int group, float degree) {
            return 0;
        }

        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            return 0;
        }
    }
}
