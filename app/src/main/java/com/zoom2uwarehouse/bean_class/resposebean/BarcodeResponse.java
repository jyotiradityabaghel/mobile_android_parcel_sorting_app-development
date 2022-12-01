package com.zoom2uwarehouse.bean_class.resposebean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.zoom2uwarehouse.util.Utility;

import java.util.ArrayList;

public class BarcodeResponse extends CommonResponse{

    @SerializedName("success")
    @Expose
    private String success;

    @SerializedName("awb")
    @Expose
    private String awb;

    @SerializedName("runId")
    @Expose
    private String RunId;

    @SerializedName("area")
    @Expose
    private String area;


    @SerializedName("stop")
    @Expose
    private String stop;

    @SerializedName("dropContactName")
    @Expose
    private String contact;

    @SerializedName("dropStreetName")
    @Expose
    private String DropStreetName ;

    @SerializedName("dropSuburb")
    @Expose
    private String DropSuburb ;

    @SerializedName("dropState")
    @Expose
    private String DropState ;

    @SerializedName("dropPostcode")
    @Expose
    private String DropPostcode ;

    @SerializedName("driver")
    @Expose
    private String Driver ;

    @SerializedName("pieces")
    @Expose
    private ArrayList<PieceObject> pieces;

    @SerializedName("pickupDateTime")
    @Expose
    private String pickupDateTime;

    @SerializedName("dropDateTime")
    @Expose
    private String dropDateTime;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("runType")
    @Expose
    private String runType;

    @SerializedName("runNumber")
    @Expose
    private String runNumber;


    @SerializedName("zone")
    @Expose
    public String  zone;

    @SerializedName("totalStops")
    @Expose
    public String totalStops;

    public String getTotalStops() {
        return totalStops;
    }

    public void setTotalStops(String totalStops) {
        this.totalStops = totalStops;
    }

    public String getDropSuburb() {
        return DropSuburb;
    }

    public ArrayList<PieceObject> getPieces() {
        return pieces;
    }

    public String getAwb() {
        return awb;
    }


    public String getRunId() {
        return RunId;
    }

    public String getArea() {
        return area;
    }


    public String getStop() {
        return stop;
    }

    public String getContact() {
        return contact;
    }


    public String getDropStreetName() {
        return DropStreetName;
    }

    public String getDropState() {
        return DropState;
    }

    public String getDropPostcode() {
        return DropPostcode;
    }

    public String getDriver() {
        return Driver;
    }

    public String getSuccess() {
        return success;
    }


    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getPickupDateTime() {
        this.pickupDateTime = Utility.getDateTimeFromServer(pickupDateTime);
        return pickupDateTime;
    }

    public String getDropDateTime() {
        this.dropDateTime = Utility.getDateTimeFromServer(dropDateTime);
        return dropDateTime;
    }

    public String getStatus() {
        return status;
    }

    public String getRunNumber() {
        return runNumber;
    }
}
