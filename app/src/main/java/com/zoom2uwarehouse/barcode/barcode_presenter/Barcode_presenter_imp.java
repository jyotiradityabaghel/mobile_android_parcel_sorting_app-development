package com.zoom2uwarehouse.barcode.barcode_presenter;

import com.zoom2uwarehouse.barcode.Barcode_view;
import com.zoom2uwarehouse.barcode.barcode_model.Barcode_model_imp;
import com.zoom2uwarehouse.barcode.barcode_model.Barcode_model_view;
import com.zoom2uwarehouse.bean_class.resposebean.BarcodeResponse;
import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;

public class Barcode_presenter_imp implements Barcode_presenter_view,Barcode_model_view.Data_response_barcode {

     private Barcode_view barcode_view;
     private Barcode_model_view barcode_model_view;


    public Barcode_presenter_imp( Barcode_view barcode_view, Barcode_model_imp barcode_model_imp) {
        this.barcode_view=barcode_view;
        this.barcode_model_view=barcode_model_imp;
    }

    @Override
    public void call_service(SharedPreferenceManager preferenceManager,String scan_code, String scanType) {
        barcode_view.showProgress();
        barcode_model_view.get_data_response(this,preferenceManager,scan_code, scanType);
    }

    @Override
    public void onSuccess(BarcodeResponse response, String scan_code) {
         barcode_view.hideProgress();
         barcode_view.success_response(response, scan_code);
    }

    @Override
    public void errorMsg(String errorMsg) {
        barcode_view.hideProgress();
        barcode_view.error(errorMsg);
    }
}
