package accepted.talentplanet_renewal2.Home;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.HashMap;
import java.util.Map;

import accepted.talentplanet_renewal2.Classes.TalentObject_Home;
import accepted.talentplanet_renewal2.Condition.MainActivity_Condition;
import accepted.talentplanet_renewal2.Cs.MainActivity_Cs;
import accepted.talentplanet_renewal2.FriendList.MainActivity_Friend;
import accepted.talentplanet_renewal2.Profile.MainActivity_Profile;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.TalentList.MainActivity_TalentList;
import accepted.talentplanet_renewal2.TalentAdd.MainActivity_TalentAdd;
import accepted.talentplanet_renewal2.VolleySingleton;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class MainActivity extends AppCompatActivity {

    Context mContext;

    ImageView img_open_dl;
    TextView tv_user_dl;
    TextView tv_email_dl;

    ImageView[] iv_talentCate = new ImageView[15];
    TextView[] tv_talentCate = new TextView[15];


    DrawerLayout dl;
    View v_drawerlayout;

    boolean mentorClicked;

    boolean isAlaram;

    private ViewPager vp;

    private ArrayList<TalentObject_Home> arrTalent;
    private Map<String, TalentObject_Home> talentMap;
    private ArrayList<LinearLayout> talentList = new ArrayList<>();
    private ArrayList<String> talentTextList = new ArrayList<>();

    SQLiteDatabase sqliteDatabase;

    Spinner spinner;
    SpinnerAdapter_Toolbar adapter_toolbar;
    ArrayList<SpinnerData_Toolbar> arrayList_spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        mContext=getApplicationContext();

        // 최초 로그인 성공시 모드는 티처모드이므로 Y부여
        SaveSharedPreference.setPrefTalentFlag(mContext, "Y");

        arrayList_spinner = new ArrayList<>();
        arrayList_spinner.add(new SpinnerData_Toolbar("Teacher Planet", R.drawable.icon_teacher, R.drawable.icon_arrow_teacher, "Y"));
        arrayList_spinner.add(new SpinnerData_Toolbar("Student Planet", R.drawable.icon_student, R.drawable.icon_arrow_student, "N"));

        spinner = findViewById(R.id.sp_toolbar);
        adapter_toolbar = new SpinnerAdapter_Toolbar(arrayList_spinner, mContext);
        spinner.setAdapter(adapter_toolbar);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SaveSharedPreference.setPrefTalentFlag(mContext, arrayList_spinner.get(position).getTalentFlag());
//                getCateList();

                if(position==0)
                    {
                        Toast.makeText(mContext,"티쳐",Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Window window = getWindow();
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
                        }

                    SaveSharedPreference.setPrefTalentFlag(mContext, "Y");

                    selectTeacher();

                    }
                else
                {
                        Toast.makeText(mContext,"학생",Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
                    }

                    SaveSharedPreference.setPrefTalentFlag(mContext, "N");

                        selectStudent();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (SaveSharedPreference.getFcmToken(mContext) == null || SaveSharedPreference.getFcmToken(mContext).isEmpty()) {
            SaveSharedPreference.setPrefFcmToken(mContext, FirebaseInstanceId.getInstance().getToken());
            saveFcmToken();
        }

        img_open_dl = findViewById(R.id.img_open_dl);
        dl = (DrawerLayout)findViewById(R.id.drawerlayout);
        v_drawerlayout=findViewById(R.id.view_drawerlayout);

        img_open_dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.openDrawer(v_drawerlayout);
            }
        });

        tv_user_dl = findViewById(R.id.tv_user_dl);
        tv_email_dl = findViewById(R.id.tv_email_dl);

        String userName = SaveSharedPreference.getUserName(mContext);
        String userId = SaveSharedPreference.getUserId(mContext);
        tv_user_dl.setText(userName);
        tv_email_dl.setText(userId);



        drawerlayoutEvent(mContext);
        isAlaram = true;
        makeTestTalentArr();

        //기본 값
        mentorClicked = true;

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

        // 07-18
        addTalentEvent();

        vp = (ViewPager) findViewById(R.id.vp_pop_talent_home);

