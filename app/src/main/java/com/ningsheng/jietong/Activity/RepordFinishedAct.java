package com.ningsheng.jietong.Activity;

import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.R;

/**
 * Created by zhushunqing on 2016/2/24.
 */
public class RepordFinishedAct extends TitleActivity {
    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_report_finish);
        setTitle("提交挂失");
        showBackwardView("",true);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initDate() {

    }
}
