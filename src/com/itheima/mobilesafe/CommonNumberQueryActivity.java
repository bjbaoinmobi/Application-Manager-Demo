package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.itheima.mobilesafe.db.dao.CommonNumberQueryDao;

public class CommonNumberQueryActivity extends Activity {
	
	/**
	 * 可以伸展的ListView
	 */
	private ExpandableListView elv;
	private MyAdapter adapter;
	private SQLiteDatabase db;
	private static String path = "/data/data/com.itheima.mobilesafe/files/commonnum.db";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_number_query);
		elv = (ExpandableListView) findViewById(R.id.elv);
		db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		adapter = new MyAdapter();
		elv.setAdapter(adapter);
		elv.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				TextView tv = (TextView) v;
				String phone = tv.getText().toString().split("\n")[1];
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:"+phone));
				startActivity(intent);
				
				return true;
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.close();
		db = null;
	}
	
	private class MyAdapter extends BaseExpandableListAdapter{

		@Override
		public int getGroupCount() {
			return CommonNumberQueryDao.getGroupCount(db);
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			
			return CommonNumberQueryDao.getChildCount(db,groupPosition);
		}

		@Override
		public Object getGroup(int groupPosition) {
			return null;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView view = null;
			if( convertView!= null){
				view = (TextView) convertView;
			}else{
				view = new TextView(getApplicationContext());
			}
			
			view.setTextColor(Color.RED);
			view.setTextSize(20);
			view.setText("          "+CommonNumberQueryDao.getGroupName(db,groupPosition));
			return view;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			TextView view = null;
			if( convertView!= null){
				view = (TextView) convertView;
			}else{
				view = new TextView(getApplicationContext());
			}
			view.setTextColor(Color.BLACK);
			view.setTextSize(20);
			view.setText(CommonNumberQueryDao.getChildName(db,groupPosition, childPosition));
			return view;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		
	}

}
