package com.ningsheng.jietong.Activity;

import android.text.Html;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.gsonUtil;
import com.ningsheng.jietong.View.RichTextView.RichTextWrapper;

/**
 * Created by zhushunqing on 2016/3/31.
 */
public class RegisterXy extends TitleActivity {
    private  WebView m_content;
    @Override
    protected void initListener() {
        setContentView(R.layout.act_regsiter_xy);
        setTitle("注册协议");
        showBackwardView("",true);

         m_content=(WebView)findViewById(R.id.regsiter_xy);
        WebView s;

//String s="<h1 align=&quot;center&quot;&gt;&lt;strong&gt;瑞祥支付用户协议&lt";
//         richTextWrapper = new RichTextWrapper(m_content);
    }

    private  RichTextWrapper richTextWrapper;
    @Override
    protected void initDate() {
        HttpSender sender=new HttpSender(Url.getContent, "", null, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
               String content=(String) gsonUtil.getInstance().getValue(data,"content");

                String html=Html.fromHtml(content).toString();
                m_content.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
                m_content.getSettings().setJavaScriptEnabled(true);
                m_content.setWebChromeClient(new WebChromeClient());

//                m_content.setText(Html.fromHtml(content));

//                richTextWrapper.setText(content);
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);

    }
}
