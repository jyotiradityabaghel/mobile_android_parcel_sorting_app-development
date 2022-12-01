package com.zoom2uwarehouse.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zoom2uwarehouse.R;

/*
 * @author avadhesh
 * @date 2018/1/23
 */
class DialogActivity   {

    private static Dialog dialog_custom;


    public static void alertDialog_permission(final Context con, String alertMsg){
        try{
            if(dialog_custom != null)
                if(dialog_custom.isShowing())
                    dialog_custom.dismiss();
        }catch(Exception e){
            e.printStackTrace();
        }

        try {
            if(dialog_custom != null)
                dialog_custom = null;
            dialog_custom = new Dialog(con);
            dialog_custom.setCancelable(false);
            dialog_custom.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#90000000")));
            dialog_custom.setContentView(R.layout.dialog_permission);

            Window window = dialog_custom.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            android.view.WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.CENTER;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);




            TextView dialog_customMsg =  dialog_custom.findViewById(R.id.textView10);

            dialog_customMsg.setText(alertMsg);
            TextView txt_btn_cancel= dialog_custom.findViewById(R.id.textView11);
            txt_btn_cancel.setOnClickListener(v -> dialog_custom.dismiss());
            TextView txt_btn_logout = dialog_custom.findViewById(R.id.textView12);
            txt_btn_logout.setOnClickListener(v -> dialog_custom.dismiss());

            dialog_custom.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
