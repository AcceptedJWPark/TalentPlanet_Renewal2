package accepted.talentplanet_renewal2.Profile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;

public class ImageActivity extends AppCompatActivity {

    Context mContext;

    ImageView iv_userimg_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);

        mContext = getApplicationContext();

        Intent intent = getIntent();
        String userImg  = intent.getStringExtra("bigImg");

        iv_userimg_image = findViewById(R.id.iv_userimg_image);
        Glide.with(mContext).load(SaveSharedPreference.getImageUri() + userImg).into(iv_userimg_image);

        // 뒤로가기 이벤트
        ((ImageView) findViewById(R.id.img_back_toolbarprofile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
