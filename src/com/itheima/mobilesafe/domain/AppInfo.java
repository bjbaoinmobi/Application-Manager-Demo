package com.itheima.mobilesafe.domain;

import android.graphics.drawable.Drawable;

public class AppInfo {
	
	private Drawable icon;
	private String name;
	private String packageName;
	private boolean isUser;//true :用户程序，false 系统自动应用
	private boolean isRom ;//true:安装手机内部，false:安装在sd卡上；
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public boolean isUser() {
		return isUser;
	}
	public void setUser(boolean isUser) {
		this.isUser = isUser;
	}
	public boolean isRom() {
		return isRom;
	}
	public void setRom(boolean isRom) {
		this.isRom = isRom;
	}
	@Override
	public String toString() {
		return "AppInfo [name=" + name + ", isUser=" + isUser + ", isRom="
				+ isRom + "]";
	}
}
