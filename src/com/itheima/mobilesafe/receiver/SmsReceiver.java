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
		Log.i(TAG, "功能清单文件注册的广播接收者");
		//接收短信的代码
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		for(Object pdu : pdus){
			SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
			String sender = message.getOriginatingAddress();//电话号码
			String bodey = message.getMessageBody();
			
			//判断是否是安全号码发过来5556---13555556999
			
			//判断命令
			
			if("#*location*#".equals(bodey)){
				//得到手机GPS：#*loactiong*#---得到位置 发短信给安全号码
				//得到手机的GPS信息
				Log.i(TAG, "得到手机的GPS信息");
				Intent intentt = new Intent(context,GPSService.class);
				context.startService(intentt);
				sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
				String lastlocation = sp.getString("lastlocation", null);
				if(TextUtils.isEmpty(lastlocation)){
					//还没有得到GPS信息
					SmsManager.getDefault().sendTextMessage(sender, null, "geting loaction....", null, null);
				}else{
					//已经得到GPS信息了，发短信给安全号码
					SmsManager.getDefault().sendTextMessage(sender, null, lastlocation, null, null);
				}
				
				
				abortBroadcast();//终止广播---短信就被截获
			}else if("#*alarm*#".equals(bodey)){
				//播放报警音乐
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
//				player.setLooping(true);//
				player.setLooping(false);
				player.setVolume(1.0f, 1.0f);
				player.start();
				Log.i(TAG, "播放报警音乐");
				abortBroadcast();//终止广播---短信就被截获
			}else if("#*wipedata*#".equals(bodey)){
				//远程删除数据
				
				Log.i(TAG, "远程删除数据");
				abortBroadcast();//终止广播---短信就被截获
			}else if("#*lockscreen*#".equals(bodey)){
				//远程锁屏
				Intent intentLockScreen = new Intent(context,LockSreenActivity.class);
//				intentLockScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				intentLockScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intentLockScreen);
				Log.i(TAG, "远程锁屏");
				abortBroadcast();//终止广播---短信就被截获
			}
			
			
		}
		
		

	}

}
