package accepted.talentplanet_renewal2.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import accepted.talentplanet_renewal2.R;

/**
 * Created by Accepted on 2019-05-05.
 */

public class MainActivity_NewTalent extends AppCompatActivity {

    private String cateCode;
    private int imgSrc;
    TextView tv_toolbar;
    TextView tv_user_data_edit;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_newtalent);
        mContext = getApplicationContext();

        // intent
        Intent intent = getIntent();
        String talentTitle = intent.getStringExtra("talentName");
        cateCode = intent.getStringExtra("cateCode");
        imgSrc = intent.getIntExtra("imgSrc", 0);

        tv_toolbar = findViewById(R.id.tv_toolbar);
        tv_user_data_edit = findViewById(R.id.tv_user_data_edit);
        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);

        Glide.with(this).load(imgSrc).into(((ImageView) findViewById(R.id.iv_bg_newtalent)));

        findViewById(R.id.img_show1x15).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        // 유저가 새롭게 재능을 등록 / 신청하는 액티비티
        // 새 재능 타이틀
        tv_toolbar.setText(talentTitle);
        // 인텐트로 코드, 이미지 줘야함

        ImageView editBtn = (ImageView) findViewById(R.id.iv_edit_user_info);

        // 수정으로 가는 이벤트
        View.OnClickListener editTalent = new View.OnClickListener() {
            // 유저의 정보 수정 이벤트 부여
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity_NewTalent.this, MainActivity_Detail.class);
                Log.d("NowCateCode" , cateCode);
                intent.putExtra("cateCode", cateCode);
                startActivityForResult(intent, 3000);
            }
        };
        // 두 버튼에 이벤트 부여
        editBtn.setOnClickListener(editTalent);
        tv_user_data_edit.setOnClickListener(editTalent);

        // 뒤로가기 이벤트
        ((ImageView) findViewById(R.id.img_open_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
