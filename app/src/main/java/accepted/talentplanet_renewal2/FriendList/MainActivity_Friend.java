package accepted.talentplanet_renewal2.FriendList;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.VolleySingleton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.WHITE;

public class MainActivity_Friend extends AppCompatActivity {

    Context mContext;
    private ListView friendList;
    private ArrayList<ItemData_Friend> oData;
    private ArrayList<String> removeFriendList;
    private StringBuilder strRemoveFriendList;
    ListAdapter_Friend oAdapter;
    private String localMode;

    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendlist_activity);

        mContext = getApplicationContext();
        ((TextView) findViewById(R.id.tv_toolbar_talentlist)).setText("친구 목록");
        ((ImageView) findViewById(R.id.iv_toolbar_search_talentlist)).setVisibility(View.GONE);

        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));

        int nDatCnt = 0;

        localMode = SaveSharedPreference.getPrefTalentFlag(mContext);
        chengeMode(localMode);

        // 뒤로가기 이벤트
        ((ImageView) findViewById(R.id.img_back_toolbar_talentlist)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getFriendList();

        ((Button)findViewById(R.id.btn_teahcer_friendlist)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localMode = "Y";
                chengeMode(localMode);
                getFriendList();
            }
        });

        ((Button)findViewById(R.id.btn_student_friendlist)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localMode = "N";
                chengeMode(localMode);
                getFriendList();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        chengeMode(localMode);
        getFriendList();
    }

    public void chengeMode(String flag) {

        if (flag.equals("Y")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
            }

            ((ImageView)findViewById(R.id.img_back_toolbar_talentlist)).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
            ((TextView)findViewById(R.id.tv_toolbar_talentlist)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
            ((RelativeLayout)findViewById(R.id.rl_talentcate_friendlist)).setBackgroundResource(R.color.color_mentor);
            ((Button)findViewById(R.id.btn_teahcer_friendlist)).setBackgroundResource(R.drawable.bgr_addtalent_leftbtn_clicekd);
            ((Button)findViewById(R.id.btn_student_friendlist)).setBackgroundResource(R.drawable.bgr_addtalent_rightbtn_unclicekd);
            ((Button)findViewById(R.id.btn_teahcer_friendlist)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
            ((Button)findViewById(R.id.btn_student_friendlist)).setTextColor(WHITE);
        } else if (flag.equals("N")) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
            }

            ((ImageView)findViewById(R.id.img_back_toolbar_talentlist)).setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
            ((TextView)findViewById(R.id.tv_toolbar_talentlist)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
            ((RelativeLayout)findViewById(R.id.rl_talentcate_friendlist)).setBackgroundResource(R.color.color_mentee);
            ((Button)findViewById(R.id.btn_student_friendlist)).setBackgroundResource(R.drawable.bgr_addtalent_rightbtn_clicekd);
            ((Button)findViewById(R.id.btn_teahcer_friendlist)).setBackgroundResource(R.drawable.bgr_addtalent_leftbtn_unclicekd);
            ((Button)findViewById(R.id.btn_student_friendlist)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
            ((Button)findViewById(R.id.btn_teahcer_friendlist)).setTextColor(WHITE);
        }
    }

        // 친구삭제 이벤트
//        findViewById(R.id.img_rightbtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                removeFriendList = new ArrayList<String>();
//                // 기본 적 UI 숨기기
//                ((ImageView) findViewById(R.id.img_open_dl)).setVisibility(View.GONE);
//
//                // 삭제 전용 UI 보여주기
//                ((TextView)findViewById(R.id.tv_remove)).setVisibility(View.VISIBLE);
//                ((TextView)findViewById(R.id.tv_Choose)).setVisibility(View.VISIBLE);
//
//                ((TextView)findViewById(R.id.tv_Choose)).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
//
//                ((TextView)findViewById(R.id.tv_remove)).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        strRemoveFriendList = new StringBuilder();
//                        for(int i = 0; i < removeFriendList.size(); i++) {
//                            if(i == 0)
//                                strRemoveFriendList.append(removeFriendList.get(i));
//                            else
//                                strRemoveFriendList.append(",").append(removeFriendList.get(i));
//                        }
//                        removeFriend();
//                    }
//                });
//
//                friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                        removeFriendList.add(oData.get(position).getStrUserID());
//                    }
//                });
//            }
//        });
//
//        getFriendList();
//    }

    public void getFriendList() {
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "FriendList/getFriendList_new.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    oData = new ArrayList<>();
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        String mode = localMode;
                        String isMentor = obj.getString("PARTNER_TALENT_FLAG");

                        // 친구리스트에서 모드에 따른 구분
                        if (!mode.equals(isMentor)) {
                            ItemData_Friend oItem = new ItemData_Friend();
                            oItem.setStrUserID(obj.getString("USER_ID"));
                            oItem.setStrUserName(obj.getString("USER_NAME"));
                            oItem.setStrUserGender(obj.getString("GENDER"));
                            oItem.setStrUserInfo(obj.getString("USER_BIRTH"));
                            oItem.setStrIsMentor(isMentor);
                            oItem.setGP_LAT(obj.getString("GP_LAT"));
                            oItem.setGP_LNG(obj.getString("GP_LNG"));
                            oItem.setS_FILE_PATH(obj.getString("S_FILE_PATH"));
                            oItem.setBIRTH_FLAG(obj.getString("BIRTH_FLAG"));
                            oData.add(oItem);
                        }
                    }

                    // ListView, Adapter 생성 및 연결 ------------------------
                    friendList = (ListView) findViewById(R.id.lv_friend);
                    oAdapter = new ListAdapter_Friend(oData);

                    friendList.setAdapter(oAdapter);

                    friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(MainActivity_Friend.this, accepted.talentplanet_renewal2.Profile.MainActivity_Profile.class);
                            intent.putExtra("userID", oData.get(position).getStrUserID());
                            intent.putExtra("userName", oData.get(position).getStrUserName());
                            String isMentor = oData.get(position).getStrIsMentor();
                            if (isMentor.equals("Y")) {
                                intent.putExtra("isMentor", true);
                            }
                            intent.putExtra("fromFriend", true);
                            startActivity(intent);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("userID", SaveSharedPreference.getUserId(mContext));
                return params;
            }
        };
        postRequestQueue.add(postJsonRequest);
    }

