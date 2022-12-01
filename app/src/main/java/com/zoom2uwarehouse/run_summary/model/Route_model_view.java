package com.zoom2uwarehouse.run_summary.model;

import com.zoom2uwarehouse.bean_class.resposebean.Route_Pickup_Response;
import com.zoom2uwarehouse.bean_class.resposebean.Route_Progress_Response;
import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;

public interface Route_model_view {



    //Use for login method call back use of this is in class LoginModelImplementation
    interface Data_response {


        void onSuccess_pickup(Route_Pickup_Response loginResponse);

        void onSuccess_progress(Route_Progress_Response loginResponse);

        void errorMsg(String errorMsg);


    }

    //Use for login method call back use of this is in class LoginPresenterImplementation
    void get_data_response_pickup( Data_response listener,SharedPreferenceManager sharedPreferenceManager);

    void get_data_response_progress( Data_response listener,SharedPreferenceManager sharedPreferenceManager);

}