//        ViewPager_PopTalent adapter = new ViewPager_PopTalent(getSupportFragmentManager() ,mContext);
//        vp.setAdapter(adapter);

    }

    // 이벤트
    private void addTalentEvent() {
        final LinearLayout ll_1 = findViewById(R.id.ll_home_con1);
        final LinearLayout ll_2 = findViewById(R.id.ll_home_con2);
        final LinearLayout ll_3 = findViewById(R.id.ll_home_con3);
        final LinearLayout ll_4 = findViewById(R.id.ll_home_con4);
        final LinearLayout ll_5 = findViewById(R.id.ll_home_con5);
        final LinearLayout ll_6 = findViewById(R.id.ll_home_con6);
        final LinearLayout ll_7 = findViewById(R.id.ll_home_con7);
        final LinearLayout ll_8 = findViewById(R.id.ll_home_con8);
        final LinearLayout ll_9 = findViewById(R.id.ll_home_con9);
        final LinearLayout ll_10 = findViewById(R.id.ll_home_con10);
        final LinearLayout ll_11 = findViewById(R.id.ll_home_con11);
        final LinearLayout ll_12 = findViewById(R.id.ll_home_con12);
        final LinearLayout ll_13 = findViewById(R.id.ll_home_con13);
        final LinearLayout ll_14 = findViewById(R.id.ll_home_con14);
        final LinearLayout ll_search = findViewById(R.id.ll_home_search);

        talentTextList.add("1");
        talentTextList.add("2");
        talentTextList.add("3");
        talentTextList.add("4");
        talentTextList.add("5");
        talentTextList.add("6");
        talentTextList.add("7");
        talentTextList.add("8");
        talentTextList.add("9");
        talentTextList.add("10");
        talentTextList.add("11");
        talentTextList.add("12");
        talentTextList.add("13");
        talentTextList.add("14");

        talentList.add(ll_1);
        talentList.add(ll_2);
        talentList.add(ll_3);
        talentList.add(ll_4);
        talentList.add(ll_5);
        talentList.add(ll_6);
        talentList.add(ll_7);
        talentList.add(ll_8);
        talentList.add(ll_9);
        talentList.add(ll_10);
        talentList.add(ll_11);
        talentList.add(ll_12);
        talentList.add(ll_13);
        talentList.add(ll_14);

        for (int i = 0; i<talentList.size(); i++) {
            final int position = i;
            talentList.get(i).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MainActivity_TalentList.class);
                    intent.putExtra("cateCode", talentTextList.get(position));
                    startActivity(intent);
                }
            });
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
                        talentObject.setHasFlag((int)obj.getLong("HasFlag") > 0);
                        if(obj.has("HASHTAG")) {
                            talentObject.setHashtag(obj.getString("HASHTAG"));
                        }
                        arrTalent.add(talentObject);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("TalentFlag", SaveSharedPreference.getPrefTalentFlag(mContext));
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


        ((LinearLayout)findViewById(R.id.ll_system_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Intent intent = new Intent(context, accepted.talentplanet_renewal2.Cs.Claim.MainActivity.class);
                startActivity(intent);
            }
        });


        //알람 On Off

