package com.zoom2uwarehouse.dialog.permission_dialog.model;

import android.app.Activity;



/**@author avadhesh mourya
 * Created by ubuntu on 1/2/18.
 */

public interface PermissionDialogModelInt {

    interface OnFinishedListener {
        void onFinished(int i);
    }

    void setDialog(Activity activity, String string1,String string2,String string3,int pos, PermissionDialogModelInt.OnFinishedListener listener);

}
