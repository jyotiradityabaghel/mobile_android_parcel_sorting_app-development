package com.zoom2uwarehouse.logout.model;

import android.app.Activity;

/*
 * @author avadhesh
 * @date 2018/1/23
 */

public interface LogoutModelInterface {

    //Call when finish operation  use of this is in class LogoutModelImplementation
    interface OnFinishedListener {
        void onFinished();
    }

    //See the use in LogoutModelImplementation

    void setDialog(Activity activity,String pos, OnFinishedListener listener);

}
