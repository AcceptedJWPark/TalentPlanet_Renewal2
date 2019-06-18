package accepted.talentplanet_renewal2.Profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.volokh.danylo.hashtaghelper.HashTagHelper;

import com.volokh.danylo.hashtaghelper.HashTagHelper;

import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.VolleySingleton;

public class MainActivity_Detail extends AppCompatActivity {

    EditText hashTextView;
    HashTagHelper mEditTextHashTagHelper;
    Button btn_save_detail;
    Context mContext;

    // 저장용 해시테그
    private String talentID;
    private String cateCode;
    private String isMentor;
    private String tagStr = "";
    private ArrayList<String> tagArr = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__detail);

        Intent intent = getIntent();
        cateCode = intent.getStringExtra("cateCode");
        isMentor = intent.getStringExtra("isMentor");
        Log.d("NowCateCode" , cateCode);
        // 기본 변수 선언
        mContext = getApplicationContext();
        hashTextView = (EditText) findViewById(R.id.et_detail);
        btn_save_detail = (Button) findViewById(R.id.btn_save_detail);
        ((TextView)findViewById(R.id.tv_toolbar)).setText("상세정보");
        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);

        hashTextView.requestFocus();
        // Hashtag Helper
        mEditTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), null);
        mEditTextHashTagHelper.handle(hashTextView);

        //키보드 보이게 하는 부분
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        findViewById(R.id.img_show1x15).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        // 완료 버튼
        btn_save_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTalent();
            }
        });

        // 뒤로가기 이벤트
        ((ImageView) findViewById(R.id.img_open_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

                immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                finish();
            }
        });
    }

    private void saveTalent(){
        // 유저가 작성한 텍스트
        // TB_TALENT_NEW  - 유저 요청 저장
        Editable talentTxt = hashTextView.getText();
        String userId = "ansrjsdn7@naver.com";
        // 사용자가 멘토인지 멘티인지.
        String talentFlag = isMentor;
        String talentCate = cateCode;

        HashMap<String, Object> commandMap = new HashMap<String, Object>();
        commandMap.put("UserID",userId);
        commandMap.put("TalentFlag",talentFlag);
        commandMap.put("TalentCateCode",talentCate);
        commandMap.put("TalentDescription",talentTxt);

        editUserTalent(commandMap);

        // 모든 해쉬테그
        // 태그 3개 이상으로 하게 끔 유도하겠끔 유효성 검사 필요
        // tagArr 만드는 부분
        List<String> allHashTags = mEditTextHashTagHelper.getAllHashTags();

        for (int i=0;i<allHashTags.size(); i++ ) {
            tagStr += (allHashTags.get(i)) + "||";
        }
        Log.d(this.getClass().getName(), tagStr.toString());



        // 저장 성공 시
        Intent resultIntent = new Intent();

        // 키보드 종료
        InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        resultIntent.putExtra("ProfileText", hashTextView.getText().toString());
        setResult(RESULT_OK, resultIntent);

        finish();
    }

    // 요청 저장 후 TalentID 를 반환
    private void editUserTalent(final Map<String,Object> commandMap) {
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SaveSharedPreference.getServerIp() + "Hashtag/editUserTalent.do",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(this.getClass().getName(), "test 1 : " +response);
                        talentID = response;
                        insertHashValue();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(this.getClass().getName(), "test 1 [error]: " +error);
                    }
                }){
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("UserID", commandMap.get("UserID").toString());
                    params.put("TalentFlag", commandMap.get("TalentFlag").toString());
                    params.put("TalentCateCode", commandMap.get("TalentCateCode").toString());
                    params.put("TalentDescription", commandMap.get("TalentDescription").toString());
                    return params;
                }
        };

        // Add StringRequest to the RequestQueue
        requestQueue.add(stringRequest);
    }

    private void getHashValue(final String aTag) {
        Log.d(this.getClass().getName(), "[getHashValue] : " + aTag);

        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SaveSharedPreference.getServerIp() + "Hashtag/getHashValue.do",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with response string

                        try {
                            JSONObject obj = new JSONObject(response);
                            String result = obj.getString("HAVE_FLAG");
                            Log.d(this.getClass().getName(), "test 2 : " +result);
                            if (result.equals("0")) {
                                // 태그가 없는 경우 이므로 태그를 생성
                                updateHashCode(aTag);
                            } else {
                                Log.d("TagID [test]", obj.getString("TagID"));
                                tagStr += (obj.getString("TagID")) + "||";
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Do something when get error
                Log.d(this.getClass().getName(), "test 2 [error]: " +error);
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("hashvalue", aTag);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void updateHashCode(final String aTag) {
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SaveSharedPreference.getServerIp() + "Hashtag/updateHashCode.do",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with response string
                        Log.d("response", response.toString());
                        try {
                            JSONObject talentArr = new JSONObject(response);
                            Log.d(this.getClass().getName(), talentArr.toString());
                            tagArr.add(talentArr.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Do something when get error
                Log.d(this.getClass().getName(), "test 3 [해쉬태그 업데이트 error]: " +error);
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("hashvalue", aTag);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void insertHashValue() {
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SaveSharedPreference.getServerIp() + "/Hashtag/insertHashValue.do",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with response string
                        Log.d(this.getClass().getName(), "test 4 [태그와 재능연결]: " +response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Do something when get error
                Log.d(this.getClass().getName(), "test 4 [태그와 재능연결 error]: " +error);
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("talentID", talentID);
                params.put("hashvalues", tagStr);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}