package com.accepted.acceptedtalentplanet.Cs;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;

import com.accepted.acceptedtalentplanet.Cs.Notice.NoticeData;
import com.accepted.acceptedtalentplanet.Cs.Notice.customDialog_Notice;
import com.accepted.acceptedtalentplanet.SaveSharedPreference;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.accepted.acceptedtalentplanet.Cs.Notice.NoticeData;
import com.accepted.acceptedtalentplanet.Cs.Notice.customDialog_Notice;
import com.accepted.acceptedtalentplanet.R;
import com.accepted.acceptedtalentplanet.SaveSharedPreference;

public class MainActivity_Notice extends AppCompatActivity {

    private Context mContext;
    private ArrayList<NoticeData> csNoticeCont = new ArrayList<>();
    private customDialog_Notice cd_Notice;

    ListView lv_listview_cs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerservice_notice_activity);
        mContext = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.rgb(255, 102, 102));
        }

        // 1
        lv_listview_cs = findViewById(R.id.lv_listview_cs);

        ImageView iv_toolbar_search_talentlist = (ImageView) findViewById(R.id.iv_toolbar_search_talentlist);
        TextView tv_toolbar_talentlist = (TextView) findViewById(R.id.tv_toolbar_talentlist);

        // 2
        iv_toolbar_search_talentlist.setVisibility(View.GONE);
        tv_toolbar_talentlist.setText("공지사항");

        getNoticeList();

        // 뒤로가기
        ((ImageView) findViewById(R.id.img_back_toolbar_talentlist)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getNoticeList(){
        RequestQueue postRequestQueue = Volley.newRequestQueue(mContext);
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Customer/getNoticeList.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    csNoticeCont = new ArrayList<NoticeData>();
                    JSONArray array = new JSONArray(response);
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        NoticeData aData = new NoticeData();

                        aData.setNOTICE_TITLE((String) obj.get("NOTICE_TITLE"));
                        aData.setNOTICE_SUMMARY((String) obj.get("NOTICE_SUMMARY"));
                        aData.setCREATION_DATE((Long) obj.get("CREATION_DATE"));

                        csNoticeCont.add(aData);
                    }

                    ListAdapter_Notice adapter_notice = new ListAdapter_Notice(mContext, csNoticeCont);
                    lv_listview_cs.setAdapter(adapter_notice);
                    lv_listview_cs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
                            double width = dm.widthPixels; //디바이스 화면 너비
                            double height = dm.heightPixels; //디바이스 화면 높이

                            cd_Notice = new customDialog_Notice(MainActivity_Notice.this, csNoticeCont.get(position));
                            WindowManager.LayoutParams wm2 = cd_Notice.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
                            wm2.copyFrom(cd_Notice.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
                            wm2.width = (int) (width / 1.1);
                            wm2.height = (int) (height / 1.1);

                            cd_Notice.show();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {};

        postRequestQueue.add(postJsonRequest);
    }
}
