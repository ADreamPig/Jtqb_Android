package com.ningsheng.jietong.Dialog;

import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.R;

import java.util.List;

/**
 * Created by zhushunqing on 2016/2/18.
 */
public class NormalPopuWindows {
    private View view;
    private NormalPopuBack onBack;
    private PopupWindow popu;
    private View alph_view;
    private OnPopuDisShowListener showDisListener;

    public void setShowDisListener(OnPopuDisShowListener showDisListener) {
        this.showDisListener = showDisListener;
    }

    public PopupWindow getPopu() {
        return popu;
    }

    public void show() {
        if (popu != null) {
            if (popu.isShowing()) {
                popu.dismiss();
//                alph_view.setAlpha(1f);
            } else {
                popu.showAsDropDown(view);
                if(showDisListener!=null){
                    showDisListener.onShow();
                }
                if(alph_view!=null){
//                    alph_view.setBackgroundColor();
                alph_view.setAlpha(0.1f);
                }
            }
        }
    }
    public void dismiss(){
        if (popu != null) {
            popu.dismiss();
        }
    }

    public void notifyDataSetChanged(List<String> list){
       this.list=list;
        adapter.notifyDataSetChanged();
    }

    private ListView lsitview;
    private  ArrayAdapter adapter;
    private List<String> list;
    private  View contentView;


    public void setOnBack(NormalPopuBack onBack) {
        this.onBack = onBack;
    }

    public NormalPopuWindows(BaseActivity activity, List<String> list, View view,final View alph_view){
        this.view=view;
        this.alph_view=alph_view;
        this.list=list;
         contentView=LayoutInflater.from(activity).inflate(R.layout.app_list,null,false);
         lsitview=(ListView) contentView.findViewById(R.id.app_list_list);
         adapter=new ArrayAdapter(activity,R.layout.app_text,R.id.app_text_text,this.list);
        lsitview.setAdapter(adapter);

        lsitview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(onBack!=null){
                    onBack.popuBack(position);
                }
            }
        });

         popu = new PopupWindow(contentView,  view.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        popu.setFocusable(true);
        popu.setBackgroundDrawable(new BitmapDrawable());
        popu.setOutsideTouchable(true);
        popu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if(showDisListener!=null){
                showDisListener.onDismiss();
                }
                if(alph_view!=null){
                alph_view.setAlpha(1f);
                }
            }
        });
    }

    public interface NormalPopuBack{
        public void popuBack(int position);
    }
    public interface OnPopuDisShowListener{
        public void onShow();
        public void onDismiss();
    }
}
