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
		// ���տ�����ɵĹ㲥
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		// �������������ˣ�sim������Ҳŷ�����Ÿ���ȫ�����
		boolean protecting = sp.getBoolean("protecting", false);
		if (protecting) {
			tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			// ��ȡ֮ǰ�����sim�ĵ���Ϣ
			String saveSim = sp.getString("sim", "") + "woshiafu";

			// ��ȡ��ǰ��sim�ĵ���Ϣ
			String realSim = tm.getSimSerialNumber();

			// �Ƚ��Ƿ�sim���
			if (saveSim.equals(realSim)) {
				// û�б��
			} else {
				// sim���Ѿ����
				// �����Ÿ���ȫ����
				System.out.println("sim���Ѿ����");
				Toast.makeText(context, "sim���Ѿ����", 1).show();
				String safenumber = sp.getString("safenumber", "");
				SmsManager.getDefault().sendTextMessage(safenumber, null, "sim change .....!!! ", null, null);
			}
		}

	}

}
