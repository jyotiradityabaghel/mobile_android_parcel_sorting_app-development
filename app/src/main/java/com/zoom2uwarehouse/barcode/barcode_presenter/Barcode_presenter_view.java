package com.zoom2uwarehouse.barcode.barcode_presenter;

import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;

interface Barcode_presenter_view {


    void call_service(SharedPreferenceManager token,String scan_code, String scanType);

}
