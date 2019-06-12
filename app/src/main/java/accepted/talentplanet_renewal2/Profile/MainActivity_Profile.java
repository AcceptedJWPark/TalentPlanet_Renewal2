package accepted.talentplanet_renewal2.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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

    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mContext = getApplicationContext();

       // makeTestTalentArr();
        talentList = new ArrayList<>();

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

        View trash;
        lv_point = findViewById(R.id.lv_point_profile);
        lv_mentor_profile = findViewById(R.id.lv_mentor_profile);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        lv_mentor_profile.setLayoutManager(mLayoutManager);

        getAllTalent("Y");
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

    private void getAllTalent(final String talentFlag) {
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SaveSharedPreference.getServerIp() + "Profile/getAllMyTalent.do",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray talentArr = new JSONArray(response);
                            for(int i = 0; i < talentArr.length(); i++){
                                JSONObject obj = talentArr.getJSONObject(i);
                                TalentObject_Home item = new TalentObject_Home(obj.getString("Name"), getResources().getIdentifier(obj.getString("BackgroundID"), "drawable", getPackageName()), getResources().getIdentifier(obj.getString("IconID"), "drawable", getPackageName()), 0);
                                item.setCateCode((int)obj.getLong("Code"));
                                talentList.add(item);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(talentFlag.equals("Y")){
                            adapter2 = new ListAdapter_Talent(talentList);
                            lv_mentor_profile.setAdapter(adapter2);
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
                params.put("TalentFlag", talentFlag);
                return params;
            }
        };

        // Add StringRequest to the RequestQueue
        requestQueue.add(stringRequest);
    }


}
