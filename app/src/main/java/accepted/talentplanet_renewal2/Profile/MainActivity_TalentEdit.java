package accepted.talentplanet_renewal2.Profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
    private String userID;
    private String talentID;
    private String targetUserID;
    private String point;
    private String ProfileSendFlag;
    private String MatchingFlag;

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
        talentID = intent.getStringExtra("talentID");
        userID = intent.getStringExtra("userID");
        String requestType = intent.getStringExtra("type");

        if (requestType.equals("MENTOR")) {
            isMentor = "Y";
        } else if (requestType.equals("MENTEE")) {
            isMentor = "N";
            ((TextView)findViewById(R.id.tv_talentprofile_profile)).setText("프로필 보내기");
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
            LinearLayout ll_request_profile_mentor = (LinearLayout)findViewById(R.id.ll_request_profile_mentor);
            ll_request_profile_mentor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isMentor.equals("Y")) {
                        // 멘토일 경우
                        sendInterest();
                    } else if (isMentor.equals("N")) {
                        // 멘티일 경우
                        sendProfile();
                    }
                }
            });
        }

        // 뒤로가기 이벤트
        ((ImageView) findViewById(R.id.img_open_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(v.getContext(), MainActivity_Profile.class);
                resultIntent.putExtra("talentFlag", isMentor);
                resultIntent.putStringArrayListExtra("cateCodeArr", CateCodeArr);
                resultIntent.putExtra("inPerson", inPerson);
                resultIntent.putExtra("userID", userID);
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

    private void getProfileData(){

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
                            final JSONArray talentArr = new JSONArray(response);

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
                                    bundle.putString("targetUserID", obj.getString("UserID"));
                                    bundle.putString("BackgroundID", obj.getString("BackgroundID"));
                                    ProfileSendFlag = obj.getString("ProfileSendFlag");
                                    MatchingFlag = obj.getString("MatchingFlag");

                                    targetUserID = obj.getString("UserID");

                                    bundleArr.add(bundle);
                                    CateCodeArr.add(CateCode);

                                    fragment.setArguments(bundle);
                                    adapter.addViews(fragment);
                                }

                                if  (isMentor.equals("Y")) {
                                    // 현재 카테고리가 멘토리스트
                                    if (MatchingFlag.equals("0")) {
                                        // 모든 재능에 멘티들이 요청할 수 있음
                                        ((TextView) findViewById(R.id.tv_talentprofile_profile)).setText("요청하기");
                                    } else if (MatchingFlag.equals("1")) {
                                        ((TextView) findViewById(R.id.tv_talentprofile_profile)).setText("취소하기");
                                    }
                                } else if (isMentor.equals("N")) {
                                    // 현재 카테고리가 멘티리스트
                                    if (ProfileSendFlag.equals("0")) {
                                        ((TextView) findViewById(R.id.tv_talentprofile_profile)).setText("프로필 보내기");
                                    } else if (ProfileSendFlag.equals("1")) {
                                        ((TextView) findViewById(R.id.tv_talentprofile_profile)).setText("취소하기");
                                    }
                                }

                            }

                            adapter.startPager(isMentor);
                            vp.setAdapter(adapter);
//                            vp.setCurrentItem(0);
                            if (clickPosition > 0) {
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
                                        try {
                                            talentID = talentArr.getJSONObject(position).getString("talentID");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
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
                params.put("UserID", userID);
                params.put("CheckUserID", SaveSharedPreference.getUserId(mContext));
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

    public void sendInterest() {
        AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity_TalentEdit.this);
        ab.setTitle("요청 보내기");
        ab.setMessage("멘토에게 줄 POINT를 정해주세요.");

        // Point input
        final EditText et = new EditText(MainActivity_TalentEdit.this);
        ab.setView(et);

        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                point = et.getText().toString();
                Log.d("point", point);

                RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
                StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "TalentSharing/newSendInterest.do", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("result");
                            if(result.equals("success")){
                                Toast.makeText(getApplicationContext(), "요청이 전달되었습니다.", Toast.LENGTH_SHORT).show();
                                ((TextView)findViewById(R.id.tv_talentprofile_profile)).setText("취소하기");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, SaveSharedPreference.getErrorListener(mContext)) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap();
                        params.put("MentorTalentID", talentID);
                        params.put("MenteeID", SaveSharedPreference.getUserId(mContext));
                        params.put("Point", point);
                        return params;
                    }
                };

                postRequestQueue.add(postJsonRequest);

                dialog.dismiss();
            }
        });

        ab.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        ab.show();
    }

    public void sendProfile() {

        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "TalentSharing/sendProfile.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String result = obj.getString("result");
                    if(result.equals("success")){
                        Toast.makeText(getApplicationContext(), "프로필이 전달되었습니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "프로필이 전달이 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("ReceiverID", targetUserID);
                params.put("SenderID", SaveSharedPreference.getUserId(mContext));
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }
}
