package com.ningsheng.jietong.Utils;

import android.content.Context;

import com.ningsheng.jietong.Dialog.MaterialDialog;

/**
 * Created by ${張恆} on 2016/3/9.
 */
public class MateriaDialogUtil {
    private MateriaDialogUtil() {
    }

    public static MaterialDialog showNorlDialog(Context context, CharSequence title, CharSequence content, CharSequence confirmButton, MaterialDialog.MaterialButtonListener materialButtonListener) {
        return showNorlDialog(context, title, content, "取消", confirmButton, materialButtonListener);
    }

    public static MaterialDialog showNorlDialog(Context context, CharSequence title, CharSequence content, CharSequence cancalButton, CharSequence confirmButton, MaterialDialog.MaterialButtonListener materialButtonListener) {
        MaterialDialog materialDialog = new MaterialDialog(context, materialButtonListener, MaterialDialog.TYPE_MATERIAL_NORL);
        materialDialog.setTitle(title);
        materialDialog.setContentNorl(title);
        materialDialog.setCancalButton(cancalButton);
        materialDialog.setConfirmButton(confirmButton);
        materialDialog.show();
        return materialDialog;
    }

    public static MaterialDialog showEditDialog(Context context, CharSequence title, CharSequence content, CharSequence cancalButton, CharSequence confirmButton, MaterialDialog.MaterialButtonListener materialButtonListener) {
        MaterialDialog materialDialog = new MaterialDialog(context, materialButtonListener, MaterialDialog.TYPE_MATERIAL_EDIT);
        materialDialog.setTitle(title);
        materialDialog.setCancalButton(cancalButton);
        materialDialog.setConfirmButton(confirmButton);
        materialDialog.show();
        return materialDialog;
    }
}
