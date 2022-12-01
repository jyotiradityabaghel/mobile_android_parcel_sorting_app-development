package com.zoom2uwarehouse.dialog.permission_dialog.model;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zoom2uwarehouse.R;

/**
 * @author avadhesh
 * Created by ubuntu on 1/2/18.
 */

public class PermissionDialog_Model_Implementation implements PermissionDialogModelInt {
    private static Dialog dialog_custom;

    @Override
    public void setDialog(final Activity activity, String string1, String string2, String string3, final int pos, final OnFinishedListener listener) {
        try {
            if (dialog_custom != null)
                if (dialog_custom.isShowing())
                    dialog_custom.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (dialog_custom != null)
                dialog_custom = null;
            dialog_custom = new Dialog(activity);
            dialog_custom.setCancelable(false);
            dialog_custom.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#90000000")));
            dialog_custom.setContentView(R.layout.dialog_permission);

            Window window = dialog_custom.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            android.view.WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.CENTER;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);


            TextView dialog_custom_title = dialog_custom.findViewById(R.id.textView13);

            dialog_custom_title.setText(string1);

            TextView dialog_customMsg = dialog_custom.findViewById(R.id.textView14);

            dialog_customMsg.setText(string2 + " " + string3);
            TextView txt_btn_cancel = dialog_custom.findViewById(R.id.textView15);
            txt_btn_cancel.setOnClickListener(v -> {
                dialog_custom.dismiss();
                listener.onFinished(pos);
            });


            dialog_custom.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
