package accepted.talentplanet_renewal2.TalentList;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import accepted.talentplanet_renewal2.Classes.TalentObject_Home;
import accepted.talentplanet_renewal2.Home.SpinnerAdapter_Toolbar;
import accepted.talentplanet_renewal2.Home.SpinnerData_Toolbar;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;

public class MainActivity_TalentList extends AppCompatActivity {

    private Map<String, TalentObject_Home> talentMap;
    private ArrayList<TalentObject_Home> talentList;
    private ArrayList<UserData_TalentList> userList;
    private Context mContext;
    private String cateCode;
    private String mode;
    private String talentFlag;
    private String titleTxt;
    private boolean hasFlag;
    private String hashtag;


    private ArrayList<Sample_UserData_TalentList> sampleUserlist;
    private Sample_ListAdapter_TalentList listAdapter_talentList;

    // 뷰 정의
    ListView userListView;
    TextView title;
    ImageView leftBtn;
    LinearLayout hsv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talentlist_activity);

        mContext = getApplicationContext();

        mode = SaveSharedPreference.getPrefTalentFlag(mContext);

        talentFlag = mode;

        if (mode.equals("Y")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
            }

            RelativeLayout ll_bar_talentlist = findViewById(R.id.ll_bar_talentlist);
            TextView tv_listhaed_talentlist = findViewById(R.id.tv_listhaed_talentlist);

            ll_bar_talentlist.setBackgroundColor(getResources().getColor(R.color.color_mentee));
            tv_listhaed_talentlist.setText("Teacher List");

        }

        // 뷰 정의
        userListView = (ListView)findViewById(R.id.lv_talentUser);
        title = (TextView)findViewById(R.id.tv_toolbar);
        leftBtn = (ImageView)findViewById(R.id.img_open_dl);

        // 07-18
        sampleUserlist = new ArrayList<Sample_UserData_TalentList>();
        sampleUserlist.add(new Sample_UserData_TalentList("박종우","남성","1991.01.23","#헬스 #운동 #다이어트","17km"));
        sampleUserlist.add(new Sample_UserData_TalentList("민권홍","남성","비공개","#근력운동 #다이어트 #PT","21km"));
        sampleUserlist.add(new Sample_UserData_TalentList("김지영","여성","1998.04.12","#크로스핏 #요가 #Personal Trainning","45km"));
        sampleUserlist.add(new Sample_UserData_TalentList("조현배","남성","비공개","#스트렝스 #근력짱 #파워리프터","67km"));
        sampleUserlist.add(new Sample_UserData_TalentList("문건우","남성","1995.11.22","#다이어트 #운동 #스트렝스","170km"));

        listAdapter_talentList = new Sample_ListAdapter_TalentList(mContext,sampleUserlist);

        userListView.setAdapter(listAdapter_talentList);

        // 인텐트 받기
        Intent intent = getIntent();

        // 인텐트 엑스트라 받기
//        titleTxt = intent.getStringExtra("talentName");
        cateCode = intent.getStringExtra("cateCode");
        Log.d("firstCateCode", cateCode);
