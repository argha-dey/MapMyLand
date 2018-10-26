package com.cyberswift.cyberengine.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.cyberswift.cyberengine.R;
import com.cyberswift.cyberengine.listeners.AlertDialogCallBack;
import com.cyberswift.cyberengine.utility.Prefs;
import com.cyberswift.cyberengine.utility.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SplashActivity extends AppCompatActivity {

    private Context mContext;
    private Prefs mPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = SplashActivity.this;

        mPrefs = new Prefs(SplashActivity.this);
        // Check version Code .....
        if (Util.checkConnectivity(mContext)) {
            new GetVersionCode().execute();
        } else {
            Util.showAlertDialogOkWithCallback(mContext, "Please check the internet connection to continue.", new AlertDialogCallBack() {
                @Override
                public void onSubmit() {
                    finish();
                }

                @Override
                public void onCancel() {}
            });
        }
    }


    public void openLoginActivity() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }


    private void openHomeActivity() {
        startActivity(new Intent(SplashActivity.this, BaseActivity.class));
        finish();
    }


    class GetVersionCode extends AsyncTask<Void, String, String> {
        String currentVersion = "0";
        String appStoreUrl = "";

        @Override
        protected void onPreExecute() {
            try {
                PackageInfo pInfo = SplashActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
                currentVersion = pInfo.versionName;
                appStoreUrl = "https://play.google.com/store/apps/details?id=" + SplashActivity.this.getPackageName() + "&hl=en";
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        // https://play.google.com/store/apps/details?id=com.cyberswift.cyberengine&hl=en;
        @Override
        protected String doInBackground(Void... voids) {
            String newVersion = null;
            try {
                Document document = Jsoup.connect(appStoreUrl)
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mContext, "Error in connecting to Play Store. Try again.", Toast.LENGTH_LONG).show();
                finish();
            }
            return newVersion;
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
                    /**Check if updating the app to Version 2.0, if so then clear the preference data as the
                    SharedPreference data types has been changed to the same data types of the fields in the server database.**/
                    if (onlineVersion.equals("2.0"))
                        mPrefs.clearPrefsData();

                    Util.showAlertDialogOkWithCallback(SplashActivity.this, "New version is available. Please update.", new AlertDialogCallBack() {
                        @Override
                        public void onSubmit() {
                            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appStoreUrl));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancel() {}
                    });
                } else
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!mPrefs.getLoginStatus()) {
                                openLoginActivity();
                            } else {
                                openHomeActivity();
                            }
                            finish();
                        }
                    }, 2000);
            }
        }
    }
}
