package com.zoom2uwarehouse.forgot_password.presenter;

import com.zoom2uwarehouse.bean_class.requestbean.LoginRequest;

/**@author avadhesh mourya
 * Created by ubuntu on 14/2/18.
 */

interface Forgot_presenter_interface {


    void validateCredentials(LoginRequest loginRequest);
}
