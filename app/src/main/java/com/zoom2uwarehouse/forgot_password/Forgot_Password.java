package com.zoom2uwarehouse.forgot_password;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zoom2uwarehouse.R;
import com.zoom2uwarehouse.bean_class.requestbean.LoginRequest;
import com.zoom2uwarehouse.bean_class.resposebean.ForgotResponse;
import com.zoom2uwarehouse.forgot_password.model.Forgot_model_implementation;
import com.zoom2uwarehouse.forgot_password.presenter.Forgot_presenter_implementation;
import com.zoom2uwarehouse.login.Login;
import com.zoom2uwarehouse.util.Utility;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zoom2uwarehouse.util.Utility.dismissLoadingDialog;
import static com.zoom2uwarehouse.util.Utility.hideKeyboard;
import static com.zoom2uwarehouse.util.Utility.isNetworkAvailable;
import static com.zoom2uwarehouse.util.Utility.messageInternet;
import static com.zoom2uwarehouse.util.Utility.showLoadingDialog;
import static com.zoom2uwarehouse.util.Utility.simple_Error_message;

public class Forgot_Password extends Activity implements  Forgot_view ,View.OnClickListener ,TextWatcher {

    // UI references.
    @BindView(R.id.editText4) EditText etEmail;
    private Forgot_presenter_implementation presenterImplementation;
    private   Dialog dialog_custom;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        initiateUI();
        presenterImplementation = new Forgot_presenter_implementation(this, new Forgot_model_implementation());
    }

    //Ui declaration
    private void initiateUI() {
        ButterKnife.bind(this);
        etEmail.addTextChangedListener(this);
        findViewById(R.id.textView54).setOnClickListener(this);
        findViewById(R.id.backFrom).setOnClickListener(this);
    }

    /**
     * Show the progress dialog when network operation is running in background.
     */
    @Override
    public void showProgress() {
        showLoadingDialog(Forgot_Password.this);
    }

    /**
     * Dismiss progress dialog when network operation executed.
     */
    @Override
    public void hideProgress() {
        dismissLoadingDialog();
    }

    /**
     * Show error message when email id is empty.
     */
    @Override
    public void setEmailBlankError() {
        Utility.simple_alert_message(Forgot_Password.this,getString(R.string.email_error));
    }

    /**
     * Show error message when email id is invalid.
     */
    @Override
    public void setEmailCorrectError() {
        Utility.simple_alert_message(Forgot_Password.this,getString(R.string.email_error1));
    }

    /**
     * Show the login error when credentials are wrong.
     * @param loginResponse it is a string message which come in response.
     */
    @Override
    public void showResult(ForgotResponse loginResponse) {

        hideKeyboard(Forgot_Password.this);
        simple_dialog();
    }

    /**
     * Show the login error when credentials are wrong.
     * @param message    it is a string message which come in response.
     */
    @Override
    public void showErrorResult(String message) {
        simple_Error_message(Forgot_Password.this,message);
    }

    /**
     * Throw the error like {@link UnknownHostException}, {@link SocketTimeoutException}
     * @param throwable object of {@link Throwable}
     */
    @Override
    public void show_Server_Error(Throwable throwable) {
        if (throwable instanceof UnknownHostException || throwable instanceof SocketTimeoutException) {
            simple_Error_message(Forgot_Password.this,"Sorry! Something went wrong here, Please try again later");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView54:
                if (etEmail != null ) {
                    if (isNetworkAvailable(Forgot_Password.this)) {
                        presenterImplementation.validateCredentials(new LoginRequest(etEmail.getText().toString().trim(),
                                "","password"));
                    } else {
                        messageInternet(Forgot_Password.this);                    }
                }
                break;

            case R.id.backFrom:
                back();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    private void back()
    {
        Intent intent = new Intent();
        intent.setClass(Forgot_Password.this, Login.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.from_right, R.anim.from_right);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if(etEmail.getText().toString().startsWith(" "))
        {
            etEmail.setText(etEmail.getText().toString().replaceAll(" ",""));
        }
    }


    private void simple_dialog()
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
            dialog_custom = new Dialog(Forgot_Password.this);
            dialog_custom.setCancelable(false);

            dialog_custom.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#90000000")));
            dialog_custom.setContentView(R.layout.dialog_permission);

            Window window = dialog_custom.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            android.view.WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.CENTER;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);


            TextView dialog_custom_title =  dialog_custom.findViewById(R.id.textView13);

            dialog_custom_title.setText(getResources().getString(R.string.successfully));

            TextView dialog_customMsg =  dialog_custom.findViewById(R.id.textView14);

            dialog_customMsg.setText("Please check your email to get password.");
            TextView txt_btn_cancel= dialog_custom.findViewById(R.id.textView15);
            txt_btn_cancel.setOnClickListener(v -> {
                dialog_custom.dismiss();
                Intent intent = new Intent();
                intent.setClass(Forgot_Password.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.from_left, R.anim.from_left);
            });


            dialog_custom.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
