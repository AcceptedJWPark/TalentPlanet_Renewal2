package accepted.talentplanet_renewal2.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import accepted.talentplanet_renewal2.Classes.TalentObject_Home;
import accepted.talentplanet_renewal2.Home.MainActivity;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;

import static android.graphics.Color.WHITE;


public class MainActivity_Profile extends AppCompatActivity {

    TextView tv_toolbar;
    talentlist_viewpager vp;
    Context mContext;
    ViewPager.OnPageChangeListener onPageChangeListener;
    ImageView iv_mentor_plus;
    ImageView iv_mentee_plus;

    ListView lv_point;
    RecyclerView lv_mentor_profile;
    private ArrayList<ItemData_Profile> userDate = new ArrayList<>();
    private ArrayList<TalentObject_Home> arrTalent;
    private ArrayList<TalentObject_Home> talentList;
    ListAdapter_Point adapter;
    ListAdapter_Talent adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mContext = getApplicationContext();

        makeTestTalentArr();

        tv_toolbar = findViewById(R.id.tv_toolbar);
        tv_toolbar.setText("Profile");
        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);
        findViewById(R.id.img_show1x15).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        // 받은 값 구현
        Intent intent = new Intent(this.getIntent());
        if (intent.getStringExtra("userName") != null) {
            ((TextView)findViewById(R.id.tv_name_profile)).setText(intent.getStringExtra("userName"));
            ((TextView)findViewById(R.id.tv_birth_profile)).setText(intent.getStringExtra("userInfo"));
        }

        Point pt = new Point();
        getWindowManager().getDefaultDisplay().getSize(pt);
        ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getSize(pt);
        int height = pt.y;
        Log.d("height", String.valueOf(height));
        View trash;
        lv_point = findViewById(R.id.lv_point_profile);
        lv_mentor_profile = findViewById(R.id.lv_mentor_profile);

        // click go to edit mentor
//        iv_mentor_plus = (ImageView)findViewById(R.id.iv_mentor_plus);
//        iv_mentor_plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent(MainActivity_Profile.this, MainActivity_TalentEdit.class);
//                intent1.putExtra("type","MENTOR");
//                startActivity(intent1);
//            }
//        });
        // click go to edit mentee
        iv_mentee_plus = (ImageView)findViewById(R.id.iv_mentee_plus);
        iv_mentee_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity_Profile.this, MainActivity_TalentEdit.class);
                intent1.putExtra("type","MENTEE");
                startActivity(intent1);
            }
        });

        vp = findViewById(R.id.vp_profile_mentor);
        vp.setAdapter(new talentlist_pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);
        onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    ((LinearLayout) findViewById(R.id.ll_firstpage_profile_mentor)).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.tv_series_profile_mentor)).setVisibility(View.GONE);
                } else if (position == 1) {
                    ((LinearLayout) findViewById(R.id.ll_firstpage_profile_mentor)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_series_profile_mentor)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_series_profile_mentor)).setText("2");
                } else {
                    ((LinearLayout) findViewById(R.id.ll_firstpage_profile_mentor)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_series_profile_mentor)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_series_profile_mentor)).setText("3");
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

        //point 부분
        userDate.add(new ItemData_Profile(true,"박종우","남성 / 29세", "2019/05/17 16:48PM 완료","+50"));
        userDate.add(new ItemData_Profile(false,"민권홍","남성 / 30세", "2019/05/14 16:48PM 완료","-50"));
        userDate.add(new ItemData_Profile(true,"이태훈","남성 / 29세", "2019/05/12 18:48PM 완료","+50"));
        userDate.add(new ItemData_Profile(false,"문건우","남성 / 25세", "2019/05/11 11:48AM 완료","-50"));
        userDate.add(new ItemData_Profile(true,"조현배","남성 / 27세", "2019/05/04 10:48PM 완료","+50"));

        adapter = new ListAdapter_Point(userDate);
        lv_point.setAdapter(adapter);
        adapter2 = new ListAdapter_Talent(talentList);
        lv_mentor_profile.setAdapter(adapter2);

        // 뒤로가기 이벤트
        ((ImageView) findViewById(R.id.img_open_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    private void getAllTalent() {
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SaveSharedPreference.getServerIp() + "Profile/getAllTalent.do",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray talentArr = new JSONArray(response);
                            for(int i = 0; i < talentArr.length(); i++){
                                JSONObject obj = talentArr.getJSONObject(i);
                                Log.d("get this", obj.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(this.getClass().getName(), "test 1 [error]: " +error);
                requestQueue.stop();
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("UserID", "ansrjsdn7@naver.com");
                params.put("TalentFlag", "Y");
                return params;
            }
        };

        // Add StringRequest to the RequestQueue
        requestQueue.add(stringRequest);
    }

    private void makeTestTalentArr() {
        talentList = new ArrayList<TalentObject_Home>();
        TalentObject_Home career = new TalentObject_Home("취업", R.drawable.pic_career,R.drawable.icon_career, 0);
        TalentObject_Home study = new TalentObject_Home("학습", R.drawable.pic_study,R.drawable.icon_study, 0);
        TalentObject_Home money = new TalentObject_Home("재테크", R.drawable.pic_money,R.drawable.icon_money, 0);
        TalentObject_Home it = new TalentObject_Home("IT", R.drawable.pic_it,R.drawable.icon_it, 0);
        TalentObject_Home camera = new TalentObject_Home("사진", R.drawable.pic_camera,R.drawable.icon_camera, 0);
        TalentObject_Home music = new TalentObject_Home("음악", R.drawable.pic_music,R.drawable.icon_music, 0);
        TalentObject_Home design = new TalentObject_Home("미술/디자인", R.drawable.pic_design,R.drawable.icon_design, 0);
        TalentObject_Home sports = new TalentObject_Home("운동", R.drawable.pic_sports,R.drawable.icon_sports, 0);
        TalentObject_Home living = new TalentObject_Home("생활", R.drawable.pic_living,R.drawable.icon_living, 0);
        TalentObject_Home beauty = new TalentObject_Home("뷰티/패션", R.drawable.pic_beauty,R.drawable.icon_beauty, 0);
        TalentObject_Home volunteer = new TalentObject_Home("사회봉사", R.drawable.pic_volunteer,R.drawable.icon_volunteer, 0);
        TalentObject_Home travel = new TalentObject_Home("여행", R.drawable.pic_travel,R.drawable.icon_travel, 0);
        TalentObject_Home culture = new TalentObject_Home("문화", R.drawable.pic_culture,R.drawable.icon_culture, 0);
        TalentObject_Home game = new TalentObject_Home("게임", R.drawable.pic_game,R.drawable.icon_game, 0);

        talentList.add(career);
        talentList.add(study);
        talentList.add(money);
        talentList.add(it);
        talentList.add(camera);
        talentList.add(music);
        talentList.add(design);
        talentList.add(sports);
        talentList.add(living);
        talentList.add(beauty);
        talentList.add(volunteer);
        talentList.add(travel);
        talentList.add(culture);
        talentList.add(game);

//        long seed = System.nanoTime();
//        Collections.shuffle(arrTalent, new Random(seed));
    }

}
