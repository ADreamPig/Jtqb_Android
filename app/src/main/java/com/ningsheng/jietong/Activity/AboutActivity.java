package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.R;

/**
 * Created by hasee-pc on 2016/2/26.
 */
public class AboutActivity extends TitleActivity {
    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_about);
        setTitle("关于我们");
        Log.i("","");
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initDate() {
    }

    public void tel(View view){
        Intent intent1 = new Intent(Intent.ACTION_CALL);
        intent1.setData(Uri.parse("tel:4008871380"));
        startActivity(intent1);
    }
}
