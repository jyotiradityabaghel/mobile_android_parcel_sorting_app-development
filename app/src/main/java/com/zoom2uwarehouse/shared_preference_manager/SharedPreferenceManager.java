package com.zoom2uwarehouse.shared_preference_manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author avadhesh mourya
 * Created by ubuntu on 12/2/18.
 */

public class SharedPreferenceManager {

    private Context mContext;
    private String Key_email = "email";
    private String Key_password= "password";
    // TODO: make these members private.
    private SharedPreferences sharedPreferences;
    private Editor mEditor;

    public SharedPreferenceManager(Context ctx, String prefFileName) {
        mContext = ctx;
        sharedPreferences = mContext.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        mEditor = sharedPreferences.edit();
    }

    /***
     * Set a value for the key
     ****/
    public void setValue(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }
    /***
     * Set a value for the key
     ****/
    public void setValue_int(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }
    /***
     * Set a value for the key
     ****/
    public void setValue(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    /***
     * Set a value for the key
     ****/
    public void setValue(String key, double value) {
        setValue(key, Double.toString(value));
    }

    /***
     * Set a value for the key
     ****/
    public void setValue(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    /****
     * Gets the value from the settings stored natively on the device.
     * @param defaultValue Default value for the key, if one is not found.
     **/
    public String getValue(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }
    /****
     * Gets the value from the settings stored natively on the device.
     * @param defaultValue Default value for the key, if one is not found.
     **/
    public int getValue_int(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }
    public int getIntValue(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public long getLongValue(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    /****
     * Gets the value from the preferences stored natively on the device.
     * @param defValue Default value for the key, if one is not found.
     **/
    public boolean getValue_boolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public void setValue(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    /**
     * Clear all the preferences store in this {@link android.content.SharedPreferences.Editor}
     */
    public boolean clear() {
        try {
            mEditor.clear().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Removes preference entry for the given key.
     */
    public void removeValue(String key) {
        if (mEditor != null) {
            mEditor.remove(key).commit();
        }
    }

    public void setValue_Login(String email, String pass) {
        mEditor.putString(Key_email, email);
        mEditor.putString(Key_password, pass);
        mEditor.commit();
    }

    public String[] getValue_Login() {
        String[] cred_array = new String[2];
        cred_array[0] = sharedPreferences.getString(Key_email, "");
        cred_array[1] = sharedPreferences.getString(Key_password, "");
        return cred_array;
    }

}