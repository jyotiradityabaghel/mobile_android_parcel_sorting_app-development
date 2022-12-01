package com.zoom2uwarehouse.login.presenter;

import com.zoom2uwarehouse.bean_class.requestbean.LoginRequest;

/*
 * @author avadhesh
 * @date 2018/1/23
 */
interface LoginPresenter {
    //for validate credentials in login file like email ,password this is also use for sending value to the server

    void validateCredentials(LoginRequest loginRequest);


}
