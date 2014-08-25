package com.itheima.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceStatusUtils {
	
	/**
	 * 校验某一个服务是否是运行中的
	 * @param context 上下文
	 * @param service 需要校验的服务
	 * @return
	 */
	public  static boolean isRunningService(Context context,String serviceName){
		
		//它可以管理Activity 还可以管理Service
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//得到运行的服务列表
		List<RunningServiceInfo> infos = am.getRunningServices(100);
		for(RunningServiceInfo info : infos){
			//服务的名称
			String name = info.service.getClassName();
			if(serviceName.equals(name)){
				return true;
			}
		}
		return false;
	}

}
