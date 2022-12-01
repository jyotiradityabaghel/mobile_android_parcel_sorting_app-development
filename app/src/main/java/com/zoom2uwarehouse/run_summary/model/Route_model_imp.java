package com.zoom2uwarehouse.run_summary.model;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zoom2uwarehouse.bean_class.resposebean.Route_Pickup_Response;
import com.zoom2uwarehouse.bean_class.resposebean.Route_Progress_Response;
import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;
import com.zoom2uwarehouse.util.Utility;
import com.zoom2uwarehouse.web_services_retrofit.ApiService;
import com.zoom2uwarehouse.web_services_retrofit.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Route_model_imp implements Route_model_view {

    private static Retrofit retrofit = null;

    @Override
    public void get_data_response_pickup(Data_response listener, SharedPreferenceManager sharedPreferenceManager) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS).addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        final ConnectableObservable<Route_Pickup_Response> dataResponse =
                apiService.pickup("Bearer"+" " +sharedPreferenceManager.getValue("access_token",""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).replay();
        // Calling connect to start emission
        dataResponse.connect();

       dataResponse
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Route_Pickup_Response>() {
                    @Override
                    public void onNext(Route_Pickup_Response route_pickup_response) {

                        if (route_pickup_response == null) {
                            listener.errorMsg("Sorry! Something went wrong here, Please try again later");
                        } else  if (route_pickup_response.getTotalRun().equals("0")) {
                            listener.errorMsg("Records not available");
                        }else {
                            listener.onSuccess_pickup(route_pickup_response);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (Utility.isHttpStatusCode(e, 400) ||
                                Utility.isHttpStatusCode(e, 404)) {

                            try {
                                ResponseBody body = ((HttpException) e).response().errorBody();
                                String read_Feed = body.string();
                                Log.d("avadhesh_login_error1", "" + body.source());
                                Log.d("avadhesh_login_error2", "" + body.string());
                                try {

                                    JSONObject jsonobject = new JSONObject(read_Feed);
                                    String error ;

                                    error = jsonobject.optString("Message");

                                    listener.errorMsg(error);
                                } catch (JSONException e1) {
                                    listener.errorMsg(e1.getLocalizedMessage());
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
                        Log.d("avadhesh_login_error2", "body.string()");
                    }

                });

    }


    @Override
    public void get_data_response_progress( Data_response listener,SharedPreferenceManager sharedPreferenceManager) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS).addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        final ConnectableObservable<Route_Progress_Response> dataResponse = apiService.progress_run_summary("Bearer"+" " +sharedPreferenceManager.getValue("access_token",""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).replay();
        // Calling connect to start emission
        dataResponse.connect();

        dataResponse
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Route_Progress_Response>() {
                    @Override
                    public void onNext(Route_Progress_Response route_Progress_Responses) {

                        if (route_Progress_Responses == null) {
                            listener.errorMsg("Sorry! Something went wrong here, Please try again later");
                        } else  if (route_Progress_Responses.getTotalRun().equals("0")) {
                            listener.errorMsg("Records not available");
                        }else {
                            listener.onSuccess_progress(route_Progress_Responses);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (Utility.isHttpStatusCode(e, 400) ||
                                Utility.isHttpStatusCode(e, 404)) {

                            try {
                                ResponseBody body = ((HttpException) e).response().errorBody();
                                String read_Feed = body.string();
                                Log.d("avadhesh_login_error1", "" + body.source());
                                Log.d("avadhesh_login_error2", "" + body.string());
                                try {

                                    JSONObject jsonobject = new JSONObject(read_Feed);
                                    String error ;

                                    error = jsonobject.optString("Message");

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
