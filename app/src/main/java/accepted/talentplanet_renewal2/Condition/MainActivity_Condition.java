package accepted.talentplanet_renewal2.Condition;

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
import android.widget.LinearLayout;
import android.widget.TextView;


import accepted.talentplanet_renewal2.R;

import static android.graphics.Color.WHITE;


public class MainActivity_Condition extends AppCompatActivity {

    TextView tv_toolbar;
    Context mContext;
    ViewPager vp_req_condition;
    ViewPager vp_proc_condition;
    condition_req_pagerAdapter adapter_req;
    condition_proc_pagerAdapter adapter_proc;


    ViewPager.OnPageChangeListener onPageChangeListener;


    boolean ismymentor;

    Button btn_req_condition;
    Button btn_proc_condition;
    Button btn_comp_condition;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);

        mContext = getApplicationContext();
        tv_toolbar = findViewById(R.id.tv_toolbar);

        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);
        ((ImageView) findViewById(R.id.img_open_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIntent().removeExtra("ismyMentor");
                finish();
            }
        });

        intent = getIntent();
        String str;
        str = intent.getExtras().getString("ismyMentor");

        if(str.equals("1"))
        {
            ismymentor=true;
        }
        else if(str.equals("2"))
        {
            ismymentor=false;
        }

        findViewById(R.id.img_show1x15).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        btn_req_condition = findViewById(R.id.btn_req_condition);
        btn_proc_condition = findViewById(R.id.btn_proc_condition);
        btn_comp_condition = findViewById(R.id.btn_comp_condition);


        vp_req_condition = findViewById(R.id.vp_req_condition);
        vp_proc_condition = findViewById(R.id.vp_proc_condition);
        adapter_req = new condition_req_pagerAdapter(getSupportFragmentManager(),ismymentor);
        adapter_proc = new condition_proc_pagerAdapter(getSupportFragmentManager(),ismymentor);
        vp_req_condition.setAdapter(adapter_req);
        vp_proc_condition.setAdapter(adapter_proc);

        if(ismymentor)
        {
            tv_toolbar.setText("My Mentor");
        }

        else
        {
            tv_toolbar.setText("My Mentee");
        }


        btn_req_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClickedbgr(btn_req_condition);
                btnUnClickedbgr(btn_proc_condition);
                btnUnClickedbgr(btn_comp_condition);
                vp_req_condition.setVisibility(View.VISIBLE);
                vp_proc_condition.setVisibility(View.GONE);
            }
        });

        btn_proc_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClickedbgr(btn_proc_condition);
                btnUnClickedbgr(btn_req_condition);
                btnUnClickedbgr(btn_comp_condition);
                vp_proc_condition.setVisibility(View.VISIBLE);
                vp_req_condition.setVisibility(View.GONE);
            }
        });

        vp_req_condition.setOnPageChangeListener(new viewpagerChangeListener(adapter_req,(TextView)findViewById(R.id.tv_series_condition)));
        vp_proc_condition.setOnPageChangeListener(new viewpagerChangeListener(adapter_proc,(TextView)findViewById(R.id.tv_series_condition)));

    }

    public void btnClickedbgr(Button btn)
    {
        btn.setBackgroundColor(getResources().getColor(R.color.bgr_mainColor));
        btn.setTextColor(WHITE);
        btn.setTypeface(null, Typeface.BOLD);
    }

    public void btnUnClickedbgr(Button btn)
    {
        btn.setBackgroundColor(getResources().getColor(R.color.bgr_gray));
        btn.setTextColor(getResources().getColor(R.color.txt_gray));
        btn.setTypeface(null, Typeface.NORMAL);
    }

    public class viewpagerChangeListener implements ViewPager.OnPageChangeListener{

        protected PagerAdapter adapter;
        protected TextView textView;

        public viewpagerChangeListener(PagerAdapter adapter, TextView textView) {
            this.adapter = adapter;
            this.textView = textView;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            textView.setText(position+1+"/"+adapter.getCount());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
