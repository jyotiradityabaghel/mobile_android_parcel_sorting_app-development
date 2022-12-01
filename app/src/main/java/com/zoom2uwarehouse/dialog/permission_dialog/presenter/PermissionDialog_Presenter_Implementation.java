package com.zoom2uwarehouse.dialog.permission_dialog.presenter;

import android.app.Activity;

import com.zoom2uwarehouse.dialog.permission_dialog.model.PermissionDialog_Model_Implementation;
import com.zoom2uwarehouse.dialog.permission_dialog.model.PermissionDialogModelInt;
import com.zoom2uwarehouse.dialog.permission_dialog.PermissionDialog_view;
/**@author avadhesh mourya
 * Created by ubuntu on 1/2/18.
 */

public class PermissionDialog_Presenter_Implementation implements PermissionDialogPresenterInt, PermissionDialogModelInt.OnFinishedListener {

    private PermissionDialog_view permissionDialog_view;
    private PermissionDialog_Model_Implementation permissionDialogModel;
    private Activity activity;

    public PermissionDialog_Presenter_Implementation(Activity activity, PermissionDialog_view permissionDialog_view, PermissionDialog_Model_Implementation permissionDialogModel) {
        this.permissionDialog_view=permissionDialog_view;
        this.permissionDialogModel=permissionDialogModel;
        this.activity = activity;
    }

    @Override
    public void onFinished(int pos) {
        switch (pos)
        {
            case 1:
                permissionDialog_view.action1();
                break;
            case 2:
                permissionDialog_view.action2();
                break;
        }
    }

    @Override
    public void call_dialog(String string1,String string2,String string3,int pos) {
        permissionDialogModel.setDialog(activity,string1,string2,string3, pos,this);
    }
}
