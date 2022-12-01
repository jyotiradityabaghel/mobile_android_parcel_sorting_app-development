package com.zoom2uwarehouse.forgot_password.model;

import com.zoom2uwarehouse.bean_class.requestbean.LoginRequest;
import com.zoom2uwarehouse.bean_class.resposebean.ForgotResponse;

/**
 * @author avadhesh
 * Created by ubuntu on 14/2/18.
 */

public interface Forgot_model_interface {



    //Use for login method call back use of this is in class LoginModelImplementation
    interface OnFinishedListener {
        void onEmailError(String code);

        void onSuccess(ForgotResponse response);

        void errorMsg(String errorMsg);

        void show_Login_Server_Error(Throwable throwable);
    }

    //Use for login method call back use of this is in class LoginPresenterImplementation
    void forgot(LoginRequest loginRequest, Forgot_model_interface.OnFinishedListener listener);
}



