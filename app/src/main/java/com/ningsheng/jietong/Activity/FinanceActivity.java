package com.ningsheng.jietong.Activity;

import android.webkit.WebView;

import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.AndroidUtil;

/**
 * Created by zhushunqing on 2016/4/15.
 */
public class FinanceActivity extends TitleActivity {


    @Override
    protected void initListener() {
    setContentView(R.layout.act_finance);
        setTitle("理财");
        showBackwardView("",true);
        WebView view=(WebView) findViewById(R.id.act_finance_web);
        AndroidUtil.setWebView(view).loadUrl("https://wx.hyjf.com/index.php?s=/Wx/project/index.html");
    }

    @Override
    protected void initDate() {

    }
}
