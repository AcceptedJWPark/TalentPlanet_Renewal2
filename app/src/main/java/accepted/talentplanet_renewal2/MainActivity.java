package accepted.talentplanet_renewal2;

import android.graphics.Typeface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import static android.graphics.Color.WHITE;

public class MainActivity extends AppCompatActivity {
    ScrollView sv1x15;
    LinearLayout ll3x5;
    ImageView img3x5;
    ImageView img1x15;
    ImageView img_open_dl;

    DrawerLayout dl;
    View v_drawerlayout;

    Button btn_mentor_home;
    Button btn_mentee_home;

    boolean mentorClicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sv1x15 = findViewById(R.id.sv_show1x15);
        ll3x5 = findViewById(R.id.ll_container);
        img3x5 = findViewById(R.id.img_show3x5);
        img1x15 = findViewById(R.id.img_show1x15);
        img_open_dl = findViewById(R.id.img_open_dl);
        dl = (DrawerLayout)findViewById(R.id.drawerlayout);
        v_drawerlayout=findViewById(R.id.view_drawerlayout);

        btn_mentor_home = findViewById(R.id.btn_mentor_home);
        btn_mentee_home = findViewById(R.id.btn_mentee_home);


        //기본 값
        mentorClicked = true;
        findViewById(R.id.inc_list3x5_home).setVisibility(View.VISIBLE);
        findViewById(R.id.inc_list1x15_home).setVisibility(View.GONE);

        //

        img_open_dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.openDrawer(v_drawerlayout);
            }
        });
        img3x5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedShow3x5list();
            }
        });
        img1x15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedShow1x15list();
            }
        });


        btn_mentor_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mentorClicked=true;
                clickedMentorMentee();
            }
        });

        btn_mentee_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mentorClicked=false;
                clickedMentorMentee();
            }
        });


    }

    //3x5로 바꾸는 함수
    public void clickedShow3x5list()
    {
        img3x5.setImageResource(R.drawable.icon_3x5_clicked);
        img1x15.setImageResource(R.drawable.icon_1x15_unclicked);
        mentorClicked = true;
        clickedMentorMentee();
        findViewById(R.id.inc_list3x5_home).setVisibility(View.VISIBLE);
        findViewById(R.id.inc_list1x15_home).setVisibility(View.GONE);
    }

    //1x15로 바꾸는 함수
    public void clickedShow1x15list()
    {
        img3x5.setImageResource(R.drawable.icon_3x5_unclicked);
        img1x15.setImageResource(R.drawable.icon_1x15_clicked);
        mentorClicked = true;
        clickedMentorMentee();
        findViewById(R.id.inc_list3x5_home).setVisibility(View.GONE);
        findViewById(R.id.inc_list1x15_home).setVisibility(View.VISIBLE);
    }



    //Mentor, Mentee 바꾸는 함수
    public void clickedMentorMentee()
    {
        if(mentorClicked)
        {
            btn_mentor_home.setBackgroundColor(getResources().getColor(R.color.bgr_mainColor));
            btn_mentor_home.setTextColor(WHITE);
            btn_mentor_home.setTypeface(null, Typeface.BOLD);
            btn_mentee_home.setBackgroundColor(getResources().getColor(R.color.bgr_gray));
            btn_mentee_home.setTextColor(getResources().getColor(R.color.txt_gray));
            btn_mentee_home.setTypeface(null, Typeface.NORMAL);
        }
        else
        {
            btn_mentee_home.setBackgroundColor(getResources().getColor(R.color.bgr_mainColor));
            btn_mentee_home.setTextColor(WHITE);
            btn_mentee_home.setTypeface(null, Typeface.BOLD);
            btn_mentor_home.setBackgroundColor(getResources().getColor(R.color.bgr_gray));
            btn_mentor_home.setTextColor(getResources().getColor(R.color.txt_gray));
            btn_mentor_home.setTypeface(null, Typeface.NORMAL);
        }
    }



}
