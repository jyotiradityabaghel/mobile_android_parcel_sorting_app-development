package com.zoom2uwarehouse.forgot_password.model;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zoom2uwarehouse.bean_class.requestbean.LoginRequest;
import com.zoom2uwarehouse.bean_class.resposebean.ForgotResponse;
import com.zoom2uwarehouse.util.Utility;
import com.zoom2uwarehouse.web_services_retrofit.ApiService;
import com.zoom2uwarehouse.web_services_retrofit.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author avadhesh
 * Created by ubuntu on 14/2/18.
 */

public class Forgot_model_implementation implements Forgot_model_interface{

    @Override
    public void forgot(LoginRequest loginRequest, final OnFinishedListener listener) {

        //validate the email is right pattern ,not empty.
        if (isDataValid(loginRequest.getEmail(),listener)) {
            //get the server response
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);

            final ConnectableObservable<ForgotResponse> ForgotResponse = apiService.ForgotPassword(loginRequest.getEmail())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).publish();
            // Calling connect to start emission
            ForgotResponse.connect();

            ForgotResponse
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<ForgotResponse>() {
                        @Override
                        public void onNext(ForgotResponse response) {
                            Log.d("avadhesh_login_error1", "" + response.getSuccess());

                            if (response.getSuccess().equals("true")) {
                                listener.onSuccess(response);
                            }else {
                                listener.errorMsg("Sorry! Something went wrong here, Please try again later");

                            }
                        }
                        @Override
                        public void onError(Throwable e) {
                            if (Utility.isHttpStatusCode(e, 200) ||
                                    Utility.isHttpStatusCode(e, 200)) {

                            }else if (Utility.isHttpStatusCode(e, 400) ||
                                    Utility.isHttpStatusCode(e, 404)) {

                                try {
                                    ResponseBody body = ((HttpException) e).response().errorBody();
                                    String read_locationFeed = body.string();
                                    Log.d("avadhesh_login_error1", "" + body.source());
                                    Log.d("avadhesh_login_error2", "" + body.string());
                                    try {

                                        JSONObject jsonobject = new JSONObject(read_locationFeed);
                                        String error = jsonobject.optString("message");
                                        listener.errorMsg(error);
                                    } catch (JSONException e1) {
                                        listener.errorMsg("Sorry! Something went wrong here, Please try again later");
                                    }
                                    if (body != null) {
                                        body.close();
                                    }
                                } catch (Exception e1) {
                                    listener.errorMsg(e1.getLocalizedMessage());
                                }
                            }else {
                                listener.errorMsg("Sorry! Something went wrong here, Please try again later");
                            }
                        }

                        @Override
                        public void onComplete() {
                            Log.d("avadhesh_login_error2", "body.string()");
                        }

                    });

        }
    }



    private boolean isDataValid(String email, OnFinishedListener validationErrorListener) {
        if (TextUtils.isEmpty(email)) {
            validationErrorListener.onEmailError("1");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            validationErrorListener.onEmailError("2");
            return false;
        }  else {
            return true;
        }
    }


}
