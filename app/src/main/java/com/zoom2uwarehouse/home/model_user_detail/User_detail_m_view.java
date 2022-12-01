package com.zoom2uwarehouse.home.model_user_detail;

import com.zoom2uwarehouse.bean_class.resposebean.Details_Customer;
import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;

import java.util.List;

public interface User_detail_m_view {

    interface Finish_api {
        void onSuccess(Details_Customer details_customer);

        void errorMsg(String errorMsg);
    }

    void run_api(Finish_api finish_api, SharedPreferenceManager sharedPreferenceManager);
}
