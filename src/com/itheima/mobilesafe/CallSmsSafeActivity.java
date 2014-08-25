package com.itheima.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.db.dao.BlackNumberDao;
import com.itheima.mobilesafe.domain.BlackNumberInfo;

public class CallSmsSafeActivity extends Activity {
	private static final String TAG = "CallSmsSafeActivity";

	private ListView list_callsms_safe;
	private BlackNumberDao dao;
	private LinearLayout ll_loading;

	private int startIndex = 0;

	private MyAdapter adapter;

	private boolean isLoading = false;
	/**
	 * ��������������
	 */
	private List<BlackNumberInfo> infos;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			isLoading = false;
			if (adapter == null) {
				adapter = new MyAdapter();
				list_callsms_safe.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();// ֪ͨ��������������UI
			}

			ll_loading.setVisibility(View.INVISIBLE);
			// list_callsms_safe.setSelection(startIndex);//���Ƽ�
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callsms_safe);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		list_callsms_safe = (ListView) findViewById(R.id.list_callsms_safe);
		dao = new BlackNumberDao(this);

		fillDate();

		list_callsms_safe.setOnScrollListener(new OnScrollListener() {

			// ��״̬�仯��ʱ��ص��÷���
			// ��ֹ--->>����
			// ����---->>��ֹ
			// ��ָ����--->>���Թ���
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:// ��ֹ״̬

					int totalCount = infos.size();// 20

					int position = list_callsms_safe.getLastVisiblePosition();// 19

					if (isLoading) {
						Toast.makeText(CallSmsSafeActivity.this, "���ڼ�����...", 0)
								.show();
						return;
					}

					int TotatCount = dao.getTotatCount();
					if (position >= (TotatCount - 1)) {
						Toast.makeText(CallSmsSafeActivity.this, "�Ѿ�û��������...",
								0).show();

						return;
					}

					if (position == (totalCount - 1)) {

						Toast.makeText(CallSmsSafeActivity.this, "���ظ�������", 0)
								.show();
						startIndex += 20;
						// �ٴ�ȥ����
						fillDate();
						isLoading = true;
					}

					break;

				case OnScrollListener.SCROLL_STATE_FLING:// ����״̬

					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// ��ָ����״̬

					break;
				}

			}

			/**
			 * ��ListView������ʱ��ص��÷���
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});

	}

	private void fillDate() {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				if (infos == null) {
					infos = dao.findPart(startIndex);
				} else {
					infos.addAll(dao.findPart(startIndex));
				}

				// ��һ����Ϣ���½���
				handler.sendEmptyMessage(0);
			};
		}.start();
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infos.size();
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			View view = null;
			ViewHolder holder = null;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
				Log.i(TAG, "������ʷ����ĵ�View����==" + position);
			} else {
				Log.i(TAG, "�����µ�View����==" + position);
				view = View.inflate(CallSmsSafeActivity.this,
						R.layout.list_callsms_safe_item, null);

				// �����ǵ�View����һ�������Ҿ�ȥ���Ҳ��ҷ����������棨�����
				holder = new ViewHolder();
				// �÷���Ҳ�ǱȽ�������Դ�ģ�����˵Ҳ�Ǻ�ʱ
				holder.tv_number = (TextView) view.findViewById(R.id.tv_number);
				holder.tv_mode = (TextView) view.findViewById(R.id.tv_mode);
				holder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);

				view.setTag(holder);// �������Ƕ�Ӧ��ϵ����˵��ι�ϵ
			}
			BlackNumberInfo info = infos.get(position);

			holder.tv_number.setText(info.getNumber());

			if ("1".equals(info.getMode())) {
				// ���ص绰
				holder.tv_mode.setText("���ص绰");
			} else if ("2".equals(info.getMode())) {
				// ���ض���
				holder.tv_mode.setText("���ض���");
			} else {
				// �������е�
				holder.tv_mode.setText("����+�绰����");
			}
			
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					System.out.println("Сͼ�걻�����"+position);
					BlackNumberInfo  info = infos.get(position);
					
					//�����ݿ�����ɾ����������
					dao.delete(info.getNumber());
					
					//�ڵ�ǰ�б�ҲҪ�Ƴ�������Ϣ
					infos.remove(info);
					
					//֪ͨ������ˢ��ҳ��
					adapter.notifyDataSetChanged();
					
				}
			});

			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	static class ViewHolder {
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
	}

	/**
	 * ����¼�--���һ������������
	 * 
	 * @param view
	 */
	public void addBlackNumber(View view) {
		AlertDialog.Builder builder = new Builder(this);
		final AlertDialog dialog = builder.create();
		View contentView = View.inflate(this, R.layout.dialog_add_black_number,
				null);
		final EditText et_phone = (EditText) contentView
				.findViewById(R.id.et_blacknumber);
		final RadioGroup rg_mode = (RadioGroup) contentView
				.findViewById(R.id.rg_mode);
		Button ok = (Button) contentView.findViewById(R.id.ok);
		Button cancel = (Button) contentView.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//ȡ���Ի���
				dialog.dismiss();
				
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//  �õ����������� ����ģʽ
				String phone = et_phone.getText().toString().trim();
				//�жϿ�
				if(TextUtils.isEmpty(phone)){
					Toast.makeText(CallSmsSafeActivity.this, "����Ϊ��", 1).show();
					return;
				}
				//ȡһ������ģʽ
				int id = rg_mode.getCheckedRadioButtonId();
				String mode = "3";
				switch (id) {
				case R.id.rb_all:
					mode = "3";//ȫ������
					break;

				case R.id.rb_phone:
					mode = "1";//�绰
					break;
					
				case R.id.rb_sms:
					mode = "2";//����
					break;
				}
				dialog.dismiss();
				BlackNumberInfo info = new BlackNumberInfo();
				//�������ݿ�����
				dao.add(phone, mode);//�����ǵ�������ӵ����ݿ�����ȥ��
				//�����ǵĵ�ǰҳ�沢û�а�������Ϣ��ӵ���ǰ�б�
				info.setMode(mode);
				info.setNumber(phone);
				infos.add(0, info);
				//ˢ��ҳ��
				adapter.notifyDataSetChanged();// ֪ͨ��������������UI
				
			}
		});
		
		dialog.setView(contentView, 0, 0, 0, 0);
		dialog.show();

	}

}
