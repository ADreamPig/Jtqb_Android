package com.ningsheng.jietong.Utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ningsheng.jietong.Activity.LoginActivity;
import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Dialog.LodingDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.Map;

/**
 * Created by ${zsq} on 2016/1/7.
 */
public class HttpSender {

    private Boolean isShowDialog = true;
    private Dialog dialog;
    private BaseActivity context;
    private RequestParams params;
    private String url;
    private String name;
    private Object RequesObject;
    private HttpCallBack callBack;
    private Callback.Cancelable cancle;
    private long CacheMaxAge;


    public HttpSender(String url, String name, Object RequesObject, HttpCallBack callBack) {
        this.url = url;
        this.name = name;
        this.RequesObject = RequesObject;
        this.callBack = callBack;
    }

    public void setContext(BaseActivity context) {
        this.context = context;
    }


    public void setCacheMaxAge(long cacheMaxAge) {
        CacheMaxAge = cacheMaxAge;
    }

    private void requestPost() {
        if (params == null) {
            params = new RequestParams(url);
        }
        if (RequesObject != null) {
            Log.i("---", "POST请求名称: " + name + "\n" + "POST请求Url: "
                    + url.toString());
            Map<String, Object> map = gsonUtil.getInstance().Obj2Map(
                    RequesObject);
            for (String key : map.keySet()) {
                Log.i("---", "POST提交参数: " + key + " = " + map.get(key).toString());
                if (key.equals("accountId")) {
                    if (map.get(key).toString() == null || map.get(key).toString().equals("")) {
                        Intent intent = new Intent(ActUtil.getInstance().getTopActivity(), LoginActivity.class);
                        ActUtil.getInstance().getTopActivity().startActivityForResult(intent, 11);
                        return;
                    }
                }
                params.addBodyParameter(key, map.get(key).toString());
            }
        }
        Log.i("--------tag--------", "tijiao-----");
        params.setCacheMaxAge(CacheMaxAge);
        cancle = x.http().post(params, new onRequestCallBack());
    }



    private void requestGet() {
        StringBuilder sb_url = new StringBuilder(url);
        if (RequesObject != null) {
            sb_url.append('?');
            Map<String, Object> map = gsonUtil.getInstance().Obj2Map(RequesObject);
            for (String key : map.keySet()) {
                sb_url.append(key + "=" + map.get(key).toString() + "&");
            }
        }
        params = new RequestParams(sb_url.toString());
//        params=new RequestParams("http://juyan.55liketest.com/app/userInfo/getUserType?groupid=1");
//        params.setCacheMaxAge(0l);
        Log.i("get请求为：", sb_url.toString());
        cancle = x.http().get(params, new onRequestCallBack());
    }

    public void cancel() {
        if (cancle != null) {
            cancle.cancel();
        }
    }

    public void addFile(String key, File file) {
        if (null == params) {
            params = new RequestParams();
        }
        params.setMultipart(true);
        params.addBodyParameter(key, file);
        Log.i("---", "File提交文件: " + key + " = " + file.toString());
    }

    public void send(String httptype) {
        if (httptype.equals(Url.Get)) {
            requestGet();
        } else if (httptype.equals(Url.Post)) {
            requestPost();
        }
    }

    /**
     * 显示等待对话框
     */
    private void showDialog() {
        if (!isShowDialog)
            return;
        if (context == null) {
            return;
        }
        if (dialog == null) {
//            LodingDialog mPd = new LodingDialog(context,
//                    ProgressDialog.THEME_HOLO_LIGHT);
            LodingDialog mPd=new LodingDialog(context);
//            mPd.setMessage("努力加载...");
            dialog = mPd;
        }
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
        if (!dialog.isShowing())
            dialog.show();
    }

    /**
     * 关闭等待对话框
     */
    private void dismissDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public interface HttpCallBack {
        public void onSuccess(String message, String code, String data);

        public void onError(Throwable throwable, boolean b);

        public void onCancelled(Callback.CancelledException e);

        public void onFinished();

        public void onLoading(long total, long current, boolean isDownloading);
        public void onStar();
    }

    private class onRequestCallBack implements Callback.ProgressCallback<String> {
        //192.168.0.30  获取生物多样性


        @Override
        public void onSuccess(String s) {
            Log.i("------onRequestCallBack--------", "=====onSuccess===" + s);
            String data = null;
            String code = null;
            String message = null;
            try {
                JSONObject object = new JSONObject(s);
                data = object.getString("CZFH");
                message = object.getString("CZMS");
                code = object.getString("CZJG");
                callBack.onSuccess(message, code, data);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onError(Throwable throwable, boolean b) {
            if(throwable!=null){
                Log.i("------onRequestCallBack--------", throwable.getMessage() + "");
                callBack.onError(throwable, b);
                if(throwable instanceof HttpException){
                    Toast.makeText(MyApplication.getInstance(),"网络错误！",Toast.LENGTH_SHORT).show();
                }
            }

        }

        @Override
        public void onCancelled(CancelledException e) {
            callBack.onCancelled(e);
            dismissDialog();
        }

        @Override
        public void onFinished() {
            Log.i("-------httpsender---onRequestCallBack---", "onFinished");
            callBack.onFinished();
            dismissDialog();
        }

        @Override
        public void onWaiting() {

        }

        @Override
        public void onStarted() {
            showDialog();
            callBack.onStar();
        }

        @Override
        public void onLoading(long total, long current, boolean isDownloading) {
            Log.i("------onRequestCallBack--------", "=====onLoading===" + total);
            callBack.onLoading(total, current, isDownloading);
        }
    }

}
