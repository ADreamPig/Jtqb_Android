package com.ningsheng.jietong.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ningsheng.jietong.Activity.IdentityValidateActivity;
import com.ningsheng.jietong.R;

/**
 * Android 5.0 Material 风格Dialog
 * Created by hasee-pc on 2016/2/24.
 */
public class MaterialDialog extends Dialog implements View.OnClickListener {
    public static final int TYPE_MATERIAL_NORL = 0;
    public static final int TYPE_MATERIAL_EDIT = 1;
    private int type = TYPE_MATERIAL_NORL;
    private Context context;
    private TextView title;
    private TextView cancalButton;
    private TextView confirmButton;
    private MaterialDialogNorl materialDialogNorl;
    private MaterialDialogEdit materialDialogEdit;
    private GradientDrawable buttonDrawable1;
    private GradientDrawable buttonDrawable2;
    private MaterialButtonListener materialButtonListener;
    private View contentView;
    private Resources resources;

    public class MaterialDialogNorl {
        private TextView content;
    }

    public class MaterialDialogEdit {
        public EditText et;
        public View line;
        public TextView tv;
        public TextView content;
        public TextView content1;
        public TextView cancalButton;
        public TextView confirmButton;
        public LinearLayout linear;
        public LinearLayout linear1;
    }

    public MaterialDialog(Context context, MaterialButtonListener materialButtonListener) {
        super(context, R.style.MaterialDialogStyle);
        this.context = context;
        this.materialButtonListener = materialButtonListener;
        init();
    }

    public MaterialDialog(Context context, MaterialButtonListener materialButtonListener, int type) {
        super(context, R.style.MaterialDialogStyle);
        this.context = context;
        this.materialButtonListener = materialButtonListener;
        this.type = type;
        init();
    }

    private void init() {
        resources = context.getResources();
        switch (type) {
            case TYPE_MATERIAL_EDIT:
                materialDialogEdit = new MaterialDialogEdit();
                contentView = View.inflate(context, R.layout.dialog_material_1, null);
                materialDialogEdit.et = (EditText) contentView.findViewById(R.id.material_1_et);
                materialDialogEdit.line = contentView.findViewById(R.id.material_1_line);
                materialDialogEdit.tv = (TextView) contentView.findViewById(R.id.material_1_tv);
                materialDialogEdit.content = (TextView) contentView.findViewById(R.id.content);
                materialDialogEdit.content1 = (TextView) contentView.findViewById(R.id.content1);
                materialDialogEdit.linear = (LinearLayout) contentView.findViewById(R.id.material_1_linear1);
                materialDialogEdit.linear1 = (LinearLayout) contentView.findViewById(R.id.material_1_linear2);
                materialDialogEdit.tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context,IdentityValidateActivity.class);
                        context.startActivity(intent);
                    }
                });



                break;
            default:
            case TYPE_MATERIAL_NORL:
                materialDialogNorl = new MaterialDialogNorl();
                contentView = View.inflate(context, R.layout.dialog_material, null);
                materialDialogNorl.content = (TextView) contentView.findViewById(R.id.content);
                break;
        }
        title = (TextView) contentView.findViewById(R.id.title);
        cancalButton = (TextView) contentView.findViewById(R.id.cancel);
        confirmButton = (TextView) contentView.findViewById(R.id.confirm);
        if (materialDialogEdit != null) {
            materialDialogEdit.cancalButton = cancalButton;
            materialDialogEdit.confirmButton = confirmButton;
        }
        confirmButton.setTag(materialDialogEdit);
        GradientDrawable backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setColor(0xFFFFFFFF);
        float scale = context.getResources().getDisplayMetrics().density;
        backgroundDrawable.setCornerRadius((int) (4 * scale + 0.5f));
        contentView.setBackgroundDrawable(backgroundDrawable);
        buttonDrawable1 = new GradientDrawable();
        buttonDrawable1.setCornerRadius((int) (scale * 4 + 0.5f));
        buttonDrawable1.setColor(0xFFEDA618);
        buttonDrawable2 = new GradientDrawable();
        buttonDrawable2.setCornerRadius((int) (scale * 4 + 0.5f));
        buttonDrawable2.setColor(0xFFEDA618);
        cancalButton.setBackgroundDrawable(buttonDrawable1);
        confirmButton.setBackgroundDrawable(buttonDrawable2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cancalButton.setOnClickListener(this);
        confirmButton.setOnClickListener(this);
        setContentView(contentView);
        setCancelable(false);
        getWindow().getAttributes().width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
    }

    public void setTitle(CharSequence title) {
        if (check(title)) {
            MaterialDialog.this.title.setVisibility(View.GONE);
        } else {
            MaterialDialog.this.title.setText(title);
        }

    }

    public void setContentNorl(CharSequence content) {
        if (type != TYPE_MATERIAL_NORL) {
            Log.e("materialDialog", "type error");
            return;
        }
        if (check(content)) {
            materialDialogNorl.content.setVisibility(View.GONE);
        } else {
            materialDialogNorl.content.setText(content);
        }

    }

    public void setContentEdit1(CharSequence content1) {
        if (type != TYPE_MATERIAL_EDIT) {
            Log.e("materialDialog", "type error");
            return;
        }
        if (check(content1)) {
            materialDialogEdit.content1.setVisibility(View.GONE);
        } else {
            materialDialogEdit.content1.setText(content1);
        }

    }

    public void setCancalButton(CharSequence cancalButton) {
        if (check(cancalButton)) {
            MaterialDialog.this.cancalButton.setVisibility(View.GONE);
        } else {
            MaterialDialog.this.cancalButton.setText(cancalButton);
        }

    }

    public void setConfirmButton(CharSequence confirmButton) {
        if (check(confirmButton)) {
            MaterialDialog.this.confirmButton.setVisibility(View.GONE);
        } else {
            MaterialDialog.this.confirmButton.setText(confirmButton);
        }

    }

    public void setCancalButtonColor(int textColor, int backgroundColor) {
        if (textColor > 0) {
            cancalButton.setTextColor(resources.getColor(textColor));
        }
        if (backgroundColor > 0) {
            buttonDrawable1.setColor(resources.getColor(backgroundColor));
            cancalButton.setBackgroundDrawable(buttonDrawable1);
        }
    }

    public void setConfirmButton(int textColor, int backgroundColor) {
        if (textColor > 0) {
            confirmButton.setTextColor(resources.getColor(textColor));
        }
        if (backgroundColor > 0) {
            buttonDrawable2.setColor(resources.getColor(backgroundColor));
            confirmButton.setBackgroundDrawable(buttonDrawable2);
        }

    }

    private boolean check(CharSequence str) {
        return TextUtils.isEmpty(str);

    }

    @Override
    public void onClick(View v) {
        if (v == cancalButton) {
            dismiss();
            if (materialButtonListener != null) {
                materialButtonListener.onCancel((TextView) v);
            }
        } else if (v == confirmButton) {
            if (materialButtonListener != null) {
                materialButtonListener.onConfirm((TextView) v);
            }
        } else {

        }

    }


    public interface MaterialButtonListener {
        void onCancel(TextView v);

        void onConfirm(TextView v);
    }
}
