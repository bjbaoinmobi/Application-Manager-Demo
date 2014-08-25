package com.itheima.mobilesafe;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.os.Bundle;

public class TrafficManagerActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//1.利用包管理器 获取每个应用程序的uid
		//2.new 文件对象  File("/proc/uid_stats/10086");
		//3.读取tcp_rcv 下载的流量    读取tcp_snd 上传的流量
		TrafficStats.getUidRxBytes(10086);//下载的总流量 单位byte  包括wifi 包括3g
		TrafficStats.getUidTxBytes(10086);//上传的总流量 单位byte  包括wifi 包括3g
		
		TrafficStats.getMobileRxBytes();//手机2g/3g/4g 下载的总流量
		TrafficStats.getMobileTxBytes();//手机2g/3g/4g 上传的总流量
		
		TrafficStats.getTotalRxBytes();// 手机总的下载的流量  wifi+2g/3g/4g
		TrafficStats.getTotalTxBytes();// 手机总的上传的流量  wifi+2g/3g/4g
		
		//记录每个应用程序 wifi 和 3g 分别产生的流量是多少。（大概的估计数据）
		//费电。 
		//原理 1.判断手机的网络状况 wifi 3g
//		ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//		cm.getActiveNetworkInfo().getType(); //获取手机的网络类型。
//		16点49 获取一次 网络类型3g 30000byte
//		16点54 获取一次网络类型3g  33000byte
		
		//流量自动校验功能。
		//llcx
		setContentView(R.layout.activity_traffic);
	}
}
