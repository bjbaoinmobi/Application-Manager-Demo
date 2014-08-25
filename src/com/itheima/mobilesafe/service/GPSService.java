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
	// λ�ù����ߣ�λ�÷���
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
		// criteria.setAccuracy(Criteria.ACCURACY_FINE);//����Ϊ��󾫶�
		// criteria.setAltitudeRequired(false);//��Ҫ�󺣰���Ϣ
		// criteria.setBearingRequired(false);//��Ҫ��λ��Ϣ
		// criteria.setCostAllowed(true);//�Ƿ�������
		// criteria.setPowerRequirement(Criteria.POWER_LOW);//�Ե�����Ҫ��
		String provider = lm.getBestProvider(criteria, true);// GPS
		// ע�����λ�÷���
		lm.requestLocationUpdates(provider, 0, 0, listener);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// ȡ��ע��λ�÷���ļ���
		lm.removeUpdates(listener);
		listener = null;
	}
	
	
	private class Mylistener  implements LocationListener{
		/**
		 * ��λ�÷����仯��ʱ��ص�ִ��
		 */
		@Override
		public void onLocationChanged(Location location) {
			String longitude = "j:"+location.getLongitude()+"\n";
			String latitude = "w:"+location.getLatitude()+"\n";
			String accuracy = "a:"+location.getAccuracy()+"\n";
			
			//�����Ÿ���ȫ����
			//�����һ�θ��µ�λ�ñ�������
			//�ѱ�׼������ת���ɻ������� --�Լ����
			
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
		 * ĳ��λ���ṩ�ߣ�״̬�����仯��ʱ��ص� --����--�رգ� �ر�--����
		 */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
		/**
		 * ĳ��λ���ṩ�߿�������
		 */
		@Override
		public void onProviderEnabled(String provider) {
			
		}
		/**
		 * ĳ��λ���ṩ�߲���������
		 */
		@Override
		public void onProviderDisabled(String provider) {
			
		}
		
	}

}
