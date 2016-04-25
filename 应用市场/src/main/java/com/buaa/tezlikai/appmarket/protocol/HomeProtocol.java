package com.buaa.tezlikai.appmarket.protocol;

import com.buaa.tezlikai.appmarket.base.BaseProtocol;
import com.buaa.tezlikai.appmarket.bean.HomeBean;
import com.google.gson.Gson;

/**
 * HomeProtocol
 */
public class HomeProtocol extends BaseProtocol<HomeBean> {

	@Override
	public String getInterfaceKey() {
		return "home";
	}

	@Override
	public HomeBean parseJson(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, HomeBean.class);
	}

}
