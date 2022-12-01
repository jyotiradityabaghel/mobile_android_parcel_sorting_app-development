package com.zoom2uwarehouse.home.model_user_detail;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zoom2uwarehouse.bean_class.resposebean.Details_Customer;
import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;
import com.zoom2uwarehouse.util.Utility;
import com.zoom2uwarehouse.web_services_retrofit.ApiService;
import com.zoom2uwarehouse.web_services_retrofit.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class User_detail_m_imp implements  User_detail_m_view {

    @Override
    public void run_api(Finish_api listener, SharedPreferenceManager sharedPreferenceManager) {
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

        final ConnectableObservable<Details_Customer> loginResponse =
                apiService.customers_details("Bearer"+" " +sharedPreferenceManager.getValue("access_token",""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).publish();
        // Calling connect to start emission
        loginResponse.connect();

        loginResponse.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Details_Customer>() {
                    @Override
                    public void onNext(Details_Customer tickets) {
                        Log.d("avadhesh_d_onSuccess", "" + tickets.getName());
                        listener.onSuccess(tickets);
                    }
                    @Override
                    public void onError(Throwable e) {
                        if (Utility.isHttpStatusCode(e, 200) ||
                                Utility.isHttpStatusCode(e, 200)) {
                            Log.d("avadhesh_login_error1", "" + e.getLocalizedMessage());
                        }else if (Utility.isHttpStatusCode(e, 400) ||
                                Utility.isHttpStatusCode(e, 404) ||
                                Utility.isHttpStatusCode(e, 401)) {

                            try {
                                ResponseBody body = ((HttpException) e).response().errorBody();
                                String read_Feed = body.string();
                                Log.d("avadhesh_login_error1", "" + body.source());
                                Log.d("avadhesh_login_error2", "" + body.string());
                                try {
                                    JSONObject jsonobject = new JSONObject(read_Feed);
                                    String error = jsonobject.optString("Message");
                                    listener.errorMsg(error);
                                } catch (JSONException e1) {
                                    listener.errorMsg("Sorry! Something went wrong here, Please try again later");
                                }
                                if (body != null) {
                                    body.close();
                                }
                            } catch (Exception e1) {
                                listener.errorMsg("Sorry! Something went wrong here, Please try again later");
                            }
                        }else {
                            listener.errorMsg("Sorry! Something went wrong here, Please try again later");
                        }
                    }

                    @Override
                    public void onComplete() {
                    }

                });
    }
}
