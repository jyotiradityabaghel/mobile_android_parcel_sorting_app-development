package com.zoom2uwarehouse.bean_class.resposebean;

import com.google.gson.annotations.SerializedName;

public class ForgotResponse {

    @SerializedName("success")

    private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
