package com.zoom2uwarehouse.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.zoom2uwarehouse.R;
import com.zoom2uwarehouse.home.MainActivity;
import com.zoom2uwarehouse.login.Login;
import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;
import com.zoom2uwarehouse.util.Utility;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class Splash extends Activity {
    public static int height, width;
    private SharedPreferenceManager sharedPreferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferenceManager= new SharedPreferenceManager(Splash.this,"warehouse_app");
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        Completable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).subscribe(this::launchMainActivity);
    }

    private void launchMainActivity() {
        //launch your activity here

        if(TextUtils.isEmpty(sharedPreferenceManager.getValue("access_token",""))){
            Intent i = new Intent(Splash.this, Login.class);
            startActivity(i);
            finish();
        }else{
            if (sharedPreferenceManager.getValue("roles","").equals("Courier")) {
                if (sharedPreferenceManager.getValue_int("carrierId",0) != 0)
                    afterLoggedInActivity(MainActivity.class, 1);
                else
                    Utility.simple_alert_message(this,"Sorry, you do not have permission to access this portal.");
            } else
                afterLoggedInActivity(MainActivity.class, 0);
        }



    }

    public void afterLoggedInActivity(Class re_assign_deliveriesClass, int calledReassignDelivery) {
        MainActivity.BOTTOMBAR_SELECTED_ITEM = 0;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(Splash.this, re_assign_deliveriesClass);
        intent.putExtra("FromSplashScreen",true);
        intent.putExtra("CallReassignDelivery", calledReassignDelivery);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.from_left, R.anim.from_left);
    }


}
