package com.zoom2uwarehouse.re_assign_delivery;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseModel_TeamListFor_ReAssign {

    @SerializedName("success")
    private boolean success;
    @SerializedName("couriers")
    private List<MyTeamList_Model> courierArray;

    @SerializedName("currentCourierId")
    private String currentCourierId;

    @SerializedName("message")
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<MyTeamList_Model> getCourierArray() {
        return courierArray;
    }

    public void setCourierArray(List<MyTeamList_Model> courierArray) {
        this.courierArray = courierArray;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrentCourierId() {
        return currentCourierId;
    }

    public void setCurrentCourierId(String currentCourierId) {
        this.currentCourierId = currentCourierId;
    }
}
