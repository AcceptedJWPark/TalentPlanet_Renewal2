package accepted.talentplanet_renewal2.TalentAdd;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import accepted.talentplanet_renewal2.R;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class MainActivity_TalentAdd extends AppCompatActivity {

    private boolean isTeacher;

    private boolean[] isRegisted_teacher = new boolean[14];
    private boolean[] isRegisted_student = new boolean[14];

    private int[] icon_src = new int[14];

    private View[] view_bgr = new View[14];
    private View[] view_bgr2 = new View[14];
    private ImageView[] iv_icon = new ImageView[14];
    private TextView[] tv_txt = new TextView[14];


    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talentadd_activity);

        mContext = getApplicationContext();

        isTeacher = true;

        findViewbyId();
        isRegisted_Teacher();
        isRegisted_Student();
        registedBgr();

        ((ImageView)findViewById(R.id.img_open_dl)).setVisibility(View.GONE);
        ((ImageView)findViewById(R.id.img_back_toolbar)).setVisibility(View.VISIBLE);
        ((ImageView)findViewById(R.id.img_search_talentadd)).setVisibility(View.VISIBLE);

        ((TextView)findViewById(R.id.tv_toolbar)).setText("재능 등록");
        ((TextView)findViewById(R.id.tv_toolbar)).setVisibility(View.VISIBLE);
        ((Spinner)findViewById(R.id.sp_toolbar)).setVisibility(View.GONE);

        ((ImageView)findViewById(R.id.img_rightbtn)).setVisibility(View.GONE);
        ((ImageView)findViewById(R.id.img_alarm)).setVisibility(View.GONE);



        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));

        ((Button)findViewById(R.id.btn_teahcer_talentadd)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTeacher = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
                }

                registedBgr();
            }
        });

        ((Button)findViewById(R.id.btn_student_talentadd)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTeacher = false;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
                }

                registedBgr();
            }
        });


    }

    public void registedBgr()
    {
        if(isTeacher)
        {
            for (int i = 0; i < isRegisted_teacher.length; i++)
            {
                if (isRegisted_teacher[i]) {
                    view_bgr[i].setVisibility(View.VISIBLE);
                    view_bgr2[i].setVisibility(View.GONE);
                    iv_icon[i].setImageResource(icon_src[i]);
                    iv_icon[i].setColorFilter(BLACK);
                    tv_txt[i].setTextColor(BLACK);
                }else
                {
                    view_bgr[i].setVisibility(View.GONE);
                    view_bgr2[i].setVisibility(View.VISIBLE);
                    iv_icon[i].setImageResource(R.drawable.icon_add);
                    iv_icon[i].setColorFilter(WHITE);
                    tv_txt[i].setTextColor(WHITE);
                }
            }

            //Teacher 모드로 바꾸는 BGR
            ((ImageView)findViewById(R.id.img_back_toolbar)).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
            ((TextView)findViewById(R.id.tv_toolbar)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
            ((ImageView)findViewById(R.id.img_search_talentadd)).setImageResource(R.drawable.icon_search_teacher);
            ((RelativeLayout)findViewById(R.id.rl_talentcate_talentadd)).setBackgroundResource(R.color.color_mentor);
            ((Button)findViewById(R.id.btn_teahcer_talentadd)).setBackgroundResource(R.drawable.bgr_addtalent_leftbtn_clicekd);
            ((Button)findViewById(R.id.btn_student_talentadd)).setBackgroundResource(R.drawable.bgr_addtalent_rightbtn_unclicekd);
            ((Button)findViewById(R.id.btn_teahcer_talentadd)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
            ((Button)findViewById(R.id.btn_student_talentadd)).setTextColor(WHITE);

        }
        else
        {
            for (int i = 0; i < isRegisted_student.length; i++)
            {
                if (isRegisted_student[i]) {
                    view_bgr[i].setVisibility(View.VISIBLE);
                    view_bgr2[i].setVisibility(View.GONE);
                    iv_icon[i].setImageResource(icon_src[i]);
                    iv_icon[i].setColorFilter(BLACK);
                    tv_txt[i].setTextColor(BLACK);
                }else
                {
                    view_bgr[i].setVisibility(View.GONE);
                    view_bgr2[i].setVisibility(View.VISIBLE);
                    iv_icon[i].setImageResource(R.drawable.icon_add);
                    iv_icon[i].setColorFilter(WHITE);
                    tv_txt[i].setTextColor(WHITE);
                }
            }

            //Student 모드로 바꾸는 BGR
            ((ImageView)findViewById(R.id.img_back_toolbar)).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
            ((ImageView)findViewById(R.id.img_search_talentadd)).setImageResource(R.drawable.icon_search_student);
            ((TextView)findViewById(R.id.tv_toolbar)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
            ((RelativeLayout)findViewById(R.id.rl_talentcate_talentadd)).setBackgroundResource(R.color.color_mentee);
            ((Button)findViewById(R.id.btn_student_talentadd)).setBackgroundResource(R.drawable.bgr_addtalent_rightbtn_clicekd);
            ((Button)findViewById(R.id.btn_teahcer_talentadd)).setBackgroundResource(R.drawable.bgr_addtalent_leftbtn_unclicekd);
            ((Button)findViewById(R.id.btn_student_talentadd)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
            ((Button)findViewById(R.id.btn_teahcer_talentadd)).setTextColor(WHITE);

        }
    }



    public void isRegisted_Teacher()
    {
        isRegisted_teacher[0] = true;
        isRegisted_teacher[1] = false;
        isRegisted_teacher[2] = false;
        isRegisted_teacher[3] = false;
        isRegisted_teacher[4] = false;
        isRegisted_teacher[5] = false;
        isRegisted_teacher[6] = false;
        isRegisted_teacher[7] = true;
        isRegisted_teacher[8] = true;
        isRegisted_teacher[9] = true;
        isRegisted_teacher[10] = true;
        isRegisted_teacher[11] = false;
        isRegisted_teacher[12] = true;
        isRegisted_teacher[13] = true;
    }

    public void isRegisted_Student()
    {
        isRegisted_student[0] = false;
        isRegisted_student[1] = false;
        isRegisted_student[2] = false;
        isRegisted_student[3] = false;
        isRegisted_student[4] = true;
        isRegisted_student[5] = true;
        isRegisted_student[6] = true;
        isRegisted_student[7] = false;
        isRegisted_student[8] = false;
        isRegisted_student[9] = false;
        isRegisted_student[10] = false;
        isRegisted_student[11] = false;
        isRegisted_student[12] = false;
        isRegisted_student[13] = false;
    }

    public void findViewbyId()
    {
        view_bgr[0] = findViewById(R.id.view_career_talentadd);
        view_bgr[1] = findViewById(R.id.view_it_talentadd);
        view_bgr[2] = findViewById(R.id.view_design_talentadd);
        view_bgr[3] = findViewById(R.id.view_beauty_talentadd);
        view_bgr[4] = findViewById(R.id.view_culture_talentadd);
        view_bgr[5] = findViewById(R.id.view_living_talentadd);
        view_bgr[6] = findViewById(R.id.view_money_talentadd);
        view_bgr[7] = findViewById(R.id.view_study_talentadd);
        view_bgr[8] = findViewById(R.id.view_camera_talentadd);
        view_bgr[9] = findViewById(R.id.view_sports_talentadd);
        view_bgr[10] = findViewById(R.id.view_volunteer_talentadd);
        view_bgr[11] = findViewById(R.id.view_game_talentadd);
        view_bgr[12] = findViewById(R.id.view_travel_talentadd);
        view_bgr[13] = findViewById(R.id.view_music_talentadd);

        view_bgr2[0] = findViewById(R.id.view2_career_talentadd);
        view_bgr2[1] = findViewById(R.id.view2_it_talentadd);
        view_bgr2[2] = findViewById(R.id.view2_design_talentadd);
        view_bgr2[3] = findViewById(R.id.view2_beauty_talentadd);
        view_bgr2[4] = findViewById(R.id.view2_culture_talentadd);
        view_bgr2[5] = findViewById(R.id.view2_living_talentadd);
        view_bgr2[6] = findViewById(R.id.view2_money_talentadd);
        view_bgr2[7] = findViewById(R.id.view2_study_talentadd);
        view_bgr2[8] = findViewById(R.id.view2_camera_talentadd);
        view_bgr2[9] = findViewById(R.id.view2_sports_talentadd);
        view_bgr2[10] = findViewById(R.id.view2_volunteer_talentadd);
        view_bgr2[11] = findViewById(R.id.view2_game_talentadd);
        view_bgr2[12] = findViewById(R.id.view2_travel_talentadd);
        view_bgr2[13] = findViewById(R.id.view2_music_talentadd);

        iv_icon[0] = findViewById(R.id.icon_career_talentadd);
        iv_icon[1] = findViewById(R.id.icon_it_talentadd);
        iv_icon[2] = findViewById(R.id.icon_design_talentadd);
        iv_icon[3] = findViewById(R.id.icon_beauty_talentadd);
        iv_icon[4] = findViewById(R.id.icon_culture_talentadd);
        iv_icon[5] = findViewById(R.id.icon_living_talentadd);
        iv_icon[6] = findViewById(R.id.icon_money_talentadd);
        iv_icon[7] = findViewById(R.id.icon_study_talentadd);
        iv_icon[8] = findViewById(R.id.icon_camera_talentadd);
        iv_icon[9] = findViewById(R.id.icon_sports_talentadd);
        iv_icon[10] = findViewById(R.id.icon_volunteer_talentadd);
        iv_icon[11] = findViewById(R.id.icon_game_talentadd);
        iv_icon[12] = findViewById(R.id.icon_travel_talentadd);
        iv_icon[13] = findViewById(R.id.icon_music_talentadd);

        tv_txt[0] = findViewById(R.id.tv_career_talentadd);
        tv_txt[1] = findViewById(R.id.tv_it_talentadd);
        tv_txt[2] = findViewById(R.id.tv_design_talentadd);
        tv_txt[3] = findViewById(R.id.tv_beauty_talentadd);
        tv_txt[4] = findViewById(R.id.tv_culture_talentadd);
        tv_txt[5] = findViewById(R.id.tv_living_talentadd);
        tv_txt[6] = findViewById(R.id.tv_money_talentadd);
        tv_txt[7] = findViewById(R.id.tv_study_talentadd);
        tv_txt[8] = findViewById(R.id.tv_camera_talentadd);
        tv_txt[9] = findViewById(R.id.tv_sports_talentadd);
        tv_txt[10] = findViewById(R.id.tv_volunteer_talentadd);
        tv_txt[11] = findViewById(R.id.tv_game_talentadd);
        tv_txt[12] = findViewById(R.id.tv_travel_talentadd);
        tv_txt[13] = findViewById(R.id.tv_music_talentadd);

        icon_src[0] = R.drawable.icon_career;
        icon_src[1] = R.drawable.icon_it;
        icon_src[2] = R.drawable.icon_design;
        icon_src[3] = R.drawable.icon_beauty;
        icon_src[4] = R.drawable.icon_culture;
        icon_src[5] = R.drawable.icon_living;
        icon_src[6] = R.drawable.icon_money;
        icon_src[7] = R.drawable.icon_study;
        icon_src[8] = R.drawable.icon_camera;
        icon_src[9] = R.drawable.icon_sports;
        icon_src[10] = R.drawable.icon_volunteer;
        icon_src[11] = R.drawable.icon_game;
        icon_src[12] = R.drawable.icon_travel;
        icon_src[13] = R.drawable.icon_music;
    }


    
}