package com.zoom2uwarehouse.login.presenter;

import com.zoom2uwarehouse.bean_class.requestbean.LoginRequest;
import com.zoom2uwarehouse.bean_class.resposebean.LoginResponse;
import com.zoom2uwarehouse.login.LoginView;
import com.zoom2uwarehouse.login.model.LoginModel;
import com.zoom2uwarehouse.login.model.LoginModelImplementation;

/*
 * @author avadhesh
 * @date 2018/1/23
 */
public class LoginPresenterImplementation implements LoginPresenter, LoginModel.OnLoginFinishedListener{

    private LoginView loginView;
    private LoginModel login_Integrator;


    public LoginPresenterImplementation(LoginView loginView, LoginModelImplementation loginIntegrator
                                       ) {
        this.loginView = loginView;
        this.login_Integrator = loginIntegrator;

    }

    @Override
    public void validateCredentials(LoginRequest loginRequest) {

        if (loginView != null) {
            loginView.showProgress();
        }
             login_Integrator.login(loginRequest, this);

    }

    @Override
    public void onEmailError(String errorType) {
        if (loginView != null) {
            loginView.hideProgress();

            if (errorType.equals("1")) {
                loginView.setEmailBlankError();
            } else {
                loginView.setEmailCorrectError();
            }
        }
    }

    @Override
    public void onPasswordError(String errorType) {
        if (loginView != null) {
            loginView.hideProgress();

            if (errorType.equals("1")) {
                loginView.setPasswordBlankError();
            } else {
                loginView.setPasswordMinimumBlankError();
            }
        }
    }

    @Override
    public void onSuccess(LoginResponse loginResponses) {
            loginView.hideProgress();
            loginView.showLoginResult(loginResponses);
    }

    @Override
    public void errorMsg(String errorMsg) {
        loginView.hideProgress();
        loginView.showLoginErrorResult(errorMsg);
    }

}
