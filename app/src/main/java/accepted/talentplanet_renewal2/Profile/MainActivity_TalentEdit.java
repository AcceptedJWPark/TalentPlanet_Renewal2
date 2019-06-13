package accepted.talentplanet_renewal2.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import accepted.talentplanet_renewal2.Classes.TalentObject_Home;
import accepted.talentplanet_renewal2.Classes.TalentObject_Profile;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;

public class MainActivity_TalentEdit extends AppCompatActivity {

    private ArrayList<TalentObject_Profile> arrTalent;
    View v_inc_profile[] = new View[2];
    ViewPager.OnPageChangeListener onPageChangeListener;
    talentlist_viewpager vp;
    Context mContext;
    HashTagHelper mHashtagHelper;
    private Map<String, TalentObject_Home> talentMap;
    int CateCode;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__talent_edit);
        mContext = getApplicationContext();

        // VeiwPager 시작
        vp = findViewById(R.id.vp_profile_mentor);

        Intent intent = getIntent();
        String requestType = intent.getStringExtra("type");
        CateCode = intent.getIntExtra("CateCode",0);
        // 이벤트를 위한 변수
        position = intent.getIntExtra("position",0) + 1;
        Log.d("position", Integer.toString(position));

        mHashtagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), null);

        ((TextView)findViewById(R.id.tv_toolbar)).setText(requestType);
        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);

        findViewById(R.id.img_show1x15).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        // 선택 재능 리스트
        findViewById(R.id.inc_mentor_profile).setVisibility(View.VISIBLE);

        // 뒤로가기 이벤트
        ((ImageView) findViewById(R.id.img_open_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getAllMyTalent();
        if (position > 0) {
            vp.setCurrentItem(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode == RESULT_OK){
            String ProfileText = data.getStringExtra("ProfileText");
            vp.getCurrentItem();
            TextView profile_talent = (TextView)findViewById(R.id.tv_profile_talant);
            profile_talent.setText(ProfileText);
            mHashtagHelper.handle(profile_talent);
        }
    }

    private void getAllMyTalent() {
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SaveSharedPreference.getServerIp() + "Profile/getAllMyTalent.do",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray talentArr = new JSONArray(response);

                            // 어뎁터 선언
                            talentlist_pagerAdapter adapter = new talentlist_pagerAdapter(getSupportFragmentManager());
                            // 전체 재능 리스트
                            ArrayList<String> CateCodeArr = new ArrayList<String>();

                            if (talentArr.length() == 0) {

                            } else {
                                // 재능수만큼 프레그먼트 생성
                                for(int i = 0; i < talentArr.length(); i++){
                                    JSONObject obj = talentArr.getJSONObject(i);
                                    Talent_SecondFragment fragment = new Talent_SecondFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("profileText", obj.getString("TalentDescription"));

                                    // 사용할 프레그먼트에 재능 코드 추가
                                    String CateCode = obj.getString("TalentCateCode");
                                    bundle.putString("CateCode", CateCode);

                                    // 첫번째 프레그먼트에 재능 코드 추가
                                    CateCodeArr.add(CateCode);

                                    fragment.setArguments(bundle);
                                    adapter.addViews(fragment);
                                }
                            }

                            adapter.startPager();
                            vp.setAdapter(adapter);
                            vp.setCurrentItem(0);
//                            if (position != 0) {
//                                vp.setCurrentItem(position);
//                            }

                            // 이벤트 내용 정의
                            onPageChangeListener = new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                }

                                @Override
                                public void onPageSelected(int position) {
                                    if (position == 0) {
                                        ((LinearLayout) findViewById(R.id.ll_firstpage_profile_mentor)).setVisibility(View.GONE);
                                        ((TextView) findViewById(R.id.tv_series_profile_mentor)).setVisibility(View.GONE);
                                    } else {
                                        ((LinearLayout) findViewById(R.id.ll_firstpage_profile_mentor)).setVisibility(View.VISIBLE);
                                        ((TextView) findViewById(R.id.tv_series_profile_mentor)).setVisibility(View.VISIBLE);
//                                         ((TextView) findViewById(R.id.tv_series_profile_mentor)).setText(position + 1);
                                    }
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {
                                }
                            };

                            // 해당 viewpager에 이벤트 부여
                            vp.addOnPageChangeListener(onPageChangeListener);

                            findViewById(R.id.ll_totalshow_profile_mentor).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    vp.setCurrentItem(0);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

    // 테스트용 배열 생성 함수
    private void makeTestTalentArr(){
        talentMap = new HashMap();

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
}
