package accepted.talentplanet_renewal2.Search;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;

public class MainActivity_Search extends AppCompatActivity {

    Context mContext;

    RelativeLayout rl_bg_search;
    LinearLayout ll_searchbtn_search;

    TextView tv_Choose;
    ImageView img_leftbtn;
    ImageView img_rightbtn;

    EditText et_searchbox_search;

    ListView lv_searchresult_search;

    ListAdapter_Search adapter;

    ArrayList<Map<String, Object>> resultArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        mContext = getApplicationContext();

        String mode = SaveSharedPreference.getPrefTalentFlag(mContext);

        if (mode.equals("Y")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
            }

            RelativeLayout ll_bar_talentlist = findViewById(R.id.ll_bar_talentlist);
            TextView tv_listhaed_talentlist = findViewById(R.id.tv_listhaed_talentlist);

            ll_bar_talentlist.setBackgroundColor(getResources().getColor(R.color.color_mentee));
            tv_listhaed_talentlist.setText("Teacher List");
        }

        tv_Choose = findViewById(R.id.tv_toolbar_talentlist);
        img_rightbtn = findViewById(R.id.iv_toolbar_search_talentlist);
        img_leftbtn = findViewById(R.id.img_back_toolbar_talentlist);


        et_searchbox_search = findViewById(R.id.et_searchbox_search);
        rl_bg_search = findViewById(R.id.rl_bg_search);
        ll_searchbtn_search = findViewById(R.id.ll_searchbtn_search);
        lv_searchresult_search = findViewById(R.id.lv_searchresult_search);

        tv_Choose.setText("#hashtag 검색");
        img_rightbtn.setVisibility(View.GONE);
        img_leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        String talentFlag = SaveSharedPreference.getPrefTalentFlag(mContext);
        if (talentFlag.equals("N")) {
            rl_bg_search.setBackgroundColor(mContext.getResources().getColor(R.color.color_mentee));
            ((LinearLayout)findViewById(R.id.ll_searchbtn_search)).setBackgroundColor(mContext.getResources().getColor(R.color.color_mentee));
        }

        ll_searchbtn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTalentListByHashtag();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        if(getIntent().hasExtra("searchTag")){
            et_searchbox_search.setText(getIntent().getStringExtra("searchTag"));
            searchTalentListByHashtag();
        }

    }

    private void searchTalentListByHashtag(){
        final String titleTxt = et_searchbox_search.getText().toString();

        RequestQueue postRequestQueue = Volley.newRequestQueue(mContext);
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "TalentSharing/searchTalentListByHashtag.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("[]")) {
                    Toast.makeText(mContext, "검색결과가 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray userArr = new JSONArray(response);

                        resultArr = new ArrayList<>();

                        for (int i=0;i<userArr.length();i++) {
                            JSONObject aUser = userArr.getJSONObject(i);

                            Map<String, Object> aUserData = new HashMap<>();
                            aUserData.put("userID", aUser.getString("UserID"));
                            aUserData.put("userName", aUser.getString("USER_NAME"));
                            aUserData.put("gender", aUser.getString("GENDER"));
                            aUserData.put("userBirth", aUser.getString("USER_BIRTH"));
                            aUserData.put("birthFlag", aUser.getString("BIRTH_FLAG"));
                            aUserData.put("tags", aUser.getString("HASHTAG"));
                            aUserData.put("talentName", aUser.getString("TalentName"));
                            aUserData.put("cateCode", aUser.getString("CateCode"));
                            aUserData.put("S_FILE_PATH", aUser.getString("S_FILE_PATH"));
                            aUserData.put("PROFILE_DESCRIPTION", aUser.getString("PROFILE_DESCRIPTION") == null ? "" : aUser.getString("PROFILE_DESCRIPTION"));
                            aUserData.put("searchTxt", titleTxt);
                            //
                            if (aUser.has("GP_LAT") && aUser.has("GP_LNG")) {
                                aUserData.put("GP_LAT", Double.parseDouble(aUser.getString("GP_LAT")));
                                aUserData.put("GP_LNG", Double.parseDouble(aUser.getString("GP_LNG")));
                            } else {
                                aUserData.put("GP_LAT", null);
                                aUserData.put("GP_LNG", null);
                            }

                            resultArr.add(aUserData);
                        }

                        adapter = new ListAdapter_Search(mContext, resultArr);
                        lv_searchresult_search.setAdapter(adapter);
                        lv_searchresult_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(MainActivity_Search.this, accepted.talentplanet_renewal2.Profile.MainActivity_Profile.class);
                                // 필요한 데이터 추가로 넣기
                                intent.putExtra("userID", (String) resultArr.get(position).get("userID"));
                                intent.putExtra("cateCode", (String)resultArr.get(position).get("cateCode"));

                                intent.putExtra("userName", (String)resultArr.get(position).get("userName"));
                                intent.putExtra("userInfo", (String) resultArr.get(position).get("userBirth"));
                                intent.putExtra("targetUserID", (String) resultArr.get(position).get("userID"));
                                intent.putExtra("userGender", (String) resultArr.get(position).get("gender"));
                                intent.putExtra("userDescription", (String) resultArr.get(position).get("PROFILE_DESCRIPTION"));
                                intent.putExtra("GP_LAT", (Double) resultArr.get(position).get("GP_LAT"));
                                intent.putExtra("GP_LNG", (Double) resultArr.get(position).get("GP_LNG"));
                                intent.putExtra("BIRTH_FLAG", (String) resultArr.get(position).get("birthFlag"));

                                startActivity(intent);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                String talentFlag = SaveSharedPreference.getPrefTalentFlag(mContext);
                if (talentFlag.equals("Y")) {
                    params.put("TalentFlag", "N");
                } else if (talentFlag.equals("N")) {
                    params.put("TalentFlag", "Y");
                }
                params.put("Hashtag", titleTxt);
                params.put("UserID", SaveSharedPreference.getUserId(mContext));
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }
}
