package com.zoom2uwarehouse.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;

import com.zoom2uwarehouse.R;
import com.zoom2uwarehouse.bean_class.requestbean.LoginRequest;
import com.zoom2uwarehouse.bean_class.resposebean.LoginResponse;
import com.zoom2uwarehouse.forgot_password.Forgot_Password;
import com.zoom2uwarehouse.home.MainActivity;
import com.zoom2uwarehouse.login.model.LoginModelImplementation;
import com.zoom2uwarehouse.login.presenter.LoginPresenterImplementation;
import com.zoom2uwarehouse.shared_preference_manager.SharedPreferenceManager;
import com.zoom2uwarehouse.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Login extends AppCompatActivity implements LoginView ,View.OnClickListener ,TextWatcher{

    @BindView(R.id.editText2)
     EditText etEmail;
    @BindView(R.id.editText3)
     EditText etPassword;
    // UI references.
    private LoginPresenterImplementation loginPresenter;
    private SharedPreferenceManager sharedPreferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Utility.setCustomFontStyle(Login.this);

        sharedPreferenceManager= new SharedPreferenceManager(Login.this,"warehouse_app");
        loginPresenter = new LoginPresenterImplementation(this, new LoginModelImplementation());
        initiateUI();
    }

    //Ui declaration
    private void initiateUI() {
        etEmail.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
        findViewById(R.id.textView2).setOnClickListener(this);
        findViewById(R.id.textView3).setOnClickListener(this);
        String [] strings=sharedPreferenceManager.getValue_Login();
        etEmail.setText(strings[0]);
        etPassword.setText(strings[1]);
        etPassword.setTransformationMethod(new PasswordTransformationMethod());

        etEmail.setSelection(etEmail.getText().length());
    }

    /**
     * Show the progress dialog when network operation is running in background.
     */
    @Override
    public void showProgress() {
       Utility.showLoadingDialog(Login.this);
    }

    /**
     * Dismiss progress dialog when network operation executed.
     */
    @Override
    public void hideProgress() {
        Utility.dismissLoadingDialog();
    }

    /** Show error message when email id is empty.*/
    @Override
    public void setEmailBlankError() {
        Utility.simple_alert_message(Login.this,getString(R.string.email_error));
    }

    /**  Show error message when email id is invalid. */
    @Override
    public void setEmailCorrectError() {
        Utility.simple_alert_message(Login.this,getString(R.string.email_error1));
    }

    /** Show error message when password field is empty.*/
    @Override
    public void setPasswordBlankError() {
        Utility.simple_alert_message(Login.this,getString(R.string.password_error));
    }

    /**
     * Show error message when password field is invalid.
     */
    @Override
    public void setPasswordMinimumBlankError() {
        Utility.simple_alert_message(Login.this,getString(R.string.password_error1));
    }

    /**
     * After successful login pass the login result to UI.
     * @param loginResponse LoginResponse model object is parameter which provided by network operations.
     */
    @Override
    public void showLoginResult(LoginResponse  loginResponse) {

        sharedPreferenceManager.setValue_Login(etEmail.getText().toString(),etPassword.getText().toString());
        sharedPreferenceManager.setValue("access_token",loginResponse.getAccess_token());
        sharedPreferenceManager.setValue("token_type",loginResponse.getToken_type());
        sharedPreferenceManager.setValue("roles",loginResponse.getRoles());
        sharedPreferenceManager.setValue_int("carrierId",loginResponse.getCarrierId());
        Utility.hideKeyboard(Login.this);

        if (loginResponse.getRoles().equals("Courier")) {
            if (loginResponse.getCarrierId() != 0)
                afterLoggedInActivity(MainActivity.class, 1);
            else
                Utility.simple_alert_message(Login.this,"Sorry, you do not have permission to access this portal.");
        } else
            afterLoggedInActivity(MainActivity.class, 0);
    }

    private void afterLoggedInActivity(Class re_assign_deliveriesClass, int calledReassignDelivery) {
        MainActivity.BOTTOMBAR_SELECTED_ITEM = 0;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(Login.this, re_assign_deliveriesClass);
        intent.putExtra("CallReassignDelivery", calledReassignDelivery);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.from_left, R.anim.from_left);
    }

    /**
     * Show the login error when credentials are wrong.
     * @param message    it is a string message which come in response.
     */
    @Override
    public void showLoginErrorResult(String message) {
        Utility.simple_Error_message(Login.this,message);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.textView2:

                if (etEmail != null && etPassword != null) {
                    if (Utility.isNetworkAvailable(Login.this)) {
                        LoginRequest loginRequest=new LoginRequest();
                        loginRequest.setEmail(etEmail.getText().toString().trim());
                        loginRequest.setPassword(etPassword.getText().toString().trim());
                        loginRequest.setGrant_type("password");
                        //calling the presenter where we call the model class to validate data and after that return response.
                        loginPresenter.validateCredentials(loginRequest);
                    } else {
                        Utility.messageInternet(Login.this);                    }
                }
                break;

            case R.id.textView3:
                Intent intent = new Intent();
                intent.setClass(Login.this, Forgot_Password.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.from_left, R.anim.from_left);
                break;
        }
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

        if(etPassword.getText().toString().startsWith(" "))
        {
            etPassword.setText(etPassword.getText().toString().replaceAll(" ",""));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
