package com.ningsheng.jietong.Utils;

import android.widget.Toast;

import com.ningsheng.jietong.App.MyApplication;

import org.xutils.common.Callback;

/**
 * Created by zhushunqing on 2016/2/25.
 */
public abstract   class OnHttpResultListener implements HttpSender.HttpCallBack {
    @Override
    public abstract void onSuccess(String message, String code, String data);

    @Override
    public void onError(Throwable throwable, boolean b) {
//        Toast.makeText(MyApplication.getInstance(),"连接异常，请稍后重试",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelled(Callback.CancelledException e) {

    }

    @Override
    public void onFinished() {

    }

    @Override
    public void onStar() {

    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {

    }
}
