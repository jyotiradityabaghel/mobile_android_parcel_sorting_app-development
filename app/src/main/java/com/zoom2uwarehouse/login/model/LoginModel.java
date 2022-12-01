/*
 *
 *  *
 *  *
 *  * Licensed under the Apache   License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.zoom2uwarehouse.login.model;

import com.zoom2uwarehouse.bean_class.requestbean.LoginRequest;
import com.zoom2uwarehouse.bean_class.resposebean.LoginResponse;

/*
 * @author avadhesh
 * @date 2018/1/23
 */
public interface LoginModel {

        //Use for login method call back use of this is in class LoginModelImplementation
        interface OnLoginFinishedListener {
            void onEmailError(String code);

            void onPasswordError(String code);

            void onSuccess(LoginResponse loginResponse);

            void errorMsg(String errorMsg);


        }

        //Use for login method call back use of this is in class LoginPresenterImplementation
        void login(LoginRequest loginRequest, OnLoginFinishedListener listener);


}
