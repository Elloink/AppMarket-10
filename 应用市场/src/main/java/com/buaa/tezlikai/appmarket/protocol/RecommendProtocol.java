package com.buaa.tezlikai.appmarket.protocol;

import com.buaa.tezlikai.appmarket.base.BaseProtocol;

import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 */
public class RecommendProtocol extends BaseProtocol<List<String>> {
    @Override
    public String getInterfaceKey() {
        return "recommend";
    }

  /*  @Override
    public List<String> parseJson(String jsonString) {
        *//*--------------- 泛型解析 -----------------*//*
        Gson gson = new Gson();
        return gson.fromJson(jsonString,new TypeToken<List<String>>(){}.getType());
    }*/
}
