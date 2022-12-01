package com.zoom2uwarehouse.get_token;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zoom2uwarehouse.bean_class.requestbean.LoginRequest;
import com.zoom2uwarehouse.bean_class.resposebean.LoginResponse;
import com.zoom2uwarehouse.login.model.LoginModel;
import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;
import com.zoom2uwarehouse.util.Utility;
import com.zoom2uwarehouse.web_services_retrofit.ApiService;
import com.zoom2uwarehouse.web_services_retrofit.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetTokenClass {

    public  void getToken(SharedPreferenceManager sharedPreferenceManager) {
        String[] strings = sharedPreferenceManager.getValue_Login();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(strings[0]);
        loginRequest.setPassword(strings[1]);
        loginRequest.setGrant_type("password");
        //calling the presenter where we call the model class to validate data and after that return response.
        login(loginRequest,sharedPreferenceManager);

    }

    public void login(LoginRequest loginRequest,SharedPreferenceManager sharedPreferenceManager) {


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

        final ConnectableObservable<LoginResponse> loginResponse = apiService.token(loginRequest.getGrant_type(), loginRequest.getEmail(),
                loginRequest.getPassword())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).publish();
        // Calling connect to start emission
        loginResponse.connect();

        loginResponse.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginResponse>() {
                    @Override
                    public void onNext(LoginResponse tickets) {
                        Log.d("avadhesh_login_error1", "" + tickets.getAccess_token());
                        Utility.dismissLoadingDialog();
                        //sharedPreferenceManager.setValue_Login(etEmail.getText().toString(),etPassword.getText().toString());
                        sharedPreferenceManager.setValue("access_token",tickets.getAccess_token());
                        sharedPreferenceManager.setValue("token_type",tickets.getToken_type());
                        sharedPreferenceManager.setValue("roles",tickets.getRoles());
                        sharedPreferenceManager.setValue_int("carrierId",tickets.getCarrierId());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utility.dismissLoadingDialog();

                        if (Utility.isHttpStatusCode(e, 200) ||
                                Utility.isHttpStatusCode(e, 200)) {

                        } else if (Utility.isHttpStatusCode(e, 400) ||
                                Utility.isHttpStatusCode(e, 404)) {

                            try {
                                ResponseBody body = ((HttpException) e).response().errorBody();
                                String readlocationFeed = body.string();
                                Log.d("avadhesh_login_error1", "" + body.source());
                                Log.d("avadhesh_login_error2", "" + body.string());
                                try {

                                    JSONObject jsonobject = new JSONObject(readlocationFeed);
                                    String error = jsonobject.optString("error_description");
                                  //  listener.errorMsg(error);
                                } catch (JSONException e1) {
                                  //  listener.errorMsg("Sorry! Something went wrong here, Please try again later");
                                }

                                if (body != null) {
                                    body.close();
                                }
                            } catch (Exception e1) {
                               // /.errorMsg("Sorry! Something went wrong here, Please try again later");
                            }
                        } else {
                           // listener.errorMsg("Sorry! Something went wrong here, Please try again later");
                        }
                    }

                    @Override
                    public void onComplete() {
                        Utility.dismissLoadingDialog();
                    }

                });
    }
}

