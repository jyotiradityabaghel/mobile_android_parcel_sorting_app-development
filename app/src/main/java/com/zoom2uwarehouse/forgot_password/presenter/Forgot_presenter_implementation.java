package com.zoom2uwarehouse.forgot_password.presenter;

import com.zoom2uwarehouse.bean_class.resposebean.ForgotResponse;
import com.zoom2uwarehouse.forgot_password.Forgot_view;
import com.zoom2uwarehouse.forgot_password.model.Forgot_model_implementation;
import com.zoom2uwarehouse.forgot_password.model.Forgot_model_interface;
import com.zoom2uwarehouse.bean_class.requestbean.LoginRequest;

/**@author avadhesh mourya
 * Created by ubuntu on 14/2/18.
 */

public class Forgot_presenter_implementation implements Forgot_presenter_interface ,Forgot_model_interface.OnFinishedListener{

    private Forgot_view forgotView;
    private Forgot_model_implementation modelImplementation;



    public Forgot_presenter_implementation(Forgot_view forgotView1,Forgot_model_implementation modelImplementation1) {

        this.forgotView = forgotView1;
        this.modelImplementation = modelImplementation1;

    }

    @Override
    public void validateCredentials(LoginRequest loginRequest) {
        if (forgotView != null) {
            forgotView.showProgress();
        }
        modelImplementation.forgot(loginRequest, this);
    }

    @Override
    public void onEmailError(String code) {
        if (forgotView != null) {
            forgotView.hideProgress();

            if (code.equals("1")) {
                forgotView.setEmailBlankError();
            } else {
                forgotView.setEmailCorrectError();
            }
        }
    }

    @Override
    public void onSuccess(ForgotResponse loginResponse) {
        forgotView.hideProgress();
        forgotView.showResult(loginResponse);

    }

    @Override
    public void errorMsg(String errorMsg) {
        forgotView.hideProgress();
        forgotView.showErrorResult(errorMsg);
    }

    @Override
    public void show_Login_Server_Error(Throwable throwable) {
        forgotView.hideProgress();
        forgotView.show_Server_Error(throwable);
    }
}
