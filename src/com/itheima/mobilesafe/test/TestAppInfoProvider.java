package com.itheima.mobilesafe.test;

import java.util.List;

import android.test.AndroidTestCase;

import com.itheima.mobilesafe.domain.AppInfo;
import com.itheima.mobilesafe.engine.AppInfoProvider;

public class TestAppInfoProvider extends AndroidTestCase {
	
	public void testgetAllAppInfos(){
		 List<AppInfo> appInfos = AppInfoProvider.getAllAppInfos(getContext());
		 
		 for(AppInfo appinfo : appInfos){
			 System.out.println(appinfo.toString());
		 }
	}

}
