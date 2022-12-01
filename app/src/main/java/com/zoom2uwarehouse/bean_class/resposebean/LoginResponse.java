package com.zoom2uwarehouse.bean_class.resposebean;

import com.google.gson.annotations.SerializedName;

public class LoginResponse extends CommonResponse {

    @SerializedName("access_token")

    private String access_token;

    @SerializedName("token_type")

    private String token_type;

    @SerializedName("roles")
    private String roles;

    @SerializedName("CarrierId")
    private int carrierId;

    @SerializedName("isTeamLeader")
    private boolean isTeamLeader;

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getRoles() {
        return roles;
    }

    public int getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(int carrierId) {
        carrierId = carrierId;
    }

    public boolean isTeamLeader() {
        return isTeamLeader;
    }

    public void setTeamLeader(boolean teamLeader) {
        isTeamLeader = teamLeader;
    }
}