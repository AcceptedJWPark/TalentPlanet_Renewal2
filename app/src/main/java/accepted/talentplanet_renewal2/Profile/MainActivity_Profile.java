package accepted.talentplanet_renewal2.Profile;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import accepted.talentplanet_renewal2.R;

import static android.graphics.Color.WHITE;


public class MainActivity_Profile extends AppCompatActivity {

    TextView tv_toolbar;
    talentlist_viewpager vp;
    Context mContext;
    ViewPager.OnPageChangeListener onPageChangeListener;

    Button btn_clickedTap[] = new Button[4];
    Button btn_clicked_profile;
    Button btn_clicked_mentor;
    Button btn_clicked_mentee;
    Button btn_clicked_point;

    View v_inc_profile[] = new View[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mContext = getApplicationContext();
        tv_toolbar=findViewById(R.id.tv_toolbar);
        tv_toolbar.setText("Profile");
        ((ImageView)findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);
        findViewById(R.id.img_show1x15).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        btn_clicked_profile = findViewById(R.id.btn_profile_profile);
        btn_clicked_mentor = findViewById(R.id.btn_mentor_profile);
        btn_clicked_mentee = findViewById(R.id.btn_mentee_profile);
        btn_clicked_point = findViewById(R.id.btn_point_profile);

        btn_clickedTap[0] = btn_clicked_profile;
        btn_clickedTap[1] = btn_clicked_mentor;
        btn_clickedTap[2] = btn_clicked_mentee;
        btn_clickedTap[3] = btn_clicked_point;

        v_inc_profile[0] = findViewById(R.id.inc_user_profile);
        v_inc_profile[1] = findViewById(R.id.inc_mentor_profile);
        v_inc_profile[2] = findViewById(R.id.inc_mentee_profile);
        v_inc_profile[3] = findViewById(R.id.inc_point_profile);

        for(int i=0; i<btn_clickedTap.length; i++) {
            final int finalI = i;
            btn_clickedTap[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickTapbtn(finalI);
                }
            });
        }

        vp = findViewById(R.id.vp_profile_mentor);
        vp.setAdapter(new talentlist_pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);
        onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0)
                {
                    ((LinearLayout)findViewById(R.id.ll_firstpage_profile_mentor)).setVisibility(View.GONE);
                    ((TextView)findViewById(R.id.tv_series_profile_mentor)).setVisibility(View.GONE);
                }
                else if(position==1)
                {
                    ((LinearLayout)findViewById(R.id.ll_firstpage_profile_mentor)).setVisibility(View.VISIBLE);
                    ((TextView)findViewById(R.id.tv_series_profile_mentor)).setVisibility(View.VISIBLE);
                    ((TextView)findViewById(R.id.tv_series_profile_mentor)).setText("2");
                }
                else
                {
                    ((LinearLayout)findViewById(R.id.ll_firstpage_profile_mentor)).setVisibility(View.VISIBLE);
                    ((TextView)findViewById(R.id.tv_series_profile_mentor)).setVisibility(View.VISIBLE);
                    ((TextView)findViewById(R.id.tv_series_profile_mentor)).setText("3");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
        vp.addOnPageChangeListener(onPageChangeListener);
        findViewById(R.id.ll_totalshow_profile_mentor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(0);
            }
        });
    }

    public void clickTapbtn(int index)
    {
        for(int i=0; i<btn_clickedTap.length; i++)
        {
            if(i==index)
            {
                btn_clickedTap[i].setBackgroundColor(getResources().getColor(R.color.bgr_mainColor));
                btn_clickedTap[i].setTextColor(WHITE);
                btn_clickedTap[i].setTypeface(null, Typeface.BOLD);
                v_inc_profile[i].setVisibility(View.VISIBLE);
            }
            else
            {
                btn_clickedTap[i].setBackgroundColor(getResources().getColor(R.color.bgr_gray));
                btn_clickedTap[i].setTextColor(getResources().getColor(R.color.txt_gray));
                btn_clickedTap[i].setTypeface(null, Typeface.NORMAL);
                v_inc_profile[i].setVisibility(View.GONE);
            }
        }
    }



}
