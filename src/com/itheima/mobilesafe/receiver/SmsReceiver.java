package com.itheima.mobilesafe.receiver;

import com.itheima.mobilesafe.HomeActivity;
import com.itheima.mobilesafe.LockSreenActivity;
import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.GPSService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = "SmsReceiver";
	private SharedPreferences sp;
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "�����嵥�ļ�ע��Ĺ㲥������");
		//���ն��ŵĴ���
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		for(Object pdu : pdus){
			SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
			String sender = message.getOriginatingAddress();//�绰����
			String bodey = message.getMessageBody();
			
			//�ж��Ƿ��ǰ�ȫ���뷢����5556---13555556999
			
			//�ж�����
			
			if("#*location*#".equals(bodey)){
				//�õ��ֻ�GPS��#*loactiong*#---�õ�λ�� �����Ÿ���ȫ����
				//�õ��ֻ���GPS��Ϣ
				Log.i(TAG, "�õ��ֻ���GPS��Ϣ");
				Intent intentt = new Intent(context,GPSService.class);
				context.startService(intentt);
				sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
				String lastlocation = sp.getString("lastlocation", null);
				if(TextUtils.isEmpty(lastlocation)){
					//��û�еõ�GPS��Ϣ
					SmsManager.getDefault().sendTextMessage(sender, null, "geting loaction....", null, null);
				}else{
					//�Ѿ��õ�GPS��Ϣ�ˣ������Ÿ���ȫ����
					SmsManager.getDefault().sendTextMessage(sender, null, lastlocation, null, null);
				}
				
				
				abortBroadcast();//��ֹ�㲥---���žͱ��ػ�
			}else if("#*alarm*#".equals(bodey)){
				//���ű�������
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
//				player.setLooping(true);//
				player.setLooping(false);
				player.setVolume(1.0f, 1.0f);
				player.start();
				Log.i(TAG, "���ű�������");
				abortBroadcast();//��ֹ�㲥---���žͱ��ػ�
			}else if("#*wipedata*#".equals(bodey)){
				//Զ��ɾ������
				
				Log.i(TAG, "Զ��ɾ������");
				abortBroadcast();//��ֹ�㲥---���žͱ��ػ�
			}else if("#*lockscreen*#".equals(bodey)){
				//Զ������
				Intent intentLockScreen = new Intent(context,LockSreenActivity.class);
//				intentLockScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				intentLockScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intentLockScreen);
				Log.i(TAG, "Զ������");
				abortBroadcast();//��ֹ�㲥---���žͱ��ػ�
			}
			
			
		}
		
		

	}

}
