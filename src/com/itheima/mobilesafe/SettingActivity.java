package com.itheima.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.itheima.mobilesafe.service.AddressService;
import com.itheima.mobilesafe.service.CallSmsSafeService;
import com.itheima.mobilesafe.service.WatchDogService;
import com.itheima.mobilesafe.ui.SettingClickView;
import com.itheima.mobilesafe.ui.SettingItemView;
import com.itheima.mobilesafe.utils.ServiceStatusUtils;

public class SettingActivity extends Activity {

	// 设置是否自动更新应用
	private SettingItemView siv_update;
	// 共享偏好
	private SharedPreferences sp;

	// 设置是否开启号码归属地显示
	private SettingItemView siv_show_address;
	private Intent show_addressIntent;
	// 设置号码归属地显示框的背景
	private SettingClickView scv_changebg;

	// 设置号码归属地显示框的位置
	private SettingClickView scv_changeposition;

	// 设置是否拦截黑名单
	private SettingItemView siv_blacknumber;
	private Intent blackNumberIntent ;

	// 程序锁的设置
	private SettingItemView siv_applock;
	private Intent watchDogIntent;
	
	
	String s;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		// 设置是否自动更新
		siv_update = (SettingItemView) findViewById(R.id.siv_update);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean update = sp.getBoolean("update", false);
		if (update) {
			// 选择状态
			siv_update.setChecked(true);
			// siv_update.setDesc("自动升级已经开启");
		} else {
			// 非选择状态
			siv_update.setChecked(false);
			// siv_update.setDesc("自动升级已经关闭");
		}
		siv_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor editor = sp.edit();
				if (siv_update.isChecked()) {
					// 非选择状态
					siv_update.setChecked(false);
					// siv_update.setDesc("自动升级已经关闭");
					editor.putBoolean("update", false);
				} else {
					// 选择状态
					siv_update.setChecked(true);
					// siv_update.setDesc("自动升级已经开启");
					editor.putBoolean("update", true);
				}
				editor.commit();// 提交才是真正的保存
			}
		});

		// 设置是否开启号码归属地显示
		siv_show_address = (SettingItemView) findViewById(R.id.siv_show_address);
		show_addressIntent = new Intent(this, AddressService.class);
		// 校验服务是否还活着或者还运行着
		boolean running = ServiceStatusUtils.isRunningService(this,
				"com.itheima.mobilesafe.service.AddressService");
		if (running) {
			// 服务是运行的
			siv_show_address.setChecked(true);
		} else {
			// 服务停止了
			siv_show_address.setChecked(false);
		}
		siv_show_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (siv_show_address.isChecked()) {
					// 关闭服务
					stopService(show_addressIntent);
					siv_show_address.setChecked(false);
				} else {
					// 开启服务
					startService(show_addressIntent);
					siv_show_address.setChecked(true);
				}

			}
		});

		// 设置号码归属地显示框的背景
		scv_changebg = (SettingClickView) findViewById(R.id.scv_changebg);
		scv_changebg.setTitle("归属地提示框风格");
		final String[] items = { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };
		int which = sp.getInt("which", 0);
		scv_changebg.setDesc(items[which]);
		scv_changebg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int tt = sp.getInt("which", 0);
				// 弹出单选对话框
				AlertDialog.Builder builder = new Builder(SettingActivity.this);
				builder.setTitle("归属地提示框风格");
				builder.setSingleChoiceItems(items, tt,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 保存选择的 更改描述信息
								Editor editor = sp.edit();
								editor.putInt("which", which);
								editor.commit();
								scv_changebg.setDesc(items[which]);
								// 消掉对话框
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						s.equals("haha");
					}
				});
				builder.show();

			}
		});

		// 设置号码归属地显示框的位置
		scv_changeposition = (SettingClickView) findViewById(R.id.scv_changeposition);
		scv_changeposition.setTitle("归属地提示框位置");
		scv_changeposition.setDesc("设置归属地提示框显示的位置");
		scv_changeposition.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this,
						DragViewActivity.class);
				startActivity(intent);

			}
		});

		// 设置是否拦截黑名单
		siv_blacknumber = (SettingItemView) findViewById(R.id.siv_blacknumber);
		blackNumberIntent = new Intent(this,CallSmsSafeService.class);
		if(ServiceStatusUtils.isRunningService(this, "com.itheima.mobilesafe.service.CallSmsSafeService")){
			//服务已经开启
			siv_blacknumber.setChecked(true);
		}else{
			//服务没有开启
			siv_blacknumber.setChecked(false);
		}
		siv_blacknumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (siv_blacknumber.isChecked()) {

					siv_blacknumber.setChecked(false);
					//关闭服务
					stopService(blackNumberIntent);
				} else {
					siv_blacknumber.setChecked(true);
					//开启服务
					startService(blackNumberIntent);
				}

			}
		});
		
		
		// 程序锁设置
		siv_applock = (SettingItemView) findViewById(R.id.siv_applock);
		watchDogIntent = new Intent(this,WatchDogService.class);
		if(ServiceStatusUtils.isRunningService(this, "com.itheima.mobilesafe.service.WatchDogService")){
			//服务已经开启
			siv_applock.setChecked(true);
		}else{
			//服务没有开启
			siv_applock.setChecked(false);
		}
		siv_applock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (siv_applock.isChecked()) {

					siv_applock.setChecked(false);
					//关闭服务
					stopService(watchDogIntent);
				} else {
					siv_applock.setChecked(true);
					//开启服务
					startService(watchDogIntent);
				}

			}
		});
	}

}
