package com.zoom2uwarehouse.run_summary;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoom2uwarehouse.R;
import com.zoom2uwarehouse.bean_class.resposebean.Route_Pickup_Response;
import com.zoom2uwarehouse.bean_class.resposebean.Route_Progress_Response;
import com.zoom2uwarehouse.database.BookingsDao;
import com.zoom2uwarehouse.database.DatabaseClient;
import com.zoom2uwarehouse.home.MainActivity;
import com.zoom2uwarehouse.model.Route_Pickup_Response_Data;
import com.zoom2uwarehouse.run_summary.model.Route_model_imp;
import com.zoom2uwarehouse.run_summary.presenter.Run_presenter_impl;
import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;
import com.zoom2uwarehouse.util.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Run_Summary1 extends Fragment implements View.OnClickListener, Run_View {

    @BindView(R.id.my_recycler_view_progress)
    LinearLayout recyclerView_progress;
    @BindView(R.id.my_recycler_view_pickup)
    LinearLayout recyclerView_pickup;
    @BindView(R.id.textView31)
    TextView txt_pickup;
    @BindView(R.id.textView32)
    TextView txt_progress;
    @BindView(R.id.linearLayout2)
    LinearLayout lin_header_pickup;
    @BindView(R.id.linearLayout3)
    LinearLayout lin_header_progress;
    @BindView(R.id.textView29)
    TextView txt_total_run;
    @BindView(R.id.textView30)
    TextView txt_time;
    @BindView(R.id.textView75)
    TextView txt_message;
    @BindView(R.id.swapRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tv_zone_awaiting)
    TextView tv_zone_awaiting;
    @BindView(R.id.tv_zone_progress)
    TextView tv_zone_progress;

    private SharedPreferenceManager sharedPreferenceManager;
    private Run_presenter_impl run_presenter_impl;
    private static int tab_select = 1;

    public static List<Route_Progress_Response.Route_Process_Response_Data> arrayOfProgressData;
    private static String GET_TOTALRUN_FOR_PROGRESS;

    public static List<Route_Pickup_Response_Data> arrayOfPickUpData;
    private static String GET_TOTALRUN_FOR_PICKUP;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.activity_run__summary1, container, false);
        sharedPreferenceManager = new SharedPreferenceManager(getActivity(), "warehouse_app");
        run_presenter_impl = new Run_presenter_impl(this, new Route_model_imp());


        ButterKnife.bind(this, view);

        txt_pickup.setOnClickListener(this);
        txt_progress.setOnClickListener(this);
        update_time();
        call_service(0);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cancel the Visual indication of a refresh
                swipeRefreshLayout.setRefreshing(false);
                call_service(1);
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.textView31:
                tab_select = 1;
                call_service(0);
                break;
            case R.id.textView32:
                tab_select = 2;
                call_service(0);
                break;
        }
    }

    @Override
    public void success_progress(Route_Progress_Response data) {

        arrayOfProgressData = data.getData();
        GET_TOTALRUN_FOR_PROGRESS = data.getTotalRun();
        setUpProgressResponseData();
    }

    private void setUpProgressResponseData() {
        boolean showZoneFiled=false;
        try {
            if (arrayOfPickUpData.size()>0)
            {
                String zone = arrayOfPickUpData.get(0).getZone();
                if (zone==null||zone.isEmpty())
                {
                    showZoneFiled=false;
                    tv_zone_progress.setVisibility(View.GONE);
                }
                else  {
                    showZoneFiled=true;
                    tv_zone_progress.setVisibility(View.VISIBLE);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < arrayOfProgressData.size(); i++) {

            Route_Progress_Response.Route_Process_Response_Data progressResponseData = arrayOfProgressData.get(i);

            LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myView = inflater1.inflate(R.layout.activity_run_progress, null);

            RelativeLayout backgroundView = myView.findViewById(R.id.backgroundView);
            TextView txtId = myView.findViewById(R.id.textView46);
            TextView txtDriver = myView.findViewById(R.id.textView47);
            TextView txtArea = myView.findViewById(R.id.textView48);
            TextView txtStop = myView.findViewById(R.id.textView49);
            TextView txtPU = myView.findViewById(R.id.textView50);
            TextView txtDU = myView.findViewById(R.id.textView51);
            TextView txtTTD = myView.findViewById(R.id.textView52);
            TextView tv_zone_value = myView.findViewById(R.id.tv_zone_value);

            int i1 = i + 1;
            txtId.setText("" + i1);
            txtDriver.setText(progressResponseData.getDriver());
            txtArea.setText(progressResponseData.getArea());
            txtStop.setText(progressResponseData.getStop());
            txtPU.setText(progressResponseData.getPickedupCount());
            txtDU.setText(progressResponseData.getDroppedOffCount());
            txtTTD.setText(progressResponseData.getAttemptedDeliveries());

            if (showZoneFiled) {
                tv_zone_value.setVisibility(View.VISIBLE);
                if (progressResponseData.getZone() == null || progressResponseData.getZone().isEmpty())
                    tv_zone_value.setText("-");
                else
                    tv_zone_value.setText(progressResponseData.getZone());
            }else tv_zone_value.setVisibility(View.GONE);


            if (i % 2 == 0) {
                backgroundView.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                backgroundView.setBackgroundColor(Color.parseColor("#F0F0F0"));
            }

            if (i == arrayOfProgressData.size()) {
                if (i % 2 == 0) {
                    backgroundView.setBackgroundResource(R.drawable.custom_curve_bottom_white);
                } else {
                    backgroundView.setBackgroundResource(R.drawable.custom_curve_bottom_light);
                }
            }

            if (progressResponseData.isNewDriver())
                backgroundView.setBackgroundColor(Color.parseColor("#FF9FAC"));

            recyclerView_progress.addView(myView);
        }
        txt_message.setVisibility(View.GONE);
        update_time();
        txt_total_run.setText(GET_TOTALRUN_FOR_PROGRESS + " Routes");
    }

    @Override
    public void success_Pickup(Route_Pickup_Response data) {
        arrayOfPickUpData = data.getData();
        GET_TOTALRUN_FOR_PICKUP = data.getTotalRun();
        setUpPickUpResponseData();
    }

    private void setUpPickUpResponseData() {
        boolean showZoneFiled=false;
        try {
            if (arrayOfPickUpData.size()>0)
            {
                String zone = arrayOfPickUpData.get(0).getZone();
                if (zone==null||zone.isEmpty())
                {
                    showZoneFiled=false;
                    tv_zone_awaiting.setVisibility(View.GONE);
                }
                else  {
                    showZoneFiled=true;
                    tv_zone_awaiting.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < arrayOfPickUpData.size(); i++) {

            Route_Pickup_Response_Data pickUpResponse = arrayOfPickUpData.get(i);

            LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myView = inflater1.inflate(R.layout.activity_run_pickup, null);

            RelativeLayout backgroundView2 = myView.findViewById(R.id.backgroundView);
            if (i % 2 == 0) {
                backgroundView2.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                backgroundView2.setBackgroundColor(Color.parseColor("#F0F0F0"));
            }
            TextView txtId = myView.findViewById(R.id.textView46);
            TextView txtDriver = myView.findViewById(R.id.textView47);
            TextView txtArea = myView.findViewById(R.id.textView48);
            TextView txtStop = myView.findViewById(R.id.textView49);
            TextView txtETA = myView.findViewById(R.id.textView50);
            TextView tv_zone = myView.findViewById(R.id.tv_zone);
            int i1 = i + 1;
            txtId.setText("" + i1);
            txtDriver.setText(pickUpResponse.getDriver());
            txtArea.setText(pickUpResponse.getArea());
            txtStop.setText(pickUpResponse.getStop());
            txtETA.setText(returnTimeInterval(pickUpResponse.getPickupETA()));

            if (showZoneFiled) {
                tv_zone.setVisibility(View.VISIBLE);
                if (pickUpResponse.getZone() == null || pickUpResponse.getZone().isEmpty())
                    tv_zone.setText("-");
                else
                    tv_zone.setText(pickUpResponse.getZone());
            }else tv_zone.setVisibility(View.GONE);

            if (i == 6) {
                if (i % 2 == 0) {
                    backgroundView2.setBackgroundResource(R.drawable.custom_curve_bottom_white);
                } else {
                    backgroundView2.setBackgroundResource(R.drawable.custom_curve_bottom_light);
                }
            }

            if (pickUpResponse.isNewDriver())
                backgroundView2.setBackgroundColor(Color.parseColor("#FF9FAC"));

            recyclerView_pickup.addView(myView);
        }
        txt_message.setVisibility(View.GONE);
        update_time();
        txt_total_run.setText( GET_TOTALRUN_FOR_PICKUP+ " Routes");
    }

    private String returnTimeInterval(String pickupETA) {
        String returnMinutesInStr = "-NA-";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Calendar now = Calendar.getInstance();
            String currentTime = format.format(now.getTime());
            pickupETA = pickupETA.replace("Z", "GMT+00:00");
            Date deliverDateTime = format.parse(pickupETA);
            Date currentDate = format.parse(currentTime);
            long mills = deliverDateTime.getTime() - currentDate.getTime();
            int hours = (int) (mills / (1000 * 60 * 60));
            if (hours > 0 || hours < 0)
                returnMinutesInStr = hours + " hrs";
            else {
                int mins = (int) (mills / (1000 * 60));
                returnMinutesInStr = mins + " mins";
            }
            format = null;
            deliverDateTime = null;
            currentDate = null;
        } catch (ParseException e) {
            e.printStackTrace();
            returnMinutesInStr = "-NA-";
        }
        return returnMinutesInStr;
    }

    @Override
    public void error(String message) {
        txt_message.setVisibility(View.VISIBLE);
        txt_message.setText(message);
        //Utility.simple_Error_message(getActivity(),message);
    }

    @Override
    public void showProgress() {
        Utility.showLoadingDialog(getActivity());
    }

    @Override
    public void hideProgress() {
        Utility.dismissLoadingDialog();
    }


    private void update_time() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("EEE, d-MMM");
        String formattedDate = df.format(c.getTime());
        txt_time.setText(formattedDate);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).set_title("Routes & Drivers");
        sharedPreferenceManager.setValue_int("last_fragment", 1);
        int nowCount = getFragmentManager().getBackStackEntryCount();
        Log.d("Found", "" + nowCount);
    }

    public void call_service(int isPullToRefresh) {
        if (Utility.isNetworkAvailable(getActivity())) {
            if (tab_select == 1) {
                apiCallForPickup(isPullToRefresh);
            } else  if (tab_select == 2){
                apiCallForProgressData(isPullToRefresh);
            }
        } else {
            Utility.messageInternet(getActivity());
        }
    }

    private void apiCallForProgressData(int isPullToRefresh) {
        txt_pickup.setBackgroundResource(R.drawable.custom_curve_left_light);
        txt_progress.setBackgroundResource(R.drawable.custom_curve_left);
        txt_pickup.setTextColor(getActivity().getResources().getColor(R.color.triadic));
        txt_progress.setTextColor(getActivity().getResources().getColor(R.color.white));
        recyclerView_progress.setVisibility(View.VISIBLE);
        recyclerView_pickup.setVisibility(View.GONE);
        lin_header_pickup.setVisibility(View.GONE);
        lin_header_progress.setVisibility(View.VISIBLE);
        recyclerView_progress.removeAllViews();

        if (arrayOfProgressData != null) {
            if (arrayOfProgressData.size() > 0 && isPullToRefresh == 1)
                run_presenter_impl.call_for_progress(sharedPreferenceManager);
            else if (arrayOfProgressData.size() > 0  && isPullToRefresh == 0)
                setUpProgressResponseData();
            else
                run_presenter_impl.call_for_progress(sharedPreferenceManager);
        } else
            run_presenter_impl.call_for_progress(sharedPreferenceManager);
    }

    private void apiCallForPickup(int isPullToRefresh) {
        txt_pickup.setBackgroundResource(R.drawable.custom_curve_right);
        txt_progress.setBackgroundResource(R.drawable.custom_curve_right_light);
        txt_pickup.setTextColor(getActivity().getResources().getColor(R.color.white));
        txt_progress.setTextColor(getActivity().getResources().getColor(R.color.triadic));
        recyclerView_progress.setVisibility(View.GONE);
        recyclerView_pickup.setVisibility(View.VISIBLE);
        lin_header_pickup.setVisibility(View.VISIBLE);
        lin_header_progress.setVisibility(View.GONE);
        recyclerView_pickup.removeAllViews();

        if (arrayOfPickUpData != null) {
            if (arrayOfPickUpData.size() > 0 && isPullToRefresh == 1)
                run_presenter_impl.call_for_pickup(sharedPreferenceManager);
            else if (arrayOfPickUpData.size() > 0  && isPullToRefresh == 0)
                setUpPickUpResponseData();
            else
                run_presenter_impl.call_for_pickup(sharedPreferenceManager);
        } else
            run_presenter_impl.call_for_pickup(sharedPreferenceManager);

    }


    /**
    * save the awaiting pickup booking in local storage
    * and delete all the previous entries
    * */
    public class SaveAwaitingPickupBookings extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            if (arrayOfPickUpData!=null)
            {

            }
            return null;
        }
    }

}
