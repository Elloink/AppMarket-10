package com.buaa.tezlikai.appmarket.protocol;

import com.buaa.tezlikai.appmarket.base.BaseProtocol;
import com.buaa.tezlikai.appmarket.bean.CategoryInfoBean;
import com.buaa.tezlikai.appmarket.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * CategoryProtocol :分类模块 信息解析
 */
public class CategoryProtocol extends BaseProtocol<List<CategoryInfoBean>> {

	@Override
	public String getInterfaceKey() {
		return "category";
	}

	@Override
	public List<CategoryInfoBean> parseJson(String jsonString) {
		/*=============== android sdk 自带的json解析 ===============*/
		List<CategoryInfoBean> categoryInfoBeans = new ArrayList<CategoryInfoBean>();
		try {
			JSONArray rootJsonArray = new JSONArray(jsonString);

			// 遍历jsonArray
			for (int i = 0; i < rootJsonArray.length(); i++) {

				JSONObject itemJsonObject = rootJsonArray.getJSONObject(i);

				String title = itemJsonObject.getString("title");
				CategoryInfoBean titleBean = new CategoryInfoBean();
				titleBean.title = title;
				titleBean.isTitle = true;
				categoryInfoBeans.add(titleBean);

				JSONArray infosJsonArray = itemJsonObject.getJSONArray("infos");
				// 遍历infosJsonArray
				for (int j = 0; j < infosJsonArray.length(); j++) {
					JSONObject infoJsonObject = infosJsonArray.getJSONObject(j);
					String name1 = infoJsonObject.getString("name1");
					String name2 = infoJsonObject.getString("name2");
					String name3 = infoJsonObject.getString("name3");
					String url1 = infoJsonObject.getString("url1");
					String url2 = infoJsonObject.getString("url2");
					String url3 = infoJsonObject.getString("url3");

					CategoryInfoBean infoBean = new CategoryInfoBean();
					infoBean.name1 = name1;
					infoBean.name2 = name2;
					infoBean.name3 = name3;
					infoBean.url1 = url1;
					infoBean.url2 = url2;
					infoBean.url3 = url3;

					categoryInfoBeans.add(infoBean);
				}
			}
			LogUtils.printList(categoryInfoBeans);
			return categoryInfoBeans;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
