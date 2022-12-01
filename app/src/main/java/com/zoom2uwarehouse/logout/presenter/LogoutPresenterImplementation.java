package com.zoom2uwarehouse.logout.presenter;

import android.app.Activity;

import com.zoom2uwarehouse.logout.model.LogoutModelImplementation;
import com.zoom2uwarehouse.logout.model.LogoutModelInterface;
import com.zoom2uwarehouse.logout.Logoutview;

/*
 * @author avadhesh
 * @date 2018/1/23
 */
public class LogoutPresenterImplementation implements LogoutPresenterInterface,LogoutModelInterface.OnFinishedListener

{
    private Logoutview mainView;
    private LogoutModelImplementation mainModel;
    private Activity activity;

    public LogoutPresenterImplementation(Activity activity, Logoutview mainView, LogoutModelImplementation mainModel) {
        this.mainView = mainView;
        this.mainModel = mainModel;
        this.activity = activity;
    }


    @Override
    public void success_call(String msg) {
      mainModel.setDialog(activity,msg ,this);

    }


    @Override
    public void onFinished() {
        mainView.Logout_success();
    }
}
