package accepted.talentplanet_renewal2.Cs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import accepted.talentplanet_renewal2.Cs.Claim.MainActivity;
import accepted.talentplanet_renewal2.R;

import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity_Cs extends AppCompatActivity {

    Context mContext;
    private ListView csList;
    private ArrayList<NoticeData> csCtgr = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerservice_activity);

        mContext = getApplicationContext();
        ((TextView)findViewById(R.id.tv_toolbar)).setText("고객센터");
        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);


        // ArrayAdapter, Adapter 생성 및 연결
        final ListAdapter_Cs oAdapter = new ListAdapter_Cs();
        csList = (ListView) findViewById(R.id.lv_cs);
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
                String status = (String) oAdapter.getItem(position).toString();
                Log.d(this.getClass().getName(), status);
                switch (status) {
                    case "공지사항" :
                        Intent intent = new Intent(MainActivity_Cs.this, MainActivity_Notice.class);
                        intent.putExtra("ctgrTitle", status);
                        startActivity(intent);
                        break;
                    case "신고 리스트" :
                        Intent intent2 = new Intent(MainActivity_Cs.this, MainActivity.class);
                        intent2.putExtra("ctgrTitle", status);
                        startActivity(intent2);
                        break;
                }
            }
        });
    }
}