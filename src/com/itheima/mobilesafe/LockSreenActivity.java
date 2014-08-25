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

	// �豸���Թ���Ա
	private DevicePolicyManager dpm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		ComponentName mDeviceAdminSample = new ComponentName(this,
				MyAdmin.class);
		
		if(dpm.isAdminActive(mDeviceAdminSample)){
			//����
			lockscreem(null);
		}else{
			openAdmin(null);
		}
		finish();

	}

	/**
	 * ����¼�--���������豸����ԱȨ��
	 * 
	 * @param view
	 */
	public void openAdmin(View view) {
		// ����һ��Intent :���һ���豸��������Ա
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		ComponentName mDeviceAdminSample = new ComponentName(this,
				MyAdmin.class);
		// ��������--�����豸����Ա
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
				mDeviceAdminSample);

		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				"�����Ұɣ������Һ����һ������");
		startActivityForResult(intent, 0);

	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		ComponentName mDeviceAdminSample = new ComponentName(this,
				MyAdmin.class);
		if (dpm.isAdminActive(mDeviceAdminSample)) {
			// �Ѿ��ǳ����豸����Ա��
		}
	}

	/**
	 * ����¼�--һ������
	 * 
	 * @param view
	 */
	public void lockscreem(View view) {

		dpm.lockNow();
		// dpm.resetPassword("", 0);
		// һ������ --Զ���������Ҽ�������

		// dpm.wipeData(0);//�ָ��ɳ������� --Զ��ɾ������
		// dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);//���ֻ�sd������ϢҲɾ����

	}

	/**
	 * ����¼�--ж�����
	 * 
	 * @param view
	 */
	public void uninstall(View view) {
		// 1.�Ȱѵ�ǰӦ�ñ����ͨӦ��
		ComponentName mDeviceAdminSample = new ComponentName(this,
				MyAdmin.class);
		dpm.removeActiveAdmin(mDeviceAdminSample);
		// 2.��ȥж����
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:com.itheima.lockscreen"));
		startActivity(intent);

	}

}
