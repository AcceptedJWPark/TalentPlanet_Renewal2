package accepted.talentplanet_renewal2.JoinLogin;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.VolleySingleton;

public class MainActivity_Find extends AppCompatActivity {

    private Context mContext;
    private String certNum_id, certNum_pw;
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
                Log.d("asdasaa", phone);
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

    }

    public void sendSMS(final String phone,final boolean isFindID){

        Log.d("Login Start", "start");
        /*isValidEmail(jEmail);*/

        //E-mail 주소 패턴 확인
        if(TextUtils.isEmpty(phone))
        {
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
                Toast.makeText(mContext, "회원님의 아이디는 " + MemID + " 입니다.", Toast.LENGTH_SHORT).show();
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
