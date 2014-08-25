package com.itheima.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.domain.TaskInfo;

public class TaskInfoProvider {
	/**
	 * 获取手机里面正在运行的进程信息
	 * @param context
	 * @return
	 */
	public static List<TaskInfo> getTaskInfos(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();
		List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		for(RunningAppProcessInfo processInfo : processInfos){
			TaskInfo taskInfo = new TaskInfo();
			String packname = processInfo.processName;
			taskInfo.setPackname(packname);
			long memsize = am.getProcessMemoryInfo(new int[]{processInfo.pid})[0].getTotalPrivateDirty()*1024;
			taskInfo.setMemsize(memsize);
			try {
				Drawable icon = pm.getPackageInfo(packname, 0).applicationInfo.loadIcon(pm);
				taskInfo.setIcon(icon);
				String name = pm.getPackageInfo(packname, 0).applicationInfo.loadLabel(pm).toString();
				taskInfo.setName(name);
				 if((pm.getPackageInfo(packname, 0).applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==0){
					 //用户进程
					 taskInfo.setUserTask(true);
				 }else{
					 //系统进程
					 taskInfo.setUserTask(false);
				 }
			} catch (Exception e) {
				e.printStackTrace();
				taskInfo.setName(packname);
				taskInfo.setIcon(context.getResources().getDrawable(R.drawable.ic_default));
			}
			taskInfos.add(taskInfo);
		}
		return taskInfos;
	}
}
