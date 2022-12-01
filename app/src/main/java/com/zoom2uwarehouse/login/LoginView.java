package com.zoom2uwarehouse.login;

import com.zoom2uwarehouse.bean_class.resposebean.LoginResponse;
import com.zoom2uwarehouse.util.Common_Interface;

/*
 * @author avadhesh
 * @date 2018/1/23
 */
public interface LoginView extends Common_Interface {

    /**
     * Show error message when email field is empty.
     */
    void setEmailBlankError();


    /**
     * Show error message when email id is invalid.
     */
    void setEmailCorrectError();

    /**
     * Show error message when password field is empty.
     */
    void setPasswordBlankError();


    /**
     * Show error message when password field is invalid.
     */
    void setPasswordMinimumBlankError();

    /**
     * After successful login pass the login result to UI.
     *
     * @param loginResponses LoginResponse model object is parameter which provided by network operations.
     */
    void showLoginResult(LoginResponse loginResponses);

    /**
     * Show the login error when credentials are wrong.
     *
     *
     * @param message    it is a string message which come in response.
     */
    void showLoginErrorResult( String message);


}
