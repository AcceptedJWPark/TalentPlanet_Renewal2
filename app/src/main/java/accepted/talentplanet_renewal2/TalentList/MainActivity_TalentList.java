package accepted.talentplanet_renewal2.TalentList;

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

import java.util.ArrayList;
import java.util.Collections;

import accepted.talentplanet_renewal2.Classes.TalentObject_Home;
import accepted.talentplanet_renewal2.R;

public class MainActivity_TalentList extends AppCompatActivity {

    private ArrayList<TalentObject_Home> talentList;
    private ArrayList<UserData_TalentList> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__talent_list);

        // 필요정보 생성
        makeTestTalentArr();
        makeUserData();

        // 뷰 정의
        ListView userListView = (ListView)findViewById(R.id.lv_talentUser);
        TextView title = (TextView)findViewById(R.id.tv_toolbar);
        ImageView leftBtn = (ImageView)findViewById(R.id.img_open_dl);
        ImageView img3x5 = (ImageView)findViewById(R.id.img_show3x5);
        ImageView rightBtn = (ImageView)findViewById(R.id.img_show1x15);
        LinearLayout hsv = (LinearLayout)findViewById(R.id.sv_show1x15);

        // 인텐트 받기
        Intent intent = getIntent();

        // 인텐트 엑스트라 받기
        String titleTxt = intent.getExtras().getString("talentName");

        // 필요한 뷰만을 표시 R.drawable.icon_back
        leftBtn.setImageResource(R.drawable.icon_back);
        img3x5.setVisibility(View.GONE);
        rightBtn.setVisibility(View.GONE);

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
        ListAdapter_TalentList oAdapter = new ListAdapter_TalentList(this, userList);
        userListView.setAdapter(oAdapter);

        // 뷰 정보 인텐트 정보로 변경
        title.setText(titleTxt);

        // 뒤로가기 이벤트
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
    }

    private void makeTestTalentArr() {
        talentList = new ArrayList<>();

        TalentObject_Home career = new TalentObject_Home("취업", R.drawable.pic_career,R.drawable.icon_career, 495);
        TalentObject_Home study = new TalentObject_Home("학습", R.drawable.pic_study,R.drawable.icon_study, 485);
        TalentObject_Home money = new TalentObject_Home("재테크", R.drawable.pic_money,R.drawable.icon_money, 410);
        TalentObject_Home it = new TalentObject_Home("IT", R.drawable.pic_it,R.drawable.icon_it, 379);
        TalentObject_Home camera = new TalentObject_Home("사진", R.drawable.pic_camera,R.drawable.icon_camera, 338);
        TalentObject_Home music = new TalentObject_Home("음악", R.drawable.pic_music,R.drawable.icon_music, 230);
        TalentObject_Home design = new TalentObject_Home("미술/디자인", R.drawable.pic_design,R.drawable.icon_design, 202);
        TalentObject_Home sports = new TalentObject_Home("운동", R.drawable.pic_sports,R.drawable.icon_sports, 192);
        TalentObject_Home living = new TalentObject_Home("생활", R.drawable.pic_living,R.drawable.icon_living, 172);
        TalentObject_Home beauty = new TalentObject_Home("뷰티/패션", R.drawable.pic_beauty,R.drawable.icon_beauty, 135);
        TalentObject_Home volunteer = new TalentObject_Home("사회봉사", R.drawable.pic_volunteer,R.drawable.icon_volunteer, 519);
        TalentObject_Home travel = new TalentObject_Home("여행", R.drawable.pic_travel,R.drawable.icon_travel, 118);
        TalentObject_Home culture = new TalentObject_Home("문화", R.drawable.pic_culture,R.drawable.icon_culture, 49);
        TalentObject_Home game = new TalentObject_Home("게임", R.drawable.pic_game,R.drawable.icon_game, 41);

        talentList.add(career);
        talentList.add(study);
        talentList.add(money);
        talentList.add(it);
        talentList.add(camera);
        talentList.add(music);
        talentList.add(design);
        talentList.add(sports);
        talentList.add(living);
        talentList.add(beauty);
        talentList.add(volunteer);
        talentList.add(travel);
        talentList.add(culture);
        talentList.add(game);

        Collections.sort(talentList);

//        long seed = System.nanoTime();
//        Collections.shuffle(arrTalent, new Random(seed));
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
