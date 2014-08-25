package com.itheima.mobilesafe.service;

import java.util.List;

import com.itheima.mobilesafe.EnterPwdActivity;
import com.itheima.mobilesafe.db.dao.ApplockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * 不停的监视手机里面正在运行的程序信息。 看门狗逻辑一般都是死循环。
 * 
 * @author Administrator
 * 
 */
public class WatchDogService extends Service {
	private ActivityManager am;
	private boolean flag;
	private ApplockDao dao;
	private InnerReceiver innerReceiver;
	private ScreenOffReceiver offReceiver;
	private DBChangeReceiver dbChangeReceiver;
	
	private List<String> lockPacknames;
	/**
	 * 临时停止保护的包名
	 */
	private String tempStopProtectPackname;
	
	private Intent intent;

	private class InnerReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			tempStopProtectPackname = intent.getStringExtra("packname");
			System.out.println("哈哈哈哈，我是看门狗，我接收到了主人的指令，停止保护："
					+ tempStopProtectPackname);
		}
	}

	private class ScreenOffReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			tempStopProtectPackname = null;
		}
	}
	
	private class DBChangeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			 System.out.println("发现数据库的内容变化了");
			 lockPacknames = dao.findAll();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// 注册自定义的广播接受者
		innerReceiver = new InnerReceiver();
		registerReceiver(innerReceiver, new IntentFilter(
				"com.itheima.mobilesafe.stopprotect"));

		offReceiver = new ScreenOffReceiver();
		registerReceiver(offReceiver,
				new IntentFilter(Intent.ACTION_SCREEN_OFF));
		
		dbChangeReceiver = new DBChangeReceiver();
		registerReceiver(dbChangeReceiver, new IntentFilter("com.itheima.mobilesafe.dbchanged"));
		
		intent = new Intent(WatchDogService.this,
				EnterPwdActivity.class);
		// 服务是没有任务栈信息的。要在服务里面开启activity必须指定这个特殊的信息。
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		dao = new ApplockDao(this);
		//在服务创建的时候 初始化了 要锁定的应用程序的集合
		lockPacknames = dao.findAll();
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		flag = true;
		new Thread() {
			public void run() {
				while (flag) {
					// 监视获取当前用户正要操作的应用程序。
					// 返回当前正在运行的任务栈信息。 把任务栈信息 排序 最近使用的在最前面
					List<RunningTaskInfo> infos = am.getRunningTasks(1);
					String packname = infos.get(0).topActivity.getPackageName();
					// 判断这个应用程序是否需要被保护
					//if (dao.find(packname)) {// 需要被保护，立刻弹出来一个输入密码的界面。
					//把查询数据库 的操作改为 查询内存。
					 if(lockPacknames.contains(packname)){
						// 判断当前应用程序是否需要临时的停止保护。
						if (packname.equals(tempStopProtectPackname)) {
							// 需要临时停止保护
						} else {
							intent.putExtra("packname", packname);
							startActivity(intent);
						}
					} else {// 不需要被保护

					}
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		flag = false;
		unregisterReceiver(innerReceiver);
		innerReceiver = null;
		unregisterReceiver(offReceiver);
		offReceiver = null;
		unregisterReceiver(dbChangeReceiver);
		dbChangeReceiver = null;
		super.onDestroy();
	}
}
