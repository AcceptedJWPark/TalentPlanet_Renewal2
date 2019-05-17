package accepted.talentplanet_renewal2.Cs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import accepted.talentplanet_renewal2.R;

public class SubActivity_notice extends AppCompatActivity {

    private ExpandableListView noticeList;
    private ArrayList<HashMap<String, Object>> csNoticeCont = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs);

        Intent intent = getIntent();
        String title = intent.getStringExtra("ctgrTitle");
        ((TextView)findViewById(R.id.tv_toolbar)).setText(title);

        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);
        ((ImageView) findViewById(R.id.img_show1x15)).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        prepareListData();

        // ListView, Adapter 생성 및 연결 ------------------------
        noticeList = (ExpandableListView)findViewById(R.id.elv_cs);
        ExpandableListAdapter oAdapter = new ExpandableListAdapter(csNoticeCont);
        noticeList.setAdapter(oAdapter);

        // 뒤로가기
        ((ImageView) findViewById(R.id.img_open_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        // 부모 리스트 클릭 이벤트
//        noticeList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                return false;
//            }
//        });
//
//        // Listview Group collasped listener
//        noticeList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//            @Override
//            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),csNoticeCont.get(groupPosition).title,Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void prepareListData() {
        String[] titleArr = {"공지사항[점검] 텔런트 플레넷이 세롭...", "공지사항. 안녕하세요 텔런트 플레..."};
        String[] regDateArr = {"2019.05.16", "2019.05.16"};

        // 가상 데이터 추가
        for (int i=0;i<2;i++) {
            HashMap<String, Object> commandMap = new HashMap<>();
            commandMap.put("title", titleArr[i]);
            commandMap.put("regDate", regDateArr[i]);
            commandMap.put("text", "Test Text" + i);
            csNoticeCont.add(commandMap);
        }

    }
}
