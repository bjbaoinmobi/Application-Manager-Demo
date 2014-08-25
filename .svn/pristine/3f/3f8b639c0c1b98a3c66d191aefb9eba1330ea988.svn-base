package com.itheima.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;

/**
 * 系统信息的工具类
 * @author Administrator
 *
 */
public class SystemInfoUtils {
	/**
	 * 获取正在运行的进程数量
	 * @param context 上下文
	 * @return
	 */
	public static int getRunningProcessCount(Context context){
		//类似电脑的进程管理器
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}
	/**
	 * 获取可用的内存空间
	 * @param context 上下文
	 * @return long byte内存空间大小
	 */
	public static long getAvailRam(Context context){
		//类似电脑的进程管理器
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}
	/**
	 * 获取总的内存空间
	 * @param context 上下文
	 * @return long byte内存空间大小
	 */
	public static long getTotalRam(Context context){
//		//类似电脑的进程管理器
//		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		MemoryInfo outInfo = new MemoryInfo();
//		am.getMemoryInfo(outInfo);
//		return outInfo.totalMem;  从4.0之后才有的api
		try {
			File file = new File("/proc/meminfo");
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			//MemTotal:         513000 kB
			String result = br.readLine();
			//字符串 String 一组字符串起来
			char[] array = result.toCharArray();
			StringBuilder sb = new StringBuilder();
			for(char c : array){
				if(c>='0'&&c<='9'){
					sb.append(c);
				}
			}
			return Integer.parseInt(sb.toString())*1024;//转化单位为byte
		} catch (Exception e) {
			e.printStackTrace();
			//can't reach
			return 0;
		}
	}
}
