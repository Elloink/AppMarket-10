package com.buaa.tezlikai.appmarket.protocol;

import com.buaa.tezlikai.appmarket.base.BaseProtocol;
import com.buaa.tezlikai.appmarket.bean.AppInfoBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/27.
 */
public class DetailProtocol extends BaseProtocol<AppInfoBean>{

    private String mPackageName;

    public DetailProtocol(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    @Override
    public String getInterfaceKey() {
        return "detail";
    }

   /* @Override
    public AppInfoBean parseJson(String jsonString) {

        Gson gson = new Gson();
        return gson.fromJson(jsonString, AppInfoBean.class);
    }*/

    @Override
    public Map<String, String> getExtraParmas() {
        Map<String,String> extraParmas = new HashMap<String,String>();
        extraParmas.put("packageName",mPackageName);
        return extraParmas;
    }
}
