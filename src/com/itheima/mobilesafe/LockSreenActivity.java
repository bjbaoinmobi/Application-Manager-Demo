package com.itheima.mobilesafe;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.itheima.mobilesafe.receiver.MyAdmin;

public class LockSreenActivity extends Activity {

	// 设备策略管理员
	private DevicePolicyManager dpm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		ComponentName mDeviceAdminSample = new ComponentName(this,
				MyAdmin.class);
		
		if(dpm.isAdminActive(mDeviceAdminSample)){
			//锁屏
			lockscreem(null);
		}else{
			openAdmin(null);
		}
		finish();

	}

	/**
	 * 点击事件--开启超级设备管理员权限
	 * 
	 * @param view
	 */
	public void openAdmin(View view) {
		// 创建一个Intent :添加一个设备超级管理员
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		ComponentName mDeviceAdminSample = new ComponentName(this,
				MyAdmin.class);
		// 激活的组件--超级设备管理员
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
				mDeviceAdminSample);

		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				"开启我吧，开启我后可以一键锁屏");
		startActivityForResult(intent, 0);

	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		ComponentName mDeviceAdminSample = new ComponentName(this,
				MyAdmin.class);
		if (dpm.isAdminActive(mDeviceAdminSample)) {
			// 已经是超级设备管理员了
		}
	}

	/**
	 * 点击事件--一键锁屏
	 * 
	 * @param view
	 */
	public void lockscreem(View view) {

		dpm.lockNow();
		// dpm.resetPassword("", 0);
		// 一键锁屏 --远程锁屏并且加上密码

		// dpm.wipeData(0);//恢复成出厂设置 --远程删除数据
		// dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);//把手机sd卡的信息也删除；

	}

	/**
	 * 点击事件--卸载软件
	 * 
	 * @param view
	 */
	public void uninstall(View view) {
		// 1.先把当前应用变成普通应用
		ComponentName mDeviceAdminSample = new ComponentName(this,
				MyAdmin.class);
		dpm.removeActiveAdmin(mDeviceAdminSample);
		// 2.再去卸载它
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:com.itheima.lockscreen"));
		startActivity(intent);

	}

}
