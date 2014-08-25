package com.itheima.mobilesafe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.itheima.mobilesafe.db.dao.AntivirusDao;
import com.itheima.mobilesafe.utils.Md5Utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AntiVirusActivity extends Activity {
	private ImageView iv_scan;
	private TextView tv_scan_status;
	private ProgressBar pb_progress;
	private LinearLayout ll_container;
	private List<String> viruspacknames;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anti_virus);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		iv_scan = (ImageView) findViewById(R.id.iv_scan);
		RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setRepeatCount(Animation.INFINITE);
		ra.setRepeatMode(Animation.RESTART);
		ra.setDuration(2000);
		iv_scan.startAnimation(ra);
		tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
		pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
		//扫描病毒。。。
		tv_scan_status.setText("正在初始化8核杀毒引擎");
		viruspacknames = new ArrayList<String>();
		scanVirus();
	}
	
	
	private void scanVirus() {
		// 检查手机里面所有的应用程序。
		new Thread(){
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				final PackageManager pm = getPackageManager();
				List<PackageInfo>  packageInfos = pm.getInstalledPackages(0);
				pb_progress.setMax(packageInfos.size());//设置最大值
				int total = 0;
				for(final PackageInfo info:packageInfos){
					String path = info.applicationInfo.sourceDir;//应用程序包apk的完整路径
					//获取特征码 查看是否是病毒。
					String md5 = Md5Utils.getFileMd5(new File(path));   
					//查询数据库是否有这个特征码存在
					final boolean result ;
					if(AntivirusDao.isVirus(md5)){
						//发现病毒。
						result = true;
						//记录这个病毒的包名。
						viruspacknames.add(info.packageName);
					}else{
						//扫描安全
						result = false;
					}
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tv_scan_status.setText("正在扫描："+info.applicationInfo.loadLabel(pm));
							TextView tv = new TextView(getApplicationContext());
							tv.setText(info.applicationInfo.loadLabel(pm));
							if(result){
								tv.setTextColor(Color.RED);
							}else{
								tv.setTextColor(Color.BLACK);
							}
							ll_container.addView(tv, 0);
						}
					});
					try {
						Thread.sleep(80);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					total++;
					pb_progress.setProgress(total);
				}
				
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tv_scan_status.setText("扫描完毕");
						iv_scan.clearAnimation();
						
						if(viruspacknames.size()>0){
							//发现了病毒， 提示用户卸载这些应用。
						}else{
							Toast.makeText(getApplicationContext(), "没有发现病毒，手机很安全", 0).show();
						}
					}
				});
				
			};
		}.start();
	}
}
