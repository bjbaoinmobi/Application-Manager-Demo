package com.itheima.mobilesafe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CleanCacheActivity extends Activity {
	protected static final int SCANING = 1;
	protected static final int FINFISH = 2;
	private static final int ADD_CACHE_INFO = 3;
	private TextView tv_scan_status;
	private ProgressBar pb;
	private PackageManager pm;
	private LinearLayout ll_container;

	private Handler hanlder = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCANING:
				PackageInfo info = (PackageInfo) msg.obj;
				tv_scan_status.setText("正在扫描："
						+ info.applicationInfo.loadLabel(pm));
				break;
			case FINFISH:
				tv_scan_status.setText("扫描完毕");
				break;
			case ADD_CACHE_INFO:
				final CacheInfo cacheinfo = (CacheInfo) msg.obj;
				TextView tv = new TextView(getApplicationContext());
				tv.setText(cacheinfo.text);
				tv.setTextColor(Color.BLACK);
				ll_container.addView(tv, 0);
				tv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						System.out.println("清除应用程序的缓存" + cacheinfo.packname);
						// mPm.deleteApplicationCacheFiles(packageName,
						// mClearCacheObserver);
						try {
							Method method = PackageManager.class.getMethod(
									"deleteApplicationCacheFiles",
									String.class, IPackageDataObserver.class);
							method.invoke(pm, cacheinfo.packname,new MyDataObserver());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clean_cache);
		tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		pb = (ProgressBar) findViewById(R.id.progressBar1);
		// 扫描每个应用程序的缓存信息。
		scanCache();
	}

	private void scanCache() {
		new Thread() {
			public void run() {
				pm = getPackageManager();
				List<PackageInfo> infos = pm.getInstalledPackages(0);
				pb.setMax(infos.size());
				int total = 0;
				for (PackageInfo info : infos) {
					String packname = info.packageName;
					total++;
					pb.setProgress(total);
					try {
						Method method = PackageManager.class.getMethod(
								"getPackageSizeInfo", String.class,
								IPackageStatsObserver.class);
						method.invoke(pm, packname, new MyObserver());
						Message msg = Message.obtain();
						msg.what = SCANING;
						msg.obj = info;
						hanlder.sendMessage(msg);
						Thread.sleep(50);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				Message msg = Message.obtain();
				msg.what = FINFISH;
				hanlder.sendMessage(msg);
			};
		}.start();
	}

	private class MyObserver extends IPackageStatsObserver.Stub {

		// 下面的代码是在另外一个子线程里面执行的，所以不能直接更新ui
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			long cachesize = pStats.cacheSize;
			if (cachesize > 0) {
				try {
					// TextView tv = new TextView(getApplicationContext());
					// tv.setText("缓存大小："
					// + Formatter.formatFileSize(getApplicationContext(),
					// cachesize)+"---"+pm.getApplicationInfo(pStats.packageName,
					// 0).loadLabel(pm));
					// tv.setTextColor(Color.BLACK);
					// ll_container.addView(tv,0);
					Message msg = Message.obtain();
					msg.what = ADD_CACHE_INFO;
					CacheInfo cacheInfo = new CacheInfo();
					cacheInfo.text = "缓存大小："
							+ Formatter.formatFileSize(getApplicationContext(),
									cachesize)
							+ "---"
							+ pm.getApplicationInfo(pStats.packageName, 0)
									.loadLabel(pm);
					cacheInfo.packname = pStats.packageName;
					msg.obj = cacheInfo;
					hanlder.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	class CacheInfo {
		String text;
		String packname;
	}
	
	private class MyDataObserver extends IPackageDataObserver.Stub{
		@Override
		public void onRemoveCompleted(String packageName, boolean succeeded)
				throws RemoteException {
			System.out.println(packageName+succeeded);
		}
	}
	/**
	 * 全部清理
	 * @param view
	 */
	public void cleanAll(View view){
		Method[] methods = PackageManager.class.getMethods();
		for(Method method : methods){
			if("freeStorageAndNotify".equals(method.getName())){
				try {
					method.invoke(pm, Integer.MAX_VALUE,new MyDataObserver());
					Toast.makeText(this, "清理完毕", 0).show();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(this, "清理失败", 0).show();
				}
				return;
			}
		}
		
		
	}
	
}
