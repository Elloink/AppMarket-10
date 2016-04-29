package com.buaa.tezlikai.appmarket.protocol;

import com.buaa.tezlikai.appmarket.base.BaseProtocol;

import java.util.List;

/**
 * HotProtocol: 推荐模块
 */
public class HotProtocol extends BaseProtocol<List<String>> {

	@Override
	public String getInterfaceKey() {

		return "hot";
	}

	/*@Override
	public List<String> parseJson(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, new TypeToken<List<String>>() {
		}.getType());
	}*/

}
