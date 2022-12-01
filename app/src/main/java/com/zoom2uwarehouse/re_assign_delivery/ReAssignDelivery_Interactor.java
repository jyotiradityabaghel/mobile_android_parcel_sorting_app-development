package com.zoom2uwarehouse.re_assign_delivery;

import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;

public interface ReAssignDelivery_Interactor {

    //************ On-finish listner to get courier list **********
    interface OnFinishListnerToGetCourierList
    {
        void onSuccess(ResponseModel_TeamListFor_ReAssign response);

        void errorMsg(String errorMsg);
    }

    void get_TeamList_response(OnFinishListnerToGetCourierList listener, SharedPreferenceManager sharedPreferenceManager);

    //************ On-finish listner of Reassign courier **********
    interface OnFinishListnerOfReassignCourier
    {
        void onSuccessOfReassignCourier(ResponseModel_ReAssignCourier response, String barcode);

        void errorMsgOfReassignCourier(String errorMsg);
    }

    void reassignCourier_response(OnFinishListnerOfReassignCourier listener, SharedPreferenceManager sharedPreferenceManager,
                                  String barcode, String courierId);
}
