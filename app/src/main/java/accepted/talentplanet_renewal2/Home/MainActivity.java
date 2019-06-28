package accepted.talentplanet_renewal2.Home;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import accepted.talentplanet_renewal2.Classes.TalentObject_Home;
import accepted.talentplanet_renewal2.Condition.MainActivity_Condition;
import accepted.talentplanet_renewal2.Cs.MainActivity_Cs;
import accepted.talentplanet_renewal2.FriendList.MainActivity_Friend;
import accepted.talentplanet_renewal2.Profile.MainActivity_Profile;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.TalentBox.MainActivity_TalentBox;
import accepted.talentplanet_renewal2.TalentList.MainActivity_TalentList;
import accepted.talentplanet_renewal2.VolleySingleton;

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
    TextView tv_user_dl;
    TextView tv_email_dl;

    EditText et_search_home;

    DrawerLayout dl;
    View v_drawerlayout;

    Button btn_mentor_home;
    Button btn_mentee_home;
    Button btn_search_home;

    boolean mentorClicked;

    boolean isAlaram;
    String talentFlag;

    private ArrayList<TalentObject_Home> arrTalent;
    private Map<String, TalentObject_Home> talentMap;

    SQLiteDatabase sqliteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext=getApplicationContext();

        if (SaveSharedPreference.getFcmToken(mContext) == null || SaveSharedPreference.getFcmToken(mContext).isEmpty()) {
            SaveSharedPreference.setPrefFcmToken(mContext, FirebaseInstanceId.getInstance().getToken());
            saveFcmToken();
        }

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
        btn_search_home = findViewById(R.id.btn_search_home);

        et_search_home = findViewById(R.id.et_search_home);

        tv_user_dl = findViewById(R.id.tv_user_dl);
        tv_email_dl = findViewById(R.id.tv_email_dl);

        String userName = SaveSharedPreference.getUserName(mContext);
        String userId = SaveSharedPreference.getUserId(mContext);
        tv_user_dl.setText(userName);
        tv_email_dl.setText(userId);

        drawerlayoutEvent(mContext);
        isAlaram = true;

        makeTestTalentArr();
        getCateList();
        talentFlag = "Y";
        //기본 값
        mentorClicked = true;
        findViewById(R.id.inc_list3x5_home).setVisibility(View.VISIBLE);
        findViewById(R.id.inc_list1x15_home).setVisibility(View.GONE);

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

        btn_search_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity_TalentList.class);
                String talentName = et_search_home.getText().toString();
                intent.putExtra("talentName", talentName);
                intent.putExtra("talentFlag", talentFlag);
                intent.putExtra("isSearch", true);
                startActivity(intent);
            }
        });

        String dbName = "/accepted.db";
        sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(getFilesDir() + dbName, null);
        Log.d("db path = ", getFilesDir() + dbName);


        String sqlCreateTbl = "CREATE TABLE IF NOT EXISTS TB_CHAT_LOG (MESSAGE_ID INTEGER PRIMARY KEY, ROOM_ID INTEGER, MASTER_ID TEXT, USER_ID TEXT, CONTENT TEXT, CREATION_DATE TEXT, READED_FLAG TEXT)";
        sqliteDatabase.execSQL(sqlCreateTbl);

        String sqlCreateTbl2 = "CREATE TABLE IF NOT EXISTS TB_CHAT_ROOM (ROOM_ID INTEGER, USER_ID TEXT, USER_NAME TEXT, MASTER_ID TEXT, START_MESSAGE_ID INTEGER, CREATION_DATE TEXT, LAST_UPDATE_DATE TEXT, ACTIVATE_FLAG TEXT, FILE_PATH TEXT, PRIMARY KEY(ROOM_ID, USER_ID, MASTER_ID))";
        sqliteDatabase.execSQL(sqlCreateTbl2);

        String sqlCreateTbl3 = "CREATE TABLE IF NOT EXISTS TB_FRIEND_LIST (MASTER_ID TEXT, FRIEND_ID TEXT, TALENT_TYPE TEXT, PRIMARY KEY(MASTER_ID, FRIEND_ID, TALENT_TYPE))";
        sqliteDatabase.execSQL(sqlCreateTbl3);

        String sqlCreateTbl4 = "CREATE TABLE IF NOT EXISTS TB_FCM_TOKEN (TOKEN TEXT)";
        sqliteDatabase.execSQL(sqlCreateTbl4);

        String sqlCreateTbl6 = "CREATE TABLE IF NOT EXISTS TB_GRANT (USER_ID TEXT PRIMARY KEY, MESSAGE_GRANT INT, CONDITION_GRANT INT, ANSWER_GRANT INT)";
        sqliteDatabase.execSQL(sqlCreateTbl6);

        String sqlCreateTbl7 = "CREATE TABLE IF NOT EXISTS TB_READED_INTEREST (USER_ID TEXT, TALENT_ID INT, PRIMARY KEY(USER_ID, TALENT_ID))";
        sqliteDatabase.execSQL(sqlCreateTbl7);

        sqliteDatabase.close();

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
            talentFlag = "Y";
            getCateList();
        }
        else
        {
            btn_mentee_home.setBackgroundColor(getResources().getColor(R.color.bgr_mainColor));
            btn_mentee_home.setTextColor(WHITE);
            btn_mentee_home.setTypeface(null, Typeface.BOLD);
            btn_mentor_home.setBackgroundColor(getResources().getColor(R.color.bgr_gray));
            btn_mentor_home.setTextColor(getResources().getColor(R.color.txt_gray));
            btn_mentor_home.setTypeface(null, Typeface.NORMAL);
            talentFlag = "N";
            getCateList();
        }
    }

    private void makeLayout(LinearLayout layout){
        LinearLayout root = (LinearLayout)layout.findViewById(R.id.ll_container_home);
        root.removeAllViews();

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

            final TalentObject_Home obj = arrTalent.get(i);
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
                    intent.putExtra("talentName", obj.getTitle());
                    intent.putExtra("cateCode", obj.getCateCode());
                    intent.putExtra("talentFlag", talentFlag);
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
        root.removeAllViews();

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

            final TalentObject_Home obj = arrTalent.get(i);
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
                    intent.putExtra("talentName", obj.getTitle());
                    intent.putExtra("cateCode", obj.getCateCode());
                    intent.putExtra("talentFlag", talentFlag);

                    startActivity(intent);
                }
            });

            rl.addView(bgImgView, bgImgParams);
            rl.addView(opacityView, viewParams);
            rl.addView(linear, linearParams);

            root.addView(rl, objectParams);
        }
    }

    private void makeTestTalentArr() {
        talentMap = new HashMap();

        TalentObject_Home career = new TalentObject_Home("취업", R.drawable.pic_career,R.drawable.icon_career, 0, "");
        TalentObject_Home study = new TalentObject_Home("학습", R.drawable.pic_study,R.drawable.icon_study, 0, "");
        TalentObject_Home money = new TalentObject_Home("재테크", R.drawable.pic_money,R.drawable.icon_money, 0, "");
        TalentObject_Home it = new TalentObject_Home("IT", R.drawable.pic_it,R.drawable.icon_it, 0, "");
        TalentObject_Home camera = new TalentObject_Home("사진", R.drawable.pic_camera,R.drawable.icon_camera, 0, "");
        TalentObject_Home music = new TalentObject_Home("음악", R.drawable.pic_music,R.drawable.icon_music, 0, "");
        TalentObject_Home design = new TalentObject_Home("미술/디자인", R.drawable.pic_design,R.drawable.icon_design, 0, "");
        TalentObject_Home sports = new TalentObject_Home("운동", R.drawable.pic_sports,R.drawable.icon_sports, 0, "");
        TalentObject_Home living = new TalentObject_Home("생활", R.drawable.pic_living,R.drawable.icon_living, 0, "");
        TalentObject_Home beauty = new TalentObject_Home("뷰티/패션", R.drawable.pic_beauty,R.drawable.icon_beauty, 0, "");
        TalentObject_Home volunteer = new TalentObject_Home("사회봉사", R.drawable.pic_volunteer,R.drawable.icon_volunteer, 0, "");
        TalentObject_Home travel = new TalentObject_Home("여행", R.drawable.pic_travel,R.drawable.icon_travel, 0, "");
        TalentObject_Home culture = new TalentObject_Home("문화", R.drawable.pic_culture,R.drawable.icon_culture, 0, "");
        TalentObject_Home game = new TalentObject_Home("게임", R.drawable.pic_game,R.drawable.icon_game, 0, "");

        talentMap.put(career.getTitle(), career);
        talentMap.put(study.getTitle(), study);
        talentMap.put(money.getTitle(), money);
        talentMap.put(it.getTitle(), it);
        talentMap.put(camera.getTitle(), camera);
        talentMap.put(music.getTitle(), music);
        talentMap.put(design.getTitle(), design);
        talentMap.put(sports.getTitle(), sports);
        talentMap.put(living.getTitle(), living);
        talentMap.put(beauty.getTitle(), beauty);
        talentMap.put(volunteer.getTitle(), volunteer);
        talentMap.put(travel.getTitle(), travel);
        talentMap.put(culture.getTitle(), culture);
        talentMap.put(game.getTitle(), game);

//        long seed = System.nanoTime();
//        Collections.shuffle(arrTalent, new Random(seed));
    }

    private void getCateList(){
        arrTalent = new ArrayList<>();
        RequestQueue postRequestQueue = Volley.newRequestQueue(mContext);
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "TalentSharing/getTalentCateList.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for(int i = 0; i < array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
                        TalentObject_Home talentObject = talentMap.get(obj.getString("CateName"));
                        talentObject.setCateCode((int)obj.getLong("CateCode"));
                        talentObject.setTalentCount((int)obj.getLong("RegistCount"));
                        arrTalent.add(talentObject);
                    }

                    makeLayout(ll3x5);
                    make15Layout(ll1x15);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("TalentFlag", talentFlag);
                params.put("UserID", SaveSharedPreference.getUserId(mContext));
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }

    private void drawerlayoutEvent(final Context context)
    {

        ((ImageView)findViewById(R.id.cimg_pic_dl)).setBackground(new ShapeDrawable((new OvalShape())));
        // 본인이미지
        Glide.with(mContext).load(SaveSharedPreference.getServerIp()+SaveSharedPreference.getMyPicturePath()).into(((ImageView)findViewById(R.id.cimg_pic_dl)));

        if(Build.VERSION.SDK_INT >= 21) {
            ((ImageView)findViewById(R.id.cimg_pic_dl)).setClipToOutline(true);
        }


        ((LinearLayout)findViewById(R.id.ll_myprofile_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.closeDrawers();
                Intent intent = new Intent(context, MainActivity_Profile.class);
                intent.putExtra ("inPerson", true);
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

        //
        ((LinearLayout)findViewById(R.id.ll_recive_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.closeDrawers();
                ((LinearLayout)findViewById(R.id.ll_click_talent_dl)).setVisibility(View.GONE);
                ((LinearLayout)findViewById(R.id.ll_second)).setVisibility(View.VISIBLE);
                Intent intent = new Intent(context, MainActivity_TalentBox.class);
                intent.putExtra("requestType", "recive");
                startActivity(intent);
            }
        });


        ((LinearLayout)findViewById(R.id.ll_send_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.closeDrawers();
                ((LinearLayout)findViewById(R.id.ll_click_talent_dl)).setVisibility(View.GONE);
                ((LinearLayout)findViewById(R.id.ll_second)).setVisibility(View.VISIBLE);
                Intent intent = new Intent(context, MainActivity_TalentBox.class);
                intent.putExtra("requestType", "send");
                startActivity(intent);
            }
        });



        ((LinearLayout)findViewById(R.id.ll_talentbox_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout)findViewById(R.id.ll_click_talent_dl)).setVisibility(View.VISIBLE);
                ((LinearLayout)findViewById(R.id.ll_second)).setVisibility(View.GONE);
            }
        });


        ((LinearLayout)findViewById(R.id.ll_message_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, accepted.talentplanet_renewal2.Messanger.List.MainActivity.class);
                startActivity(intent);
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
        // 탤런트박스 클릭 이벤트
        ((ImageView)findViewById(R.id.iv_closetalentbox_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout)findViewById(R.id.ll_click_talent_dl)).setVisibility(View.GONE);
                ((LinearLayout)findViewById(R.id.ll_second)).setVisibility(View.VISIBLE);
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

    public void saveFcmToken() {
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Login/saveFCMToken.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("result").equals("success")) {
                        Log.d("saveToken", "토큰 저장 성공");

                    } else {
                        Log.d("saveToken", "토큰 저장 실패");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("userID", SaveSharedPreference.getUserId(mContext));
                params.put("fcmToken", SaveSharedPreference.getFcmToken(mContext));


                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);

    }
}
