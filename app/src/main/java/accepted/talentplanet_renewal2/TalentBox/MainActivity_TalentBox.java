package accepted.talentplanet_renewal2.TalentBox;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.TalentList.ListAdapter_TalentList;
import accepted.talentplanet_renewal2.TalentList.MainActivity_TalentList;
import accepted.talentplanet_renewal2.TalentList.UserData_TalentList;

public class MainActivity_TalentBox extends AppCompatActivity {

    private Context mContext;

    private ListView userTalentListView;
    private TextView title;
    private ImageView leftBtn;
    private ImageView img3x5;
    private ImageView rightBtn;

    // 탤런트박스 요청 구분을 위한 변수
    private String requestType;
    // 로그인되어있는 유저
    private String userID;

    private ArrayList<TalentBoxObject_TalentBox> sendList;
    private ArrayList<String> reciveList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__talent_box);

        mContext = getApplicationContext();

        userID = SaveSharedPreference.getUserId(mContext);

        // Intent
        Intent intent= getIntent();
        requestType = intent.getStringExtra("requestType");

        // 뷰 정의
        title = (TextView)findViewById(R.id.tv_toolbar);
        userTalentListView = (ListView)findViewById(R.id.lv_talentBox);
        leftBtn = (ImageView)findViewById(R.id.img_open_dl);
        img3x5 = (ImageView)findViewById(R.id.img_show3x5);
        rightBtn = (ImageView)findViewById(R.id.img_show1x15);

        img3x5.setVisibility(View.GONE);
        rightBtn.setVisibility(View.GONE);
        title.setText("탤런트 박스");
        leftBtn.setImageResource(R.drawable.icon_back);

        if (requestType.equals("send")) {
            // 보낸 프로필 리스트
            getSendList();
        } else if (requestType.equals("recive")) {
            // 받은 프로필 리스트
            getReciveList();
        }

        // 뒤로가기 이벤트
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // 보낸 리스트 전용 함수
    private void getSendList() {
        RequestQueue postRequestQueue = Volley.newRequestQueue(mContext);
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "/TalentSharing/getSendList.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sendList = new ArrayList<TalentBoxObject_TalentBox>();
                try {
                    JSONArray array = new JSONArray(response);
                    for(int i = 0; i < array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
//                        SimpleDateFormat sdf = new SimpleDateFormat("YYYY");

                        TalentBoxObject_TalentBox aUser = new TalentBoxObject_TalentBox();

                        aUser.setRequestType(requestType);
                        aUser.setCode(obj.getString("code"));
                        aUser.setRECEIVER_ID(obj.getString("RECEIVER_ID"));
                        aUser.setSENDER_ID(obj.getString("SENDER_ID"));

                        sendList.add(aUser);
                    }
                    ListAdapter_TalentBox oAdapter = new ListAdapter_TalentBox(mContext, sendList);
                    userTalentListView.setAdapter(oAdapter);

                    userTalentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(MainActivity_TalentBox.this, accepted.talentplanet_renewal2.Profile.MainActivity_Profile.class);

                            intent.putExtra("userID", sendList.get(position).getRECEIVER_ID());
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
                params.put("SENDER_ID", userID);
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }

    // 받은 리스트 전용 함수
    private void getReciveList() {
        RequestQueue postRequestQueue = Volley.newRequestQueue(mContext);
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "/TalentSharing/getReciveList.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("getReciveList", response);
                sendList = new ArrayList<TalentBoxObject_TalentBox>();
                try {
                    JSONArray array = new JSONArray(response);
                    for(int i = 0; i < array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
//                        SimpleDateFormat sdf = new SimpleDateFormat("YYYY");

                        TalentBoxObject_TalentBox aUser = new TalentBoxObject_TalentBox();
                        aUser.setRequestType(requestType);
                        aUser.setMentorID(obj.getString("MentorID"));

                        sendList.add(aUser);
                    }
                    ListAdapter_TalentBox oAdapter = new ListAdapter_TalentBox(mContext, sendList);
                    userTalentListView.setAdapter(oAdapter);

                    userTalentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(MainActivity_TalentBox.this, accepted.talentplanet_renewal2.Profile.MainActivity_Profile.class);

                            intent.putExtra("userID", sendList.get(position).getMentorID());
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
                params.put("MenteeID", userID);
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }
}
