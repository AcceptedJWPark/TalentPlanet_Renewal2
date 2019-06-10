package accepted.talentplanet_renewal2.TalentList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import accepted.talentplanet_renewal2.Classes.TalentObject_Home;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.VolleySingleton;

public class MainActivity_TalentList extends AppCompatActivity {

    private Map<String, TalentObject_Home> talentMap;
    private ArrayList<TalentObject_Home> talentList;
    private ArrayList<UserData_TalentList> userList;
    private Context mContext;
    private int cateCode;
    private String talentFlag;
    private String titleTxt;

    // 뷰 정의
    ListView userListView;
    TextView title;
    ImageView leftBtn;
    ImageView img3x5;
    ImageView rightBtn;
    LinearLayout hsv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__talent_list);

        mContext = getApplicationContext();

        // 뷰 정의
        userListView = (ListView)findViewById(R.id.lv_talentUser);
        title = (TextView)findViewById(R.id.tv_toolbar);
        leftBtn = (ImageView)findViewById(R.id.img_open_dl);
        img3x5 = (ImageView)findViewById(R.id.img_show3x5);
        rightBtn = (ImageView)findViewById(R.id.img_show1x15);
        hsv = (LinearLayout)findViewById(R.id.sv_show1x15);

        // 인텐트 받기
        Intent intent = getIntent();

        // 인텐트 엑스트라 받기
        titleTxt = intent.getStringExtra("talentName");
        cateCode = intent.getIntExtra("cateCode", 0);
        talentFlag = intent.getStringExtra("talentFlag");

        // 필요정보 생성
        makeTestTalentArr();
        getTalentListNew();

        // 필요한 뷰만을 표시 R.drawable.icon_back
        leftBtn.setImageResource(R.drawable.icon_back);
        img3x5.setVisibility(View.GONE);
        rightBtn.setVisibility(View.GONE);

        // 뷰 정보 인텐트 정보로 변경
        title.setText(titleTxt);

        // 뒤로가기 이벤트
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void makeTestTalentArr() {
        talentMap = new HashMap();

        TalentObject_Home career = new TalentObject_Home("취업", R.drawable.pic_career,R.drawable.icon_career, 0);
        TalentObject_Home study = new TalentObject_Home("학습", R.drawable.pic_study,R.drawable.icon_study, 0);
        TalentObject_Home money = new TalentObject_Home("재테크", R.drawable.pic_money,R.drawable.icon_money, 0);
        TalentObject_Home it = new TalentObject_Home("IT", R.drawable.pic_it,R.drawable.icon_it, 0);
        TalentObject_Home camera = new TalentObject_Home("사진", R.drawable.pic_camera,R.drawable.icon_camera, 0);
        TalentObject_Home music = new TalentObject_Home("음악", R.drawable.pic_music,R.drawable.icon_music, 0);
        TalentObject_Home design = new TalentObject_Home("미술/디자인", R.drawable.pic_design,R.drawable.icon_design, 0);
        TalentObject_Home sports = new TalentObject_Home("운동", R.drawable.pic_sports,R.drawable.icon_sports, 0);
        TalentObject_Home living = new TalentObject_Home("생활", R.drawable.pic_living,R.drawable.icon_living, 0);
        TalentObject_Home beauty = new TalentObject_Home("뷰티/패션", R.drawable.pic_beauty,R.drawable.icon_beauty, 0);
        TalentObject_Home volunteer = new TalentObject_Home("사회봉사", R.drawable.pic_volunteer,R.drawable.icon_volunteer, 0);
        TalentObject_Home travel = new TalentObject_Home("여행", R.drawable.pic_travel,R.drawable.icon_travel, 0);
        TalentObject_Home culture = new TalentObject_Home("문화", R.drawable.pic_culture,R.drawable.icon_culture, 0);
        TalentObject_Home game = new TalentObject_Home("게임", R.drawable.pic_game,R.drawable.icon_game, 0);

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
                try {
                    userList = new ArrayList<UserData_TalentList>();
                    JSONArray array = new JSONArray(response);
                    for(int i = 0; i < array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
                        SimpleDateFormat sdf = new SimpleDateFormat("YYYY");

                        UserData_TalentList aUser = new UserData_TalentList();
                        aUser.setUserName(obj.getString("USER_NAME"));
                        aUser.setUserGender(obj.getString("GENDER"));
                        aUser.setUserAge(Integer.parseInt(sdf.format(new Date())) - Integer.parseInt(obj.getString("USER_BIRTH").split("-")[0]) + 1 + "");

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
                params.put("CateCode", String.valueOf(cateCode));
                params.put("UserID", "mkh9012@naver.com");
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }

    private void makeUserData() {
        userList = new ArrayList<UserData_TalentList>();

        String[] nameArr = {"박종우", "민권홍", "조현배", "문건우"};
        String[] ageArr = {"29", "30", "27", "25"};

        for (int i=0; i<nameArr.length; i++) {
            UserData_TalentList aUser = new UserData_TalentList();
            aUser.setUserName(nameArr[i] );
            aUser.setUserGender("남");
            aUser.setUserAge(ageArr[i]);

            userList.add(aUser);
        }
    }
}
