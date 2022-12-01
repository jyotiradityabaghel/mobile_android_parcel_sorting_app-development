package com.zoom2uwarehouse.bean_class.resposebean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.zoom2uwarehouse.model.Route_Pickup_Response_Data;

import java.util.List;

public class Route_Pickup_Response extends CommonResponse {


    @SerializedName("$id")
    @Expose
    private String id;
    @SerializedName("$type")
    @Expose
    private String type;
    @SerializedName("data")
    @Expose
    private List<Route_Pickup_Response_Data> data = null;

    @SerializedName("totalRun")
    @Expose
    private String TotalRun;


    public List<Route_Pickup_Response_Data> getData() {
        return data;
    }

    public void setData(List<Route_Pickup_Response_Data> data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTotalRun() {
        return TotalRun;
    }

    public void setTotalRun(String totalRun) {
        TotalRun = totalRun;
    }


}
