package com.zoom2uwarehouse.home;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.kiba.bottomnavigation.BottomNavigationItem;
import com.kiba.bottomnavigation.BottomNavigationView;
import com.kiba.bottomnavigation.OnNavigationItemSelectListener;
import com.zoom2uwarehouse.R;
import com.zoom2uwarehouse.barcode.BarCode;
import com.zoom2uwarehouse.barcode.ReturnView;
import com.zoom2uwarehouse.bean_class.resposebean.Details_Customer;
import com.zoom2uwarehouse.bottomnavigation_items.home.HomeFragment;
import com.zoom2uwarehouse.database.DatabaseClient;
import com.zoom2uwarehouse.dialog.permission_dialog.PermissionDialog_view;
import com.zoom2uwarehouse.dialog.permission_dialog.model.PermissionDialog_Model_Implementation;
import com.zoom2uwarehouse.dialog.permission_dialog.presenter.PermissionDialog_Presenter_Implementation;
import com.zoom2uwarehouse.get_token.GetTokenClass;
import com.zoom2uwarehouse.home.model_user_detail.User_detail_m_imp;
import com.zoom2uwarehouse.home.presenter_user_detail.User_detail_p_imp;
import com.zoom2uwarehouse.login.Login;
import com.zoom2uwarehouse.logout.Logoutview;
import com.zoom2uwarehouse.logout.model.LogoutModelImplementation;
import com.zoom2uwarehouse.logout.presenter.LogoutPresenterImplementation;
import com.zoom2uwarehouse.re_assign_delivery.Re_Assign_Deliveries;
import com.zoom2uwarehouse.run_summary.Run_Summary1;
import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;
import com.zoom2uwarehouse.util.Common_Interface;
import com.zoom2uwarehouse.util.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements Logoutview,
        PermissionDialog_view, View.OnClickListener, User_detail_view , Common_Interface {

    public static int BOTTOMBAR_SELECTED_ITEM = 0;

    private DrawerLayout drawer;
    private LogoutPresenterImplementation mainPresenter;
    private PermissionDialog_Presenter_Implementation permissionDialog_presenter_implementation;
    private static final int Location_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private SharedPreferenceManager sharedPreferenceManager;
    public static TextView txt_title;
    private TextView txt_initial_name, txt_full_name;
    private Fragment f_fr;

    public static BottomNavigationView bottomNavigationView;
    public static Set<String> stringLinkedHashSet = new LinkedHashSet<>();

    Typeface customFont;

    int isCourierLogin;
    boolean isFromSplashScreen=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        customFont = Typeface.createFromAsset(getAssets(), "gothamrnd_medium.otf");

        isCourierLogin = getIntent().getIntExtra("CallReassignDelivery", 0);

        isFromSplashScreen = getIntent().getBooleanExtra("FromSplashScreen", false);





        // load the animation
        txt_title = findViewById(R.id.title_main);
        txt_full_name = findViewById(R.id.textView4);
        sharedPreferenceManager = new SharedPreferenceManager(MainActivity.this, "warehouse_app");
        if(isFromSplashScreen){
            Utility.showLoadingDialog(MainActivity.this);
            GetTokenClass getTokenClass=new GetTokenClass();
            getTokenClass.getToken(sharedPreferenceManager);
        }
        new DeleteOldScannedRecords().execute();

        permissionDialog_presenter_implementation = new PermissionDialog_Presenter_Implementation(MainActivity.this, this, new PermissionDialog_Model_Implementation());
        mainPresenter = new LogoutPresenterImplementation(MainActivity.this, this, new LogoutModelImplementation());
        User_detail_p_imp userDetailPImp = new User_detail_p_imp(MainActivity.this, this, new User_detail_m_imp());
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,                  /* host Activity */
                drawer,         /* DrawerLayout object */toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description */ R.string.navigation_drawer_close  /* "close drawer" description */
        ) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Utility.hideKeyboard(MainActivity.this);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);

        TextView txt_close_drawer = findViewById(R.id.textView5);
        TextView txt_logout = findViewById(R.id.textView9);

        txt_close_drawer.setOnClickListener(this);
        txt_logout.setOnClickListener(this);
        ImageView img_menu = findViewById(R.id.img_menu);
        img_menu.setOnClickListener(this);
        txt_initial_name = findViewById(R.id.textView22);

        //getting bottom navigation view and attaching the listener
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        List<BottomNavigationItem> list = new ArrayList<>();;

        if (isCourierLogin == 0) {

            findViewById(R.id.returnBtnSideNavigation).setOnClickListener(this);
            findViewById(R.id.returnBtnSideNavigation).setVisibility(View.VISIBLE);

            BottomNavigationItem item1 = new BottomNavigationItem("Home", R.drawable.home_icon, R.drawable.home_sel, 0, customFont);
            BottomNavigationItem item2 = new BottomNavigationItem("Routes", R.drawable.user_icon, R.drawable.route_sel, 0, customFont);
            BottomNavigationItem item3 = new BottomNavigationItem("Barcode", R.drawable.scan_check, R.drawable.barcode_sel, 0, customFont);
            BottomNavigationItem item4 = new BottomNavigationItem("Re-assign", R.drawable.unselecrt_eassign, R.drawable.select_reassign, 0, customFont);

            list.add(item1);
            list.add(item2);
            list.add(item3);
            list.add(item4);
        } else {

            findViewById(R.id.returnBtnSideNavigation).setVisibility(View.GONE);
            BottomNavigationItem item1 = new BottomNavigationItem("Home", R.drawable.home_icon, R.drawable.home_sel, 0, customFont);
            BottomNavigationItem item2 = new BottomNavigationItem("Scan", R.drawable.scan_check, R.drawable.barcode_sel, 0, customFont);
            BottomNavigationItem item3 = new BottomNavigationItem("Re-assign", R.drawable.unselecrt_eassign, R.drawable.select_reassign, 0, customFont);

            list.add(item1);
            list.add(item2);
            list.add(item3);
        }

        bottomNavigationView.addItems(list);
        bottomNavigationView.bottomViewItemSelected (bottomNavigationView.getItemView(0), 0, BOTTOMBAR_SELECTED_ITEM);

        f_fr = new HomeFragment();
        set_title("Home");

        Bundle bundleToCallBarcodeAPI = new Bundle();
        bundleToCallBarcodeAPI.putInt("isCourierLogin", isCourierLogin);
        f_fr.setArguments(bundleToCallBarcodeAPI);

        displaySelectedFragment(f_fr);
        userDetailPImp.call_api_user(sharedPreferenceManager);

        bottomNavigationView.setNavigationItemSelectListener(new OnNavigationItemSelectListener() {
            @Override
            public void onSelected(BottomNavigationView bottomNavigationView, View itemView, int position) {
                //bottomNavigationView.setBadgeViewNumberByPosition(position, 1);    // to show badge icon on bottom bar view
                switch (position) {
                    case 0:
                        BOTTOMBAR_SELECTED_ITEM = 0;
                        onNavigationItemSelected(0);
                        break;
                    case 1:
                        BOTTOMBAR_SELECTED_ITEM = 1;
                        onNavigationItemSelected(1);
                        break;
                    case 2:
                        BOTTOMBAR_SELECTED_ITEM = 2;
                        onNavigationItemSelected(2);
                        break;
                    case 3:
                        callReAssignView();
                        break;
                }
            }
        });
    }

    private void callReAssignView() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, Re_Assign_Deliveries.class);
        intent.putExtra("CallReassignDelivery", 1);
        intent.putExtra("isCourierLogin", isCourierLogin);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        FragmentManager fm = getFragmentManager();
        int nowCount = getFragmentManager().getBackStackEntryCount();
        Log.d("Found_back", "" + nowCount);
        if (nowCount == 1) {
            stringLinkedHashSet.clear();
            stringLinkedHashSet.add("frag11");
            Utility.close_app(MainActivity.this);
        } else {
            fm.popBackStackImmediate();
        }
    }

    @Override
    public void Logout_success() {
        //sharedPreferenceManager.setValue_Login("", "");
        sharedPreferenceManager.setValue("access_token","");
        sharedPreferenceManager.setValue("token_type","");
        sharedPreferenceManager.setValue("roles","");
        sharedPreferenceManager.setValue_int("carrierId",0);
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.from_right, R.anim.from_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView5:
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.textView9:
                drawer.closeDrawers();
                mainPresenter.success_call(getResources().getString(R.string.msg_logout));
                break;
            case R.id.img_menu:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.returnBtnSideNavigation:
                drawer.closeDrawers();
                Intent returnViewIntent = new Intent(MainActivity.this, ReturnView.class);
                startActivity(returnViewIntent);
                returnViewIntent = null;
                break;
        }
    }

    private void onNavigationItemSelected(int item) {
        // Handle navigation view item clicks here.
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (item) {
            case 0:
                Fragment fragment = new HomeFragment();
                Bundle bundleToCallBarcodeAPI = new Bundle();
                bundleToCallBarcodeAPI.putInt("isCourierLogin", isCourierLogin);
                fragment.setArguments(bundleToCallBarcodeAPI);

                transaction.replace(R.id.container, fragment, "frag0");
                set_title("Home");

                transaction.commit();
                break;

            case 1:

                if (isCourierLogin == 0) {
                    transaction.replace(R.id.container, new Run_Summary1(), "frag1");
                    transaction.commit();
                    set_title("Routes & Drivers");
                } else {
                    BarCode.radioButtonSelectStr = "Scan";

                    Fragment fragment1CourierLogin = new BarCode();
                    Bundle bundleToCallBarcodeAPI1 = new Bundle();
                    bundleToCallBarcodeAPI1.putInt("isCourierLogin", isCourierLogin);
                    fragment1CourierLogin.setArguments(bundleToCallBarcodeAPI1);

                    transaction.replace(R.id.container, fragment1CourierLogin, "frag2");
                    transaction.commit();
                    set_title("Scan AWB number");
                }
                break;

            case 2:

                if (isCourierLogin == 0) {
                    BarCode.radioButtonSelectStr = "Scan";

                    Fragment fragment2 = new BarCode();
                    Bundle bundleToCallBarcodeAPI2 = new Bundle();
                    bundleToCallBarcodeAPI2.putInt("isCourierLogin", isCourierLogin);
                    fragment2.setArguments(bundleToCallBarcodeAPI2);

                    transaction.replace(R.id.container, fragment2, "frag2");
                    transaction.commit();
                    set_title("Scan AWB number");
                } else
                    callReAssignView();
                break;

            case 3:
                Utility.simple_dialog(MainActivity.this, "Info", "This option are coming soon...");
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
    }


    private void displaySelectedFragment(Fragment fragment) {

        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment, "frag1");

          if (!stringLinkedHashSet.contains("frag11")) {
              transaction.addToBackStack("frag11");
          } else {
              transaction.disallowAddToBackStack();
          }
          stringLinkedHashSet.add("frag11");

          transaction.commit();
    }


    private void trig_dialog(int pos) {
        permissionDialog_presenter_implementation.call_dialog(MainActivity.this.getResources().getString(R.string.permission), MainActivity.this.getResources().getString(R.string.permission_msg), MainActivity.this.getResources().getString(R.string.camera)
                + "," + MainActivity.this.getResources().getString(R.string.storage) + " and " + MainActivity.this.getResources().getString(R.string.location), pos);
    }

    @Override
    public void action1() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, Location_PERMISSION_CONSTANT);
    }

    @Override
    public void action2() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
    }

    private void showCameraPreview() {
        // BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
        } else {
            // Permission is missing and must be requested.
            requestCameraPermission();
        }
        // END_INCLUDE(startCamera)
    }

    /**
     * Requests the {@link android.Manifest.permission#CAMERA} permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private void requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with cda button to request the missing permission.
            trig_dialog(2);
        } else {
            trig_dialog(1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Location_PERMISSION_CONSTANT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                        //Show Information about why you need the permission
                        trig_dialog(1);
                    } else {
                        trig_dialog(2);
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showCameraPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void set_title(String s) {
        txt_title.setText(s);
    }

    @Override
    public void user_success(Details_Customer details_customer) {

        txt_full_name.setText(details_customer.getName());

        String[] customerName = null;
        try {
            customerName = details_customer.getName().split(" ");
            txt_initial_name.setText(customerName[0].charAt(0)+""+customerName[customerName.length - 1].charAt(0));
        } catch (Exception e) {
            e.printStackTrace();
            txt_initial_name.setText("--");
        }
    }

    @Override
    public void user_error(String s) {

    }


    @Override
    public void showProgress() {
        Utility.showLoadingDialog(MainActivity.this);
    }

    @Override
    public void hideProgress() {
        Utility.dismissLoadingDialog();
    }

    /**
     * RecyclerView: Implementing single item click and long press (Part-II)
     * <p>
     * - creating an Interface for single tap and long press
     * - Parameters are its respective view and its position
     */

    interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    /**
     * RecyclerView: Implementing single item click and long press (Part-II)
     * <p>
     * - creating an innerclass implementing RecyclerView.OnItemTouchListener
     * - Pass clickListener interface as parameter
     */

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private final ClickListener clicklistener;
        private final GestureDetector gestureDetector;

        RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    //delete old date saved scanned results
    public class DeleteOldScannedRecords extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //get the saved date from sharedPreference and check with current date
                String lastScanned = sharedPreferenceManager.getValue("scannedDate", null);
                if (lastScanned!=null&&!lastScanned.isEmpty()) {
                    SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");

                    Date time = Calendar.getInstance().getTime();
                    String todayDate = sdf.format(time);

                    String scannedDate = sharedPreferenceManager.getValue("scannedDate", null);
                    if (scannedDate!=null)
                    {
                        if (!todayDate.equals(scannedDate))
                        {
                            DatabaseClient.getInstance(MainActivity.this).getAppDatabase()
                                    .scannedDao().deleteAll();
                        }
                    }
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }
    }
}
