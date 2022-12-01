package com.zoom2uwarehouse.barcode.barcode_model;

import com.zoom2uwarehouse.bean_class.resposebean.BarcodeResponse;
import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;

public interface Barcode_model_view {


     interface Data_response_barcode
    {
        void onSuccess(BarcodeResponse response, String scan_code);

        void errorMsg(String errorMsg);
    }


    void get_data_response(Data_response_barcode listener, SharedPreferenceManager sharedPreferenceManager,String scan_code, String scanType);
}
