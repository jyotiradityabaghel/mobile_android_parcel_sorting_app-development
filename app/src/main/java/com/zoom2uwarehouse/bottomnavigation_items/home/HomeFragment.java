package com.zoom2uwarehouse.bottomnavigation_items.home;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zoom2uwarehouse.R;
import com.zoom2uwarehouse.barcode.BarCode;
import com.zoom2uwarehouse.home.MainActivity;
import com.zoom2uwarehouse.re_assign_delivery.Re_Assign_Deliveries;
import com.zoom2uwarehouse.run_summary.Run_Summary1;
import com.zoom2uwarehouse.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.routeHomeView)
    TextView routeHomeView;
    @BindView(R.id.barcodeHomeView)
    TextView barcodeHomeView;
    @BindView(R.id.reAssignInHomeView)
    TextView reAssignInHomeView;

    int isCourierLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.home_bottom_nav_item1, container, false);

        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        isCourierLogin = bundle.getInt("isCourierLogin", 0);

        routeHomeView.setOnClickListener(this);
        barcodeHomeView.setOnClickListener(this);
        reAssignInHomeView.setOnClickListener(this);

        if (isCourierLogin == 1) {
            barcodeHomeView.setVisibility(View.INVISIBLE);
            barcodeHomeView.setOnClickListener(null);

            RelativeLayout.LayoutParams paramRouteBtn = new RelativeLayout.LayoutParams(Utility.dip2px(getActivity(),270.0f), Utility.dip2px(getActivity(),110.0f));
            paramRouteBtn.setMargins(0, 0, 0, 0);
            paramRouteBtn.addRule(RelativeLayout.CENTER_IN_PARENT);
            paramRouteBtn.addRule(RelativeLayout.ABOVE, R.id.barcodeHomeView);
            routeHomeView.setLayoutParams(paramRouteBtn);
            routeHomeView.setText("Normal Scan");

            RelativeLayout.LayoutParams paramBarcodeBtn = new RelativeLayout.LayoutParams(Utility.dip2px(getActivity(),270.0f), Utility.dip2px(getActivity(),40.0f));
            paramBarcodeBtn.addRule(RelativeLayout.CENTER_VERTICAL);
            barcodeHomeView.setLayoutParams(paramBarcodeBtn);

            RelativeLayout.LayoutParams paramreAssignBtn = new RelativeLayout.LayoutParams(Utility.dip2px(getActivity(),270.0f), Utility.dip2px(getActivity(),110.0f));
            paramreAssignBtn.setMargins(0, 0, 0, 0);
            paramreAssignBtn.addRule(RelativeLayout.CENTER_IN_PARENT);
            paramreAssignBtn.addRule(RelativeLayout.BELOW, R.id.barcodeHomeView);
            reAssignInHomeView.setLayoutParams(paramreAssignBtn);
            reAssignInHomeView.setBackgroundResource(R.drawable.round_btn_home_barcode);
        }

        return view;
    }

    @Override
    public void onClick(View v) {

        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (v.getId()) {
            case R.id.routeHomeView:
                MainActivity.BOTTOMBAR_SELECTED_ITEM = 1;
                MainActivity.bottomNavigationView.bottomViewItemSelected (MainActivity.bottomNavigationView.getItemView(1), 1, MainActivity.BOTTOMBAR_SELECTED_ITEM);
                MainActivity.bottomNavigationView.resetActiveItems(0);
                if (isCourierLogin == 0) {
                    transaction.replace(R.id.container, new Run_Summary1(), "frag1");
                    transaction.commit();
                    setTitleTxt("Routes & Drivers");
                } else {
                    BarCode.radioButtonSelectStr = "Scan";

                    Fragment fragment2 = new BarCode();
                    Bundle bundleToCallBarcodeAPI2 = new Bundle();
                    bundleToCallBarcodeAPI2.putInt("isCourierLogin", isCourierLogin);
                    fragment2.setArguments(bundleToCallBarcodeAPI2);

                    transaction.replace(R.id.container, fragment2, "frag2");
                    transaction.commit();
                    setTitleTxt("Scan AWB number");
                }
                break;
            case R.id.barcodeHomeView:
                MainActivity.BOTTOMBAR_SELECTED_ITEM = 2;
                MainActivity.bottomNavigationView.bottomViewItemSelected (MainActivity.bottomNavigationView.getItemView(2), 2, MainActivity.BOTTOMBAR_SELECTED_ITEM);
                MainActivity.bottomNavigationView.resetActiveItems(0);
                BarCode.radioButtonSelectStr = "Scan";

                Fragment fragment = new BarCode();
                Bundle bundleToCallBarcodeAPI = new Bundle();
                bundleToCallBarcodeAPI.putInt("isCourierLogin", 0);
                fragment.setArguments(bundleToCallBarcodeAPI);

                transaction.replace(R.id.container, fragment, "frag2");
                Fragment fragmentB = fragmentManager.findFragmentByTag("frag2");
                transaction.commit();
                setTitleTxt("Scan AWB number");
                break;

            case R.id.reAssignInHomeView:
                if (isCourierLogin == 0)
                    bottomBarSelection(3);
                 else
                    bottomBarSelection(2);

                Intent intent = new Intent();
                intent.setClass(getActivity(), Re_Assign_Deliveries.class);
                intent.putExtra("CallReassignDelivery", 1);
                intent.putExtra("isCourierLogin", isCourierLogin);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.from_left, R.anim.from_left);

                break;
        }
    }

    private void bottomBarSelection(int item) {
        MainActivity.bottomNavigationView.bottomViewItemSelected(MainActivity.bottomNavigationView.getItemView(item), item, MainActivity.BOTTOMBAR_SELECTED_ITEM);
        MainActivity.bottomNavigationView.resetActiveItems(item);
    }

    private void setTitleTxt(String titleTxt) {
        if (MainActivity.txt_title != null)
            MainActivity.txt_title.setText(titleTxt);
    }

}
