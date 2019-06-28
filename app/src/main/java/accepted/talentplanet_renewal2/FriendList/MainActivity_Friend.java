package accepted.talentplanet_renewal2.FriendList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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

public class MainActivity_Friend extends AppCompatActivity {

    Context mContext;
    private ListView friendList;
    private  ArrayList<ItemData_Friend> oData;
    private ArrayList<String> removeFriendList;
    private StringBuilder strRemoveFriendList;
    ListAdapter_Friend oAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        mContext = getApplicationContext();
        ((TextView)findViewById(R.id.tv_toolbar)).setText("친구 목록");
        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);
        ((ImageView) findViewById(R.id.img_show1x15)).setImageResource(R.drawable.icon_trash_btn);
//        findViewById(R.id.img_show1x15).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        int nDatCnt=0;


        // 뒤로가기 이벤트
        findViewById(R.id.img_open_dl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 친구삭제 이벤트
        findViewById(R.id.img_show1x15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFriendList = new ArrayList<String>();
                // 기본 적 UI 숨기기
                ((ImageView) findViewById(R.id.img_show1x15)).setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.img_open_dl)).setVisibility(View.GONE);

                // 삭제 전용 UI 보여주기
                ((TextView)findViewById(R.id.tv_remove)).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.tv_Choose)).setVisibility(View.VISIBLE);

                ((TextView)findViewById(R.id.tv_Choose)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                ((TextView)findViewById(R.id.tv_remove)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        strRemoveFriendList = new StringBuilder();
                        for(int i = 0; i < removeFriendList.size(); i++) {
                            if(i == 0)
                                strRemoveFriendList.append(removeFriendList.get(i));
                            else
                                strRemoveFriendList.append(",").append(removeFriendList.get(i));
                        }
                        removeFriend();
                    }
                });

                friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        removeFriendList.add(oData.get(position).getStrUserID());
                    }
                });
            }
        });

        getFriendList();
    }

    public void getFriendList() {
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "FriendList/getFriendList_new.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    oData = new ArrayList<>();
                    JSONArray array = new JSONArray(response);
                    for(int i = 0; i < array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);

                        SimpleDateFormat sdf = new SimpleDateFormat("YYYY");

                        ItemData_Friend oItem = new ItemData_Friend();
                        oItem.setStrUserName(obj.getString("USER_NAME"));
                        String Gender = obj.getString("GENDER").equals("남") ? "남성" : "여성";
                        int Age = Integer.parseInt(sdf.format(new Date())) - Integer.parseInt(obj.getString("USER_BIRTH").split("-")[0]) + 1;

                        oItem.setStrUserInfo(Gender + " / " + Age + "세");
                        oItem.setStrUserID(obj.getString("USER_ID"));
                        oData.add(oItem);
                    }

                    // ListView, Adapter 생성 및 연결 ------------------------
                    friendList = (ListView)findViewById(R.id.lv_friend);
                    oAdapter = new ListAdapter_Friend(oData);

                    friendList.setAdapter(oAdapter);

                    friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // 토스트 테스트 성공
                            //Toast.makeText(MainActivity_Friend.this , oData.get(position).strUserName,Toast.LENGTH_SHORT).show();
                            // 테스트 데이터 전송
                            Intent intent = new Intent(MainActivity_Friend.this, accepted.talentplanet_renewal2.Profile.MainActivity_Profile.class);

                            String userInfo = oData.get(position).strUserInfo;
                            String[] temp = userInfo.split(" / ");
                            intent.putExtra("userName", oData.get(position).strUserName);
                            intent.putExtra("userInfo", temp[1]);
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

    public void removeFriend(){
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "FriendList/updateFriendList_new.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("result").equals("success")){
                        Toast.makeText(mContext, "친구 삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                        for(int j = 0; j < oData.size(); j++) {
                            ItemData_Friend item =oData.get(j);
                            for (int i = 0; i < removeFriendList.size(); i++) {
                                if(item.getStrUserID().equals(removeFriendList.get(i))){
                                    oData.remove(j);
                                }
                            }
                        }

                        oAdapter.notifyDataSetChanged();

                        ((TextView)findViewById(R.id.tv_remove)).setVisibility(View.GONE);
                        ((TextView)findViewById(R.id.tv_Choose)).setVisibility(View.GONE);

                        // 삭제 전용 UI 보여주기
                        ((ImageView) findViewById(R.id.img_show1x15)).setVisibility(View.VISIBLE);
                        ((ImageView) findViewById(R.id.img_open_dl)).setVisibility(View.VISIBLE);
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
                params.put("userID", SaveSharedPreference.getUserId(mContext));
                params.put("friendID", strRemoveFriendList.toString());
                params.put("updateFlag", "R");
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }

}