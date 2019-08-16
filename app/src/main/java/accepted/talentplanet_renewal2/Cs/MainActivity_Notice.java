package accepted.talentplanet_renewal2.Cs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import accepted.talentplanet_renewal2.R;

public class MainActivity_Notice extends AppCompatActivity {

    private ExpandableListView noticeList;
    private ArrayList<NoticeData> csNoticeCont = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerservice_notice_activity);

        Intent intent = getIntent();
        String title = intent.getStringExtra("ctgrTitle");

        ((TextView)findViewById(R.id.tv_toolbar_talentlist)).setText("공지사항");
        ((ImageView)findViewById(R.id.iv_toolbar_search_talentlist)).setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.rgb(255, 102, 102));
        }



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
        ((ImageView) findViewById(R.id.img_back_toolbar_talentlist)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
