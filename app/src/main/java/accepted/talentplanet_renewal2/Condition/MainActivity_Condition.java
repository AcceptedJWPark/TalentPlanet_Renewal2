package accepted.talentplanet_renewal2.Condition;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import accepted.talentplanet_renewal2.R;

import static android.graphics.Color.WHITE;


public class MainActivity_Condition extends AppCompatActivity {

    TextView tv_toolbar;
    Context mContext;
    ViewPager vp_condition;
    condition_pagerAdapter adapter;

    boolean ismymentor;
    int condition;
    int cnttalent;

    Button btn_req_condition;
    Button btn_proc_condition;
    Button btn_comp_condition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);

        mContext = getApplicationContext();
        tv_toolbar = findViewById(R.id.tv_toolbar);

        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);
        findViewById(R.id.img_show1x15).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        btn_req_condition = findViewById(R.id.btn_req_condition);
        btn_proc_condition = findViewById(R.id.btn_proc_condition);
        btn_comp_condition = findViewById(R.id.btn_comp_condition);


        vp_condition = findViewById(R.id.vp_condition);
        adapter = new condition_pagerAdapter(getSupportFragmentManager());
        ismymentor = true;
        cnttalent = 2;
        adapter.setCnttalent(cnttalent);

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

                adapter.setIsmentor(ismymentor);
                adapter.setCondition(0);
                adapter.notifyDataSetChanged();
                vp_condition.setAdapter(adapter);
            }
        });

        btn_proc_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClickedbgr(btn_proc_condition);
                btnUnClickedbgr(btn_req_condition);
                btnUnClickedbgr(btn_comp_condition);

                adapter.setIsmentor(ismymentor);
                adapter.setCondition(1);
                adapter.notifyDataSetChanged();
                vp_condition.setAdapter(adapter);
            }
        });

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
}
