package com.zoom2uwarehouse.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.zoom2uwarehouse.R;
import com.zoom2uwarehouse.home.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**@author avadhesh mourya
 * Created by ubuntu on 1/2/18.
 */

public class Utility {

    public static Typeface customFontBold;
    public static Typeface customFontBook;
    public static Typeface customFontMedium;

    private static final String TAG = Utility.class.getSimpleName();
    private static ProgressDialog progressDialog;
    private static Dialog dialog_custom;

    public static void setCustomFontStyle (Context context) {
        customFontBold = Typeface.createFromAsset(context.getAssets(), "gothamrnd_bold.otf");
        customFontBook = Typeface.createFromAsset(context.getAssets(), "gothamrnd_book.otf");
        customFontMedium = Typeface.createFromAsset(context.getAssets(), "gothamrnd_medium.otf");
    }

    /**
     * Method is used for checking network availability.
     * @param context activity
     * @return isNetAvailable: boolean true for Internet availability, false
     * otherwise
     */
    public static boolean isNetworkAvailable(Context context) {

        boolean isNetAvailable = false;
        if (context != null) {

            final ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (mConnectivityManager != null) {

                boolean mobileNetwork = false;
                boolean wifiNetwork = false;

                boolean mobileNetworkConnected = false;
                boolean wifiNetworkConnected = false;

                final NetworkInfo mobileInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                final NetworkInfo wifiInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mobileInfo != null) {

                    mobileNetwork = mobileInfo.isAvailable();
                }

                if (wifiInfo != null) {

                    wifiNetwork = wifiInfo.isAvailable();
                }

                if (wifiNetwork || mobileNetwork) {

                    if (mobileInfo != null) {

                        mobileNetworkConnected = mobileInfo.isConnectedOrConnecting();
                    }
                    wifiNetworkConnected = wifiInfo.isConnectedOrConnecting();
                }
                isNetAvailable = (mobileNetworkConnected || wifiNetworkConnected);
            }
        }

        return isNetAvailable;
    }

    /**
     * Hides the already popped up keyboard from the screen.
     *
     * @param context activity
     */
    public static void hideKeyboard(Activity context) {
        try {
            View view = context.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }

        } catch (Exception e) {
            Log.e(TAG, "Sigh, cant even hide keyboard " + e.getMessage());
        }
    }


    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show();
    }


    public static void start_progress_dialog(Activity context)
    {
        progressDialog = new ProgressDialog(context);
      //  progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Processing..."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
    }
    public static void dismiss_progress_dialog()
    {
        if(progressDialog!=null) {
            progressDialog.dismiss();
        }
    }

    public static void showLoadingDialog(Activity context) {

        if(progressDialog != null) {
            if(progressDialog.isShowing())
                progressDialog.dismiss();

            progressDialog = null;
        }

         progressDialog = new ProgressDialog(context);
         progressDialog.setIndeterminate(true);
         progressDialog.setCancelable(true);
         progressDialog.setCanceledOnTouchOutside(false);
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.show();
        progressDialog.setContentView(R.layout.dialog_progress);
    }

    public static void dismissLoadingDialog()
    {
        if(progressDialog!=null) {
            progressDialog.dismiss();
        }
    }
    public static void simple_dialog(Activity activity,String string1,String string2)
    {
        try{
            if(dialog_custom != null)
                if(dialog_custom.isShowing())
                    dialog_custom.dismiss();
        }catch(Exception e){
            e.printStackTrace();
        }

        try {
            if(dialog_custom != null)
                dialog_custom = null;
            dialog_custom = new Dialog(activity);
            dialog_custom.setCancelable(false);

            dialog_custom.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#90000000")));
            dialog_custom.setContentView(R.layout.dialog_permission);


            Window window = dialog_custom.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            android.view.WindowManager.LayoutParams wlp = window.getAttributes();
            window.getAttributes().windowAnimations = R.style.DialogAnimation;

            wlp.gravity = Gravity.CENTER;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);


            TextView dialog_custom_title =  dialog_custom.findViewById(R.id.textView13);

            dialog_custom_title.setText(string1);

            TextView dialog_customMsg =  dialog_custom.findViewById(R.id.textView14);

            dialog_customMsg.setText(string2);
            TextView txt_btn_cancel= dialog_custom.findViewById(R.id.textView15);
            txt_btn_cancel.setOnClickListener(v -> dialog_custom.dismiss());


            dialog_custom.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void messageInternet (Activity activity)
    {
        Utility.simple_dialog(activity,activity.getResources().getString(R.string.info),activity.getResources().getString(R.string.internet));
    }

    public static void simple_alert_message (Activity activity,String msg)
    {
        Utility.simple_dialog(activity,activity.getResources().getString(R.string.alert),msg);
    }

    public static void simple_Error_message (Activity activity,String msg)
    {
        Utility.simple_dialog(activity,activity.getResources().getString(R.string.error),msg);
    }

    public static boolean isHttpStatusCode(Throwable throwable, int statusCode) {
        return throwable instanceof HttpException
                && ((HttpException) throwable).code() == statusCode;
    }

    public static  void close_app(Activity activity)
    {

        try{
            if(dialog_custom != null)
                if(dialog_custom.isShowing())
                    dialog_custom.dismiss();
        }catch(Exception e){
            e.printStackTrace();
        }

        try {
            if(dialog_custom != null)
                dialog_custom = null;
            dialog_custom = new Dialog(activity);
            dialog_custom.setCancelable(false);
            dialog_custom.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#90000000")));
            dialog_custom.setContentView(R.layout.dialog_yes_no);

            Window window = dialog_custom.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            android.view.WindowManager.LayoutParams wlp = window.getAttributes();
            window.getAttributes().windowAnimations = R.style.DialogAnimation;

            wlp.gravity = Gravity.CENTER;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);

            TextView dialog_customMsg =  dialog_custom.findViewById(R.id.textView17);
            dialog_customMsg.setText("Are you sure you want to close this app?");
            TextView txt_btn_cancel= dialog_custom.findViewById(R.id.textView18);
            TextView txt_btn_okay= dialog_custom.findViewById(R.id.textView19);


            txt_btn_okay.setOnClickListener(v -> {
                dialog_custom.dismiss();
                MainActivity.BOTTOMBAR_SELECTED_ITEM = 0;
                activity.finish();
            });
            txt_btn_cancel.setOnClickListener(v -> {
                dialog_custom.dismiss();
            });
            dialog_custom.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("SimpleDateFormat")
    public static String getDateTimeFromServer(String serverDateTimeValue) {
        String dateTimeReturn = "-NA-";

        try {
            if(!serverDateTimeValue.equals("")){
                SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
                //getting GMT timezone, you can get any timezone e.g. UTC
                utcFormat.setTimeZone(TimeZone.getTimeZone("IST"));
                Date convertedDate = new Date();
                try {
                    serverDateTimeValue = serverDateTimeValue.replace("Z", "GMT+00:00");
                    convertedDate = utcFormat.parse(serverDateTimeValue);
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy | hh:mm a");

                    return dateFormatter.format(convertedDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTimeReturn;
    }

    public static int dip2px(Context context, float dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
