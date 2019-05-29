package accepted.talentplanet_renewal2.Friend;

import accepted.talentplanet_renewal2.R;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SubActivity_Friend extends AppCompatActivity {

    Intent intent;
    String userName;
    String userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub__friend);

        // 메인에서 받은 값을 받을 준비
        intent = getIntent();
        userName = intent.getStringExtra("userName");
        userInfo = intent.getStringExtra("userInfo");

        // 필요한 UI 만 표시
        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);

        ((LinearLayout)findViewById(R.id.addFriend)).setVisibility(View.GONE);
        ((ImageView)findViewById(R.id.img_show3x5)).setVisibility(View.GONE);
        ((ImageView)findViewById(R.id.img_show1x15)).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.tv_birth_profile)).setVisibility(View.GONE);

        ((TextView)findViewById(R.id.tv_name_profile)).setText(userName);
        ((TextView)findViewById(R.id.tv_age_profile)).setText(userInfo);
        ((TextView)findViewById(R.id.tv_toolbar)).setText(userName);

        // 뒤로가기 이벤트
        findViewById(R.id.img_open_dl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
