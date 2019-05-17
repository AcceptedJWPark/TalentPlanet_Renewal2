package accepted.talentplanet_renewal2.Cs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import accepted.talentplanet_renewal2.R;

public class MainActivity_Notice extends AppCompatActivity {

    private ExpandableListView noticeList;
    private ArrayList<NoticeData> csNoticeCont = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs2);

        Intent intent = getIntent();
        String title = intent.getStringExtra("ctgrTitle");
        ((TextView)findViewById(R.id.tv_toolbar)).setText(title);

        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);
        ((ImageView) findViewById(R.id.img_show1x15)).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        for (int i=1; i<4; i++) {
            NoticeData aData = new NoticeData();
            aData.title = "공지사항 콘텐츠 " + i;
            aData.regDate = "2019.05.0" + i;
            aData.text.add( "공지사항 콘텐츠 " + i + " 입니다. \n" + "2019-05-0" + i);
            csNoticeCont.add(aData);
        }

        // ListView, Adapter 생성 및 연결 ------------------------
        ExpandableListAdapter_Cs oAdapter = new ExpandableListAdapter_Cs(csNoticeCont);
        noticeList = (ExpandableListView)findViewById(R.id.elv_cs);
        noticeList.setAdapter(oAdapter);

        // 뒤로가기
        ((ImageView) findViewById(R.id.img_open_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
