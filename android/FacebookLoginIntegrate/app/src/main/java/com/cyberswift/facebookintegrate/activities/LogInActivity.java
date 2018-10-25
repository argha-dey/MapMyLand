package com.cyberswift.facebookintegrate.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cyberswift.facebookintegrate.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;


public class LogInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    CallbackManager callbackManager;
    private LoginButton fb_sign_in_button;
    private Button custom_Fb_Button, custom_google_sign_in_btn;
    private SignInButton googleLogInButton;

   private GoogleSignInClient mGoogleSignInClient;
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private static final int GOOGLE_SIGN_IN = 007;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mContext = LogInActivity.this;
        initViewActivity();
    }

    private void initViewActivity() {
        facebookLoginMethod();
        googleLoginMethod();
    }

    private void googleLoginMethod() {
        custom_google_sign_in_btn = (Button)findViewById(R.id.custom_google_sign_in_btn);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //get Sign in client
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        custom_google_sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, GOOGLE_SIGN_IN);

            }
        });



    }


    private void facebookLoginMethod() {
        callbackManager = CallbackManager.Factory.create();
        fb_sign_in_button = (LoginButton) findViewById(R.id.fb_sign_in_button);
        custom_Fb_Button = (Button) findViewById(R.id.custom_fb_btn);
        custom_Fb_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fb_sign_in_button.performClick();
            }
        });
        fb_sign_in_button.setReadPermissions("email");
        fb_sign_in_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                getFbUserDetails(loginResult);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            hideProgressDialog();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("GOOGLE AUTH", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            //get currently signed in user returns null if there is no logged in user
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            //GoogleSignInAccount acct = result.getSignInAccount();
            updateUI(true, acct);
        } else {
            updateUI(false, null);
        }
    }


    private void updateUI(boolean isSignedIn, GoogleSignInAccount userAccountData) {
        if (isSignedIn) {
            String personName = userAccountData.getDisplayName();
            String email = userAccountData.getEmail();
            String accountId = userAccountData.getId();
            Uri profilePic = userAccountData.getPhotoUrl();

            Log.e("GOOGLE AUTH", "Name: " + personName + ", email: " + email + ",accountId: " + accountId + ",Profile Uri :" + profilePic);

            Log.i(" successful ", "Google Log in");
          //  Intent intent = new Intent(LogInActivity.this, GoogleUserProfile.class);
           // Intent intent = new Intent(LogInActivity.this,  MultiShapeMapActivity.class);
            Intent intent = new Intent(LogInActivity.this,  ShapeListActivity.class);   // Goto ShapeList Activity....
           // intent.putExtra("Name",personName);
          //  intent.putExtra("Email",email);
          //  intent.putExtra("AccountId",accountId);
            startActivity(intent);


        } else {

        }
    }

    protected void getFbUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json_object, GraphResponse response) {
                        System.out.println("Response :" + json_object.toString());
                        System.out.println("GraphResponse :" + response.toString());
                        Intent intent = new Intent(LogInActivity.this, FbUserProfile.class);
                        intent.putExtra("userProfile", json_object.toString());
                        startActivity(intent);
                    }
                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

   @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgressDialog();
    }


    private void test() {}
}