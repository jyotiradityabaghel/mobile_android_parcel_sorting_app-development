package com.zoom2uwarehouse.barcode;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zoom2uwarehouse.R;
import com.zoom2uwarehouse.re_assign_delivery.ReAssignDelivery_View;
import com.zoom2uwarehouse.re_assign_delivery.Re_Assign_Deliveries;
import com.zoom2uwarehouse.ui.Splash;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

public class Controller implements View.OnTouchListener {

    private Camera mCamera;
    private CameraPreview mPreview;
    RelativeLayout headerBarCodeScanner;
    private Handler autoFocusHandler;
    private ImageScanner scanner;
    private boolean barcodeScanned = false;
    private boolean previewing = true;
    private Context currentBarcodeContext;
    private FrameLayout preview;
    private float croppedViewX;
    private float croppedViewY;
    private float croppedViewHeight;
    private float croppedViewWidth;
    private int _xDelta;
    private int _yDelta;
    private ViewGroup.MarginLayoutParams headerBarCodeScannerViewHeightParam;
    private ViewGroup.MarginLayoutParams scannerViewParam;
    private ImageView scannerView;
    private  BarCode barCode;
    private static Dialog dialog_custom;

    int isReturnView = 0;
    ReturnView returnView;

    Re_Assign_Deliveries assignDeliveryView;

    static {
        try {
            System.loadLibrary("iconv");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Controller(Context currentBarcodeContext, FrameLayout preview, ImageView cornerBorder, RelativeLayout headerBarCodeScanner, BarCode barCode) {

        this.preview = preview;
        this.headerBarCodeScanner = headerBarCodeScanner;
        this.currentBarcodeContext = currentBarcodeContext;
        this.scannerView = cornerBorder;
        this.barCode = barCode;
        isReturnView = 0;
        inItBarcodeScanner();
    }

    public Controller(Context currentBarcodeContext, FrameLayout preview, ImageView cornerBorder, RelativeLayout headerBarCodeScanner, ReturnView returnView, int isReturnView) {

        this.preview = preview;
        this.headerBarCodeScanner = headerBarCodeScanner;
        this.currentBarcodeContext = currentBarcodeContext;
        this.scannerView = cornerBorder;
        this.returnView = returnView;
        this.isReturnView = isReturnView;
        inItBarcodeScanner();
    }

    public Controller(Context currentBarcodeContext, FrameLayout preview, ImageView cornerBorder, RelativeLayout headerBarCodeScanner, Re_Assign_Deliveries assignDeliveryView, int isReturnView) {

        this.preview = preview;
        this.headerBarCodeScanner = headerBarCodeScanner;
        this.currentBarcodeContext = currentBarcodeContext;
        this.scannerView = cornerBorder;
        this.assignDeliveryView = assignDeliveryView;
        this.isReturnView = isReturnView;
        inItBarcodeScanner();
    }


    private void inItBarcodeScanner() {

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        // Instance barcode scanner
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 4);
        scanner.setConfig(0, Config.Y_DENSITY, 4);

        scannerViewParam = (ViewGroup.MarginLayoutParams) scannerView.getLayoutParams();
        headerBarCodeScannerViewHeightParam = (ViewGroup.MarginLayoutParams) headerBarCodeScanner.getLayoutParams();

        croppedViewX = (Splash.width - scannerViewParam.width) / 2;

        int scannerViewTopMarginFromOrigin = ((scannerViewParam.height / 2) + headerBarCodeScannerViewHeightParam.height + getStatusBarHeight());
        croppedViewY = (Splash.height / 2 - scannerViewTopMarginFromOrigin);
        scannerViewParam.setMargins((int) croppedViewX, (int) croppedViewY, 0, 0);
        scannerView.setLayoutParams(scannerViewParam);

        croppedViewHeight = scannerViewParam.height;
        croppedViewWidth = scannerViewParam.width;

        scannerView.setOnTouchListener(this);

        mPreview = new CameraPreview(currentBarcodeContext, mCamera, previewCb, autoFocusCB);
        preview.addView(mPreview);
        preview.removeView(scannerView);
        preview.addView(scannerView);

        previewing = true;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = currentBarcodeContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = currentBarcodeContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    private static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            try {
                if (previewing)
                    mCamera.autoFocus(autoFocusCB);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    // Mimic continuous auto-focusing
    private Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1500);
        }
    };


    public void resetBarcodeScanner() {
        if (barcodeScanned) {
                barcodeScanned = false;
                mCamera.setPreviewCallback(previewCb);
                mCamera.startPreview();
                previewing = true;
                mCamera.autoFocus(autoFocusCB);
        }
    }

    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                ViewGroup.MarginLayoutParams lParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;

                view.setLayoutParams(layoutParams);
                croppedViewX = layoutParams.leftMargin;
                croppedViewY = layoutParams.topMargin;
                break;
        }
        preview.invalidate();
        return true;
    }

    private Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (previewing) {
                try {
                    Camera.Parameters parameters = camera.getParameters();
                    Camera.Size size = parameters.getPreviewSize();

                    float zBarViewWidth = Splash.width;
                    float zBarViewHeight = Splash.height - (headerBarCodeScannerViewHeightParam.height + getStatusBarHeight());

                    float factorWidth = size.width / zBarViewHeight;
                    float factorHeight = size.height / zBarViewWidth;

                    Image barcode = new Image(size.width, size.height, "Y800");
                    barcode.setData(data);

                    barcode.setCrop((int) (croppedViewY * factorWidth), (int) (croppedViewX * factorHeight), (int) (croppedViewHeight * factorWidth), (int) (croppedViewWidth * factorHeight));

                    int result = scanner.scanImage(barcode);

                    if (result != 0) {

                        previewing = false;
                        mCamera.setPreviewCallback(null);
                        mCamera.stopPreview();

                        SymbolSet symbolSet = scanner.getResults();
                        for (Symbol sym : symbolSet) {

                            String scanResult = sym.getData().trim();
                            /// showAlertDialog(scanResult);
                            //    ScanButton1.setText(scanResult);
                            if (isReturnView == 1)
                                returnView.call_service(scanResult);
                            else if (isReturnView == 2)
                                assignDeliveryView.call_api_for_reassign(scanResult);
                            else
                                barCode.call_service(scanResult);
                        //    simple_dialog(scanResult);
                            Log.e("Code", "" + scanResult);
                            barcodeScanned = true;
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void simple_dialog(String titleStr, String msgStr) {
        try {
            if (dialog_custom != null)
                if (dialog_custom.isShowing())
                    dialog_custom.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (dialog_custom != null)
            dialog_custom = null;
            dialog_custom = new Dialog(currentBarcodeContext);
            dialog_custom.setCancelable(false);
            dialog_custom.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#90000000")));
            dialog_custom.setContentView(R.layout.dialog_permission);

            Window window = dialog_custom.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            android.view.WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.CENTER;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);


            TextView dialog_custom_title = dialog_custom.findViewById(R.id.textView13);

            dialog_custom_title.setText(titleStr);

            TextView dialog_customMsg = dialog_custom.findViewById(R.id.textView14);

            dialog_customMsg.setText(msgStr);
            TextView txt_btn_cancel = dialog_custom.findViewById(R.id.textView15);
            txt_btn_cancel.setOnClickListener(v -> {
                dialog_custom.dismiss();
                resetBarcodeScanner();
            });
            stop_cam();
            dialog_custom.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop_cam() {
        previewing = false;
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        barcodeScanned = true;
    }


}
