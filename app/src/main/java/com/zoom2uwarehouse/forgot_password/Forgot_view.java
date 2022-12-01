package com.zoom2uwarehouse.forgot_password;

import com.zoom2uwarehouse.bean_class.resposebean.ForgotResponse;
import com.zoom2uwarehouse.util.Common_Interface;

/**@author avadhesh mourya
 * Created by ubuntu on 14/2/18.
 */

public interface Forgot_view extends Common_Interface {

    /**
     * Show error message when email field is empty.
     */
    void setEmailBlankError();


    /**
     * Show error message when email id is invalid.
     */
    void setEmailCorrectError();

    /**
     * After successful login pass the login result to UI.
     *
     * @param loginResponse LoginResponse model object is parameter which provided by network operations.
     */
    void showResult(ForgotResponse loginResponse);

    /**
     * Show the login error when credentials are wrong.
     *
     *
     * @param message    it is a string message which come in response.
     */
    void showErrorResult( String message);

    /**
     * Throw the error like {@link java.net.UnknownHostException}, {@link java.net.SocketTimeoutException}
     *
     * @param throwable object of {@link Throwable}
     */
    void show_Server_Error(Throwable throwable);
}