//    public void removeFriend(){
//        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
//        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "FriendList/updateFriendList_new.do", new Response.Listener<String>(){
//            @Override
//            public void onResponse(String response){
//                try {
//                    JSONObject obj = new JSONObject(response);
//                    if(obj.getString("result").equals("success")){
//                        Toast.makeText(mContext, "친구 삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
//
//                        for(int j = 0; j < oData.size(); j++) {
//                            ItemData_Friend item =oData.get(j);
//                            for (int i = 0; i < removeFriendList.size(); i++) {
//                                if(item.getStrUserID().equals(removeFriendList.get(i))){
//                                    oData.remove(j);
//                                }
//                            }
//                        }
//
//                        oAdapter.notifyDataSetChanged();
//
//                        ((TextView)findViewById(R.id.tv_remove)).setVisibility(View.GONE);
//                        ((TextView)findViewById(R.id.tv_Choose)).setVisibility(View.GONE);
//
//                        // 삭제 전용 UI 보여주기
//                        ((ImageView) findViewById(R.id.img_rightbtn)).setVisibility(View.VISIBLE);
//                        ((ImageView) findViewById(R.id.img_open_dl)).setVisibility(View.VISIBLE);
//                    }
//
//                }
//                catch(JSONException e){
//                    e.printStackTrace();
//                }
//            }
//        }, SaveSharedPreference.getErrorListener(mContext)) {
//            @Override
//            protected Map<String, String> getParams(){
//                Map<String, String> params = new HashMap();
//                params.put("userID", SaveSharedPreference.getUserId(mContext));
//                params.put("friendID", strRemoveFriendList.toString());
//                params.put("updateFlag", "R");
//                return params;
//            }
//        };
//
//        postRequestQueue.add(postJsonRequest);
//    }


}
