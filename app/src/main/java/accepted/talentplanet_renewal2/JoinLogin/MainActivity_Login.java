package accepted.talentplanet_renewal2.JoinLogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import accepted.talentplanet_renewal2.Home.MainActivity;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.VolleySingleton;


public class MainActivity_Login extends AppCompatActivity {


    Context mContext;

    // 기본 변수 선언
    EditText et_email_login, et_pw_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mContext = getApplicationContext();

        if(getIntent().hasExtra("dupFlag")){
            Toast.makeText(mContext, "다른 기기에서 로그인되어 접속이 종료됩니다.", Toast.LENGTH_SHORT).show();
        }

        final RelativeLayout rl_container;
        rl_container = findViewById(R.id.rl_container_login);
        Glide.with(mContext)
                .load(R.drawable.pic_loginbgr)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new ViewTarget<RelativeLayout, GlideDrawable>(rl_container) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation anim) {
                        RelativeLayout myView = this.view;
                        myView.setBackground(resource);
                    }
                });

        // 기본 변수 정의
        et_email_login = findViewById(R.id.et_email_login);
        et_pw_login = findViewById(R.id.et_pw_login);
        et_pw_login.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        et_pw_login.setTransformationMethod(PasswordTransformationMethod.getInstance());

        ((Button)findViewById(R.id.btn_login_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                //SaveSharedPreference.setPrefUsrId(mContext, et_email_login.getText().toString());
                startActivity(intent);
                loginClicked();
            }
        });

        ((Button)findViewById(R.id.btn_join_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity_Join.class);
                startActivity(intent);
            }
        });

        et_email_login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    SaveSharedPreference.hideKeyboard(v,mContext);
                }
            }
        });

        et_pw_login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    SaveSharedPreference.hideKeyboard(v,mContext);
                }
            }
        });

    }

    public void loginClicked(){

        final String userID = et_email_login.getText().toString();
        final String userPW = et_pw_login.getText().toString();

        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Login/checkLoginInfo.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject obj = new JSONObject(response);
                    String result = obj.getString("result");
                    if(result.equals("success")){
                        String userName = obj.getString("userName");
                        SaveSharedPreference.setPrefUsrName(mContext, userName);
                        SaveSharedPreference.setPrefUsrId(mContext, userID);

                        getMyProfileInfo_new();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getBaseContext(), accepted.talentplanet_renewal2.Home.MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("Activity", true);
                                startActivity(intent);
                            }
                        },500);

                    }else if(result.equals("fail")){
                        Toast.makeText(getApplicationContext(), "비밀번호를 잘못 입력하셨습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap();
                params.put("userID", userID);
                params.put("userPW", userPW);

                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);

    }

    private void getMyProfileInfo_new() {
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "/Profile/getMyProfileInfo_new.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject obj = new JSONObject(response);
                    SaveSharedPreference.setPrefGender(mContext, obj.getString("GENDER"));
                    SaveSharedPreference.setPrefUserBirth(mContext, obj.getString("USER_BIRTH"));
                    SaveSharedPreference.setPrefUserGpLng(mContext, obj.getString("GP_LNG"));
                    SaveSharedPreference.setPrefUserGpLat(mContext, obj.getString("GP_LAT"));
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap();
                params.put("userID", SaveSharedPreference.getUserId(mContext));
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }


}


