package accepted.talentplanet_renewal2.Cs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import accepted.talentplanet_renewal2.Condition.condition_proc_pagerAdapter;
import accepted.talentplanet_renewal2.Condition.condition_req_pagerAdapter;
import accepted.talentplanet_renewal2.R;

import static android.graphics.Color.WHITE;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import accepted.talentplanet_renewal2.R;


public class MainActivity_Cs extends AppCompatActivity {

    Context mContext;
    private ListView csList;
    private ArrayList<NoticeData> csCtgr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs);

        mContext = getApplicationContext();
        ((TextView)findViewById(R.id.tv_toolbar)).setText("고객센터");
        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);
        findViewById(R.id.img_show1x15).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        String[] titleArr = {"공지사항", "텔런트플레넷 정보 이용동의", "신고 리스트", "공지사항", "질문과 답변"};
        String[] regDateArr = {"", "", "", "", ""};

        // 가상 데이터 추가
        for (int i=0;i<5;i++) {
            NoticeData fakeData = new NoticeData();
            fakeData.title = titleArr[i];
            fakeData.regDate = regDateArr[i];
            csCtgr.add(fakeData);
        }

        // ListView, Adapter 생성 및 연결 ------------------------
        csList = (ListView) findViewById(R.id.lv_cs);
        ListAdapter oAdapter = new ListAdapter(csCtgr);
        csList.setAdapter(oAdapter);

        // 뒤로가기
        ((ImageView) findViewById(R.id.img_open_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        csList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 테스트 데이터 전송
                String status = csCtgr.get(position).title;
                switch (status) {
                    case "공지사항" :
                        Intent intent = new Intent(MainActivity_Cs.this, SubActivity_notice.class);
                        intent.putExtra("ctgrTitle", status);
                        startActivity(intent);
                        break;
                }
            }
        });
    }
}
