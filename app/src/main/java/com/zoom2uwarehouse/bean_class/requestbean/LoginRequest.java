package com.zoom2uwarehouse.bean_class.requestbean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {


    @SerializedName("username")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("grant_type")
    @Expose
    private String grant_type;

    public LoginRequest(String email, String password, String grant_type) {
        super();
        this.email = email;
        this.password = password;
        this.grant_type=grant_type;

    }

    public LoginRequest() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }
}