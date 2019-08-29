package com.accepted.acceptedtalentplanet.JoinLogin;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.accepted.acceptedtalentplanet.SaveSharedPreference;
import com.accepted.acceptedtalentplanet.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.accepted.acceptedtalentplanet.R;
import com.accepted.acceptedtalentplanet.SaveSharedPreference;
import com.accepted.acceptedtalentplanet.VolleySingleton;

public class MainActivity_Find extends AppCompatActivity {

    private Context mContext;
    private String certNum_id, certNum_pw;
    private ImageView iv_toolbar_search_talentlist;
    private ImageView img_back_toolbar_talentlist;
    private EditText et_admit_id, et_phone_id;
    private EditText et_admit_pw, et_phone_pw, et_id_idpw;
    private EditText et_chgpassword_pw, et_chgpasswordcomp_pw;
    private String MemID, MemPW;
    private Button btn_chkphonenum_id, btn_chkcertnum_id, btn_chkphonenum_pw, btn_chkcertnum_pw, btn_chgpassword_pw;
    private RelativeLayout rl_chgpassword_pw, rl_chgpasswordcomp_pw;
    private boolean isCheckCertNum_id, isCheckCertNum_pw;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.idpw_activity);
        ((TextView)findViewById(R.id.tv_toolbar_talentlist)).setText("Find ID/PW");
        iv_toolbar_search_talentlist = (ImageView) findViewById(R.id.iv_toolbar_search_talentlist);
        img_back_toolbar_talentlist = (ImageView) findViewById(R.id.img_back_toolbar_talentlist);
        ((ImageView)findViewById(R.id.iv_toolbar_search_talentlist)).setVisibility(View.GONE);

        iv_toolbar_search_talentlist.setVisibility(View.GONE);

        et_phone_id = findViewById(R.id.et_phone_id);
        et_admit_id = findViewById(R.id.et_admit_id);
        et_admit_pw = findViewById(R.id.et_admit_pw);
        et_phone_pw = findViewById(R.id.et_phone_pw);
        et_id_idpw = findViewById(R.id.et_id_idpw);
        et_chgpassword_pw = findViewById(R.id.et_chgpassword_pw);
        et_chgpasswordcomp_pw = findViewById(R.id.et_chgpasswordcomp_pw);

        btn_chkphonenum_id = findViewById(R.id.btn_chkphonenum_id);
        btn_chkcertnum_id = findViewById(R.id.btn_chkcertnum_id);
        btn_chkphonenum_pw = findViewById(R.id.btn_chkphonenum_pw);
        btn_chkcertnum_pw = findViewById(R.id.btn_chkcertnum_pw);
        btn_chgpassword_pw = findViewById(R.id.btn_chgpassword_pw);

        rl_chgpassword_pw = findViewById(R.id.rl_chgpassword_pw);
        rl_chgpasswordcomp_pw = findViewById(R.id.rl_chgpasswordcomp_pw);

        btn_chkphonenum_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone_id.getText().toString();
                sendSMS(phone, true);
            }
        });

        btn_chkcertnum_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputStr = et_admit_id.getText().toString();
                confirmPhone(certNum_id, inputStr, true);
            }
        });

        btn_chkphonenum_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone_pw.getText().toString();
                if(et_id_idpw.getText().toString().isEmpty()){
                    Toast.makeText(mContext, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!Patterns.EMAIL_ADDRESS.matcher(et_id_idpw.getText().toString()).matches())
                {
                    Toast.makeText(getApplicationContext(),"잘못된 아이디 형식입니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                sendSMS(phone, false);
            }
        });

        btn_chkcertnum_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputStr = et_admit_pw.getText().toString();
                confirmPhone(certNum_pw, inputStr, false);
            }
        });

        btn_chgpassword_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        img_back_toolbar_talentlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void sendSMS(final String phone,final boolean isFindID){

        Log.d("Login Start", "start");
        /*isValidEmail(jEmail);*/

        //E-mail 주소 패턴 확인
        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(mContext, "핸드폰 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", phone))
        {
            Toast.makeText(getApplicationContext(),"잘못된 휴대폰 번호입니다.",Toast.LENGTH_SHORT).show();
            return;
        }

        final RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();

        final StringRequest sendSMSRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "/Member/findSMS.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.d("FIND LOG", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("result").equals("fail")){
                        Toast.makeText(mContext, "해당 정보로 조회되는 회원이 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject MemInfo = obj.getJSONObject("MemInfo");

                    if(isFindID){
                        certNum_id = obj.getString("certNum");
                        MemID = MemInfo.getString("USER_ID");
                    }else{
                        certNum_pw = obj.getString("certNum");
                        MemPW = MemInfo.getString("PASSWORD");
                    }

                    Toast.makeText(mContext, "휴대폰에서 인증번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap();
                params.put("sRecieveNum", phone);
                if(!isFindID){
                    params.put("UserID", et_id_idpw.getText().toString());
                }
                return params;
            }
        };

        postRequestQueue.add(sendSMSRequest);
    }

    public void confirmPhone(String certNum, String inputStr, boolean isFindID){
        if(certNum == null || certNum.isEmpty()){
            Toast.makeText(mContext, "인증번호를 발급하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(certNum.equals(inputStr)){
            if(isFindID) {
                isCheckCertNum_id = true;
                // TODO 다이얼로그로 교체
                AlertDialog.Builder ad = new AlertDialog.Builder(mContext);
                ad.setTitle("아이디 찾기");
                ad.setMessage("회원님의 아이디는 \n"+ "\"" + MemID + "\"" + " 입니다.");
                ad.show();

//                Toast.makeText(mContext, "회원님의 아이디는 " + MemID + " 입니다.", Toast.LENGTH_SHORT).show();
            }else{
                isCheckCertNum_pw = true;
                Toast.makeText(mContext, "인증이 완료되었습니다. 비밀번호를 변경해주세요.", Toast.LENGTH_SHORT).show();
                rl_chgpasswordcomp_pw.setVisibility(View.VISIBLE);
                rl_chgpassword_pw.setVisibility(View.VISIBLE);
            }
        }else{
            if(isFindID) {
                isCheckCertNum_id = false;
            }else{
                isCheckCertNum_pw = false;
            }
            Toast.makeText(mContext, "인증번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public void changePassword(){

        final String pw = et_chgpassword_pw.getText().toString();
        final String pwComp = et_chgpasswordcomp_pw.getText().toString();

        if(pw.length() < 6){
            Toast.makeText(getApplicationContext(), "비밀번호는 6자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!pw.equals(pwComp)){
            Toast.makeText(getApplicationContext(), "비밀번호 확인과 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        final RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();

        final StringRequest sendSMSRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "/Regist/changePassword.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.d("FIND LOG", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("result").equals("success")){
                        Toast.makeText(mContext, "비밀번호 변경이 완료되었습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(mContext, "비밀번호 변경이 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap();
                params.put("userID", et_id_idpw.getText().toString());
                params.put("userPW", pw);

                return params;
            }
        };

        postRequestQueue.add(sendSMSRequest);
    }
}
