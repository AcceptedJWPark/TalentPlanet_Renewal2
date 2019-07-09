package accepted.talentplanet_renewal2.System;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import accepted.talentplanet_renewal2.Condition.condition_proc_pagerAdapter;
import accepted.talentplanet_renewal2.Condition.condition_req_pagerAdapter;
import accepted.talentplanet_renewal2.R;

import static android.graphics.Color.WHITE;


public class MainActivity_System extends AppCompatActivity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);

        mContext = getApplicationContext();
        ((TextView)findViewById(R.id.tv_toolbar)).setText("설정");
        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);

        ((ImageView) findViewById(R.id.img_open_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