//        ((ImageView)findViewById(R.id.iv_alarm_dl)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isAlaram)
//                {
//                    isAlaram=false;
//                    Toast.makeText(mContext,"알람이 비활성화되었습니다.",Toast.LENGTH_SHORT).show();
//                    ((ImageView)findViewById(R.id.iv_alarm_dl)).setImageResource(R.drawable.icon_dl_alarmoff);
//                }else
//                {
//                    isAlaram=true;
//                    Toast.makeText(mContext,"알람이 활성화되었습니다.",Toast.LENGTH_SHORT).show();
//                    ((ImageView)findViewById(R.id.iv_alarm_dl)).setImageResource(R.drawable.icon_dl_alarmon);
//                }
//            }
//        });




        ((LinearLayout)findViewById(R.id.ll_addtalent_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.closeDrawers();
                Intent intent = new Intent(context, MainActivity_TalentAdd.class);
                startActivity(intent);
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

    public void selectTeacher()
    {
        talentCateFindView();
        Glide.with(mContext).load(R.drawable.bgr_home_teacher).into((ImageView)findViewById(R.id.img_bgr_home));
        ((ImageView)findViewById(R.id.img_open_dl)).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
        ((ImageView)findViewById(R.id.img_rightbtn)).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
        ((ImageView)findViewById(R.id.img_arrow_addcate)).setColorFilter(WHITE);
        ((ImageView)findViewById(R.id.img_addcate)).setImageResource(R.drawable.icon_addcate_teacher);


        ((LinearLayout)findViewById(R.id.ll_bgr_talentcate)).setBackgroundResource(R.color.color_mentor);
        ((LinearLayout)findViewById(R.id.ll_bgr_addcate)).setBackgroundResource(R.color.color_mentor);

        ((TextView)findViewById(R.id.txt_line1_bgr)).setText("당신의");
        ((TextView)findViewById(R.id.txt_line2_bgr)).setText(" 재능");
        ((TextView)findViewById(R.id.txt_line3_bgr)).setText("을 기다립니다.");
        ((TextView)findViewById(R.id.txt_line4_bgr)).setText("지금 바로");
        ((TextView)findViewById(R.id.txt_line5_bgr)).setText(" 학생");
        ((TextView)findViewById(R.id.txt_line6_bgr)).setText("들을 찾아보세요!");
        ((TextView)findViewById(R.id.txt_line7_bgr)).setText("Teacher 재능 등록하기");


        ((TextView)findViewById(R.id.txt_line1_addcate)).setTextColor(WHITE);
        ((TextView)findViewById(R.id.txt_line2_addcate)).setTextColor(WHITE);
        ((TextView)findViewById(R.id.txt_line3_addcate)).setTextColor(WHITE);

        for(int i=0; i<tv_talentCate.length-1; i++)
        {
            tv_talentCate[i].setTextColor(WHITE);
            iv_talentCate[i].setColorFilter(WHITE);
        }
        tv_talentCate[14].setTextColor(WHITE);
        iv_talentCate[14].setImageResource(R.drawable.icon_search_teacher);


    }

    public void selectStudent()
    {
        talentCateFindView();
        Glide.with(mContext).load(R.drawable.bgr_home_student).into((ImageView)findViewById(R.id.img_bgr_home));
        ((ImageView)findViewById(R.id.img_open_dl)).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
        ((ImageView)findViewById(R.id.img_rightbtn)).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
        ((ImageView)findViewById(R.id.img_arrow_addcate)).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
        ((ImageView)findViewById(R.id.img_addcate)).setImageResource(R.drawable.icon_addcate_student);

        ((LinearLayout)findViewById(R.id.ll_bgr_talentcate)).setBackgroundResource(R.color.color_mentee);
        ((LinearLayout)findViewById(R.id.ll_bgr_addcate)).setBackgroundResource(R.color.color_mentee);

        ((TextView)findViewById(R.id.txt_line1_bgr)).setText("당신의");
        ((TextView)findViewById(R.id.txt_line2_bgr)).setText(" 배움");
        ((TextView)findViewById(R.id.txt_line3_bgr)).setText("을 응원합니다.");
        ((TextView)findViewById(R.id.txt_line4_bgr)).setText("지금 바로");
        ((TextView)findViewById(R.id.txt_line5_bgr)).setText(" 선생님");
        ((TextView)findViewById(R.id.txt_line6_bgr)).setText("을 찾아보세요!");
        ((TextView)findViewById(R.id.txt_line7_bgr)).setText("Student 재능 등록하기");

        ((TextView)findViewById(R.id.txt_line1_addcate)).setTextColor(BLACK);
        ((TextView)findViewById(R.id.txt_line2_addcate)).setTextColor(BLACK);
        ((TextView)findViewById(R.id.txt_line3_addcate)).setTextColor(BLACK);

        for(int i=0; i<tv_talentCate.length-1; i++)
        {
            tv_talentCate[i].setTextColor(BLACK);
            iv_talentCate[i].setColorFilter(BLACK);
        }
        tv_talentCate[14].setTextColor(BLACK);
        iv_talentCate[14].setImageResource(R.drawable.icon_search_student);
    }

    public void talentCateFindView()
    {
        iv_talentCate[0] = findViewById(R.id.img_career_home);
        iv_talentCate[1] = findViewById(R.id.img_money_home);
        iv_talentCate[2] = findViewById(R.id.img_living_home);
        iv_talentCate[3] = findViewById(R.id.img_travel_home);
        iv_talentCate[4] = findViewById(R.id.img_study_home);
        iv_talentCate[5] = findViewById(R.id.img_volunteer_home);
        iv_talentCate[6] = findViewById(R.id.img_it_home);
        iv_talentCate[7] = findViewById(R.id.img_culture_home);
        iv_talentCate[8] = findViewById(R.id.img_sports_home);
        iv_talentCate[9] = findViewById(R.id.img_music_home);
        iv_talentCate[10] = findViewById(R.id.img_camera_home);
        iv_talentCate[11] = findViewById(R.id.img_beauty_home);
        iv_talentCate[12] = findViewById(R.id.img_design_home);
        iv_talentCate[13] = findViewById(R.id.img_game_home);
        iv_talentCate[14] = findViewById(R.id.img_search_home);
        tv_talentCate[0] = findViewById(R.id.txt_career_home);
        tv_talentCate[1] = findViewById(R.id.txt_money_home);
        tv_talentCate[2] = findViewById(R.id.txt_living_home);
        tv_talentCate[3] = findViewById(R.id.txt_travel_home);
        tv_talentCate[4] = findViewById(R.id.txt_study_home);
        tv_talentCate[5] = findViewById(R.id.txt_volunteer_home);
        tv_talentCate[6] = findViewById(R.id.txt_it_home);
        tv_talentCate[7] = findViewById(R.id.txt_culture_home);
        tv_talentCate[8] = findViewById(R.id.txt_sports_home);
        tv_talentCate[9] = findViewById(R.id.txt_music_home);
        tv_talentCate[10] = findViewById(R.id.txt_camera_home);
        tv_talentCate[11] = findViewById(R.id.txt_beauty_home);
        tv_talentCate[12] = findViewById(R.id.txt_design_home);
        tv_talentCate[13] = findViewById(R.id.txt_game_home);
        tv_talentCate[14] = findViewById(R.id.txt_search_home);
    }

    @Override
    public void onResume(){
        super.onResume();
        getCateList();
    }
}
