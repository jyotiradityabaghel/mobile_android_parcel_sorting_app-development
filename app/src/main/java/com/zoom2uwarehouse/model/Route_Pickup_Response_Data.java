package com.zoom2uwarehouse.model;

import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahendra Dabi on 03-12-2021.
 */
public class Route_Pickup_Response_Data {
    @SerializedName("runId")
    @Expose
    private String RunId;

    @SerializedName("driver")
    @Expose
    private String Driver;

    @SerializedName("totalBookingsOfRuns")
    @Expose
    private String TotalBookingsOfRuns;

    @SerializedName("eta")
    @Expose
    private String ETA;

    @SerializedName("stop")
    @Expose
    private String Stop;

    @SerializedName("area")
    @Expose
    private String Area;

    @SerializedName("isNewDriver")
    @Expose
    private boolean isNewDriver;

    @Ignore
    @SerializedName("zone")
    @Expose
    private String zone;

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public boolean isNewDriver() {
        return isNewDriver;
    }

    public String getRunId() {
        return RunId;
    }

    public void setRunId(String runId) {
        RunId = runId;
    }

    public String getDriver() {
        return Driver;
    }

    public void setDriver(String driver) {
        Driver = driver;
    }


    public String getTotalBookingsOfRuns() {
        return TotalBookingsOfRuns;
    }

    public void setTotalBookingsOfRuns(String totalBookingsOfRuns) {
        TotalBookingsOfRuns = totalBookingsOfRuns;
    }


    public String getPickupETA() {
        return ETA;
    }

    public void setPickupETA(String ETA) {
        ETA = ETA;
    }

    public String getStop() {
        return Stop;
    }

    public void setStop(String stop) {
        Stop = stop;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }



    public void setNewDriver(boolean newDriver) {
        isNewDriver = newDriver;
    }

    public String getETA() {
        return ETA;
    }


    public void setETA(String ETA) {
        this.ETA = ETA;
    }
}
