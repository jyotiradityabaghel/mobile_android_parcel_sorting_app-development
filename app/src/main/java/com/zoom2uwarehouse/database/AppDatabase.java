package com.zoom2uwarehouse.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.zoom2uwarehouse.model.Route_Pickup_Response_Data;


/**
 * Created by Mahendra Dabi on 03-12-2021.
 */
@Database(entities = {ScannedBookings.class},exportSchema = false,version = 1)
@TypeConverters(Converters.class)
public  abstract class AppDatabase extends RoomDatabase {
public abstract BookingsDao bookingsDao();
public abstract ScannedDao scannedDao();
}
