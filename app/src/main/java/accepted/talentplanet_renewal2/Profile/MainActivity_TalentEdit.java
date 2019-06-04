package accepted.talentplanet_renewal2.Profile;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import accepted.talentplanet_renewal2.Classes.TalentObject_Profile;
import accepted.talentplanet_renewal2.R;

import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.CENTER;
import static android.view.View.GONE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity_TalentEdit extends AppCompatActivity {

    private ArrayList<TalentObject_Profile> arrTalent;
    View v_inc_profile[] = new View[2];
    ViewPager.OnPageChangeListener onPageChangeListener;
    talentlist_viewpager vp;

    HashTagHelper mHashtagHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__talent_edit);

        Intent intent = getIntent();
        String requestType = intent.getStringExtra("type");
        mHashtagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), null);

        ((TextView)findViewById(R.id.tv_toolbar)).setText(requestType);
        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);

        findViewById(R.id.img_show1x15).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        // 선택 재능 리스트
        findViewById(R.id.inc_mentor_profile).setVisibility(View.VISIBLE);

        // VeiwPager 시작
        vp = findViewById(R.id.vp_profile_mentor);
        vp.setAdapter(new talentlist_pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);

        // 이벤트 내용 정의
        onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    ((LinearLayout) findViewById(R.id.ll_firstpage_profile_mentor)).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.tv_series_profile_mentor)).setVisibility(View.GONE);
                } else if (position == 1) {
                    ((LinearLayout) findViewById(R.id.ll_firstpage_profile_mentor)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_series_profile_mentor)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_series_profile_mentor)).setText("2");
                } else {
                    ((LinearLayout) findViewById(R.id.ll_firstpage_profile_mentor)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_series_profile_mentor)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_series_profile_mentor)).setText("3");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };

        // 해당 viewpager에 이벤트 부여
        vp.addOnPageChangeListener(onPageChangeListener);

        findViewById(R.id.ll_totalshow_profile_mentor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(0);
            }
        });

        // 뒤로가기 이벤트
        ((ImageView) findViewById(R.id.img_open_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.d("asdf", "asdfasdf" + resultCode);
        if(resultCode == RESULT_OK){
            String ProfileText = data.getStringExtra("ProfileText");
            vp.getCurrentItem();
            TextView profile_talent = (TextView)findViewById(R.id.tv_profile_talant);
            profile_talent.setText(ProfileText);
            mHashtagHelper.handle(profile_talent);
        }
    }
}
