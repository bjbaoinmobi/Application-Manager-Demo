package com.itheima.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceStatusUtils {
	
	/**
	 * У��ĳһ�������Ƿ��������е�
	 * @param context ������
	 * @param service ��ҪУ��ķ���
	 * @return
	 */
	public  static boolean isRunningService(Context context,String serviceName){
		
		//�����Թ���Activity �����Թ���Service
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		//�õ����еķ����б�
		List<RunningServiceInfo> infos = am.getRunningServices(100);
		for(RunningServiceInfo info : infos){
			//���������
			String name = info.service.getClassName();
			if(serviceName.equals(name)){
				return true;
			}
		}
		return false;
	}

}
