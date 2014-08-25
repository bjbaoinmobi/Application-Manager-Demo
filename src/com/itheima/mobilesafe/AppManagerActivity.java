package com.itheima.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.db.dao.ApplockDao;
import com.itheima.mobilesafe.domain.AppInfo;
import com.itheima.mobilesafe.engine.AppInfoProvider;
import com.itheima.mobilesafe.utils.DensityUtil;

public class AppManagerActivity extends Activity implements OnClickListener {

	private static final String TAG = "AppManagerActivity";
	private TextView tv_avail_rom;
	private TextView tv_avail_sd;

	private ListView list_app_item;
	private LinearLayout ll_loading;
	private List<AppInfo> infos;

	private List<AppInfo> userinfos;// 用户自己安装程序的集合
	private List<AppInfo> systeminfos;// 系统应用程序的集合

	private MyAdapter adapter;

	private TextView tv_item;

	private PopupWindow window;

	private LinearLayout ll_uninstall;
	private LinearLayout ll_start;
	private LinearLayout ll_share;

	private AppInfo info;
	
	/**
	 * 程序锁数据库的dao
	 */
	private ApplockDao dao;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ll_loading.setVisibility(View.GONE);
			adapter = new MyAdapter();
			list_app_item.setAdapter(adapter);

		};
	};

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// 加上自定义TextView (用户程序)+1 ，系统程序+1
			return userinfos.size() + 1 + systeminfos.size() + 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			AppInfo info = null;
			if (position == 0) {
				TextView textView = new TextView(getApplicationContext());
				textView.setTextSize(20);
				textView.setTextColor(Color.WHITE);
				textView.setBackgroundColor(Color.GRAY);
				textView.setText("User Applications(" + userinfos.size() + ")");
				return textView;
			} else if (position == (userinfos.size() + 1)) {
				TextView textView = new TextView(getApplicationContext());
				textView.setTextSize(20);
				textView.setTextColor(Color.WHITE);
				textView.setBackgroundColor(Color.GRAY);
				textView.setText("System Applications(" + systeminfos.size() + ")");
				return textView;
			} else if (position <= userinfos.size()) {
				// 用户程序显示的区域
				int newposition = position - 1;
				info = userinfos.get(newposition);
			} else {
				// 系统应用
				int newposition = position - 1 - userinfos.size() - 1;
				info = systeminfos.get(newposition);
			}

			View view = null;
			ViewHolder viewHolder = null;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(AppManagerActivity.this,
						R.layout.list_app_item, null);

				// 容器，保存对应关系
				viewHolder = new ViewHolder();
				viewHolder.iv_icon = (ImageView) view
						.findViewById(R.id.iv_icon);
				viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				viewHolder.tv_location = (TextView) view
						.findViewById(R.id.tv_location);
				viewHolder.iv_status = (ImageView) view.findViewById(R.id.iv_lock_status);
				//每个应用程序锁定的状态都是存放在数据库里面的
				//TODO:查询数据库 获取每个应用程序锁定的状态。
				view.setTag(viewHolder);// 把我们对应的层次关系真正的保存起来；
			}

			// 得到某一个应用的对象
			//
			// if(position < userinfos.size()){
			// //用户程序
			// info = userinfos.get(position);
			// }else{
			// int newPosition = position - userinfos.size();
			// info = systeminfos.get(newPosition);
			// }
			viewHolder.iv_icon.setImageDrawable(info.getIcon());
			viewHolder.tv_name.setText(info.getName());
			if (info.isRom()) {
				// 安装在手机内部
				viewHolder.tv_location.setText("Internal Storage");
			} else {
				// 安装在外部存储
				viewHolder.tv_location.setText("External Storage");
			}
			if(dao.find(info.getPackageName())){
				viewHolder.iv_status.setImageResource(R.drawable.lock);
			}else{
				viewHolder.iv_status.setImageResource(R.drawable.unlock);
			}
			return view;
		}

		@Override
		public Object getItem(int position) {
			AppInfo info = null;
			if (position == 0) {
				return null;
			} else if (position == (userinfos.size() + 1)) {
				return null;
			} else if (position <= userinfos.size()) {
				// 用户程序显示的区域
				int newposition = position - 1;
				info = userinfos.get(newposition);
			} else {
				// 系统应用
				int newposition = position - 1 - userinfos.size() - 1;
				info = systeminfos.get(newposition);
			}
			return info;

		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);
		dao  = new ApplockDao(this);
		tv_avail_rom = (TextView) findViewById(R.id.tv_avail_rom);
		tv_avail_sd = (TextView) findViewById(R.id.tv_avail_sd);
		tv_item = (TextView) findViewById(R.id.tv_item);
		tv_avail_rom.setText("Int. Storage: "
				+ getAvailSpace(Environment.getDataDirectory()
						.getAbsolutePath()));
		tv_avail_sd.setText("Ext. Storage: "
				+ getAvailSpace(Environment.getExternalStorageDirectory()
						.getAbsolutePath()));
		list_app_item = (ListView) findViewById(R.id.list_app_item);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);

		fillData();

		list_app_item.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				dimissPopupWindow();

				if (userinfos != null && systeminfos != null) {
					if (firstVisibleItem > userinfos.size()) {
						// 系统程序
						tv_item.setText("System Applications(" + systeminfos.size() + ")");
					} else {
						// 用户程序
						tv_item.setText("User Applications(" + userinfos.size() + ")");
					}
				}

			}
		});

		//设置条目的点击事件  长点击事件也是一种特殊的点击事件
		list_app_item.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Object obj = list_app_item.getItemAtPosition(position);
				if (obj != null) {
					info = (AppInfo) obj;

					dimissPopupWindow();
					System.out.println("getPackageName=="
							+ info.getPackageName());
					// TextView contentView = new
					// TextView(getApplicationContext());
					// contentView.setText(info.getPackageName());

					// contentView.setTextColor(Color.RED);
					View contentView = View.inflate(AppManagerActivity.this,
							R.layout.popupwindow_app_item, null);
					ll_uninstall = (LinearLayout) contentView
							.findViewById(R.id.ll_uninstall);
					ll_start = (LinearLayout) contentView
							.findViewById(R.id.ll_start);
					ll_share = (LinearLayout) contentView
							.findViewById(R.id.ll_share);

					ll_uninstall.setOnClickListener(AppManagerActivity.this);
					ll_start.setOnClickListener(AppManagerActivity.this);
					ll_share.setOnClickListener(AppManagerActivity.this);

					window = new PopupWindow(contentView,
							LayoutParams.WRAP_CONTENT, -2);
					
					//如果要想播放动画，PopupWindow必须设置背景才能播放
					window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//透明的背景也是背景
					int[] location = new int[2];
					view.getLocationInWindow(location);
					//google写的源代码， 在代码里面 程序写的所有的数字 单位都是px 像素
					//dip与像素密度关联的一个单位。
					int dip = 60;//定义一个60的dip
					int px = DensityUtil.dip2px(getApplicationContext(), dip);
					System.out.println("px="+px);
					window.showAtLocation(parent, Gravity.LEFT + Gravity.TOP,
							px, location[1]);
					AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
					aa.setDuration(500);

					ScaleAnimation sa = new ScaleAnimation(0.5f, 1.0f, 0.5f,
							1.0f, Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, 0.5f);
					sa.setDuration(500);
					
					AnimationSet set = new AnimationSet(false);
					set.addAnimation(aa);
					set.addAnimation(sa);
					contentView.startAnimation(set);
					

				}

			}
		});
		
		list_app_item.setOnItemLongClickListener(new OnItemLongClickListener() {
			//方法的返回值 true 代表的是 长点击事件被消费掉了。  false没有消费掉。
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object obj = list_app_item.getItemAtPosition(position);
				if (obj != null) {
					info = (AppInfo) obj;
					//被长点击条目的包名。
					ViewHolder holder = (ViewHolder) view.getTag();
					String packname = info.getPackageName();
					//判断包名是否被锁定
					if(dao.find(packname)){//已经锁定，解除锁定
						dao.delete(packname);//从数据库移除
						holder.iv_status.setImageResource(R.drawable.unlock);//更新界面。
					}else{//没有锁定，锁定应用程序
						dao.add(packname);//添加数据库
						holder.iv_status.setImageResource(R.drawable.lock);//更新界面。
					}
					//大吼一声 说数据库 的内容变化了。
					Intent intent = new Intent();
					intent.setAction("com.itheima.mobilesafe.dbchanged");
					sendBroadcast(intent);
				}
				return true;//事件到此为止
			}
		});
	}

	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);
		// 加载数据
		new Thread() {
			public void run() {
				infos = AppInfoProvider.getAllAppInfos(AppManagerActivity.this);
				userinfos = new ArrayList<AppInfo>();
				systeminfos = new ArrayList<AppInfo>();
				for (AppInfo info : infos) {

					if (info.isUser()) {
						// 用户集合
						userinfos.add(info);
					} else {
						// 系统集合
						systeminfos.add(info);
					}
				}

				handler.sendEmptyMessage(0);
			};
		}.start();
	}

	/**
	 * 返回某一个目录的可用空间
	 * 
	 * @param path
	 * @return
	 */
	public String getAvailSpace(String path) {
		// 最多只支持2G的空间

		StatFs statFs = new StatFs(path);
		// 可以用空间有多少块
		long blocks = statFs.getAvailableBlocks();
		// 某一个空间块的大小
		long blockSize = statFs.getBlockSize();

		return Formatter.formatFileSize(this, blocks * blockSize);
	}

	private void dimissPopupWindow() {
		if (window != null && window.isShowing()) {
			window.dismiss();
			window = null;
		}
	}

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_location;
		ImageView iv_status;
	}

	@Override
	public void onClick(View v) {
		dimissPopupWindow();
		switch (v.getId()) {
		case R.id.ll_uninstall:// 卸载软件
			Log.i(TAG, "卸载==" + info.getPackageName());
			uninstllApp();
			break;

		case R.id.ll_start:// 启动软件
			Log.i(TAG, "启动==" + info.getPackageName());
			startApp();

			break;
		case R.id.ll_share:// 分享软件
			Log.i(TAG, "分享==" + info.getPackageName());
			appShare();

			break;
		}

	}

	/**
	 * 启动软件
	 */
	private void startApp() {
		Intent intent = new Intent();

		// 得到包管理器
		PackageManager pm = getPackageManager();
		String packageName = info.getPackageName();
		try {
			// 功能清单文件
			PackageInfo packInfo = pm.getPackageInfo(packageName,
					PackageManager.GET_ACTIVITIES);
			ActivityInfo[] infos = packInfo.activities;
			if (infos != null && infos.length > 0) {
				// 有界面
				ActivityInfo activityInfo = infos[0];
				String name = activityInfo.name;
				intent.setClassName(packageName, name);
				startActivity(intent);
			} else {
				Toast.makeText(this, "No landing activity for this app", 1).show();
			}
		} catch (NameNotFoundException e) {
			Toast.makeText(this, "Failed to Start", 1).show();
			e.printStackTrace();
		}

	}

	/**
	 * 软件的分享
	 */
	private void appShare() {
		Intent intent = new Intent();
		// <action android:name="android.intent.action.SEND" />
		// <category android:name="android.intent.category.DEFAULT" />
		// <data android:mimeType="text/plain" />
		intent.setAction("android.intent.action.SEND");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT,
				"Click to Download: https://play.google.com/store/apps/details?id="
						+ info.getPackageName());
		startActivity(intent);

		// 支持分享到QQ空间 ，短信 ，微博

	}

	/**
	 * 卸载软件
	 */
	private void uninstllApp() {
		if (info.isUser()) {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.DELETE");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.setData(Uri.parse("package:" + info.getPackageName()));
			startActivityForResult(intent, 0);
		} else {
			Toast.makeText(this, "ROOT Premission is required", 1).show();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fillData();// 刷新数据
	}

}
