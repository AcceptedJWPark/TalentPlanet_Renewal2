package com.accepted.acceptedtalentplanet.Loading;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.accepted.acceptedtalentplanet.MarketVersionChecker;

import com.accepted.acceptedtalentplanet.JoinLogin.MainActivity_Login;
import com.accepted.acceptedtalentplanet.MarketVersionChecker;
import com.accepted.acceptedtalentplanet.R;


public class MainActivity_Loading extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.loading);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                String storeVersion = MarketVersionChecker.getMarketVersion(getPackageName());
//                try {
//                    String deviceVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
//                    if (!storeVersion.equals(deviceVersion)){
//                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
//                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                        startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(getBaseContext(), MainActivity_Login.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                }catch (Exception e){
                    Intent intent = new Intent(getBaseContext(), MainActivity_Login.class);
                    startActivity(intent);
                    finish();
//                    e.printStackTrace();
//                }
            }
        },3000);


    }
}
