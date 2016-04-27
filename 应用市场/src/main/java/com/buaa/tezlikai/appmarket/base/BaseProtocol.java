package com.buaa.tezlikai.appmarket.base;

import com.buaa.tezlikai.appmarket.conf.Constants;
import com.buaa.tezlikai.appmarket.utils.FileUtils;
import com.buaa.tezlikai.appmarket.utils.IOUtils;
import com.buaa.tezlikai.appmarket.utils.LogUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * BaseProtocol
 */
public abstract class BaseProtocol<T> {
	public T loadData(int index) throws Exception {

		/*=============== 读取本地缓存 ===============*/
		T localData = getDataFromLocal(index);
		if (localData != null) {
			LogUtils.sf("###读取本地文件-->" + getCacheFile(index).getAbsolutePath());
			return localData;
		}

		/*=============== 发送网络请求 ===============*/
		String jsonString = getDataFromNet(index);

		/*=============== json解析 ===============*/
		T homeBean = parseJson(jsonString);
		return homeBean;
	}

	private T getDataFromLocal(int index) {
		/*
			//读取本地文件
			if(文件存在){
				//读取插入时间
				//判断是否过期
				if(未过期){
					//读取缓存内容
					//Json解析解析内容
					if(不为null){
						//返回并结束
					}	
				}
			}
		 */
		File cacheFile = getCacheFile(index);
		if (cacheFile.exists()) {// 文件存在
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(cacheFile));

				// //读取插入时间
				String timeTimeMillis = reader.readLine();
				if (System.currentTimeMillis() - Long.parseLong(timeTimeMillis) < Constants.PROTOCOLTIMEOUT) {

					// 读取缓存内容
					String jsonString = reader.readLine();

					// Json解析解析内容
					return parseJson(jsonString);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.close(reader);
			}
		}
		return null;
	}

	public File getCacheFile(int index) {
		String dir = FileUtils.getDir("json");// sdcard/Android/data/包名/json
		//如果是详情页interfacekey + "." + 包名
		Map<String,String> extraParmas = getExtraParmas();
		String name = "";
		if (extraParmas == null){
			name = getInterfaceKey() + "." + index;// interfacekey+"."+index
		}else {//详情页,防止加载缓存的时候只读取一个文件
			for (Map.Entry<String,String> info : extraParmas.entrySet()){
				String key = info.getKey();//packageName
				String value = info.getValue();
				name = getInterfaceKey() + "." + value;
			}
		}
		File cacheFile = new File(dir, name);
		return cacheFile;
	}

	/**
	 * 返回额外的参数
	 * 默认在基类里面返回的是null，但是如果子类需要返回额外的参数的时候，复写该方法
	 * @return
	 */
	public Map<String ,String>getExtraParmas(){
		return null;
	}
	public String getDataFromNet(int index) throws HttpException, IOException {
		HttpUtils httpUtils = new HttpUtils();
		// http://localhost:8080/GooglePlayServer/home?index=0
		String url = Constants.URLS.BASEURL + getInterfaceKey();
		RequestParams parmas = new RequestParams();

		Map<String,String> extraParmas = getExtraParmas();
		if (extraParmas == null){
			parmas.addQueryStringParameter("index", index + "");
		}else {//子类覆写了getExtraParmas()方法，返回了额外的参数
			//取出额外的参数
			for (Map.Entry<String,String> info :extraParmas.entrySet()){
				String name = info.getKey();
				String value = info.getValue();
				parmas.addQueryStringParameter(name,value);
			}
		}

		ResponseStream responseStream = httpUtils.sendSync(HttpMethod.GET, url, parmas);
//		LogUtils.i(Constants.URLS.HTTP,responseStream.getRequestUrl());
		// 读取网络数据
		String jsonString = responseStream.readString();
		/*--------------- 保存网络数据到本地 ---------------*/
		File cacheFile = getCacheFile(index);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(cacheFile));
			writer.write(System.currentTimeMillis() + "");// 第一行写入插入时间
			writer.write("\r\n");// 换行
			writer.write(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.close(writer);
		}
		/*	
			if(网络数据加载成功){
				//加载网络数据
				//保存网络数据到本地
				//Json解析内容
				//返回并结束
		
			}else{
				//返回null
			}*/

		return jsonString;
	}

	public abstract String getInterfaceKey();

	public abstract T parseJson(String jsonString);

}
