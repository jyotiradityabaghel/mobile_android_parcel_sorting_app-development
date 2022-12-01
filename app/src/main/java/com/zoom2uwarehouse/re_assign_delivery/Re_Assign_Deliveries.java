package com.zoom2uwarehouse.re_assign_delivery;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zoom2uwarehouse.R;
import com.zoom2uwarehouse.barcode.BarCode;
import com.zoom2uwarehouse.barcode.Controller;
import com.zoom2uwarehouse.dialog.permission_dialog.PermissionDialog_view;
import com.zoom2uwarehouse.dialog.permission_dialog.model.PermissionDialog_Model_Implementation;
import com.zoom2uwarehouse.dialog.permission_dialog.presenter.PermissionDialog_Presenter_Implementation;
import com.zoom2uwarehouse.home.MainActivity;
import com.zoom2uwarehouse.login.Login;
import com.zoom2uwarehouse.logout.Logoutview;
import com.zoom2uwarehouse.logout.model.LogoutModelImplementation;
import com.zoom2uwarehouse.logout.presenter.LogoutPresenterImplementation;
import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;
import com.zoom2uwarehouse.util.Utility;
import java.util.ArrayList;
import java.util.List;

public class Re_Assign_Deliveries extends AppCompatActivity implements PermissionDialog_view, ReAssignDelivery_View,
        Logoutview,
        View.OnClickListener,
        AdapterAssignToOtherCourier.OnItemClickListener {

    private LogoutPresenterImplementation logoutPresenter;
    private PermissionDialog_Presenter_Implementation permissionDialog_presenter_for_camera;
    private Controller mPreview;
    private SharedPreferenceManager sharedPreferenceManager;

    ImageView backBtnReassignView;
    TextView titleReassignView, logoutBtnReassignView;
    RecyclerView recycleViewReassignView;
    EditText edtSearchViewReassignView;

    View barcodeFrameLayoutView;

    RelativeLayout barcodeViewReassignView;
    TextView barcodeViewManualScanBtn;
    FrameLayout barcodeViewCameraPreview;
    ImageView barcodeViewTargetingBox;

    SwipeRefreshLayout pullToRefreshLayout;
    TextView notAvailTxt;

    AdapterAssignToOtherCourier adapterCourierList;

    List<MyTeamList_Model> arrayOfCouriers;
    String courierId, courierName, currentCourierId = "";

    ReAssignDelivery_Presentor_Impl reAssignDelivery_presentor_impl;

    int callReassignDelivery, isCourierLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reassign_courier_list);

        callReassignDelivery = getIntent().getIntExtra("CallReassignDelivery", 0);
        isCourierLogin = getIntent().getIntExtra("isCourierLogin", 0);

        sharedPreferenceManager= new SharedPreferenceManager(Re_Assign_Deliveries.this,"warehouse_app");

        backBtnReassignView = (ImageView) findViewById(R.id.backBtnReassignView);
        backBtnReassignView.setOnClickListener(this);
        titleReassignView = (TextView) findViewById(R.id.titleReassignView);
        logoutBtnReassignView   = (TextView) findViewById(R.id.logoutBtnReassignView);
        logoutBtnReassignView.setOnClickListener(this);
        if (callReassignDelivery == 1) {
            logoutBtnReassignView.setVisibility(View.INVISIBLE);
            backBtnReassignView.setVisibility(View.VISIBLE);
        } else {
            logoutBtnReassignView.setVisibility(View.VISIBLE);
            backBtnReassignView.setVisibility(View.GONE);
        }

        edtSearchViewReassignView  = (EditText) findViewById(R.id.edtSearchViewReassignView);

        barcodeFrameLayoutView = findViewById(R.id.barcodeFrameLayoutView);
        barcodeFrameLayoutView.setVisibility(View.GONE);
        barcodeViewManualScanBtn = (TextView) barcodeFrameLayoutView.findViewById(R.id.barcodeViewManualScanBtn);
        barcodeViewManualScanBtn.setTypeface(Utility.customFontMedium);
        barcodeViewManualScanBtn.setOnClickListener(this);

        barcodeViewReassignView = (RelativeLayout) barcodeFrameLayoutView.findViewById(R.id.barcodeViewReassignView);

        barcodeViewCameraPreview = (FrameLayout) barcodeFrameLayoutView.findViewById(R.id.barcodeViewCameraPreview);

        barcodeViewTargetingBox = (ImageView) barcodeFrameLayoutView.findViewById(R.id.barcodeViewTargetingBox);

        pullToRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pullToRefreshLayout);
        notAvailTxt = (TextView) findViewById(R.id.notAvailTxt);

        recycleViewReassignView = findViewById(R.id.recycleViewReassignView);
        arrayOfCouriers = new ArrayList<>();
        adapterCourierList = new AdapterAssignToOtherCourier(this, arrayOfCouriers,  this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycleViewReassignView.setLayoutManager(mLayoutManager);
        recycleViewReassignView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));
        recycleViewReassignView.setItemAnimator(new DefaultItemAnimator());
        recycleViewReassignView.setAdapter(adapterCourierList);

        logoutPresenter = new LogoutPresenterImplementation(Re_Assign_Deliveries.this, this, new LogoutModelImplementation());

        permissionDialog_presenter_for_camera = new PermissionDialog_Presenter_Implementation(Re_Assign_Deliveries.this, this, new PermissionDialog_Model_Implementation());

        reAssignDelivery_presentor_impl = new ReAssignDelivery_Presentor_Impl(this, new ReAssignDelivery_Interactor_Impl());
        reAssignDelivery_presentor_impl.call_api_teamlist_for_reassign(sharedPreferenceManager);

        edtSearchViewReassignView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterCourierList.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pullToRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefreshLayout.setRefreshing(false);
                reAssignDelivery_presentor_impl.call_api_teamlist_for_reassign(sharedPreferenceManager);
            }
        });
    }

    private void tabBarActivity() {
        MainActivity.BOTTOMBAR_SELECTED_ITEM = 0;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(Re_Assign_Deliveries.this, MainActivity.class);
        intent.putExtra("CallReassignDelivery", isCourierLogin);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.from_left, R.anim.from_left);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (backBtnReassignView.getVisibility() == View.GONE) {
            if (callReassignDelivery == 1) {
                resetBottomSelectedItem();
                MainActivity.bottomNavigationView.bottomViewItemSelected (MainActivity.bottomNavigationView.getItemView(MainActivity.BOTTOMBAR_SELECTED_ITEM), MainActivity.BOTTOMBAR_SELECTED_ITEM, MainActivity.BOTTOMBAR_SELECTED_ITEM);
                tabBarActivity();
            } else
                logoutPresenter.success_call(getResources().getString(R.string.msg_logout));
        } else {
            if (callReassignDelivery == 1) {
                if (barcodeFrameLayoutView.getVisibility() == View.GONE) {
                    resetBottomSelectedItem();
                    MainActivity.bottomNavigationView.bottomViewItemSelected(MainActivity.bottomNavigationView.getItemView(MainActivity.BOTTOMBAR_SELECTED_ITEM), MainActivity.BOTTOMBAR_SELECTED_ITEM, MainActivity.BOTTOMBAR_SELECTED_ITEM);
                    tabBarActivity();
                } else
                    showCourierListNhideBarcode();
            } else
                showCourierListNhideBarcode();
        }
    }

    private void resetBottomSelectedItem() {
        if (isCourierLogin == 0)
            MainActivity.bottomNavigationView.resetActiveItems(3);
        else
            MainActivity.bottomNavigationView.resetActiveItems(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logoutBtnReassignView:
                logoutPresenter.success_call(getResources().getString(R.string.msg_logout));
                break;
            case R.id.backBtnReassignView:
                if (callReassignDelivery == 1) {
                    if (logoutBtnReassignView.getVisibility() == View.GONE) {
                        showCourierListNhideBarcode();
                        logoutBtnReassignView.setVisibility(View.INVISIBLE);
                    } else {
                        resetBottomSelectedItem();
                        MainActivity.bottomNavigationView.bottomViewItemSelected(MainActivity.bottomNavigationView.getItemView(MainActivity.BOTTOMBAR_SELECTED_ITEM), MainActivity.BOTTOMBAR_SELECTED_ITEM, MainActivity.BOTTOMBAR_SELECTED_ITEM);
                        tabBarActivity();
                    }
                } else {
                    if (logoutBtnReassignView.getVisibility() == View.GONE)
                        logoutBtnReassignView.setVisibility(View.VISIBLE);
                    showCourierListNhideBarcode();
                }
                break;
            case R.id.barcodeViewManualScanBtn:
                manual_scan();
                break;
        }
    }

    @Override
    public void onItemClick(MyTeamList_Model item, AdapterAssignToOtherCourier.MyViewHolder holder, int position) {
        courierId = item.getCourierId();
        courierName = item.getFirstName()+" "+item.getLastName();
        for (int i = 0; i < arrayOfCouriers.size(); i++) {
             if (arrayOfCouriers.get(i).getCourierId().equals(courierId)) {
                 arrayOfCouriers.get(i).setSetFlagToSelectItem(true);
             } else {
                 arrayOfCouriers.get(i).setSetFlagToSelectItem(false);
             }
        }
        adapterCourierList.notifyDataSetChanged();
        showCameraPreview();
        logoutBtnReassignView.setVisibility(View.GONE);
    }

    @Override
    public void success_response(ResponseModel_TeamListFor_ReAssign response) {
        if (response != null) {
            if (response.getCourierArray() != null) {
                initCourierArray();
                currentCourierId = response.getCurrentCourierId();
                for (MyTeamList_Model myTeamListModel : response.getCourierArray()) {
                    try {
                        if (response.getCurrentCourierId().equals(myTeamListModel.getCourierId()))
                            myTeamListModel.setSetFlagToSelectItem(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    arrayOfCouriers.add(myTeamListModel);
                }
                showNotAvailTxtIfCouriersNotAvail();
            } else
                showNotAvailTxtIfCouriersNotAvail();
        } else
            showNotAvailTxtIfCouriersNotAvail();

        // refreshing recycler view
        adapterCourierList.notifyDataSetChanged();
    }

    //******** Init courier array ********
    private void initCourierArray() {
        if (arrayOfCouriers == null)
            arrayOfCouriers = new ArrayList<>();
        else
            arrayOfCouriers.clear();
    }

    //******** Show hide list not avail text ********
    private void showNotAvailTxtIfCouriersNotAvail() {
        if (arrayOfCouriers != null) {
            if (arrayOfCouriers.size() == 0) {
                notAvailTxt.setVisibility(View.VISIBLE);
                edtSearchViewReassignView.setVisibility(View.GONE);
            } else {
                notAvailTxt.setVisibility(View.GONE);
                edtSearchViewReassignView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void success_response_To_ReassignCourier(ResponseModel_ReAssignCourier responseModelReAssignCourier, String barcode) {
        if (backBtnReassignView.getVisibility() == View.VISIBLE && mPreview != null)
            mPreview.simple_dialog ("Assigned!", "Delivery contains AWB/Piece "+barcode+" has been successfully assigned to "+courierName);
    }

    @Override
    public void error(String message) {
        if (backBtnReassignView.getVisibility() == View.VISIBLE && mPreview != null)
            mPreview.simple_dialog ("Error!", message);
        else
            Utility.simple_dialog(Re_Assign_Deliveries.this, "Error!", message);
    }

    @Override
    public void showProgress() {
        Utility.showLoadingDialog(Re_Assign_Deliveries.this);
    }

    @Override
    public void hideProgress() {
        Utility.dismissLoadingDialog();
    }

    private void showCameraPreview() {
        // BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted

        showBarcodeNhideCourierList();
        if (mPreview != null)
            mPreview.resetBarcodeScanner();
        else {
            if (ActivityCompat.checkSelfPermission(Re_Assign_Deliveries.this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                // Permission is already available, start camera preview
                view_barcode();
            } else {
                // Permission is missing and must be requested.
                requestCameraPermission();
            }
        }
    }

    private void showBarcodeNhideCourierList (){
        backBtnReassignView.setVisibility(View.VISIBLE);
        barcodeFrameLayoutView.setVisibility(View.VISIBLE);
        titleReassignView.setVisibility(View.GONE);
        logoutBtnReassignView.setVisibility(View.GONE);
        edtSearchViewReassignView.setVisibility(View.GONE);
        recycleViewReassignView.setVisibility(View.GONE);
    }

    private void showCourierListNhideBarcode (){
        backBtnReassignView.setVisibility(View.GONE);
        barcodeFrameLayoutView.setVisibility(View.GONE);
        titleReassignView.setVisibility(View.VISIBLE);
        edtSearchViewReassignView.setVisibility(View.VISIBLE);
        recycleViewReassignView.setVisibility(View.VISIBLE);

        mPreview.stop_cam();

        if (callReassignDelivery == 1)
            backBtnReassignView.setVisibility(View.VISIBLE);
        else {
            backBtnReassignView.setVisibility(View.GONE);
            logoutBtnReassignView.setVisibility(View.VISIBLE);
        }
    }

    private void view_barcode() {
        mPreview = new Controller(Re_Assign_Deliveries.this, barcodeViewCameraPreview, barcodeViewTargetingBox, barcodeViewReassignView,this, 2);
    }

    /**
     * Requests the {@link android.Manifest.permission#CAMERA} permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private void requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(Re_Assign_Deliveries.this,
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with cda button to request the missing permission.
            trig_dialog(1);

        } else {
            trig_dialog(2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case BarCode.Location_PERMISSION_CONSTANT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    proceedAfterPermission();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(Re_Assign_Deliveries.this, Manifest.permission.CAMERA)) {
                        //Show Information about why you need the permission
                        trig_dialog(1);
                    } else {
                        trig_dialog(2);
                    }
                }
                break;
        }
    }

    private void proceedAfterPermission() {
        //Toast.makeText(Re_Assign_Deliveries.this, " Permission", Toast.LENGTH_LONG).show();
        view_barcode();
    }

    /**
     * function handle scan result
     * @param requestCode scanned code
     * @param resultCode  result of scanned code
     * @param data        intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BarCode.REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(Re_Assign_Deliveries.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    public void action1() {
        ActivityCompat.requestPermissions(Re_Assign_Deliveries.this, new String[]{Manifest.permission.CAMERA}, BarCode.Location_PERMISSION_CONSTANT);
    }

    @Override
    public void action2() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", Re_Assign_Deliveries.this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, BarCode.REQUEST_PERMISSION_SETTING);
    }

    private void trig_dialog(int pos) {
        permissionDialog_presenter_for_camera.call_dialog(getResources().getString(R.string.permission), getResources().getString(R.string.permission_msg), getResources().getString(R.string.camera), pos);
    }

    public void call_api_teamlist_for_reassign()
    {
        reAssignDelivery_presentor_impl.call_api_teamlist_for_reassign(sharedPreferenceManager);
    }

    public void call_api_for_reassign(String barcode)
    {
        reAssignDelivery_presentor_impl.call_api_for_reassign(sharedPreferenceManager, barcode, courierId);
    }

    @Override
    public void Logout_success() {
        sharedPreferenceManager.setValue_Login("", "");
        Intent intent = new Intent();
        intent.setClass(Re_Assign_Deliveries.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.from_right, R.anim.from_right);
    }

    private void manual_scan() {

        if (BarCode.dialog_custom != null)
            if (BarCode.dialog_custom.isShowing())
                BarCode.dialog_custom.dismiss();

        BarCode.dialog_custom = new Dialog(Re_Assign_Deliveries.this);
        BarCode.dialog_custom.setCancelable(false);
        BarCode.dialog_custom.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#90000000")));
        BarCode.dialog_custom.setContentView(R.layout.dialog_manual_scan);

        Window window = BarCode.dialog_custom.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.getAttributes().windowAnimations = R.style.DialogAnimation;

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        EditText editText=BarCode.dialog_custom.findViewById(R.id.editText);

        TextView txt_btn_cancel = BarCode.dialog_custom.findViewById(R.id.textView27);
        txt_btn_cancel.setOnClickListener(v -> {
            BarCode.dialog_custom.dismiss();
            mPreview.resetBarcodeScanner();

        });
        TextView dialog_customMsg = BarCode.dialog_custom.findViewById(R.id.textView28);
        dialog_customMsg.setOnClickListener(v -> {
            String code =  editText.getText().toString();
            if (TextUtils.isEmpty(code)) {
                Utility.showToast(Re_Assign_Deliveries.this, getResources().getString(R.string.scan_hint));
            }else if (code.trim().length()==0) {
                Utility.showToast(Re_Assign_Deliveries.this, getResources().getString(R.string.scan_hint));
            }else if (Utility.isNetworkAvailable(Re_Assign_Deliveries.this)) {
                BarCode.dialog_custom.dismiss();
                call_api_for_reassign(code);
            } else {
                Utility.messageInternet(Re_Assign_Deliveries.this);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editText.getText().toString().startsWith(" "))
                {
                    editText.setText(editText.getText().toString().replaceAll(" ",""));
                }
            }
        });
        mPreview.stop_cam();
        BarCode.dialog_custom.show();
    }
}
