package com.zoom2uwarehouse.re_assign_delivery;

import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;

public class ReAssignDelivery_Presentor_Impl implements ReAssign_Delivery_Presentor,
        ReAssignDelivery_Interactor.OnFinishListnerToGetCourierList,
        ReAssignDelivery_Interactor.OnFinishListnerOfReassignCourier {

    ReAssignDelivery_View reAssignDelivery_view;
    ReAssignDelivery_Interactor_Impl reAssignDelivery_interactor_impl;

    public ReAssignDelivery_Presentor_Impl(ReAssignDelivery_View reAssignDelivery_view, ReAssignDelivery_Interactor_Impl reAssignDelivery_interactor_impl) {
        this.reAssignDelivery_view = reAssignDelivery_view;
        this.reAssignDelivery_interactor_impl = reAssignDelivery_interactor_impl;
    }

    @Override
    public void call_api_teamlist_for_reassign(SharedPreferenceManager prefManagerToGetToken) {
        reAssignDelivery_view.showProgress();
        reAssignDelivery_interactor_impl.get_TeamList_response(this, prefManagerToGetToken);
    }

    @Override
    public void call_api_for_reassign(SharedPreferenceManager prefManagerToGetToken, String barcode, String courierId) {
        reAssignDelivery_view.showProgress();
        reAssignDelivery_interactor_impl.reassignCourier_response(this, prefManagerToGetToken, barcode, courierId);
    }

    @Override
    public void onSuccess(ResponseModel_TeamListFor_ReAssign response) {
        reAssignDelivery_view.success_response(response);
        reAssignDelivery_view.hideProgress();
    }

    @Override
    public void errorMsg(String errorMsg) {
        reAssignDelivery_view.hideProgress();
        reAssignDelivery_view.error(errorMsg);
    }

    @Override
    public void onSuccessOfReassignCourier(ResponseModel_ReAssignCourier response, String barcode) {
        reAssignDelivery_view.success_response_To_ReassignCourier(response, barcode);
        reAssignDelivery_view.hideProgress();
    }

    @Override
    public void errorMsgOfReassignCourier(String errorMsg) {
        reAssignDelivery_view.hideProgress();
        reAssignDelivery_view.error(errorMsg);
    }
}