//        talentFlag = SaveSharedPreference.getPrefTalentFlag(mContext);
//        hasFlag = intent.getBooleanExtra("hasFlag", false);
//        if(hasFlag) {
//            if(intent.hasExtra("hashtag")) {
//                hashtag = intent.getStringExtra("hashtag");
//            }
//            ((LinearLayout)findViewById(R.id.ll_addTalent_list)).setVisibility(View.GONE);
//        }else{
//        }
//        // 필요정보 생성
//        makeTestTalentArr();
//        if(intent.hasExtra("isSearch")){
//            // 뷰 정보 인텐트 정보로 변경
//            title.setText("#" + titleTxt);
//            searchTalentListByHashtag();
//        }else {
//            // 뷰 정보 인텐트 정보로 변경
//            title.setText(titleTxt);
//            getTalentListNew();
//        }

        getTalentListNew();
        // 뒤로가기 이벤트
        leftBtn.setImageDrawable(getResources().getDrawable(R.drawable.icon_back));

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void makeTestTalentArr() {
        talentMap = new HashMap();

        TalentObject_Home career = new TalentObject_Home("취업", R.drawable.pic_career,R.drawable.icon_career, 0,"");
        TalentObject_Home study = new TalentObject_Home("학습", R.drawable.pic_study,R.drawable.icon_study, 0,"");
        TalentObject_Home money = new TalentObject_Home("재테크", R.drawable.pic_money,R.drawable.icon_money, 0,"");
        TalentObject_Home it = new TalentObject_Home("IT", R.drawable.pic_it,R.drawable.icon_it, 0,"");
        TalentObject_Home camera = new TalentObject_Home("사진", R.drawable.pic_camera,R.drawable.icon_camera, 0,"");
        TalentObject_Home music = new TalentObject_Home("음악", R.drawable.pic_music,R.drawable.icon_music, 0,"");
        TalentObject_Home design = new TalentObject_Home("미술/디자인", R.drawable.pic_design,R.drawable.icon_design, 0,"");
        TalentObject_Home sports = new TalentObject_Home("운동", R.drawable.pic_sports,R.drawable.icon_sports, 0,"");
        TalentObject_Home living = new TalentObject_Home("생활", R.drawable.pic_living,R.drawable.icon_living, 0,"");
        TalentObject_Home beauty = new TalentObject_Home("뷰티/패션", R.drawable.pic_beauty,R.drawable.icon_beauty, 0,"");
        TalentObject_Home volunteer = new TalentObject_Home("사회봉사", R.drawable.pic_volunteer,R.drawable.icon_volunteer, 0,"");
        TalentObject_Home travel = new TalentObject_Home("여행", R.drawable.pic_travel,R.drawable.icon_travel, 0,"");
        TalentObject_Home culture = new TalentObject_Home("문화", R.drawable.pic_culture,R.drawable.icon_culture, 0,"");
        TalentObject_Home game = new TalentObject_Home("게임", R.drawable.pic_game,R.drawable.icon_game, 0,"");

        talentMap.put(career.getTitle(), career);
        talentMap.put(study.getTitle(), study);
        talentMap.put(money.getTitle(), money);
        talentMap.put(it.getTitle(), it);
        talentMap.put(camera.getTitle(), camera);
        talentMap.put(music.getTitle(), music);
        talentMap.put(design.getTitle(), design);
        talentMap.put(sports.getTitle(), sports);
        talentMap.put(living.getTitle(), living);
        talentMap.put(beauty.getTitle(), beauty);
        talentMap.put(volunteer.getTitle(), volunteer);
        talentMap.put(travel.getTitle(), travel);
        talentMap.put(culture.getTitle(), culture);
        talentMap.put(game.getTitle(), game);

//        long seed = System.nanoTime();
//        Collections.shuffle(arrTalent, new Random(seed));
    }


    private void getTalentListNew(){
        talentList = new ArrayList<>();
        RequestQueue postRequestQueue = Volley.newRequestQueue(mContext);
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "TalentSharing/getTalentListNew.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responseTest", response);
                try {
                    userList = new ArrayList<UserData_TalentList>();
                    JSONArray array = new JSONArray(response);
                    for(int i = 0; i < array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
                        SimpleDateFormat sdf = new SimpleDateFormat("YYYY");

                        UserData_TalentList aUser = new UserData_TalentList();
                        aUser.setUserName(obj.getString("USER_NAME"));
                        aUser.setUserGender(obj.getString("GENDER"));
                        aUser.setHashtag(obj.has("HASHTAG") ? obj.getString("HASHTAG") : "");
                        aUser.setUserAge(Integer.parseInt(sdf.format(new Date())) - Integer.parseInt(obj.getString("USER_BIRTH").split("-")[0]) + 1 + "");
                        aUser.setUserID(obj.getString("UserID"));

                        userList.add(aUser);
                    }

                    for (int i=0;i<talentList.size();i++) {
                        TextView tv = new TextView(getApplicationContext());
                        tv.setText(talentList.get(i).getTitle());
                        Log.d(this.getClass().getName(), titleTxt.toString() + ":" +talentList.get(i).getTitle().toString());
                        if (titleTxt.toString().equals(talentList.get(i).getTitle().toString())) {
                            tv.setTextColor(getResources().getColor(R.color.bgr_mainColor));
                        }
                        tv.setPadding(10,10,10,10);
                        hsv.addView(tv);
                    }
                    // 리스트 뷰
                    ListAdapter_TalentList oAdapter = new ListAdapter_TalentList(mContext, userList);
                    userListView.setAdapter(oAdapter);

                    // 리스트 뷰 프로필 이동 이벤트
                    userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(MainActivity_TalentList.this, accepted.talentplanet_renewal2.Profile.MainActivity_Profile.class);

                            String userInfo = userList.get(position).getUserGender() + " / " + userList.get(position).getUserAge() + "세";

                            intent.putExtra("userName", userList.get(position).getUserName());
                            intent.putExtra("userInfo", userInfo);
                            intent.putExtra("targetUserID", userList.get(position).getUserID());
                            intent.putExtra("userID", userList.get(position).getUserID());
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
                String flag = "";
                if (talentFlag.equals("Y")) {
                    flag = "N";
                } else if (talentFlag.equals("N")) {
                    flag = "Y";
                }
                Map<String, String> params = new HashMap();
                params.put("TalentFlag", flag);
                params.put("CateCode", cateCode);
                params.put("UserID", SaveSharedPreference.getUserId(mContext));
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }

    private void searchTalentListByHashtag(){
        talentList = new ArrayList<>();
        RequestQueue postRequestQueue = Volley.newRequestQueue(mContext);
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "TalentSharing/searchTalentListByHashtag.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    userList = new ArrayList<UserData_TalentList>();
                    JSONArray array = new JSONArray(response);
                    for(int i = 0; i < array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
                        SimpleDateFormat sdf = new SimpleDateFormat("YYYY");

                        UserData_TalentList aUser = new UserData_TalentList();
                        aUser.setUserName(obj.getString("USER_NAME"));
                        aUser.setUserGender(obj.getString("GENDER"));
                        aUser.setHashtag(obj.has("HASHTAG") ? obj.getString("HASHTAG") : "");
                        aUser.setUserAge(Integer.parseInt(sdf.format(new Date())) - Integer.parseInt(obj.getString("USER_BIRTH").split("-")[0]) + 1 + "");
                        aUser.setUserID(obj.getString("UserID"));
                        aUser.setTalentID(obj.getString("TalentID"));

                        userList.add(aUser);
                    }

                    for (int i=0;i<talentList.size();i++) {
                        TextView tv = new TextView(getApplicationContext());
                        tv.setText(talentList.get(i).getTitle());
                        Log.d(this.getClass().getName(), titleTxt.toString() + ":" +talentList.get(i).getTitle().toString());
                        if (titleTxt.toString().equals(talentList.get(i).getTitle().toString())) {
                            tv.setTextColor(getResources().getColor(R.color.bgr_mainColor));
                        }
                        tv.setPadding(10,10,10,10);
                        hsv.addView(tv);
                    }
                    // 리스트 뷰
                    ListAdapter_TalentList oAdapter = new ListAdapter_TalentList(mContext, userList);
                    userListView.setAdapter(oAdapter);

                    // 리스트 뷰 프로필 이동 이벤트
                    userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(MainActivity_TalentList.this, accepted.talentplanet_renewal2.Profile.MainActivity_Profile.class);

                            String userInfo = userList.get(position).getUserGender() + " / " + userList.get(position).getUserAge() + "세";
                            intent.putExtra("userName", userList.get(position).getUserName());
                            intent.putExtra("userInfo", userInfo);
                            intent.putExtra("userID", userList.get(position).getUserID());
                            intent.putExtra("talentID", userList.get(position).getTalentID());
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
                params.put("TalentFlag", talentFlag);
                params.put("Hashtag", titleTxt);
                params.put("UserID", SaveSharedPreference.getUserId(mContext));
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }

}
