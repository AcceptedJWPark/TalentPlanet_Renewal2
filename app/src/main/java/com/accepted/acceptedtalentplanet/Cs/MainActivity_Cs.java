package com.accepted.acceptedtalentplanet.Cs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.accepted.acceptedtalentplanet.Cs.Claim.MainActivity;
import com.accepted.acceptedtalentplanet.Cs.Notice.NoticeData;
import com.accepted.acceptedtalentplanet.R;

import android.widget.AdapterView;
import android.widget.ListView;

import com.accepted.acceptedtalentplanet.Cs.Notice.NoticeData;

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
        ((TextView)findViewById(R.id.tv_toolbar_talentlist)).setText("고객센터");
        ((ImageView)findViewById(R.id.iv_toolbar_search_talentlist)).setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.rgb(255, 102, 102));
        }



        // ArrayAdapter, Adapter 생성 및 연결
        final ListAdapter_Cs oAdapter = new ListAdapter_Cs();
        csList = (ListView) findViewById(R.id.lv_cs);
        csList.setAdapter(oAdapter);

        // 뒤로가기
        ((ImageView) findViewById(R.id.img_back_toolbar_talentlist)).setOnClickListener(new View.OnClickListener() {
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
                        Intent intent2 = new Intent(MainActivity_Cs.this, com.accepted.acceptedtalentplanet.Cs.Claim.ClaimList.MainActivity.class);
                        intent2.putExtra("ctgrTitle", status);
                        startActivity(intent2);
                        break;

                    case "질문과 답변" :
                        Intent intent3 = new Intent(MainActivity_Cs.this, MainActivity_Qna.class);
                        startActivity(intent3);
                        break;

                    case "회원탈퇴" :
                        Intent intent4 = new Intent(MainActivity_Cs.this, MainActivity_DeleteId.class);
                        startActivity(intent4);
                        break;

                    case "개인정보 이용동의":
                        AlertDialog.Builder AlarmDeleteDialog = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity_Cs.this, R.style.myDialog));
                        AlarmDeleteDialog.setMessage("개인정보 처리방침 Website로 이동합니다.")
                                .setPositiveButton("이동하기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri uri = Uri.parse("http://13.209.191.97/Accepted/Privacy");
                                        Intent it  = new Intent(Intent.ACTION_VIEW,uri);
                                        startActivity(it);
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = AlarmDeleteDialog.create();
                        alertDialog.show();
                        alertDialog.getButton((DialogInterface.BUTTON_NEGATIVE)).setTextColor(getResources().getColor(R.color.loginPasswordLost));
                        alertDialog.getButton((DialogInterface.BUTTON_POSITIVE)).setTextColor(getResources().getColor(R.color.loginPasswordLost));
                        break;

                    case "정보 이용동의":
                        AlertDialog.Builder AlarmDeleteDialog2 = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity_Cs.this, R.style.myDialog));
                        AlarmDeleteDialog2.setMessage("정보 이용동의 Website로 이동합니다.")
                                .setPositiveButton("이동하기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri uri = Uri.parse("http://13.209.191.97/Accepted/Service");
                                        Intent it  = new Intent(Intent.ACTION_VIEW,uri);
                                        startActivity(it);
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog2 = AlarmDeleteDialog2.create();
                        alertDialog2.show();
                        alertDialog2.getButton((DialogInterface.BUTTON_NEGATIVE)).setTextColor(getResources().getColor(R.color.loginPasswordLost));
                        alertDialog2.getButton((DialogInterface.BUTTON_POSITIVE)).setTextColor(getResources().getColor(R.color.loginPasswordLost));
                        break;
                }
            }
        });
    }
}