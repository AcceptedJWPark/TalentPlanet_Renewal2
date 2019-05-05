package accepted.talentplanet_renewal2.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import accepted.talentplanet_renewal2.R;


public class MainActivity_Profile extends AppCompatActivity {

    TextView tv_toolbar;
    ViewPager vp;
    Context mContext=getApplicationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        tv_toolbar=findViewById(R.id.tv_toolbar);
        tv_toolbar.setText("Profile");
        ((ImageView)findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);
        findViewById(R.id.img_show1x15).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        vp = findViewById(R.id.vp_profile_mentor);
        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);

    }

    private class pagerAdapter extends FragmentStatePagerAdapter {
        public pagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position)
            {
                case 0:
                    return new Talent_FirstFragment();
                case 1:
                    return new Talent_SecondFragment();
                case 2:
                    return new Talent_ThirdFragment();
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return 3;
        }
    }




}
