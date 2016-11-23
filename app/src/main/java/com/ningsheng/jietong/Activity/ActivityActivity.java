package com.ningsheng.jietong.Activity;

import android.text.Html;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.AndroidUtil;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.MediaUtil;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.gsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/2/22.
 */
public class ActivityActivity extends TitleActivity {
    private String id;
    private WebView m_Content;
    private ImageView m_Image;
    private WebChromeClient wcc;

    @Override
    protected void initListener() {
        setContentView(R.layout.activity_activity);
        setTitle("活动详情");
        showBackwardView("", true);
//        WebView webView=(WebView) findViewById(R.id.activity_webview);
//        AndroidUtil.setWebView(webView).loadUrl("http://hyjf.shizitegong.com:8080/jtqb-pc/jtqbContent/getContent?flg=userAgreement");

        m_Content = (WebView) findViewById(R.id.act_actdetails);
        m_Image = (ImageView) findViewById(R.id.act_act_image);
        id = getIntent().getStringExtra("id");
        wcc = new WebChromeClient();
    }

    @Override
    protected void initDate() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        HttpSender sender = new HttpSender(Url.merchantSalesInfo, "卡详情", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                String content = (String) gsonUtil.getInstance().getValue(data, "description");
                String imageApp = (String) gsonUtil.getInstance().getValue(data, "imageInfo");

                String html = Html.fromHtml(content).toString();
                m_Content.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
                m_Content.getSettings().setJavaScriptEnabled(true);
//                m_Content.getSettings().setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
//                m_Content.getSettings().requestFocus();
                m_Content.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                m_Content.getSettings().setPluginState(WebSettings.PluginState.ON);
                m_Content.getSettings().setAllowFileAccess(true);
                m_Content.getSettings().setJavaScriptEnabled(true);
                m_Content.getSettings().setUseWideViewPort(true);
                m_Content.getSettings().setLoadWithOverviewMode(true);
                m_Content.getSettings().setSupportZoom(false);
                m_Content.setWebChromeClient(wcc);
//                m_Content.setText(Html.fromHtml(content));
                MediaUtil.displayImage(ActivityActivity.this, imageApp, m_Image);
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }
}
