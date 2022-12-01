package com.zoom2uwarehouse.run_summary.presenter;

import com.zoom2uwarehouse.bean_class.resposebean.Route_Pickup_Response;
import com.zoom2uwarehouse.bean_class.resposebean.Route_Progress_Response;
import com.zoom2uwarehouse.run_summary.Run_View;
import com.zoom2uwarehouse.run_summary.model.Route_model_imp;
import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;

public class Run_presenter_impl implements Run_presenter_view ,Route_model_imp.Data_response{


    private Run_View run_view;
    private Route_model_imp route_model_imp;

    public Run_presenter_impl(Run_View run_view, Route_model_imp route_model_imp) {
        this. run_view=run_view;
        this.route_model_imp=route_model_imp;
    }

    @Override
    public void call_for_pickup( SharedPreferenceManager sharedPreferenceManager) {
        route_model_imp.get_data_response_pickup(this,sharedPreferenceManager);
        run_view.showProgress();
    }
    @Override
    public void call_for_progress( SharedPreferenceManager sharedPreferenceManager) {
        route_model_imp.get_data_response_progress(this,sharedPreferenceManager);
        run_view.showProgress();
    }

    @Override
    public void onSuccess_pickup(Route_Pickup_Response loginResponse ) {
              run_view.success_Pickup(loginResponse);
              run_view.hideProgress();
    }

    @Override
    public void onSuccess_progress(Route_Progress_Response loginResponse ) {
        run_view.success_progress(loginResponse);
        run_view.hideProgress();
    }

    @Override
    public void errorMsg(String errorMsg) {
        run_view.error(errorMsg);
        run_view.hideProgress();
    }
}
