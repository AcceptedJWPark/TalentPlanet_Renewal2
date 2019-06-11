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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__talent_edit);
        mContext = getApplicationContext();

        Intent intent = getIntent();
        String requestType = intent.getStringExtra("type");
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
                            // VeiwPager 시작
                            vp = findViewById(R.id.vp_profile_mentor);
                            talentlist_pagerAdapter adapter = new talentlist_pagerAdapter(getSupportFragmentManager());

                            if (talentArr.length() == 0) {
                                Talent_ThirdFragment fragment = new Talent_ThirdFragment();
                                Bundle bundle = new Bundle();
                                fragment.setArguments(bundle);
                                adapter.addViews(fragment);
                            } else {
                                for(int i = 0; i < talentArr.length(); i++){
                                    JSONObject obj = talentArr.getJSONObject(i);
                                    Talent_SecondFragment fragment = new Talent_SecondFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("profileText", obj.getString("TalentDescription"));
                                    fragment.setArguments(bundle);
                                    adapter.addViews(fragment);
                                }
                            }

                            vp.setAdapter(adapter);
                            vp.setCurrentItem(0);

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
                                        // ((TextView) findViewById(R.id.tv_series_profile_mentor)).setText(position + 1);
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
}
