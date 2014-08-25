package com.itheima.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

	private SharedPreferences sp;

	private TelephonyManager tm;

	@Override
	public void onReceive(Context context, Intent intent) {
		// 接收开机完成的广播
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		// 开启防盗保护了，sim变更，我才发大短信给安全号码吧
		boolean protecting = sp.getBoolean("protecting", false);
		if (protecting) {
			tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			// 读取之前保存的sim的的信息
			String saveSim = sp.getString("sim", "") + "woshiafu";

			// 读取当前的sim的的信息
			String realSim = tm.getSimSerialNumber();

			// 比较是否sim变更
			if (saveSim.equals(realSim)) {
				// 没有变更
			} else {
				// sim就已经变更
				// 发短信给安全号码
				System.out.println("sim就已经变更");
				Toast.makeText(context, "sim就已经变更", 1).show();
				String safenumber = sp.getString("safenumber", "");
				SmsManager.getDefault().sendTextMessage(safenumber, null, "sim change .....!!! ", null, null);
			}
		}

	}

}
