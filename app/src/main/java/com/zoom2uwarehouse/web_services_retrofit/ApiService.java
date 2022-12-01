package com.zoom2uwarehouse.web_services_retrofit;

import com.zoom2uwarehouse.bean_class.resposebean.BarcodeResponse;
import com.zoom2uwarehouse.bean_class.resposebean.Details_Customer;
import com.zoom2uwarehouse.bean_class.resposebean.ForgotResponse;
import com.zoom2uwarehouse.bean_class.resposebean.LoginResponse;
import com.zoom2uwarehouse.bean_class.resposebean.Route_Pickup_Response;
import com.zoom2uwarehouse.bean_class.resposebean.Route_Progress_Response;
import com.zoom2uwarehouse.re_assign_delivery.ResponseModel_ReAssignCourier;
import com.zoom2uwarehouse.re_assign_delivery.ResponseModel_TeamListFor_ReAssign;

import java.util.List;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.zoom2uwarehouse.web_services_retrofit.Constants.BookingDetailsByAWB;
import static com.zoom2uwarehouse.web_services_retrofit.Constants.Customers_Details;
import static com.zoom2uwarehouse.web_services_retrofit.Constants.ForgotPassword;
import static com.zoom2uwarehouse.web_services_retrofit.Constants.pickup;
import static com.zoom2uwarehouse.web_services_retrofit.Constants.progress_run_summary;
import static com.zoom2uwarehouse.web_services_retrofit.Constants.reAssignToSelectedCourier;
import static com.zoom2uwarehouse.web_services_retrofit.Constants.teamListAPIForReAssign;
import static com.zoom2uwarehouse.web_services_retrofit.Constants.token;

public interface ApiService  {

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(token)
    Observable<LoginResponse> token(@Field("grant_type") String page, @Field("username") String order, @Field("password") String author);

    @GET(progress_run_summary)
    Observable<Route_Progress_Response> progress_run_summary(@Header("Authorization") String page );

    @GET(pickup)
    Observable<Route_Pickup_Response> pickup(@Header("Authorization") String page );

    @GET(BookingDetailsByAWB)
    Observable<BarcodeResponse> barcode( @Header("Authorization") String page, @Query("barcode") String awb, @Query("action") String action) ;

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @POST(ForgotPassword)
    Observable<ForgotResponse> ForgotPassword(@Query("username") String username);

    @GET(Customers_Details)
    Observable<Details_Customer> customers_details(@Header("Authorization") String page );

    @GET(teamListAPIForReAssign)
    Observable<ResponseModel_TeamListFor_ReAssign> getTeamListForReassign(@Header("Authorization") String token) ;

    @POST(reAssignToSelectedCourier)
    Observable<ResponseModel_ReAssignCourier> reassignToCourier(@Header("Authorization") String token, @Query("barcode") String barcode, @Query("toCourierId") String courierId) ;

}
