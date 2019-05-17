package accepted.talentplanet_renewal2.Friend;

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

import accepted.talentplanet_renewal2.R;

import java.util.ArrayList;

public class MainActivity_Friend extends AppCompatActivity {

    Context mContext;
    private ListView friendList;
    private  ArrayList<ItemData_Friend> oData;

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
        String[] nameArr = {"박종우","민권홍","이태훈","조현배","문건우"};
        String[] infoArr = {"남성 / 29세","남성 / 30세","남성 / 29세","남성 / 27세","남성 / 25세"};
        oData = new ArrayList<>();
        for (int i=0; i<5; ++i) {
            ItemData_Friend oItem = new ItemData_Friend();
            oItem.strUserName = nameArr[i];
            oItem.strUserInfo = infoArr[i];
            oData.add(oItem);
        }
        Log.d(this.getClass().getName(), oData.toString());

        // ListView, Adapter 생성 및 연결 ------------------------
        friendList = (ListView)findViewById(R.id.lv_friend);
        ListAdapter_Friend oAdapter = new ListAdapter_Friend(oData);

        friendList.setAdapter(oAdapter);

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
                // 기본 적 UI 숨기기
                ((ImageView) findViewById(R.id.img_show1x15)).setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.img_open_dl)).setVisibility(View.GONE);

                // 삭제 전용 UI 보여주기
                ((TextView)findViewById(R.id.tv_remove)).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.tv_Choose)).setVisibility(View.VISIBLE);
            }
        });

        // 친구삭제완료 이벤트
        findViewById(R.id.tv_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 기본 적 UI 숨기기
                ((TextView)findViewById(R.id.tv_remove)).setVisibility(View.GONE);
                ((TextView)findViewById(R.id.tv_Choose)).setVisibility(View.GONE);

                // 삭제 전용 UI 보여주기
                ((ImageView) findViewById(R.id.img_show1x15)).setVisibility(View.VISIBLE);
                ((ImageView) findViewById(R.id.img_open_dl)).setVisibility(View.VISIBLE);
            }
        });

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
    }

}