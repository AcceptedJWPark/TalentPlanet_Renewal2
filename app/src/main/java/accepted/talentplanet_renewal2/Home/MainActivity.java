package accepted.talentplanet_renewal2.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import accepted.talentplanet_renewal2.Classes.TalentObject_Home;
import accepted.talentplanet_renewal2.Condition.MainActivity_Condition;
import accepted.talentplanet_renewal2.Cs.MainActivity_Cs;
import accepted.talentplanet_renewal2.FriendList.MainActivity_Friend;
import accepted.talentplanet_renewal2.Profile.MainActivity_Profile;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.TalentList.MainActivity_TalentList;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends AppCompatActivity {

    Context mContext;

    ScrollView sv1x15;
    LinearLayout ll3x5;
    LinearLayout ll1x15;
    ImageView img3x5;
    ImageView img1x15;
    ImageView img_open_dl;

    DrawerLayout dl;
    View v_drawerlayout;

    Button btn_mentor_home;
    Button btn_mentee_home;

    boolean mentorClicked;

    boolean isAlaram;

    private ArrayList<TalentObject_Home> arrTalent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext=getApplicationContext();

        sv1x15 = findViewById(R.id.sv_show1x15);
        ll3x5 = findViewById(R.id.ll_container_home);
        ll1x15 = findViewById(R.id.ll_container_home_15);

        img3x5 = findViewById(R.id.img_show3x5);
        img1x15 = findViewById(R.id.img_show1x15);
        img_open_dl = findViewById(R.id.img_open_dl);
        dl = (DrawerLayout)findViewById(R.id.drawerlayout);
        v_drawerlayout=findViewById(R.id.view_drawerlayout);

        btn_mentor_home = findViewById(R.id.btn_mentor_home);
        btn_mentee_home = findViewById(R.id.btn_mentee_home);

        drawerlayoutEvent(mContext);
        isAlaram = true;

        makeTestTalentArr();
        makeLayout(ll3x5);
        make15Layout(ll1x15);

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

    private void makeLayout(LinearLayout layout){
        LinearLayout root = (LinearLayout)layout.findViewById(R.id.ll_container_home);

        LinearLayout row = null;
        layout.setMinimumWidth(MATCH_PARENT);
        layout.setMinimumHeight(MATCH_PARENT);

        // row layout 의 Parameter 정의
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(MATCH_PARENT, 0);
        rowParams.weight = 1;
        rowParams.bottomMargin = (int) getResources().getDimension(R.dimen.size_5dp);
        rowParams.leftMargin = (int) getResources().getDimension(R.dimen.size_5dp);

        // 하나의 객체의 Layout Parameter 정의
        LinearLayout.LayoutParams objectParams = new LinearLayout.LayoutParams(0, MATCH_PARENT);
        objectParams.weight = 1;
        objectParams.rightMargin = (int) getResources().getDimension(R.dimen.size_5dp);

        // 객체 background image 의 Parameter 정의
        LinearLayout.LayoutParams bgImgParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        bgImgParams.gravity = CENTER;

        // 객체 opacity background 의 Parameter 정의
        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);

        // 텍스트 및 돌출 이미지에 대한 LinearLayout Parameter 정의
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);

        // 돌출 텍스트에 대한 Parameter 정의
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);

        int rowNum = 0;

        for (int i = 0; i < arrTalent.size(); i++){
            if(i % 3 == 0){
                row = new LinearLayout(getApplicationContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
            }

            TalentObject_Home obj = arrTalent.get(i);
            RelativeLayout rl = new RelativeLayout(getApplicationContext());

            ImageView bgImgView = new ImageView(getApplicationContext());
            bgImgView.setScaleType(ImageView.ScaleType.FIT_XY);

            Glide.with(this).load(obj.getBackgroundResourceID()).into(bgImgView);

            LinearLayout linear = new LinearLayout(getApplicationContext());
            View opacityView = new View(getApplicationContext());
            final TextView textView = new TextView(getApplicationContext());
            View basicView = new View(getApplicationContext());

            linear.setGravity(CENTER);
            linear.setOrientation(LinearLayout.VERTICAL);

            opacityView.setBackgroundColor(Color.parseColor("#68000000"));

            textView.setText(obj.getTitle() + "\n" + obj.getTalentCount());
            textView.setTextColor(WHITE);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.isTalent_Txt));
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setGravity(CENTER);

            linear.addView(textView, textParams);

            linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity_TalentList.class);
                    String talentName = (String) textView.getText();
                    intent.putExtra("talentName", talentName.split("\n")[0]);

                    startActivity(intent);
                }
            });

            rl.addView(bgImgView, bgImgParams);
            rl.addView(opacityView, viewParams);
            rl.addView(linear, linearParams);

            row.addView(rl, objectParams);
            if(i == arrTalent.size() - 1 || i % 3 == 2){
                layout.addView(row, rowParams);
                rowNum++;
            }
        }

    }

    private void make15Layout(LinearLayout layout){
        LinearLayout root = (LinearLayout)layout.findViewById(R.id.ll_container_home_15);

        LinearLayout row = null;
        layout.setMinimumWidth(MATCH_PARENT);
        layout.setMinimumHeight(MATCH_PARENT);

        // 하나의 객체의 Layout Parameter 정의
        LinearLayout.LayoutParams objectParams = new LinearLayout.LayoutParams(MATCH_PARENT, (int) getResources().getDimension(R.dimen.size_120dp));

        // 객체 background image 의 Parameter 정의
        LinearLayout.LayoutParams bgImgParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        bgImgParams.gravity = CENTER;

        // 객체 opacity background 의 Parameter 정의
        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);

        // 텍스트 및 돌출 이미지에 대한 LinearLayout Parameter 정의
        RelativeLayout.LayoutParams linearParams = new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        linearParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        // 돌출 텍스트에 대한 Parameter 정의
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        textParams.topMargin = (int)getResources().getDimension(R.dimen.size_10dp);

        // 돌출 아이콘에 대한 Parameter 정의
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.isTalent_pic), (int) getResources().getDimension(R.dimen.isTalent_pic));

        int rowNum = 0;

        for (int i = 0; i < arrTalent.size(); i++){

            TalentObject_Home obj = arrTalent.get(i);
            RelativeLayout rl = new RelativeLayout(getApplicationContext());

            ImageView bgImgView = new ImageView(getApplicationContext());
            bgImgView.setScaleType(ImageView.ScaleType.FIT_XY);

            Glide.with(this).load(obj.getBackgroundResourceID()).into(bgImgView);

            View opacityView = new View(getApplicationContext());
            opacityView.setBackgroundColor(Color.parseColor("#68000000"));

            LinearLayout linear = new LinearLayout(getApplicationContext());
            linear.setGravity(CENTER);
            linear.setOrientation(LinearLayout.VERTICAL);

            ImageView iconView = new ImageView(getApplicationContext());
            iconView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(this).load(obj.getIconResourceID()).into(iconView);

            final TextView textView = new TextView(getApplicationContext());
            textView.setText(obj.getTitle() + " " + obj.getTalentCount());
            textView.setTextColor(WHITE);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.isTalent_Txt));
            textView.setTypeface(Typeface.DEFAULT_BOLD);

            linear.addView(iconView, iconParams);
            linear.addView(textView, textParams);

            linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity_TalentList.class);
                    String talentName = (String) textView.getText();
                    intent.putExtra("talentName", talentName.split("\n")[0]);

                    startActivity(intent);
                }
            });

            rl.addView(bgImgView, bgImgParams);
            rl.addView(opacityView, viewParams);
            rl.addView(linear, linearParams);

            root.addView(rl, objectParams);
        }
    }

    private void makeTestTalentArr(){
        arrTalent = new ArrayList<>();

        TalentObject_Home career = new TalentObject_Home("취업", R.drawable.pic_career,R.drawable.icon_career, 495);
        TalentObject_Home study = new TalentObject_Home("학습", R.drawable.pic_study,R.drawable.icon_study, 485);
        TalentObject_Home money = new TalentObject_Home("재테크", R.drawable.pic_money,R.drawable.icon_money, 410);
        TalentObject_Home it = new TalentObject_Home("IT", R.drawable.pic_it,R.drawable.icon_it, 379);
        TalentObject_Home camera = new TalentObject_Home("사진", R.drawable.pic_camera,R.drawable.icon_camera, 338);
        TalentObject_Home music = new TalentObject_Home("음악", R.drawable.pic_music,R.drawable.icon_music, 230);
        TalentObject_Home design = new TalentObject_Home("미술/디자인", R.drawable.pic_design,R.drawable.icon_design, 202);
        TalentObject_Home sports = new TalentObject_Home("운동", R.drawable.pic_sports,R.drawable.icon_sports, 192);
        TalentObject_Home living = new TalentObject_Home("생활", R.drawable.pic_living,R.drawable.icon_living, 172);
        TalentObject_Home beauty = new TalentObject_Home("뷰티/패션", R.drawable.pic_beauty,R.drawable.icon_beauty, 135);
        TalentObject_Home volunteer = new TalentObject_Home("사회봉사", R.drawable.pic_volunteer,R.drawable.icon_volunteer, 519);
        TalentObject_Home travel = new TalentObject_Home("여행", R.drawable.pic_travel,R.drawable.icon_travel, 118);
        TalentObject_Home culture = new TalentObject_Home("문화", R.drawable.pic_culture,R.drawable.icon_culture, 49);
        TalentObject_Home game = new TalentObject_Home("게임", R.drawable.pic_game,R.drawable.icon_game, 41);

        arrTalent.add(career);
        arrTalent.add(study);
        arrTalent.add(money);
        arrTalent.add(it);
        arrTalent.add(camera);
        arrTalent.add(music);
        arrTalent.add(design);
        arrTalent.add(sports);
        arrTalent.add(living);
        arrTalent.add(beauty);
        arrTalent.add(volunteer);
        arrTalent.add(travel);
        arrTalent.add(culture);
        arrTalent.add(game);

        Collections.sort(arrTalent);

//        long seed = System.nanoTime();
//        Collections.shuffle(arrTalent, new Random(seed));
    }

    private void drawerlayoutEvent(final Context context)
    {

        ((ImageView)findViewById(R.id.cimg_pic_dl)).setBackground(new ShapeDrawable((new OvalShape())));
        if(Build.VERSION.SDK_INT >= 21) {
            ((ImageView)findViewById(R.id.cimg_pic_dl)).setClipToOutline(true);
        }


        ((LinearLayout)findViewById(R.id.ll_myprofile_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.closeDrawers();
                Intent intent = new Intent(context, MainActivity_Profile.class);
                startActivity(intent);
            }
        });


        ((LinearLayout)findViewById(R.id.ll_mymentor_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.closeDrawers();
                ((LinearLayout)findViewById(R.id.ll_click_condition_dl)).setVisibility(View.GONE);
                ((LinearLayout)findViewById(R.id.ll_unclick_condition_dl)).setVisibility(View.VISIBLE);
                Intent intent = new Intent(context, MainActivity_Condition.class);
                intent.putExtra("ismyMentor","1");
                startActivity(intent);
            }
        });


        ((LinearLayout)findViewById(R.id.ll_mymentee_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.closeDrawers();
                ((LinearLayout)findViewById(R.id.ll_click_condition_dl)).setVisibility(View.GONE);
                ((LinearLayout)findViewById(R.id.ll_unclick_condition_dl)).setVisibility(View.VISIBLE);
                Intent intent = new Intent(context, MainActivity_Condition.class);
                intent.putExtra("ismyMentor","2");
                startActivity(intent);
            }
        });





        ((LinearLayout)findViewById(R.id.ll_talentbox_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });



        ((LinearLayout)findViewById(R.id.ll_message_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        ((LinearLayout)findViewById(R.id.ll_friend_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.closeDrawers();
                Intent intent = new Intent(context, MainActivity_Friend.class);
                startActivity(intent);
            }
        });

        ((LinearLayout)findViewById(R.id.ll_cs_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dl.closeDrawers();
                Intent intent = new Intent(context, MainActivity_Cs.class);
                startActivity(intent);
            }
        });


        //알람 On Off

        ((ImageView)findViewById(R.id.iv_alarm_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAlaram)
                {
                    isAlaram=false;
                    Toast.makeText(mContext,"알람이 비활성화되었습니다.",Toast.LENGTH_SHORT).show();
                    ((ImageView)findViewById(R.id.iv_alarm_dl)).setImageResource(R.drawable.icon_dl_alarmoff);
                }else
                {
                    isAlaram=true;
                    Toast.makeText(mContext,"알람이 활성화되었습니다.",Toast.LENGTH_SHORT).show();
                    ((ImageView)findViewById(R.id.iv_alarm_dl)).setImageResource(R.drawable.icon_dl_alarmon);
                }
            }
        });

        //재능상태 클릭했을 때 Mymentor,Mymentee 노출 기능
        ((ImageView)findViewById(R.id.iv_closecondition_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout)findViewById(R.id.ll_click_condition_dl)).setVisibility(View.GONE);
                ((LinearLayout)findViewById(R.id.ll_unclick_condition_dl)).setVisibility(View.VISIBLE);
            }
        });

        ((LinearLayout)findViewById(R.id.ll_condition_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout)findViewById(R.id.ll_click_condition_dl)).setVisibility(View.VISIBLE);
                ((LinearLayout)findViewById(R.id.ll_unclick_condition_dl)).setVisibility(View.GONE);
            }
        });
    }
}
