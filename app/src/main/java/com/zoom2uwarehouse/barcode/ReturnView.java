package com.zoom2uwarehouse.barcode;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.zoom2uwarehouse.barcode.barcode_model.Barcode_model_imp;
import com.zoom2uwarehouse.barcode.barcode_presenter.Barcode_presenter_imp;
import com.zoom2uwarehouse.bean_class.resposebean.BarcodeResponse;
import com.zoom2uwarehouse.bean_class.resposebean.PieceObject;
import com.zoom2uwarehouse.dialog.permission_dialog.PermissionDialog_view;
import com.zoom2uwarehouse.dialog.permission_dialog.model.PermissionDialog_Model_Implementation;
import com.zoom2uwarehouse.dialog.permission_dialog.presenter.PermissionDialog_Presenter_Implementation;
import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;
import com.zoom2uwarehouse.util.Utility;

public class ReturnView extends AppCompatActivity implements View.OnClickListener, PermissionDialog_view, Barcode_view {

    private PermissionDialog_Presenter_Implementation mainPresenter;
    private Barcode_presenter_imp barcode_presenter_imp;
    private static final int Location_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private Controller mPreview;
    private SharedPreferenceManager sharedPreferenceManager;
    private static Dialog dialog_custom_scan, dialog_custom;

    RelativeLayout headerViewForTitleTxt;

    RelativeLayout relativeLayoutRadioGroup;

    static {
        System.loadLibrary("iconv");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bar_code);

        sharedPreferenceManager= new SharedPreferenceManager(ReturnView.this,"warehouse_app");
        mainPresenter = new PermissionDialog_Presenter_Implementation(ReturnView.this, this, new PermissionDialog_Model_Implementation());
        barcode_presenter_imp=new Barcode_presenter_imp(this,new Barcode_model_imp());

        Typeface customFontBold = Typeface.createFromAsset(ReturnView.this.getAssets(), "gothamrnd_bold.otf");

