package com.itheima.mobilesafe.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.receiver.MyWidget;
import com.itheima.mobilesafe.utils.SystemInfoUtils;

public class UpdateWidgetService extends Service {
	// 注册两个广播接受者 屏幕锁屏 屏幕解锁（特殊的广播事件） 只能用代码注册

	private class ScreenOnReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			startTimer();
		}
	}

	private class ScreenOffReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("屏幕锁屏了。");
			Intent i = new Intent();
			i.setAction("com.itheima.mobilesafe.killall");
			sendBroadcast(i);
			stopTimer();
		}
	}

	// 开启一个定时器 ，定时的更新内容
	private Timer timer;
	private TimerTask task;
	private AppWidgetManager awm;
	private ScreenOnReceiver onReceiver;
	private ScreenOffReceiver offReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		onReceiver = new ScreenOnReceiver();
		registerReceiver(onReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
		
		offReceiver = new ScreenOffReceiver();
		registerReceiver(offReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		
		awm = AppWidgetManager.getInstance(this);
		startTimer();
		super.onCreate();
	}

	private void startTimer() {
		if (timer == null && task == null) {
			timer = new Timer();
			task = new TimerTask() {
				@Override
				public void run() {
					System.out.println("更新widget。。。");
					// 更新远程桌面里面显示的内容。 更新另外一个进程里面的view显示的内容
					ComponentName provider = new ComponentName(
							getApplicationContext(), MyWidget.class);
					RemoteViews views = new RemoteViews(getPackageName(),
							R.layout.process_widget);
					views.setTextViewText(
							R.id.process_count,
							"正在运行的进程："
									+ SystemInfoUtils
											.getRunningProcessCount(getApplicationContext()));
					views.setTextViewText(
							R.id.process_memory,
							"可用内存："
									+ Formatter
											.formatFileSize(
													getApplicationContext(),
													SystemInfoUtils
															.getAvailRam(getApplicationContext())));
					// 延期意图是一种特殊的意图，是让另外一个应用程序执行的意图。
					// 获取一个自定义广播的 延期意图。
					Intent intent = new Intent();
					intent.setAction("com.itheima.mobilesafe.killall");
					PendingIntent pendingIntent = PendingIntent.getBroadcast(
							getApplicationContext(), 0, intent,
							PendingIntent.FLAG_UPDATE_CURRENT);
					views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
					awm.updateAppWidget(provider, views);
				}
			};
			timer.schedule(task, 0, 5000);
		}
	}

	@Override
	public void onDestroy() {
		stopTimer();
		unregisterReceiver(offReceiver);
		unregisterReceiver(onReceiver);
		offReceiver = null;
		onReceiver = null;
		super.onDestroy();
	}

	private void stopTimer() {
		if (timer != null && task != null) {
			timer.cancel();
			task.cancel();
			timer = null;
			task = null;
		}
	}
}
