package com.zoom2uwarehouse.re_assign_delivery;

import android.util.Log;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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

public class ReAssignDelivery_Interactor_Impl implements ReAssignDelivery_Interactor {

    private ApiService getApiService() {
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

        return apiService;
    }

    @Override
    public void get_TeamList_response(OnFinishListnerToGetCourierList listener,
                                      SharedPreferenceManager sharedPreferenceManager) {

        ApiService apiService = getApiService();

        ConnectableObservable<ResponseModel_TeamListFor_ReAssign> dataResponse =
                apiService.getTeamListForReassign("Bearer"+" " +sharedPreferenceManager.getValue("access_token",""))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).replay();
        // Calling connect to start emission


        dataResponse.connect();
        dataResponse
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ResponseModel_TeamListFor_ReAssign>() {
                    @Override
                    public void onNext(ResponseModel_TeamListFor_ReAssign response) {
                        Log.d("API Sucess", " -- TeamListForReassign -- "+response.isSuccess());
                        if (response.isSuccess())
                            listener.onSuccess(response);
                        else
                            listener.errorMsg(response.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (Utility.isHttpStatusCode(e, 400) || Utility.isHttpStatusCode(e, 404)) {

                            try {
                                ResponseBody body = ((HttpException) e).response().errorBody();
                                String read_locationFeed = body.string();
                                try {
                                    JSONObject jsonobject = new JSONObject(read_locationFeed);
                                    String  error = jsonobject.optString("Message");
                                    listener.errorMsg(error);
                                    Log.d("API Sucess", " -- TeamListForReassign -- "+error + body.source());
                                } catch (JSONException e1) {
                                    listener.errorMsg("Sorry! Something went wrong here, Please try again later");
                                    Log.d("API Sucess", " -- TeamListForReassign -- " +e1.getLocalizedMessage());
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
                        Log.d("avadhesh_barcode_error", "body.string()");
                        dataResponse.unsubscribeOn(Schedulers.io());
                    }

                });
    }

    @Override
    public void reassignCourier_response(OnFinishListnerOfReassignCourier listener, SharedPreferenceManager sharedPreferenceManager, String barcode, String courierId) {
        ApiService apiService = getApiService();

        ConnectableObservable<ResponseModel_ReAssignCourier> dataResponse =
                apiService.reassignToCourier("Bearer"+" " +sharedPreferenceManager.getValue("access_token",""), barcode, courierId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).replay();
        // Calling connect to start emission

        dataResponse.connect();
        dataResponse
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ResponseModel_ReAssignCourier>() {
                    @Override
                    public void onNext(ResponseModel_ReAssignCourier response) {
                        Log.d("API Sucess", " -- ReAssign to Courier -- "+response.isSuccess());
                        if (response.isSuccess())
                            listener.onSuccessOfReassignCourier(response, barcode);
                        else
                            listener.errorMsgOfReassignCourier(response.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (Utility.isHttpStatusCode(e, 400) || Utility.isHttpStatusCode(e, 404)) {

                            try {
                                ResponseBody body = ((HttpException) e).response().errorBody();
                                String read_locationFeed = body.string();
                                try {
                                    JSONObject jsonobject = new JSONObject(read_locationFeed);
                                    String  error = jsonobject.optString("Message");
                                    listener.errorMsgOfReassignCourier(error);
                                    Log.d("API Sucess", " -- ReAssign to Courier -- "+error + body.source());
                                } catch (JSONException e1) {
                                    listener.errorMsgOfReassignCourier("Sorry! Something went wrong here, Please try again later");
                                    Log.d("API Sucess", " -- ReAssign to Courier -- " +e1.getLocalizedMessage());
                                }
                                if (body != null) {
                                    body.close();
                                }
                            } catch (Exception e1) {
                                listener.errorMsgOfReassignCourier(e1.getLocalizedMessage());
                            }
                        }else {
                            listener.errorMsgOfReassignCourier("Sorry! Something went wrong here, Please try again later");
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
