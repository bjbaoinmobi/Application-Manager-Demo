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
				tv_scan_status.setText("����ɨ�裺"
						+ info.applicationInfo.loadLabel(pm));
				break;
			case FINFISH:
				tv_scan_status.setText("ɨ�����");
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
						System.out.println("���Ӧ�ó���Ļ���" + cacheinfo.packname);
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
		// ɨ��ÿ��Ӧ�ó���Ļ�����Ϣ��
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

		// ����Ĵ�����������һ�����߳�����ִ�еģ����Բ���ֱ�Ӹ���ui
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			long cachesize = pStats.cacheSize;
			if (cachesize > 0) {
				try {
					// TextView tv = new TextView(getApplicationContext());
					// tv.setText("�����С��"
					// + Formatter.formatFileSize(getApplicationContext(),
					// cachesize)+"---"+pm.getApplicationInfo(pStats.packageName,
					// 0).loadLabel(pm));
					// tv.setTextColor(Color.BLACK);
					// ll_container.addView(tv,0);
					Message msg = Message.obtain();
					msg.what = ADD_CACHE_INFO;
					CacheInfo cacheInfo = new CacheInfo();
					cacheInfo.text = "�����С��"
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
	 * ȫ������
	 * @param view
	 */
	public void cleanAll(View view){
		Method[] methods = PackageManager.class.getMethods();
		for(Method method : methods){
			if("freeStorageAndNotify".equals(method.getName())){
				try {
					method.invoke(pm, Integer.MAX_VALUE,new MyDataObserver());
					Toast.makeText(this, "�������", 0).show();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(this, "����ʧ��", 0).show();
				}
				return;
			}
		}
		
		
	}
	
}
