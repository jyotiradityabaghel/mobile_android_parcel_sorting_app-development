package com.zoom2uwarehouse.re_assign_delivery;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Arun on 17-july-2018.
 */

public class MyTeamList_Model {

    @SerializedName("courierId")
    private String courierId;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("companyName")
    private String companyName;

    private boolean setFlagToSelectItem;

    public String getCourierId() {
        return courierId;
    }

    public void setCourierId(String courierId) {
        this.courierId = courierId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isSetFlagToSelectItem() {
        return setFlagToSelectItem;
    }

    public void setSetFlagToSelectItem(boolean setFlagToSelectItem) {
        this.setFlagToSelectItem = setFlagToSelectItem;
    }
}
