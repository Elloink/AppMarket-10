package com.buaa.tezlikai.appmarket.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.buaa.tezlikai.appmarket.base.BaseFragment;
import com.buaa.tezlikai.appmarket.base.LoadingPager;
import com.buaa.tezlikai.appmarket.protocol.HotProtocol;
import com.buaa.tezlikai.appmarket.utils.UIUtils;
import com.buaa.tezlikai.appmarket.views.FlowLayout;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/4/22.
 */
public class HotFragment extends BaseFragment {

    private List<String> mDatas;

    @Override
    public LoadingPager.LoadedResult initData() {//真正加载数据
        HotProtocol protocol = new HotProtocol();
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
        //返回成功的视图
        ScrollView scrollView = new ScrollView(UIUtils.getContext());
        //创建流式布局
        FlowLayout fl = new FlowLayout(UIUtils.getContext());

        for (final String data : mDatas){
            TextView tv = new TextView(UIUtils.getContext());
            tv.setText(data);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(22);

            int padding = UIUtils.dip2Px(10);
            tv.setPadding(padding, padding, padding, padding);
            tv.setGravity(Gravity.CENTER);

//            tv.setBackgroundResource(R.drawable.shape_hot_fl_tv);//可以通过setBackgroundResource设置，该改变圆角和颜色
            /*------------------- normalDrawable begin---------------------*/
            //通常用的方法
            GradientDrawable normalDrawable = new GradientDrawable();
            //得到随机颜色
            Random random = new Random();
            int alpha = 255;//设置透明度（255表示完全不透明）
            int red = random.nextInt(190)+30;//随机取颜色值,30-220 颜色柔和
            int green = random.nextInt(190)+30;//随机取颜色值
            int blue = random.nextInt(190)+30;//随机取颜色值
            int argb = Color.argb(alpha, red, green, blue);

            //设置填充颜色
            normalDrawable.setColor(argb);

            //设置圆角半径
            normalDrawable.setCornerRadius(UIUtils.dip2Px(10));
            /*------------------- normalDrawable end ---------------------*/

            /*------------------- pressedDrawable start ---------------------*/
            GradientDrawable pressedDrawable = new GradientDrawable();
            pressedDrawable.setColor(Color.DKGRAY);
            pressedDrawable.setCornerRadius(UIUtils.dip2Px(10));
            /*------------------- pressedDrawable end ---------------------*/

            //设置一个状态图片
            StateListDrawable stateListDrawable = new StateListDrawable();

            stateListDrawable.addState(new int[]{ android.R.attr.state_pressed},pressedDrawable);
            stateListDrawable.addState(new int[]{},normalDrawable);
            tv.setBackgroundDrawable(stateListDrawable);

            tv.setClickable(true);//设置点击事件

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(),data,Toast.LENGTH_SHORT).show();
                }
            });

            //往流式布局里添加孩子
            fl.addView(tv);
        }
        scrollView.addView(fl);
        return scrollView;
    }
}
