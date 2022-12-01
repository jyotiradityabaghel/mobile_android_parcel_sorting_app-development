package com.zoom2uwarehouse.re_assign_delivery;

import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;

public interface ReAssign_Delivery_Presentor {

    void call_api_teamlist_for_reassign(SharedPreferenceManager prefManagerToGetToken);

    void call_api_for_reassign(SharedPreferenceManager prefManagerToGetToken, String barcode, String courierId);

}
