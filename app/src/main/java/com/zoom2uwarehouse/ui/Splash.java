package com.zoom2uwarehouse.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.zoom2uwarehouse.R;
import com.zoom2uwarehouse.login.Login;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class Splash extends Activity {
    public static int height, width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        Completable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).subscribe(this::launchMainActivity);
    }

    private void launchMainActivity() {
        //launch your activity here
        Intent i = new Intent(Splash.this, Login.class);
        startActivity(i);
        finish();
    }

}
