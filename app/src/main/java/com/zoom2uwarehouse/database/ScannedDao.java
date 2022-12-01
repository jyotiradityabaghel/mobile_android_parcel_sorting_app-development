package com.zoom2uwarehouse.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Created by Mahendra Dabi on 04-12-2021.
 */
@Dao
public interface ScannedDao {
    @Query("SELECT * FROM scanned where runId=:runId")
    ScannedBookings getScannedRecordByRunId(String runId);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ScannedBookings> list);


    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertScanned(ScannedBookings scannedBookings);

    @Query("DELETE FROM scanned")
    void deleteAll();
}
