package com.buaa.tezlikai.appmarket.protocol;

import com.buaa.tezlikai.appmarket.base.BaseProtocol;
import com.buaa.tezlikai.appmarket.bean.AppInfoBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * AppProtocol
 */
public class AppProtocol extends BaseProtocol<List<AppInfoBean>> {

	@Override
	public String getInterfaceKey() {
		return "app";
	}

	@Override
	public List<AppInfoBean> parseJson(String jsonString) {
		Gson gson = new Gson();

		/*=============== 泛型解析（对于List<AppInfoBean>的解析形式） ===============*/
		return gson.fromJson(jsonString, new TypeToken<List<AppInfoBean>>() {
		}.getType());

	}
}
