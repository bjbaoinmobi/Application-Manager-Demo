package com.itheima.mobilesafe.service;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class GPSService extends Service {
	// 位置管理者，位置服务；
	private LocationManager lm;
	private Mylistener listener;
	private SharedPreferences sp;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		listener = new Mylistener();
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// criteria.setAccuracy(Criteria.ACCURACY_FINE);//设置为最大精度
		// criteria.setAltitudeRequired(false);//不要求海拔信息
		// criteria.setBearingRequired(false);//不要求方位信息
		// criteria.setCostAllowed(true);//是否允许付费
		// criteria.setPowerRequirement(Criteria.POWER_LOW);//对电量的要求
		String provider = lm.getBestProvider(criteria, true);// GPS
		// 注册监听位置服务
		lm.requestLocationUpdates(provider, 0, 0, listener);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 取消注册位置服务的监听
		lm.removeUpdates(listener);
		listener = null;
	}
	
	
	private class Mylistener  implements LocationListener{
		/**
		 * 当位置发生变化的时候回调执行
		 */
		@Override
		public void onLocationChanged(Location location) {
			String longitude = "j:"+location.getLongitude()+"\n";
			String latitude = "w:"+location.getLatitude()+"\n";
			String accuracy = "a:"+location.getAccuracy()+"\n";
			
			//发短信给安全号码
			//把最后一次更新的位置保存起来
			//把标准的坐标转换成火星坐标 --自己完成
			
			try {
				ModifyOffset modifyOffset = ModifyOffset.getInstance(getAssets().open("axisoffset.dat"));
				PointDouble  pointDouble = modifyOffset.s2c(new PointDouble(location.getLongitude(), location.getLatitude()));
				longitude = "j"+pointDouble.x+"\n";
				latitude = "w"+pointDouble.y+"\n";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Editor editor = sp.edit();
			editor.putString("lastlocation", longitude+latitude+accuracy);
			editor.commit();
			
		}

		/**
		 * 某个位置提供者，状态发生变化的时候回调 --开启--关闭； 关闭--开启
		 */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
		/**
		 * 某个位置提供者可以用了
		 */
		@Override
		public void onProviderEnabled(String provider) {
			
		}
		/**
		 * 某个位置提供者不可以用了
		 */
		@Override
		public void onProviderDisabled(String provider) {
			
		}
		
	}

}
