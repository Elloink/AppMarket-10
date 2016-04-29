package com.buaa.tezlikai.appmarket.protocol;

import com.buaa.tezlikai.appmarket.base.BaseProtocol;
import com.buaa.tezlikai.appmarket.bean.SubjectInfoBean;

import java.util.List;

/**
 * SubjectProtocol :专题
 */
public class SubjectProtocol extends BaseProtocol<List<SubjectInfoBean>> {

	@Override
	public String getInterfaceKey() {
		return "subject";
	}

	/*@Override
	public List<SubjectInfoBean> parseJson(String jsonString) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, new TypeToken<List<SubjectInfoBean>>() {
		}.getType());
	}*/
}
