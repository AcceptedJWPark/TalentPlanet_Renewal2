package accepted.talentplanet_renewal2.JoinLogin;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.VolleySingleton;


public class MainActivity_Join extends AppCompatActivity {

    
    Context mContext;

    // 인증 관련
    boolean malecheck;
    boolean gendercheck = false;
    boolean phonecheck = false;
    
    // 기본 View ID 생성
    TextView tv_toolbar;
    LinearLayout ll_malecheck;
    LinearLayout ll_femalecheck;

    ImageView img_malecheck;
    ImageView img_femalecheck;
    
    EditText et_email_join;
    EditText et_pw_join, et_pwcheck_join;
    EditText et_name_join, et_phone_join, et_admit_join, et_birthdate_join;
    CheckBox cb_hide_birth, cb_hide_gender;
    CheckBox cb_agree_service, cb_agree_privacy;
    Button btn_submit_join, btn_emailcheck_join, btn_sendsms_join, btn_chkcertnum_join;
    
    // 메일 중복 체크 여부
    boolean isconfirmEmailCheck = false;

    // 핸드폰 인증 관련 변수
    String certNum;
    boolean isCheckCertNum = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        malecheck=false;
        setContentView(R.layout.activity_join);

        // 기본 변수 정의
        et_email_join = findViewById(R.id.et_email_join);
        et_pw_join = findViewById(R.id.et_pw_join);
        et_pwcheck_join = findViewById(R.id.et_pwcheck_join);
        et_name_join = findViewById(R.id.et_name_join);
        et_phone_join = findViewById(R.id.et_phone_join);
        et_admit_join = findViewById(R.id.et_admit_join);
        et_birthdate_join = findViewById(R.id.et_birthdate_join);
        cb_hide_birth = findViewById(R.id.cb_hide_birth);
        cb_hide_gender = findViewById(R.id.cb_hide_gender);
        cb_agree_service = findViewById(R.id.cb_agree_service);
        cb_agree_privacy = findViewById(R.id.cb_agree_privacy);
        btn_submit_join = findViewById(R.id.btn_submit_join);
        btn_emailcheck_join = findViewById(R.id.btn_emailcheck_join);
        btn_sendsms_join = findViewById(R.id.btn_sendsms_join);
        btn_chkcertnum_join = findViewById(R.id.btn_chkcertnum_join);
        
        img_malecheck = findViewById(R.id.img_malecheck_join);
        img_femalecheck = findViewById(R.id.img_femalecheck_join);
        ll_malecheck = findViewById(R.id.ll_malecheck_join);
        ll_femalecheck = findViewById(R.id.ll_femalecheck_join);

