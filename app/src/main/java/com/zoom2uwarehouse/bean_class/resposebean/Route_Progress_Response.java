package com.zoom2uwarehouse.bean_class.resposebean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Route_Progress_Response {
    @SerializedName("$id")
    @Expose
    private String id;
    @SerializedName("$type")
    @Expose
    private String type;
    @SerializedName("data")
    @Expose
    private List<Route_Process_Response_Data> data = null;

    @SerializedName("totalRun")
    @Expose
    private String TotalRun;

    @SerializedName("zone")
    @Expose
    private String zone;

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public List<Route_Process_Response_Data> getData() {
        return data;
    }

    public void setData(List<Route_Process_Response_Data> data) {
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

    public class Route_Process_Response_Data {

        @SerializedName("runId")
        @Expose
        private String RunId;

        @SerializedName("driver")
        @Expose
        private String Driver;


        @SerializedName("droppedOffCount")
        @Expose
        private String DroppedOffCount;


        @SerializedName("pickedupCount")
        @Expose
        private String PickedupCount;


        @SerializedName("totalBookingsOfRuns")
        @Expose
        private String TotalBookingsOfRuns;


        @SerializedName("attemptedDeliveries")
        @Expose
        private String AttemptedDeliveries;


        @SerializedName("pickupETA")
        @Expose
        private String PickupETA;


        @SerializedName("stop")
        @Expose
        private String Stop;


        @SerializedName("area")
        @Expose
        private String Area;

        @SerializedName("isNewDriver")
        @Expose
        private boolean IsNewDriver;

        @SerializedName("zone")
        @Expose
        private String zone;

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
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

        public String getDroppedOffCount() {
            return DroppedOffCount;
        }

        public void setDroppedOffCount(String droppedOffCount) {
            DroppedOffCount = droppedOffCount;
        }

        public String getPickedupCount() {
            return PickedupCount;
        }

        public void setPickedupCount(String pickedupCount) {
            PickedupCount = pickedupCount;
        }

        public String getTotalBookingsOfRuns() {
            return TotalBookingsOfRuns;
        }

        public void setTotalBookingsOfRuns(String totalBookingsOfRuns) {
            TotalBookingsOfRuns = totalBookingsOfRuns;
        }

        public String getAttemptedDeliveries() {
            return AttemptedDeliveries;
        }

        public void setAttemptedDeliveries(String attemptedDeliveries) {
            AttemptedDeliveries = attemptedDeliveries;
        }

        public String getPickupETA() {
            return PickupETA;
        }

        public void setPickupETA(String pickupETA) {
            PickupETA = pickupETA;
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

        public boolean isNewDriver() {
            return IsNewDriver;
        }

        public void setNewDriver(boolean newDriver) {
            IsNewDriver = newDriver;
        }
    }

}
