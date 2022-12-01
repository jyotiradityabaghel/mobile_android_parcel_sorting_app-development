package com.zoom2uwarehouse.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;

/**
 * Created by Mahendra Dabi on 04-12-2021.
 */
@Entity(tableName = "scanned")
public class ScannedBookings {
    @PrimaryKey
    @NonNull
    String  runId;

    @TypeConverters(Converters.class)
    ArrayList<String> awbList=new ArrayList<>();

    @NonNull
    public String getRunId() {
        return runId;
    }

    public void setRunId(@NonNull String runId) {
        this.runId = runId;
    }

    public ArrayList<String> getAwbList() {
        return awbList;
    }

    public void setAwbList(ArrayList<String> awbList) {
        this.awbList = awbList;
    }
}