        tv_toolbar=findViewById(R.id.tv_toolbar);
        tv_toolbar.setText("회원가입");
        ((ImageView)findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);
        findViewById(R.id.img_show1x15).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        ll_malecheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                malecheck = true;
                checkGender("male");
            }
        });

        ll_femalecheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                malecheck = false;
                checkGender("female");
            }
        });

        et_email_join.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isconfirmEmailCheck = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_phone_join.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isCheckCertNum = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_submit_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidate()){
                    goRegist();
                }
            }
        });

        btn_emailcheck_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailCheck();
            }
        });

        btn_sendsms_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS();
            }
        });

        btn_chkcertnum_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPhone();
            }
        });

    }

    public void checkGender(String gender)
    {
        gendercheck = true;
        if (gender.equals("male"))
        {
            img_malecheck.setImageResource(R.drawable.icon_check1on);
            img_femalecheck.setImageResource(R.drawable.icon_check1off);
        }
        else
        {
            img_malecheck.setImageResource(R.drawable.icon_check1off);
            img_femalecheck.setImageResource(R.drawable.icon_check1on);
        }
    }

    private boolean checkValidate(){
        if(!isconfirmEmailCheck){
            Toast.makeText(getApplicationContext(), "아이디 중복확인을 해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(et_pw_join.getText().length() < 6){
            Toast.makeText(getApplicationContext(), "비밀번호는 6자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!et_pw_join.getText().toString().equals(et_pwcheck_join.getText().toString())){
            Toast.makeText(getApplicationContext(), "비밀번호 확인과 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(et_name_join.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!phonecheck){
            Toast.makeText(getApplicationContext(), "휴대폰 인증을 해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(et_birthdate_join.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!gendercheck){
            Toast.makeText(getApplicationContext(), "성별을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!cb_agree_service.isChecked()){
            Toast.makeText(getApplicationContext(), "서비스 이용약관에 동의해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!cb_agree_privacy.isChecked()){
            Toast.makeText(getApplicationContext(), "개인정보 취급방침에 동의해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void emailCheck(){

        final String jEmail = et_email_join.getText().toString();

        //E-mail 주소 패턴 확인
        if(TextUtils.isEmpty(jEmail))
        {
            return;
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(jEmail).matches())
        {
            Toast.makeText(getApplicationContext(),"잘못된 E-mail 주소입니다.",Toast.LENGTH_SHORT).show();
            return;
        }

        final RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();


        StringRequest emailDupCheckReq = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Regist/checkDupID.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject obj = new JSONObject(response);
                    String result = obj.getString("result");
                    if(result.equals("duplicated")){
                        Toast.makeText(getApplicationContext(), "이미 가입된 E-Mail 입니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "사용가능한 E-Mail 입니다.", Toast.LENGTH_SHORT).show();
                    isconfirmEmailCheck = true;


                }
                catch(JSONException e){
                    e.printStackTrace();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        },SaveSharedPreference.getErrorListener(mContext)
        ) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap();
                params.put("userID", jEmail);
                return params;
            }
        };

        postRequestQueue.add(emailDupCheckReq);
    }

    public void sendSMS(){

        Log.d("Login Start", "start");
        final String phone = et_phone_join.getText().toString().replaceAll("-","");
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

        final StringRequest sendSMSRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "/Member/sendJoinSMS.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject obj = new JSONObject(response);
                    certNum = obj.getString("certNum");
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

                return params;
            }
        };

        postRequestQueue.add(sendSMSRequest);
    }

    public void confirmPhone(){
        if(certNum == null || certNum.isEmpty()){
            Toast.makeText(mContext, "인증번호를 발급하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(certNum.equals(et_admit_join.getText().toString())){
            isCheckCertNum = true;
            Toast.makeText(mContext, "휴대폰 인증이 완료되었습니다.", Toast.LENGTH_SHORT).show();
        }else{
            isCheckCertNum = false;
            Toast.makeText(mContext, "인증번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public void goRegist() {
        String birthYearTxt, birthMonthTxt, birthDayTxt;
        try {
            birthYearTxt = et_birthdate_join.getText().toString().substring(0, 1);
            birthMonthTxt = et_birthdate_join.getText().toString().substring(2, 3);
            birthDayTxt = et_birthdate_join.getText().toString().substring(4, 5);

            if (birthYearTxt == null || birthYearTxt.isEmpty() || birthMonthTxt == null || birthMonthTxt.isEmpty() || birthDayTxt == null || birthDayTxt.isEmpty()) {
                Toast.makeText(mContext, "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            Pattern pattern = Pattern.compile("^((19|20)[0-9]{2})");
            Matcher matcher = pattern.matcher(birthYearTxt);
            if(!matcher.find()){
                Toast.makeText(mContext, "년도 형식을 확인해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }
        }catch (IndexOutOfBoundsException e){
            Toast.makeText(mContext, "생년월일을 확인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }



        try{
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            df.setLenient(false);
            df.parse(birthYearTxt + birthMonthTxt + birthDayTxt);
        }catch (Exception e){
            Toast.makeText(mContext, "생년월일을 확인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Integer.parseInt(birthMonthTxt) < 10)
            birthMonthTxt = "0"+birthMonthTxt.substring(birthMonthTxt.length() - 1);
        if(Integer.parseInt(birthDayTxt) < 10)
            birthDayTxt = "0"+birthDayTxt.substring(birthDayTxt.length() - 1);


        final String birth = birthYearTxt + birthMonthTxt + birthDayTxt;


        if (birthYearTxt.length() == 0 || birthMonthTxt.length() == 0 || birthDayTxt.length() == 0) {
            Toast.makeText(mContext, "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Log.d("Login Start", "start");

            RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
            StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Regist/goRegist.do", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        String result = obj.getString("result");
                        if (result.equals("success")) {
                            Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }

                        Intent intent = new Intent(getBaseContext(), accepted.talentplanet_renewal2.JoinLogin.MainActivity_Login.class);
                        startActivity(intent);
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, SaveSharedPreference.getErrorListener(mContext)) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap();
                    params.put("userID", et_email_join.getText().toString());
                    params.put("userPW", et_pw_join.getText().toString());
                    params.put("userName", et_name_join.getText().toString());
                    params.put("userGender", malecheck ? "남" : "여");
                    params.put("userBirth", birth);
                    params.put("phone", et_phone_join.getText().toString());
                    params.put("genderFlag", (!cb_hide_gender.isChecked())?"Y":"N");
                    params.put("birthFlag", (!cb_hide_birth.isChecked())?"Y":"N");
                    return params;
                }
            };

            postRequestQueue.add(postJsonRequest);
        }
    }
}
