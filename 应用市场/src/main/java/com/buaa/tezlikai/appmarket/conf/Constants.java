package com.buaa.tezlikai.appmarket.conf;


import com.buaa.tezlikai.appmarket.utils.LogUtils;

/**
 * 全局变量
 */
public class Constants {
	public static final int PAGESIZE = 20;//每页返回20条数据
	public static final int	PROTOCOLTIMEOUT	= 5 * 60 * 1000;		// 5分钟

	public static final int	DEBUGLEVEL	= LogUtils.LEVEL_ALL;//LEVEL_ALL,显示所有的日子,OFF:关闭日志的显示
	public static final class URLS {//资源相关
		public static final String	BASEURL			= "http://192.168.3.92:8080/GooglePlayServer/";
		// http://localhost:8080/GooglePlayServer/image?name=

		public static final String	IMAGEBASEURL	= BASEURL + "image?name=";
		// http://localhost:8080/GooglePlayServer/download
		public static final String DOWNLOADBASEURL = BASEURL + "download";
		public static final String HTTP = "HTTP";
	}

	public static final class PAY {//支付相关

	}

	public static final class REQ {//请求相关

	}

	public static final class RES {//响应相关

	}
}
