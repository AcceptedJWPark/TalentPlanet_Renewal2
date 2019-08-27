package com.accepted.acceptedtalentplanet.Profile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.accepted.acceptedtalentplanet.SaveSharedPreference;
import com.bumptech.glide.Glide;

import com.accepted.acceptedtalentplanet.R;
import com.accepted.acceptedtalentplanet.SaveSharedPreference;

import static android.graphics.Color.WHITE;

public class ImageActivity extends AppCompatActivity {

    Context mContext;

    ImageView iv_userimg_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.image_activity);

        mContext = getApplicationContext();

        Intent intent = getIntent();
        String userImg  = intent.getStringExtra("bigImg");

        iv_userimg_image = findViewById(R.id.iv_userimg_image);
        Glide.with(mContext).load(SaveSharedPreference.getImageUri() + userImg).into(iv_userimg_image);


        // 뒤로가기 이벤트
        ((ImageView) findViewById(R.id.iv_share_profile)).setImageResource(R.drawable.icon_close);
        ((ImageView) findViewById(R.id.iv_share_profile)).setColorFilter(WHITE);
        ((ImageView) findViewById(R.id.img_back_toolbarprofile)).setVisibility(View.GONE);
        ((ImageView) findViewById(R.id.iv_share_profile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
