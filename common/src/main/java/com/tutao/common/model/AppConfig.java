package com.tutao.common.model;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.pm.ApplicationInfo;

import com.app.controller.IFunctionRouter;
import com.app.util.Util;
import com.igexin.sdk.GTIntentService;

/**
 * @ClassName: AppConfig
 * @Description: TODO(产品配置类)
 * @author guopeng
 * @date 2014 2014年10月23日 下午8:39:30
 *
 */
public class AppConfig {
	//产品标识
	public String xCode = "";
	//打包时间
	public String buildAt = "";
	//服务器IP,base64编码
	public String ip = "";
	public String jsServerAddress;

	public String imagesAddress;
	//后台service类
	public Class<?> service = null;

	//后台service类
	public Class<? extends Service> gt_service = null;

	public Class<? extends Service> msgPushService = null;
	//快捷方式启动类名
	public String shortcutClassName = "";
	//应用图标资源ID
	public int iconResourceId = -1;
	//友盟统计
	public String umengKey = "";
	//渠道
	public String channel = "";
	//支持的SDK
	public String sdks = "";//mm,alipay

	public Class<? extends Activity> startActivity = null;

	public Class<? extends Activity> weexActivity = null;

	public Class<? extends Activity> mainActivity = null;

	public int notificationCount = 1;

	public int notificationIcon = -1;

	public int notificationImg = -1;
	/**
	 * 是否解析json,根据json文件显示tab
	 */
	private boolean isDynamicTab=false;
	public boolean isDynamicTab() {
		return isDynamicTab;
	}
	public void setDynamicTab(boolean dynamicTab) {
		isDynamicTab = dynamicTab;
	}
	/**
	 * 作用:产品功能路由
	 */
	public IFunctionRouter appFunctionRouter = null;
	/**
	 * 地理经纬度
	 */
	public String latitude_longitude = "";
	//是否使用其它定位SDK
	public boolean useOtherLocationSDK = false;
	/*
	* api版本
	 */
	public String api_version = "";

	public String content_types = "";//支持的消息类型

	public int version_code = 0;
	public String version_name = "";

	public QQConfig qqConfig;//qq登录需要配置信息
	public WeChatConfig weChatConfig;//微信需要配置信息

	//debug模式
	private boolean debug = false;
	private Application application = null;

	private boolean useZip = true;

	public String themeVersion = "";

	public String mainPage = "";
	public boolean isColdBoot = false;//是否为冷启动
	public boolean httpdns = false;
	//请求解密密码
	public String key="";
	//是否需要解密
	public boolean isEncryption=false;
	public static class QQConfig {
		public String appid;

		public QQConfig(String appid) {
			this.appid = appid;
		}
	}

	public static class WeChatConfig {
		public WeChatConfig(String appid, String secret) {
			this.appid = appid;
			this.secret = secret;
		}

		public String appid;
		public String secret;
	}

	public AppConfig(Application application) {
		this.application = application;
		version_code = Util.getVersionCode(application.getApplicationContext());
		version_name = Util.getVersionName(application.getApplicationContext());
	}

	public boolean getDebug() {
		return this.debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;

		if (application != null && this.debug) {
			try {
				ApplicationInfo info = application.getApplicationInfo();
				this.debug = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
			} catch (Exception e) {
			}
		}
	}

	public void setUseZip(boolean use) {
		this.useZip = use;
	}

	public boolean isUseZip() {
		return this.useZip;
	}
}
