package com.accepted.acceptedtalentplanet.SharingList;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.accepted.acceptedtalentplanet.Home.MainActivity;
import com.accepted.acceptedtalentplanet.Profile.MainActivity_Profile;
import com.accepted.acceptedtalentplanet.R;
import com.accepted.acceptedtalentplanet.SaveSharedPreference;

import static android.graphics.Color.WHITE;

public class MainActivity_SharingList extends AppCompatActivity {

    private boolean isTeacher;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharinglist_activity);

        mContext = getApplicationContext();

        isTeacher = true;


        ((ImageView)findViewById(R.id.img_open_dl)).setVisibility(View.GONE);
        ((ImageView)findViewById(R.id.img_back_toolbar)).setVisibility(View.VISIBLE);
        ((ImageView)findViewById(R.id.img_search_talentadd)).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.tv_toolbar)).setText("공유 내역");
        ((TextView)findViewById(R.id.tv_toolbar)).setVisibility(View.VISIBLE);
        ((Spinner)findViewById(R.id.sp_toolbar)).setVisibility(View.GONE);

        ((ImageView)findViewById(R.id.img_rightbtn)).setVisibility(View.GONE);

        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
        }
        ((Button)findViewById(R.id.btn_teahcer_sharinglist)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTeacher = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
                }
                registedBgr();
            }
        });

        ((Button)findViewById(R.id.btn_student_sharinglist)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTeacher = false;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
                }
                registedBgr();
            }
        });

        ((ImageView)findViewById(R.id.img_back_toolbar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void registedBgr()
    {
        if(isTeacher) {
            //Teacher 모드로 바꾸는 BGR
            ((ImageView)findViewById(R.id.img_back_toolbar)).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
            ((TextView)findViewById(R.id.tv_toolbar)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
            ((ImageView)findViewById(R.id.img_search_talentadd)).setImageResource(R.drawable.icon_search_teacher);
            ((RelativeLayout)findViewById(R.id.rl_talentcate_sharinglist)).setBackgroundResource(R.color.color_mentor);
            ((Button)findViewById(R.id.btn_teahcer_sharinglist)).setBackgroundResource(R.drawable.bgr_addtalent_leftbtn_clicekd);
            ((Button)findViewById(R.id.btn_student_sharinglist)).setBackgroundResource(R.drawable.bgr_addtalent_rightbtn_unclicekd);
            ((Button)findViewById(R.id.btn_teahcer_sharinglist)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
            ((Button)findViewById(R.id.btn_student_sharinglist)).setTextColor(WHITE);

        } else {
            //Student 모드로 바꾸는 BGR
            ((ImageView)findViewById(R.id.img_back_toolbar)).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
            ((ImageView)findViewById(R.id.img_search_talentadd)).setImageResource(R.drawable.icon_search_student);
            ((TextView)findViewById(R.id.tv_toolbar)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
            ((RelativeLayout)findViewById(R.id.rl_talentcate_sharinglist)).setBackgroundResource(R.color.color_mentee);
            ((Button)findViewById(R.id.btn_student_sharinglist)).setBackgroundResource(R.drawable.bgr_addtalent_rightbtn_clicekd);
            ((Button)findViewById(R.id.btn_teahcer_sharinglist)).setBackgroundResource(R.drawable.bgr_addtalent_leftbtn_unclicekd);
            ((Button)findViewById(R.id.btn_student_sharinglist)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
            ((Button)findViewById(R.id.btn_teahcer_sharinglist)).setTextColor(WHITE);
        }
    }


}