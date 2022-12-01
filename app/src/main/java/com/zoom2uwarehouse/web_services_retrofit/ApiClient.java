package com.zoom2uwarehouse.web_services_retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zoom2uwarehouse.bean_class.requestbean.LoginRequest;

import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author avadhesh
 * Created 02/03/18.
 */

public class ApiClient {
    private static String TAG = ApiClient.class.getSimpleName();
    private static Retrofit retrofit = null;


    public static Retrofit getClient_login() {

       // if (okHttpClient == null)
         //   initOkHttp_login(loginRequest);


        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }


        return retrofit;
    }

    private static void initOkHttp_login(LoginRequest loginRequest) {
        int REQUEST_TIMEOUT = 60;
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(interceptor);

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            String data = URLEncoder.encode("grant_type", "UTF-8") + "=" + URLEncoder.encode(loginRequest.getGrant_type(), "UTF-8");
            data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(loginRequest.getEmail(), "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(loginRequest.getPassword(), "UTF-8");

            RequestBody body = RequestBody.create(mediaType,data);
            Request.Builder requestBuilder = original.newBuilder()
                        .post(body)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Cache-Control", "no-cache");
            Request request = requestBuilder.build();

            return chain.proceed(request);
        });

        OkHttpClient okHttpClient = httpClient.build();
    }

}
