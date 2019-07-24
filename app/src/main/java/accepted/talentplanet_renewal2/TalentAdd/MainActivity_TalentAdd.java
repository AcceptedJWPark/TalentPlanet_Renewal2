package accepted.talentplanet_renewal2.TalentAdd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import accepted.talentplanet_renewal2.Classes.TalentObject_Home;
import accepted.talentplanet_renewal2.Profile.MainActivity_Profile;
import accepted.talentplanet_renewal2.Profile.SpinnerAdapter_Talent;
import accepted.talentplanet_renewal2.Profile.customDialog_Profile;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class MainActivity_TalentAdd extends AppCompatActivity {

    private boolean isTeacher;

    private boolean[] isRegisted_teacher = new boolean[14];
    private boolean[] isRegisted_student = new boolean[14];

    private int[] icon_src = new int[14];
    private int[] bg_src = new int[14];


    private View[] view_bgr = new View[14];
    private View[] view_bgr2 = new View[14];
    private ImageView[] iv_icon = new ImageView[14];
    private TextView[] tv_txt = new TextView[14];
    private Map<String, Object>[] teacherCodeArr =  new Map[14];
    private Map<String, Object>[] studentCodeArr =  new Map[14];
    private String[] talentTitle =  new String[14];


    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talentadd_activity);

        mContext = getApplicationContext();

        isTeacher = true;

        findViewbyId();
        getAllTalent("Y");


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

                SaveSharedPreference.setPrefTalentFlag(mContext, "Y");
                getAllTalent("Y");
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

                SaveSharedPreference.setPrefTalentFlag(mContext, "N");
                getAllTalent("N");
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

    @Override
    protected void onResume() {
        super.onResume();

        getAllTalent(SaveSharedPreference.getPrefTalentFlag(mContext));
    }

    public void registedBgr()
    {
        if(isTeacher) {
            int idx = 0;

            for (int i = 0; i < isRegisted_teacher.length; i++) {
                if (isRegisted_teacher[i]) {
                    view_bgr[i].setVisibility(View.VISIBLE);
                    view_bgr2[i].setVisibility(View.GONE);
                    iv_icon[i].setImageResource(icon_src[i]);
                    iv_icon[i].setColorFilter(BLACK);
                    tv_txt[i].setTextColor(BLACK);

                    final int spinnerIdx = idx;

                    view_bgr[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity_Profile.class);
                            intent.putExtra ("inPerson", true);
                            intent.putExtra ("spinnerIdx", spinnerIdx);
                            startActivity(intent);
                        }
                    });

                    idx++;
                }else {

                    view_bgr[i].setVisibility(View.GONE);
                    view_bgr2[i].setVisibility(View.VISIBLE);
                    iv_icon[i].setImageResource(R.drawable.icon_add);
                    iv_icon[i].setColorFilter(WHITE);
                    tv_txt[i].setTextColor(WHITE);

                    if (teacherCodeArr[i].get("flag").equals("-")) {
                        final String code = String.valueOf(i + 1);
                        final String flag = "Y";
                        final int bgID = bg_src[i];
                        final String title = talentTitle[i];

                        view_bgr2[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity_Profile.class);
                                intent.putExtra("inPerson", true);
                                intent.putExtra("isNewTalent", true);
                                intent.putExtra("talentTitle", title);
                                intent.putExtra("Code", code);
                                intent.putExtra("talentFlag", flag);
                                intent.putExtra("backgroundID", bgID);

                                startActivity(intent);
                            }
                        });
                    }
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

        } else {
            int idx = 0;
            for (int i = 0; i < isRegisted_student.length; i++)
            {
                if (isRegisted_student[i]) {
                    view_bgr[i].setVisibility(View.VISIBLE);
                    view_bgr2[i].setVisibility(View.GONE);
                    iv_icon[i].setImageResource(icon_src[i]);
                    iv_icon[i].setColorFilter(BLACK);
                    tv_txt[i].setTextColor(BLACK);

                    final int spinnerIdx = idx;

                    view_bgr[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity_Profile.class);
                            intent.putExtra ("inPerson", true);
                            intent.putExtra ("spinnerIdx", spinnerIdx);
                            startActivity(intent);
                        }
                    });
                    idx++;
                }else {
                    view_bgr[i].setVisibility(View.GONE);
                    view_bgr2[i].setVisibility(View.VISIBLE);
                    iv_icon[i].setImageResource(R.drawable.icon_add);
                    iv_icon[i].setColorFilter(WHITE);
                    tv_txt[i].setTextColor(WHITE);

//                    나의 배우고 싶은 재능 등록으로 연결
                    if (studentCodeArr[i].get("flag").equals("-")) {
                        final String code = String.valueOf(i + 1);
                        final String flag = "N";
                        final int bgID = bg_src[i];
                        final String title = talentTitle[i];

                        view_bgr2[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity_Profile.class);
                                intent.putExtra("inPerson", true);
                                intent.putExtra("isNewTalent", true);
                                intent.putExtra("talentTitle", title);
                                intent.putExtra("Code", code);
                                intent.putExtra("talentFlag", flag);
                                intent.putExtra("backgroundID", bgID);

                                startActivity(intent);
                            }
                        });
                    }
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

    public void getAllTalent(final String talentFlag) {
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SaveSharedPreference.getServerIp() + "Profile/getAllMyTalent.do",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray talentArr = new JSONArray(response);

                            for(int i = 0; i < talentArr.length(); i++) {
                                JSONObject obj = talentArr.getJSONObject(i);

                                String code = obj.getString("Code");

                                HashMap<String, Object> commandMap = new HashMap<>();
                                commandMap.put("code", code);
                                commandMap.put("flag",obj.getString("TalentFlag"));
                                commandMap.put("backgroundID", obj.getString("BackgroundID"));

                                if (isTeacher) {
                                    if (code != null && !code.equals("")) {
                                        teacherCodeArr[Integer.parseInt(code) -1] = commandMap;
                                    }

                                } else {
                                    if (code != null && !code.equals("")) {
                                        studentCodeArr[Integer.parseInt(code) -1] = commandMap;
                                    }
                                }

                            }

                            isRegisted_Teacher();
                            isRegisted_Student();
                            registedBgr();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(this.getClass().getName(), "getAllTalent [error]: " +error);
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("UserID", SaveSharedPreference.getUserId(mContext));
                params.put("CheckUserID", SaveSharedPreference.getUserId(mContext));
                params.put("TalentFlag", talentFlag);
                return params;
            }
        };

        // Add StringRequest to the RequestQueue
        requestQueue.add(stringRequest);
    }

    // 티처 모드에서 볼때
    public void isRegisted_Teacher()
    {
        isRegisted_teacher[0] = false;
        isRegisted_teacher[1] = false;
        isRegisted_teacher[2] = false;
        isRegisted_teacher[3] = false;
        isRegisted_teacher[4] = false;
        isRegisted_teacher[5] = false;
        isRegisted_teacher[6] = false;
        isRegisted_teacher[7] = false;
        isRegisted_teacher[8] = false;
        isRegisted_teacher[9] = false;
        isRegisted_teacher[10] = false;
        isRegisted_teacher[11] = false;
        isRegisted_teacher[12] = false;
        isRegisted_teacher[13] = false;

        if (teacherCodeArr.length > 0) {
            for (int i=0; i<teacherCodeArr.length; i++) {
                if (!teacherCodeArr[i].get("flag").equals("-")) {
                    int idx = Integer.parseInt((String) teacherCodeArr[i].get("code"));
                    isRegisted_teacher[idx - 1] = true;
                }
            }
        }
    }

    // 학생 모드에서 볼때
    public void isRegisted_Student()
    {
        isRegisted_student[0] = false;
        isRegisted_student[1] = false;
        isRegisted_student[2] = false;
        isRegisted_student[3] = false;
        isRegisted_student[4] = false;
        isRegisted_student[5] = false;
        isRegisted_student[6] = false;
        isRegisted_student[7] = false;
        isRegisted_student[8] = false;
        isRegisted_student[9] = false;
        isRegisted_student[10] = false;
        isRegisted_student[11] = false;
        isRegisted_student[12] = false;
        isRegisted_student[13] = false;

        if (studentCodeArr.length > 0) {
            for (int i=0;i<studentCodeArr.length;i++) {
                if (!studentCodeArr[i].get("flag").equals("-")) {
                    int idx = Integer.parseInt((String) studentCodeArr[i].get("code"));
                    isRegisted_student[idx - 1] = true;
                }
            }
        }
    }

    public void findViewbyId()
    {
        icon_src[0] = R.drawable.icon_career;
        icon_src[1] = R.drawable.icon_study;
        icon_src[2] = R.drawable.icon_money;
        icon_src[3] = R.drawable.icon_it;
        icon_src[4] = R.drawable.icon_camera;
        icon_src[5] = R.drawable.icon_music;
        icon_src[6] = R.drawable.icon_design;
        icon_src[7] = R.drawable.icon_sports;
        icon_src[8] = R.drawable.icon_living;
        icon_src[9] = R.drawable.icon_beauty;
        icon_src[10] = R.drawable.icon_volunteer;
        icon_src[11] = R.drawable.icon_travel;
        icon_src[12] = R.drawable.icon_culture;
        icon_src[13] = R.drawable.icon_game;

        bg_src[0] = R.drawable.pic_career;
        bg_src[1] = R.drawable.pic_study;
        bg_src[2] = R.drawable.pic_money;
        bg_src[3] = R.drawable.pic_it;
        bg_src[4] = R.drawable.pic_camera;
        bg_src[5] = R.drawable.pic_music;
        bg_src[6] = R.drawable.pic_design;
        bg_src[7] = R.drawable.pic_sports;
        bg_src[8] = R.drawable.pic_living;
        bg_src[9] = R.drawable.pic_beauty;
        bg_src[10] = R.drawable.pic_volunteer;
        bg_src[11] = R.drawable.pic_travel;
        bg_src[12] = R.drawable.pic_culture;
        bg_src[13] = R.drawable.pic_game;

        view_bgr[0] = findViewById(R.id.view_career_talentadd);
        view_bgr[1] = findViewById(R.id.view_study_talentadd);
        view_bgr[2] = findViewById(R.id.view_money_talentadd);
        view_bgr[3] = findViewById(R.id.view_it_talentadd);
        view_bgr[4] = findViewById(R.id.view_camera_talentadd);
        view_bgr[5] = findViewById(R.id.view_music_talentadd);
        view_bgr[6] = findViewById(R.id.view_design_talentadd);
        view_bgr[7] = findViewById(R.id.view_sports_talentadd);
        view_bgr[8] = findViewById(R.id.view_living_talentadd);
        view_bgr[9] = findViewById(R.id.view_beauty_talentadd);
        view_bgr[10] = findViewById(R.id.view_volunteer_talentadd);
        view_bgr[11] = findViewById(R.id.view_travel_talentadd);
        view_bgr[12] = findViewById(R.id.view_culture_talentadd);
        view_bgr[13] = findViewById(R.id.view_game_talentadd);

        view_bgr2[0] = findViewById(R.id.view2_career_talentadd);
        view_bgr2[1] = findViewById(R.id.view2_study_talentadd);
        view_bgr2[2] = findViewById(R.id.view2_money_talentadd);
        view_bgr2[3] = findViewById(R.id.view2_it_talentadd);
        view_bgr2[4] = findViewById(R.id.view2_camera_talentadd);
        view_bgr2[5] = findViewById(R.id.view2_music_talentadd);
        view_bgr2[6] = findViewById(R.id.view2_design_talentadd);
        view_bgr2[7] = findViewById(R.id.view2_sports_talentadd);
        view_bgr2[8] = findViewById(R.id.view2_living_talentadd);
        view_bgr2[9] = findViewById(R.id.view2_beauty_talentadd);
        view_bgr2[10] = findViewById(R.id.view2_volunteer_talentadd);
        view_bgr2[11] = findViewById(R.id.view2_travel_talentadd);
        view_bgr2[12] = findViewById(R.id.view2_culture_talentadd);
        view_bgr2[13] = findViewById(R.id.view2_game_talentadd);

        tv_txt[0] = findViewById(R.id.tv_career_talentadd);
        tv_txt[1] = findViewById(R.id.tv_study_talentadd);
        tv_txt[2] = findViewById(R.id.tv_money_talentadd);
        tv_txt[3] = findViewById(R.id.tv_it_talentadd);
        tv_txt[4] = findViewById(R.id.tv_camera_talentadd);
        tv_txt[5] = findViewById(R.id.tv_music_talentadd);
        tv_txt[6] = findViewById(R.id.tv_design_talentadd);
        tv_txt[7] = findViewById(R.id.tv_sports_talentadd);
        tv_txt[8] = findViewById(R.id.tv_living_talentadd);
        tv_txt[9] = findViewById(R.id.tv_beauty_talentadd);
        tv_txt[10] = findViewById(R.id.tv_volunteer_talentadd);
        tv_txt[11] = findViewById(R.id.tv_travel_talentadd);
        tv_txt[12] = findViewById(R.id.tv_culture_talentadd);
        tv_txt[13] = findViewById(R.id.tv_game_talentadd);

        iv_icon[0] = findViewById(R.id.icon_career_talentadd);
        iv_icon[1] = findViewById(R.id.icon_study_talentadd);
        iv_icon[2] = findViewById(R.id.icon_money_talentadd);
        iv_icon[3] = findViewById(R.id.icon_it_talentadd);
        iv_icon[4] = findViewById(R.id.icon_camera_talentadd);
        iv_icon[5] = findViewById(R.id.icon_music_talentadd);
        iv_icon[6] = findViewById(R.id.icon_design_talentadd);
        iv_icon[7] = findViewById(R.id.icon_sports_talentadd);
        iv_icon[8] = findViewById(R.id.icon_living_talentadd);
        iv_icon[9] = findViewById(R.id.icon_beauty_talentadd);
        iv_icon[10] = findViewById(R.id.icon_volunteer_talentadd);
        iv_icon[11] = findViewById(R.id.icon_travel_talentadd);
        iv_icon[12] = findViewById(R.id.icon_culture_talentadd);
        iv_icon[13] = findViewById(R.id.icon_game_talentadd);

        icon_src[0] = R.drawable.icon_career;
        icon_src[1] = R.drawable.icon_study;
        icon_src[2] = R.drawable.icon_money;
        icon_src[3] = R.drawable.icon_it;
        icon_src[4] = R.drawable.icon_camera;
        icon_src[5] = R.drawable.icon_music;
        icon_src[6] = R.drawable.icon_design;
        icon_src[7] = R.drawable.icon_sports;
        icon_src[8] = R.drawable.icon_living;
        icon_src[9] = R.drawable.icon_beauty;
        icon_src[10] = R.drawable.icon_volunteer;
        icon_src[11] = R.drawable.icon_travel;
        icon_src[12] = R.drawable.icon_culture;
        icon_src[13] = R.drawable.icon_game;


        Map<String, Object> defalutMap = new HashMap<>();
        defalutMap.put("flag", "-");

        teacherCodeArr[0] = defalutMap;
        teacherCodeArr[1] = defalutMap;
        teacherCodeArr[2] = defalutMap;
        teacherCodeArr[3] = defalutMap;
        teacherCodeArr[4] = defalutMap;
        teacherCodeArr[5] = defalutMap;
        teacherCodeArr[6] = defalutMap;
        teacherCodeArr[7] = defalutMap;
        teacherCodeArr[8] = defalutMap;
        teacherCodeArr[9] = defalutMap;
        teacherCodeArr[10] = defalutMap;
        teacherCodeArr[11] = defalutMap;
        teacherCodeArr[12] = defalutMap;
        teacherCodeArr[13] = defalutMap;

        studentCodeArr[0] = defalutMap;
        studentCodeArr[1] = defalutMap;
        studentCodeArr[2] = defalutMap;
        studentCodeArr[3] = defalutMap;
        studentCodeArr[4] = defalutMap;
        studentCodeArr[5] = defalutMap;
        studentCodeArr[6] = defalutMap;
        studentCodeArr[7] = defalutMap;
        studentCodeArr[8] = defalutMap;
        studentCodeArr[9] = defalutMap;
        studentCodeArr[10] = defalutMap;
        studentCodeArr[11] = defalutMap;
        studentCodeArr[12] = defalutMap;
        studentCodeArr[13] = defalutMap;

        talentTitle[0] = "취업";
        talentTitle[1] = "학습";
        talentTitle[2] = "재테크";
        talentTitle[3] = "IT";
        talentTitle[4] = "사진";
        talentTitle[5] = "음악";
        talentTitle[6] = "미술/디자인";
        talentTitle[7] = "운동";
        talentTitle[8] = "생활";
        talentTitle[9] = "뷰티/패션";
        talentTitle[10] = "사회봉사";
        talentTitle[11] = "여행";
        talentTitle[12] = "문화";
        talentTitle[13] = "게임";
    }


    
}