package com.zoom2uwarehouse.home.presenter_user_detail;

import com.zoom2uwarehouse.bean_class.resposebean.Details_Customer;
import com.zoom2uwarehouse.home.MainActivity;
import com.zoom2uwarehouse.home.User_detail_view;
import com.zoom2uwarehouse.home.model_user_detail.User_detail_m_imp;
import com.zoom2uwarehouse.home.model_user_detail.User_detail_m_view;
import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;

import java.util.List;

public class User_detail_p_imp implements User_detail_p_view ,User_detail_m_view.Finish_api{

    private User_detail_view detailView;
    private User_detail_m_view detailMView;

    public User_detail_p_imp(MainActivity mainActivity, User_detail_view user_detail_view, User_detail_m_imp user_detail_m_imp) {
          this.detailView=user_detail_view;
          this.detailMView=user_detail_m_imp;

    }

    @Override
    public void call_api_user(SharedPreferenceManager sharedPreferenceManager) {

        detailMView.run_api(this,sharedPreferenceManager);

    }


    @Override
    public void onSuccess(Details_Customer detailsCustomer) {
        detailView.user_success(detailsCustomer);
    }

    @Override
    public void errorMsg(String errorMsg) {
        detailView.user_error(errorMsg);

    }
}
