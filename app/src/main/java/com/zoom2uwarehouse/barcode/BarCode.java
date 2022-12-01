package com.zoom2uwarehouse.barcode;


import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zoom2uwarehouse.R;
import com.zoom2uwarehouse.barcode.barcode_model.Barcode_model_imp;
import com.zoom2uwarehouse.barcode.barcode_presenter.Barcode_presenter_imp;
import com.zoom2uwarehouse.bean_class.resposebean.BarcodeResponse;
import com.zoom2uwarehouse.bean_class.resposebean.PieceObject;
import com.zoom2uwarehouse.database.DatabaseClient;
import com.zoom2uwarehouse.database.ScannedBookings;
import com.zoom2uwarehouse.dialog.permission_dialog.PermissionDialog_view;
import com.zoom2uwarehouse.dialog.permission_dialog.model.PermissionDialog_Model_Implementation;
import com.zoom2uwarehouse.dialog.permission_dialog.presenter.PermissionDialog_Presenter_Implementation;
import com.zoom2uwarehouse.home.MainActivity;
import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;
import com.zoom2uwarehouse.util.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BarCode extends Fragment implements PermissionDialog_view ,Barcode_view{

    private PermissionDialog_Presenter_Implementation mainPresenter;
    private Barcode_presenter_imp barcode_presenter_imp;
    public static final int Location_PERMISSION_CONSTANT = 100;
    public static final int REQUEST_PERMISSION_SETTING = 101;
    private View view_after;
    private Controller mPreview;
    private SharedPreferenceManager sharedPreferenceManager;
    private static Dialog dialog_custom_scan;
    public static Dialog dialog_custom;

    ImageView cornerBorder;

    RelativeLayout relativeLayoutRadioGroup;
    RadioButton radioNormalScan, radioReceived;

    public static String radioButtonSelectStr = "Scan";

    int isCourierLogin;

    static {
        System.loadLibrary("iconv");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_bar_code, container, false);
        view_after=view;
        sharedPreferenceManager= new SharedPreferenceManager(getActivity(),"warehouse_app");
        mainPresenter = new PermissionDialog_Presenter_Implementation(getActivity(), this, new PermissionDialog_Model_Implementation());
        barcode_presenter_imp=new Barcode_presenter_imp(this,new Barcode_model_imp());

        Bundle bundle = getArguments();
        isCourierLogin = bundle.getInt("isCourierLogin", 0);

        Typeface customFontBold = Typeface.createFromAsset(getActivity().getAssets(), "gothamrnd_bold.otf");

        cornerBorder = view.findViewById(R.id.imageView4);
        cornerBorder.setVisibility(View.VISIBLE);
        relativeLayoutRadioGroup = (RelativeLayout) view.findViewById(R.id.relativeLayoutRadioGroup);

        if (isCourierLogin == 0) {
            relativeLayoutRadioGroup.setVisibility(View.VISIBLE);
            RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.rgFilter);

            radioNormalScan = (RadioButton) radioGroup.findViewById(R.id.radioBtnNormalScan);
            radioNormalScan.setTypeface(customFontBold);
            radioReceived = (RadioButton) radioGroup.findViewById(R.id.radioBtnReceived);
            radioReceived.setTypeface(customFontBold);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.radioBtnNormalScan:
                            radioButtonSelectStr = "Scan";
                            radioNormalScan.setChecked(true);
                            radioReceived.setChecked(false);
                            radioNormalScan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.icon_checked);
                            radioReceived.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.uncheck);
                            break;
                        case R.id.radioBtnReceived:
                            radioButtonSelectStr = "Received";
                            radioNormalScan.setChecked(false);
                            radioReceived.setChecked(true);
                            radioNormalScan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.uncheck);
                            radioReceived.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.icon_checked);
                            break;
                    }
                }
            });
        } else {
            radioButtonSelectStr = "Scan";
            relativeLayoutRadioGroup.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showCameraPreview();
        sharedPreferenceManager.setValue_int("last_fragment",2);
        ((MainActivity)getActivity()).set_title("Scan AWB number");
        int nowCount = getFragmentManager().getBackStackEntryCount();
        Log.d("Found", "" + nowCount);
    }

    private void view_barcode(View view)
    {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView txt_manual_scan = view.findViewById(R.id.textView25);
        RelativeLayout relativeLayout2 = view.findViewById(R.id.relativeLayout2);
        FrameLayout preview = view.findViewById(R.id.cameraPreview);

        mPreview = new Controller(getActivity(), preview, cornerBorder, relativeLayout2,this);
        txt_manual_scan.setOnClickListener(v -> manual_scan());

    }
    private void showCameraPreview() {
        // BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
                view_barcode(view_after);
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
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
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
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
        //Toast.makeText(getActivity(), " Permission", Toast.LENGTH_LONG).show();
        view_barcode(view_after);
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
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    public void action1() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, Location_PERMISSION_CONSTANT);
    }

    @Override
    public void action2() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
    }

    private void trig_dialog(int pos) {
        mainPresenter.call_dialog(getActivity().getResources().getString(R.string.permission), getActivity().getResources().getString(R.string.permission_msg), getActivity().getResources().getString(R.string.camera), pos);
    }

    @Override
    public void onPause() {
        super.onPause();
//        if(mPreview!=null) {
//            mPreview.releaseCamera();
//        }
    }

    private void manual_scan() {

        dialog_custom = new Dialog(getActivity());
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
               Utility.showToast(getActivity(),getActivity().getResources().getString(R.string.scan_hint));
            }else if (code.trim().length()==0) {
                Utility.showToast(getActivity(),getActivity().getResources().getString(R.string.scan_hint));
            }else if (Utility.isNetworkAvailable(getActivity())) {
                    dialog_custom.dismiss();
                    call_service(editText.getText().toString());
                } else {
                    Utility.messageInternet(getActivity());
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
            dialog_custom_scan = new Dialog(getActivity());
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

            //zone views
            TextView tv_zone_label =dialog_custom_scan.findViewById(R.id.tv_zone_label);
            TextView tv_zone_value =  dialog_custom_scan.findViewById(R.id.tv_zone_value);

            try {
                if (response.zone==null||response.zone.isEmpty())
                {
                    tv_zone_label.setVisibility(View.GONE);
                    tv_zone_value.setVisibility(View.GONE);
                }else
                {
                    tv_zone_label.setVisibility(View.VISIBLE);
                    tv_zone_value.setVisibility(View.VISIBLE);
                    tv_zone_value.setText(response.getZone());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

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
                                try {
                                    runStatusTxtCustomerScanAlrt.setText(response.getStatus());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    runStatusTxtCustomerScanAlrt.setText(response.getStatus());
                                }
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
                try {
                    String totalStops = response.getTotalStops();
                    if (totalStops!=null&&!totalStops.isEmpty())
                    {
                        int stopsSize = Integer.parseInt(totalStops);
                        ScannedBookings scannedRecordByRunId = DatabaseClient.getInstance(getActivity())
                                .getAppDatabase().scannedDao().getScannedRecordByRunId(response.getRunId());
                        if (scannedRecordByRunId!=null&&scannedRecordByRunId.getAwbList()!=null)
                        {
                            if (scannedRecordByRunId.getAwbList().size()==stopsSize)
                            {
                               if (mPreview!=null)
                                   mPreview.simple_dialog("","All shipments for run "+response.getRunId()+" have now been scanned");
                            }
                        }

                    }
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
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

    private void setDialog_result_scan_for_pickup(BarcodeResponse response, String scan_Code) {

        try {
            dialog_custom_scan = new Dialog(getActivity());
            dialog_custom_scan.setCancelable(false);
            dialog_custom_scan.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#90000000")));
            dialog_custom_scan.setContentView(R.layout.custom_scan_alert_for_pickup);

            Window window = dialog_custom_scan.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            android.view.WindowManager.LayoutParams wlp = window.getAttributes();
            window.getAttributes().windowAnimations = R.style.DialogAnimation;

            wlp.gravity = Gravity.CENTER;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);

            TextView runNoCustomerScanAlrt = dialog_custom_scan.findViewById(R.id.runNoCustomerScanAlrt);

            TextView awbValueCustomerScanAlrt = dialog_custom_scan.findViewById(R.id.awbValueCustomerScanAlrt);

            TextView pieceValueCustomerScanAlrt = dialog_custom_scan.findViewById(R.id.pieceValueCustomerScanAlrt);

            TextView pickValueCustomerScanAlrt = dialog_custom_scan.findViewById(R.id.pickValueCustomerScanAlrt);

            TextView dropByValueCustomerScanAlrt = dialog_custom_scan.findViewById(R.id.dropByValueCustomerScanAlrt);

            TextView suburbPostCodeCustomerScanAlrt = dialog_custom_scan.findViewById(R.id.suburbPostCodeCustomerScanAlrt);

            runNoCustomerScanAlrt.setText(response.getStop());
            awbValueCustomerScanAlrt.setText(response.getAwb());
            pieceValueCustomerScanAlrt.setText(""+response.getPieces().size());
            pickValueCustomerScanAlrt.setText(response.getPickupDateTime());
            dropByValueCustomerScanAlrt.setText(response.getDropDateTime());

            suburbPostCodeCustomerScanAlrt.setText(response.getDropStreetName()+",\n"+response.getDropSuburb()+", "+response.getDropPostcode());

            TextView okBtnCustomScanAlert = dialog_custom_scan.findViewById(R.id.okBtnCustomScanAlert);

            okBtnCustomScanAlert.setOnClickListener(v -> {
                try {
                    String totalStops = response.getTotalStops();
                    if (totalStops!=null&&!totalStops.isEmpty())
                    {
                        int stopsSize = Integer.parseInt(totalStops);
                        ScannedBookings scannedRecordByRunId = DatabaseClient.getInstance(getActivity())
                                .getAppDatabase().scannedDao().getScannedRecordByRunId(response.getRunId());
                        if (scannedRecordByRunId!=null&&scannedRecordByRunId.getAwbList()!=null)
                        {
                            if (scannedRecordByRunId.getAwbList().size()==stopsSize)
                            {
                                if (mPreview!=null)
                                    mPreview.simple_dialog("","All shipments for run "+response.getRunId()+" have now been scanned");
                            }
                        }

                    }
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
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

    private void saveScannedBookings(String runId, ArrayList<String> awbList)
    {
        DatabaseClient databaseClient = DatabaseClient.getInstance(getActivity());
        ScannedBookings scannedBookings=new ScannedBookings();
        scannedBookings.setRunId(runId);
        scannedBookings.setAwbList(awbList);
        databaseClient.getAppDatabase().scannedDao().insertScanned(scannedBookings);

        try {
            SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
            Date time = Calendar.getInstance().getTime();
            String todayDate = sdf.format(time);
            sharedPreferenceManager.setValue("scannedDate",todayDate);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void success_response(BarcodeResponse response, String scan_code) {

        try {
            if (response!=null)
            {
                DatabaseClient databaseClient = DatabaseClient.getInstance(getActivity());
                String runId = response.getRunId();
                if (runId!=null&&!runId.equals("0"))
                {
                    String awbRes=response.getAwb();
                    ScannedBookings scannedRecordByRunId = databaseClient
                            .getAppDatabase().scannedDao().getScannedRecordByRunId(runId);
                    if (scannedRecordByRunId!=null)
                    {
                        ArrayList<String> awbList = scannedRecordByRunId.getAwbList();
                        if (awbList!=null&&awbRes!=null&&!awbRes.isEmpty())
                        {
                            boolean awbAlreadyExist=false;
                            for (String awb:awbList)
                            {
                                if (awb.equals(awbRes))
                                    awbAlreadyExist=true;
                            }
                            if (!awbAlreadyExist)
                            {
                                awbList.add(awbRes);
                               saveScannedBookings(runId,awbList);
                            }
                        }

                    }else{
                        ArrayList<String> awbList=new ArrayList<>();
                        awbList.add(awbRes);
                        saveScannedBookings(runId,awbList);
                    }
                }
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

        int isPiecePickedup = 0;
        try {
            if (response.getStatus().equals("Picked up")) {
                isPiecePickedup = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            isPiecePickedup = 0;
        }

        if (isPiecePickedup == 1) {
            if (isCourierLogin == 0)
                setDialog_result_scan_for_pickup(response, scan_code);
            else
                setDialog_result_scan(response, scan_code);
        } else
            setDialog_result_scan(response, scan_code);
    }

    @Override
    public void error(String message) {
           mPreview.simple_dialog ("Error!", message);
    }

    @Override
    public void showProgress() {
        Utility.showLoadingDialog(getActivity());
    }

    @Override
    public void hideProgress() {
        Utility.dismissLoadingDialog();
    }


    public void call_service(String barcode)
    {
        barcode_presenter_imp.call_service(sharedPreferenceManager, barcode, radioButtonSelectStr);
    }


}
