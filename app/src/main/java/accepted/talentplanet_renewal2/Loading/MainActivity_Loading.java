package accepted.talentplanet_renewal2.Loading;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import accepted.talentplanet_renewal2.JoinLogin.MainActivity_Login;
import accepted.talentplanet_renewal2.R;


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
                Intent intent = new Intent(getBaseContext(), MainActivity_Login.class);
                startActivity(intent);
                finish();
            }
        },3000);


    }
}