        headerViewForTitleTxt = (RelativeLayout) findViewById(R.id.headerViewForTitleTxt);
        headerViewForTitleTxt.setVisibility(View.VISIBLE);
        findViewById(R.id.closeBtnReturnView).setOnClickListener(this);
        relativeLayoutRadioGroup = (RelativeLayout) findViewById(R.id.relativeLayoutRadioGroup);
        relativeLayoutRadioGroup.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        showCameraPreview();
        sharedPreferenceManager.setValue_int("last_fragment",2);
        int nowCount = getFragmentManager().getBackStackEntryCount();
        Log.d("Found", "" + nowCount);
    }

    private void view_barcode()
    {
        ImageView cornerBorder = findViewById(R.id.imageView4);

        ReturnView.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView txt_manual_scan = findViewById(R.id.textView25);
        RelativeLayout relativeLayout2 = findViewById(R.id.relativeLayout2);
        FrameLayout preview = findViewById(R.id.cameraPreview);

        mPreview = new Controller(ReturnView.this, preview, cornerBorder, relativeLayout2,this, 1);
        txt_manual_scan.setOnClickListener(v -> manual_scan());

    }
    private void showCameraPreview() {
        // BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(ReturnView.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            view_barcode();
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(ReturnView.this,
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
            case Location_PERMISSION_CONSTANT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    proceedAfterPermission();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ReturnView.this, Manifest.permission.CAMERA)) {
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
        //Toast.makeText(ReturnView.this, " Permission", Toast.LENGTH_LONG).show();
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
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(ReturnView.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    public void action1() {
        ActivityCompat.requestPermissions(ReturnView.this, new String[]{Manifest.permission.CAMERA}, Location_PERMISSION_CONSTANT);
    }

    @Override
    public void action2() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", ReturnView.this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
    }

    private void trig_dialog(int pos) {
        mainPresenter.call_dialog(ReturnView.this.getResources().getString(R.string.permission), ReturnView.this.getResources().getString(R.string.permission_msg), ReturnView.this.getResources().getString(R.string.camera), pos);
    }

    @Override
    public void onPause() {
        super.onPause();
//        if(mPreview!=null) {
//            mPreview.releaseCamera();
//        }
    }

    private void manual_scan() {

        dialog_custom = new Dialog(ReturnView.this);
        dialog_custom.setCancelable(false);
        dialog_custom.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#90000000")));
        dialog_custom.setContentView(R.layout.dialog_manual_scan);

        Window window = dialog_custom.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.getAttributes().windowAnimations = R.style.DialogAnimation;

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        EditText editText=dialog_custom.findViewById(R.id.editText);

        TextView txt_btn_cancel = dialog_custom.findViewById(R.id.textView27);
        txt_btn_cancel.setOnClickListener(v -> {
            dialog_custom.dismiss();
            mPreview.resetBarcodeScanner();

        });
        TextView dialog_customMsg = dialog_custom.findViewById(R.id.textView28);
        dialog_customMsg.setOnClickListener(v -> {
            String code =  editText.getText().toString();
            if (TextUtils.isEmpty(code)) {
                Utility.showToast(ReturnView.this,ReturnView.this.getResources().getString(R.string.scan_hint));
            }else if (code.trim().length()==0) {
                Utility.showToast(ReturnView.this,ReturnView.this.getResources().getString(R.string.scan_hint));
            }else if (Utility.isNetworkAvailable(ReturnView.this)) {
                dialog_custom.dismiss();
                call_service(editText.getText().toString());
            } else {
                Utility.messageInternet(ReturnView.this);
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
        dialog_custom.show();

    }


    private void setDialog_result_scan(BarcodeResponse response, String scan_Code) {

        try {
            dialog_custom_scan = new Dialog(ReturnView.this);
            dialog_custom_scan.setCancelable(false);
            dialog_custom_scan.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#90000000")));
            dialog_custom_scan.setContentView(R.layout.customer_scan_alert);

            Window window = dialog_custom_scan.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            android.view.WindowManager.LayoutParams wlp = window.getAttributes();
            window.getAttributes().windowAnimations = R.style.DialogAnimation;

            wlp.gravity = Gravity.CENTER;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);

            TextView runNoCustomerScanAlrt = dialog_custom_scan.findViewById(R.id.runNoCustomerScanAlrt);
            TextView runTxtCustomerScanAlrt = dialog_custom_scan.findViewById(R.id.runTxtCustomerScanAlrt);

            TextView awbValueCustomerScanAlrt = dialog_custom_scan.findViewById(R.id.awbValueCustomerScanAlrt);

            TextView pieceValueCustomerScanAlrt = dialog_custom_scan.findViewById(R.id.pieceValueCustomerScanAlrt);

            TextView stopValueCustomerScanAlrt = dialog_custom_scan.findViewById(R.id.stopValueCustomerScanAlrt);

            TextView pickValueCustomerScanAlrt = dialog_custom_scan.findViewById(R.id.pickValueCustomerScanAlrt);

            TextView dropByValueCustomerScanAlrt = dialog_custom_scan.findViewById(R.id.dropByValueCustomerScanAlrt);

            TextView runStatusTxtCustomerScanAlrt = dialog_custom_scan.findViewById(R.id.runStatusTxtCustomerScanAlrt);
            TextView suburbPostCodeCustomerScanAlrt = dialog_custom_scan.findViewById(R.id.suburbPostCodeCustomerScanAlrt);
            TextView courierValueCustomerScanAlrt = dialog_custom_scan.findViewById(R.id.courierValueCustomerScanAlrt);

            try {
                if (response.getRunNumber().equals("")) {
                    runNoCustomerScanAlrt.setVisibility(View.GONE);
                    runTxtCustomerScanAlrt.setVisibility(View.GONE);
                } else {
                    runNoCustomerScanAlrt.setVisibility(View.VISIBLE);
                    runTxtCustomerScanAlrt.setVisibility(View.VISIBLE);
                    runNoCustomerScanAlrt.setText(""+response.getRunNumber());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runNoCustomerScanAlrt.setVisibility(View.GONE);
                runTxtCustomerScanAlrt.setVisibility(View.GONE);
            }

            awbValueCustomerScanAlrt.setText(response.getAwb());
            stopValueCustomerScanAlrt.setText(response.getStop());
            pieceValueCustomerScanAlrt.setText(""+response.getPieces().size());
            pickValueCustomerScanAlrt.setText(response.getPickupDateTime());
            dropByValueCustomerScanAlrt.setText(response.getDropDateTime());

            if (response.getAwb().equals(scan_Code)) {
                runStatusTxtCustomerScanAlrt.setText(response.getStatus());
            } else {
                for (PieceObject pieceObject : response.getPieces()) {
                    if (pieceObject.getPieceIdBarcode().equals(scan_Code)) {
                        try {
                            if (response.getStatus() == null) {
                                runStatusTxtCustomerScanAlrt.setText(pieceObject.getStatus());
                            } else {
                                runStatusTxtCustomerScanAlrt.setText(response.getStatus());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            runStatusTxtCustomerScanAlrt.setText(pieceObject.getStatus());
                        }
                        break;
                    }
                }
            }

            suburbPostCodeCustomerScanAlrt.setText(response.getDropStreetName()+",\n"+response.getDropSuburb()+", "+response.getDropPostcode());

            try {
                if (response.getDriver().equals(""))
                    courierValueCustomerScanAlrt.setText("Pending");
                else
                    courierValueCustomerScanAlrt.setText(response.getDriver());
            } catch (Exception e) {
                e.printStackTrace();
                courierValueCustomerScanAlrt.setText("Pending");
            }

            TextView okBtnCustomScanAlert = dialog_custom_scan.findViewById(R.id.okBtnCustomScanAlert);

            okBtnCustomScanAlert.setOnClickListener(v -> {
                dialog_custom_scan.dismiss();
                mPreview.resetBarcodeScanner();
            });
            mPreview.stop_cam();
            dialog_custom_scan.show();
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void success_response(BarcodeResponse response, String scan_code) {
        setDialog_result_scan(response, scan_code);
    }

    @Override
    public void error(String message) {
        mPreview.simple_dialog ("Error!", message);
    }

    @Override
    public void showProgress() {
        Utility.showLoadingDialog(ReturnView.this);
    }

    @Override
    public void hideProgress() {
        Utility.dismissLoadingDialog();
    }


    public void call_service(String barcode)
    {
        barcode_presenter_imp.call_service(sharedPreferenceManager, barcode, "Returned");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.closeBtnReturnView:
                finish();
                break;
        }

    }
}
