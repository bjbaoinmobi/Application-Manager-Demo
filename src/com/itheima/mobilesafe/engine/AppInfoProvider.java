package com.itheima.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.itheima.mobilesafe.domain.AppInfo;

public class AppInfoProvider {

	/**
	 * �õ��ֻ��ﰲװ��ϵͳӦ�ú��û���װ��Ӧ�õ���Ϣ
	 * @param context
	 * @return
	 */
	public static List<AppInfo> getAllAppInfos(Context context) {
		List<AppInfo>  appInfos = new ArrayList<AppInfo>();
		// �õ���������
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packInfos = pm.getInstalledPackages(0);
		for(PackageInfo info : packInfos){
			//ĳһ��APK����Ϣ
			AppInfo appInfo = new AppInfo();
			String packageName = info.packageName;
			Drawable icon = info.applicationInfo.loadIcon(pm);
			String name = info.applicationInfo.loadLabel(pm).toString();
			int uid = info.applicationInfo.uid;
			appInfo.setIcon(icon);
			appInfo.setName(name+uid);
			appInfo.setPackageName(packageName);
			//Ӧ�ó���ı�־�������������־�����
			int flags = info.applicationInfo.flags;//Ӧ�ó��򽻵Ĵ��⿨
			
			if((flags & ApplicationInfo.FLAG_SYSTEM) ==0){
				//�û�����
				appInfo.setUser(true);
			}else{
				//ϵͳ����
				appInfo.setUser(false);
			}
			
			if((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) ==0){
				//�ֻ��ڲ��洢�ռ�
				appInfo.setRom(true);
			}else{
				//�ⲿ�洢�ռ�
				appInfo.setRom(false);
			}
			
			appInfos.add(appInfo);
			
		}
		return appInfos;

	}

}