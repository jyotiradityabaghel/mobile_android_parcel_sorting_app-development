package com.zoom2uwarehouse.barcode.barcode_model;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zoom2uwarehouse.barcode.BarCode;
import com.zoom2uwarehouse.bean_class.resposebean.BarcodeResponse;
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

public class Barcode_model_imp implements Barcode_model_view {


    @Override
    public void get_data_response(Data_response_barcode listener, SharedPreferenceManager sharedPreferenceManager,String scan_code, String scanType) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
      //  "16665554321"
        ApiService apiService = retrofit.create(ApiService.class);

         ConnectableObservable<BarcodeResponse> dataResponse =
                apiService.barcode("Bearer"+" " +sharedPreferenceManager.getValue("access_token",""),scan_code, scanType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).replay();
        // Calling connect to start emission


        dataResponse.connect();
        dataResponse
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BarcodeResponse>() {
                    @Override
                    public void onNext(BarcodeResponse response) {
                        Log.d("avadhesh_login_error1", ""+response.getAwb());
                        if (response.getAwb()!=null)
                        {
                            listener.onSuccess(response, scan_code);
                        }else {
                            listener.errorMsg(response.getMessage());
                       }

                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        try {
                            ResponseBody body = ((HttpException) e).response().errorBody();
                            String read_locationFeed = body.string();
                            try {
                                JSONObject jsonobject = new JSONObject(read_locationFeed);
                                String  error = jsonobject.optString("Message");
                                listener.errorMsg(error);
                                Log.d("avadhesh_login_error1", ""+error + body.source());
                            } catch (JSONException e1) {
                                listener.errorMsg("Sorry! Something went wrong here, Please try again later");
                                Log.d("avadhesh_login_error1", "" +e1.getLocalizedMessage());
                            }
                            if (body != null) {
                                body.close();
                            }
                        } catch (IOException e1) {
                            listener.errorMsg(e1.getLocalizedMessage());
                        } catch (Exception e2){
                            e2.printStackTrace();
                            listener.errorMsg("Sorry! Something went wrong here, Please try again later");
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.d("avadhesh_barcode_error", "body.string()");
                        dataResponse.unsubscribeOn(Schedulers.io());
                    }

                });

    }



}
