package accepted.talentplanet_renewal2.Cs;

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


public class MainActivity_Cs extends AppCompatActivity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs);

        mContext = getApplicationContext();
        ((TextView)findViewById(R.id.tv_toolbar)).setText("고객센터");
        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);
        findViewById(R.id.img_show1x15).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        ((ImageView) findViewById(R.id.img_open_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
