package accepted.talentplanet_renewal2.JoinLogin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;

import accepted.talentplanet_renewal2.Home.MainActivity;
import accepted.talentplanet_renewal2.R;


public class MainActivity_Login extends AppCompatActivity {


    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mContext = getApplicationContext();


        final RelativeLayout rl_container;
        rl_container = findViewById(R.id.rl_container_login);
        Glide.with(mContext)
                .load(R.drawable.pic_loginbgr)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new ViewTarget<RelativeLayout, GlideDrawable>(rl_container) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation anim) {
                        RelativeLayout myView = this.view;
                        myView.setBackground(resource);
                    }
                });

        ((Button)findViewById(R.id.btn_login_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
            }
        });

        ((Button)findViewById(R.id.btn_join_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity_Join.class);
                startActivity(intent);
            }
        });


    }






}


