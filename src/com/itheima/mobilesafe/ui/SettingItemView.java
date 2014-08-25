package com.itheima.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobilesafe.R;

/**
 * 自定义的组合控件，他是一个特殊的View，里面包含两个TextView，还有一CheckBox ,还有一View
 * 
 * @author Administrator
 * 
 */
public class SettingItemView extends RelativeLayout {
	private CheckBox cb_status;
	private TextView tv_title;
	private TextView tv_desc;

	private String desc_off;
	private String desc_on;

	/**
	 * 初始化View，也就是说，初始化布局文件
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		// 把一个布局文件----》View
		View.inflate(context, R.layout.setting_item_view, this);
		cb_status = (CheckBox) findViewById(R.id.cb_status);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_desc = (TextView) findViewById(R.id.tv_desc);

	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	/**
	 * 在布局文件声明控件时，会调用该构造方法初始化该控件
	 * 
	 * @param context
	 * @param attrs
	 */
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		String title = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.itheima.mobilesafe",
				"title");
		desc_off = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.itheima.mobilesafe",
				"desc_off");
		desc_on = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.itheima.mobilesafe",
				"desc_on");
		setDesc(desc_off);
		setTitle(title);
	}

	public SettingItemView(Context context) {
		super(context);
		initView(context);
	}
	/**
	 * 设置组合控件的标题
	 */
	
	public void setTitle(String text){
		tv_title.setText(text);
	}

	/**
	 * 判断组合控件是否是选中的状态
	 */

	public boolean isChecked() {
		return cb_status.isChecked();
	}

	/**
	 * 设置组合控件的状态
	 */

	public void setChecked(boolean checked) {
		if(checked){
			setDesc(desc_on);
		}else{
			setDesc(desc_off);
		}
		cb_status.setChecked(checked);

	}

	/**
	 * 设置组合控件的描述信息
	 */

	public void setDesc(String text) {
		tv_desc.setText(text);
	}

}
