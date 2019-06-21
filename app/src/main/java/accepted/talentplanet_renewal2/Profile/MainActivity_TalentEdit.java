package accepted.talentplanet_renewal2.Profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import accepted.talentplanet_renewal2.Classes.TalentObject_Home;
import accepted.talentplanet_renewal2.Classes.TalentObject_Profile;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.VolleySingleton;

public class MainActivity_TalentEdit extends AppCompatActivity {

    Context mContext;
    HashTagHelper mHashtagHelper;

    private ArrayList<String> CateCodeArr;
    private String isMentor;
    private boolean inPerson;

    ViewPager.OnPageChangeListener onPageChangeListener;
    talentlist_viewpager vp;

    int clickCateCode;
    int clickPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__talent_edit);

        mContext = getApplicationContext();

        Intent intent = getIntent();
        inPerson = intent.getBooleanExtra("inPerson", false);
        String requestType = intent.getStringExtra("type");

        if (requestType.equals("MENTOR")) {
            isMentor = "Y";
        } else if (requestType.equals("MENTEE")) {
            isMentor = "N";
        }
        clickCateCode = intent.getIntExtra("CateCode",0);
        // 이벤트를 위한 변수
        clickPosition = intent.getIntExtra("position",0) + 1;

        mHashtagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), null);

        ((TextView) findViewById(R.id.tv_toolbar)).setText(requestType);
        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);

        findViewById(R.id.img_show1x15).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        // 선택 재능 리스트
        findViewById(R.id.inc_mentor_profile).setVisibility(View.VISIBLE);
        if (inPerson) {
            // 유저 본인인 경우
            ((TextView)findViewById(R.id.tv_talentprofile_profile)).setText("삭제하기");
        } else {
            // 다른 유저일 경우
            //findViewById(R.id.tv_user_data_edit).setVisibility(View.GONE);
        }

        // 뒤로가기 이벤트
        ((ImageView) findViewById(R.id.img_open_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(v.getContext(), MainActivity_Profile.class);
                resultIntent.putExtra("talentFlag", isMentor);
                resultIntent.putStringArrayListExtra("cateCodeArr", CateCodeArr);
                resultIntent.putExtra("inPerson", inPerson);
//                setResult(3000, resultIntent);
                startActivity(resultIntent);
                finish();
            }
        });

        getAllMyTalent();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode == RESULT_OK){
            Intent intent = getIntent();
            intent.putExtra("talentFlag", isMentor);
            intent.putExtra("cateCodeArr", CateCodeArr);
            intent.putExtra("type", intent.getStringExtra("type"));

            finish();
            startActivity(intent);
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
                            CateCodeArr = new ArrayList<String>();
                            // VeiwPager 시작
                            vp = findViewById(R.id.vp_profile_mentor);
                            if (talentArr.length() == 0) {

                            } else {
                                // 재능수만큼 프레그먼트 생성
                                ArrayList<Bundle> bundleArr = new ArrayList<Bundle>();
                                for(int i = 0; i < talentArr.length(); i++){
                                    JSONObject obj = talentArr.getJSONObject(i);
                                    Talent_SecondFragment fragment = new Talent_SecondFragment();

                                    // 사용할 프레그먼트에 재능 코드 추가
                                    String CateCode = obj.getString("TalentCateCode");

                                    if(clickCateCode == Integer.parseInt(CateCode)){
                                        clickPosition = i + 1;
                                    }
                                    Bundle bundle = new Bundle();
                                    bundle.putString("profileText", obj.getString("TalentDescription"));
                                    bundle.putString("CateCode", CateCode);
                                    bundle.putString("isMentor", isMentor);
                                    bundle.putBoolean("inPerson", inPerson);

                                    bundleArr.add(bundle);
                                    CateCodeArr.add(CateCode);

                                    fragment.setArguments(bundle);
                                    adapter.addViews(fragment);
                                }

                                Log.d("CateCodeArr", CateCodeArr.toString());
                            }

                            adapter.startPager(isMentor);
                            vp.setAdapter(adapter);
//                            vp.setCurrentItem(0);
                            if (clickPosition > 0) {
                                Log.d("position", clickPosition + "");
                                vp.setCurrentItem(clickPosition);
                                ((LinearLayout) findViewById(R.id.ll_firstpage_profile_mentor)).setVisibility(View.VISIBLE);
                                ((TextView) findViewById(R.id.tv_series_profile_mentor)).setVisibility(View.VISIBLE);
                            } else {
                                vp.setCurrentItem(0);
                            }

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
                params.put("TalentFlag",  isMentor);
                return params;
            }
        };

        // Add StringRequest to the RequestQueue
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void sendInterest(final String talentID) {

        final String TalentID = talentID;
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "TalentSharing/newSendInterest.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String result = obj.getString("result");
                    if(result.equals("success")){
                        Toast.makeText(getApplicationContext(), "Shall we가 전달되었습니다.", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(mContext, );
//                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        startActivity(intent);
//                        finish();
                    }else {
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("masterID", talentID);
                //String senderID = (sendFlag)? SaveSharedPreference.getTakeTalentData(mContext).getTalentID() : SaveSharedPreference.getGiveTalentData(mContext).getTalentID();
                //params.put("senderID", senderID);
                return params;
            }
        };


        postRequestQueue.add(postJsonRequest);

    }

}
