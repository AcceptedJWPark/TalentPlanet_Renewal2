package com.accepted.acceptedtalentplanet.Loading;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.accepted.acceptedtalentplanet.JoinLogin.MainActivity_Login;
import com.accepted.acceptedtalentplanet.MarketVersionChecker;
import com.accepted.acceptedtalentplanet.R;
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


public class MainActivity_Loading extends Activity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.loading);

        mContext = getApplicationContext();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String lastID = SaveSharedPreference.getUserId(mContext);
                String lastPW = SaveSharedPreference.getPrefUserPw(mContext);
                if (!lastID.equals("") && !lastPW.equals("")) {
                    getMyProfileInfo_new();
                } else {
                    Intent intent = new Intent(getBaseContext(), MainActivity_Login.class);
                    startActivity(intent);
                }
                finish();
            }
        },3000);
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
                    if(obj.has("PROFILE_DESCRIPTION")) {
                        SaveSharedPreference.setPrefUserDescription(mContext, obj.getString("PROFILE_DESCRIPTION"));
                    }
                    SaveSharedPreference.setPrefTalentPoint(mContext, obj.getInt("TALENT_POINT"));
                    SaveSharedPreference.setMyPicturePath(obj.getString("FILE_PATH"), obj.getString("S_FILE_PATH"));
                    SaveSharedPreference.setPrefUserBirthFlag(mContext, obj.getString("BIRTH_FLAG"));

                    if (obj.has("GP_LNG") && obj.has("GP_LAT")) {
                        if(obj.getString("GP_LNG").isEmpty()){
                            SaveSharedPreference.setPrefUserGpLng(mContext,"0");
                            SaveSharedPreference.setPrefUserGpLat(mContext, "0");
                        }else{
                            SaveSharedPreference.setPrefUserGpLng(mContext, obj.getString("GP_LNG"));
                            SaveSharedPreference.setPrefUserGpLat(mContext, obj.getString("GP_LAT"));
                        }
                    }else{
                        SaveSharedPreference.setPrefUserGpLng(mContext,"0");
                        SaveSharedPreference.setPrefUserGpLat(mContext, "0");
                    }

                    if (obj.has("SumScore") && obj.has("CountScore")) {
                        int sumScore = Integer.parseInt((String) obj.getString("SumScore"));
                        int countScore = Integer.parseInt((String) obj.getString("CountScore"));

                        int avgScore =  (sumScore + 9) / (countScore + 1);

                        SaveSharedPreference.setPrefUserScore(mContext, String.valueOf(avgScore));
                    }

                    Intent intent = new Intent(getBaseContext(), com.accepted.acceptedtalentplanet.Home.MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Activity", true);
                    SaveSharedPreference.setPrefTalentFlag(mContext, "Y");
                    startActivity(intent);

                } catch(JSONException e){
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
