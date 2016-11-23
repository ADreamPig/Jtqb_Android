package com.ningsheng.jietong.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public abstract class Adapter<T> extends BaseAdapter {
	protected BaseActivity mactivity;
	protected List<T> mlist;
	private int mlayout;

	public Adapter(BaseActivity activity, List<T> list, int layout) {
		this.mactivity = activity;
		this.mlist = list;
		this.mlayout = layout;
	}

	public List<T> getList(){
		return mlist;
	}
	@Override
	public int getCount() {
		return mlist != null ? mlist.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		return mlist != null ? mlist.get(arg0) : null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder vh = ViewHolder.get(mactivity, arg1, arg2, mlayout, arg0);
		getview(vh,arg0,mlist.get(arg0));
		return vh.getConvertView();
	};

	public abstract void getview(ViewHolder vh,int position,T T);
	public void setDeviceList(ArrayList<T> list) {
		if (list != null) {
			mlist = (ArrayList<T>) list.clone();
			notifyDataSetChanged();
		}
	}
	public void addAll(List<T> list) {
		if (list != null && list.size() != 0) {
			mlist.addAll(list);
			notifyDataSetChanged();
		}
	}

	public void clearDeviceList() {
		if (mlist != null) {
			mlist.clear();
		}
		notifyDataSetChanged();
	}

	protected void initViewholder(View view, Object object) {
		Field[] fs = object.getClass().getDeclaredFields();
		for (Field field : fs) {
			field.setAccessible(true);
			try {
				field.set(object, view.findViewById(R.id.class.getField(
						field.getName()).getInt(null)));
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	static class a 
	{
	}
}
