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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.VolleySingleton;

public class MainActivity_Detail extends AppCompatActivity {

    EditText hashTextView;
    HashTagHelper mEditTextHashTagHelper;
    Button btn_save_detail;
    Context mContext;

    // 저장용 해시테그
    private int talentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__detail);

        mContext = getApplicationContext();

        // 기본 변수 선언
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
        // to-do 저장 로직
        // 유저가 작성한 텍스트
        // TB_TALENT_NEW  - 유저 요청 저장
        Editable talentTxt = hashTextView.getText();
        String userId = "ansrjsdn7@naver.com";
        String talentFlag = "";
        String talentCate = "";

        HashMap<String, Object> commandMap = new HashMap<String, Object>();
        commandMap.put("UserID",userId);
        commandMap.put("TalentFlag",talentFlag);
        commandMap.put("TalentCateCode",talentCate);
        commandMap.put("TalentDescripton",talentTxt);

        editUserTalent(commandMap);

        // 모든 해쉬테그
        // 태그 3개 이상으로 하게 끔 유도하겠끔 유효성 검사
        List<String> allHashTags = mEditTextHashTagHelper.getAllHashTags();
        for (int i=0;i<allHashTags.size(); i++ ) {
            
        }

        // 저장 성공 시
        Intent resultIntent = new Intent();
        resultIntent.putExtra("ProfileText", hashTextView.getText().toString());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void editUserTalent(final Map<String,Object> commandMap) {
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SaveSharedPreference.getServerIp() +"/Hashtag/editUserTalent.do",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with response string
                        Log.d(this.getClass().getName(), "test 1 : " +response);
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when get error
                        Log.d(this.getClass().getName(), "test 1 [error]: " +error);
                        requestQueue.stop();
                    }
                }){
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("UserID", commandMap.get("UserID").toString());
                    params.put("TalentFlag", commandMap.get("TalentFlag").toString());
                    params.put("TalentCateCode", commandMap.get("TalentCateCode").toString());
                    params.put("TalentDescripton", commandMap.get("TalentDescripton").toString());
                    return params;
                }
        };

        // Add StringRequest to the RequestQueue
        requestQueue.add(stringRequest);
    }
}