package com.accepted.acceptedtalentplanet.Cs;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accepted.acceptedtalentplanet.R;

public class MainActivity_DeleteId extends AppCompatActivity {

    Context mContext;
    private Window window;
    private ImageView[] iv_deleteId = new ImageView[5];
    private LinearLayout[] ll_deleteId = new LinearLayout[5];
    private boolean[] deleteIded = new boolean[5];

    private boolean isCheckAgreement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerservice_deleteid_activity);

        mContext = getApplicationContext();
        ((TextView) findViewById(R.id.tv_toolbar_talentlist)).setText("탈퇴 신청");
        ((ImageView) findViewById(R.id.iv_toolbar_search_talentlist)).setVisibility(View.GONE);

        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        }
        // 뒤로가기 이벤트
        ((ImageView) findViewById(R.id.img_back_toolbar_talentlist)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_deleteId[0] = findViewById(R.id.iv1_deleteid);
        iv_deleteId[1] = findViewById(R.id.iv2_deleteid);
        iv_deleteId[2] = findViewById(R.id.iv3_deleteid);
        iv_deleteId[3] = findViewById(R.id.iv4_deleteid);
        iv_deleteId[4] = findViewById(R.id.iv5_deleteid);
        ll_deleteId[0] = findViewById(R.id.ll1_deleteid);
        ll_deleteId[1] = findViewById(R.id.ll2_deleteid);
        ll_deleteId[2] = findViewById(R.id.ll3_deleteid);
        ll_deleteId[3] = findViewById(R.id.ll4_deleteid);
        ll_deleteId[4] = findViewById(R.id.ll5_deleteid);

        for(int i=0; i<ll_deleteId.length; i++) {
            final int finalI = i;
            deleteIded[i] = false;
            ll_deleteId[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!deleteIded[finalI])
                    {
                        deleteIded[finalI] = true;
                        iv_deleteId[finalI].setImageResource(R.drawable.icon_checkbox_selected);
                    }
                    else
                    {
                        deleteIded[finalI] = false;
                        iv_deleteId[finalI].setImageResource(R.drawable.icon_checkbox_unselected);
                    }
                }
            });
        }

        isCheckAgreement = false;

        ((LinearLayout)findViewById(R.id.ll_deleteid)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isCheckAgreement)
                {
                    isCheckAgreement = true;
                    ((ImageView)findViewById(R.id.iv_deleteid)).setImageResource(R.drawable.icon_checkbox_selected);
                }
                else
                {
                    isCheckAgreement = false;
                    ((ImageView)findViewById(R.id.iv_deleteid)).setImageResource(R.drawable.icon_checkbox_unselected);
                }
            }
        });





    }
}
