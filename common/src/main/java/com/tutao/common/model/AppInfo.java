package com.tutao.common.model;

public class AppInfo {

	private String name;//应用程序标签
	private String pkg;//应用程序包名
	private String code;//应用程序版本号
	public String getAppName() {
		return name;
	}
	public void setAppName(String appName) {
		this.name = appName;
	}
	public String getPkg() {
		return pkg;
	}
	public void setPkg(String pkg) {
		this.pkg = pkg;
	}
	public String getVersionCode() {
		return code;
	}
	public void setVersionCode(String versionCode) {
		this.code = versionCode;
	}

}
