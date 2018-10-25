package com.cyberswift.facebookintegrate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyberswift.facebookintegrate.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by User-129-pc on 21-09-2018.
 */

public class GoogleUserProfile extends AppCompatActivity {
    private Button buttonLogout;
   private TextView textViewEmail;
    private TextView textViewPersonName,textViewAccountId;
    private ImageView imageViewProfilePic;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_user_profile);
        initView();
        valueFromIntent();
    }

    private void valueFromIntent() {
        Intent intent = getIntent();
        textViewEmail.setText(intent.getStringExtra("Email"));
        textViewPersonName.setText(intent.getStringExtra("Name"));
        textViewAccountId.setText(intent.getStringExtra("AccountId"));
    }

    private void initView() {
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPersonName = findViewById(R.id.textViewPersonName);
        imageViewProfilePic = findViewById(R.id.imageViewProfilePic);
        textViewAccountId = findViewById(R.id.textViewAccountId);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //get Sign in client
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        buttonLogout = (Button)findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }
    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                  finish();
                }
            }
        });

    }

}
