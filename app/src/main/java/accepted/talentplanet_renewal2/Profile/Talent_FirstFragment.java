package accepted.talentplanet_renewal2.Profile;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import accepted.talentplanet_renewal2.R;

/**
 * Created by Accepted on 2019-05-05.
 */

public class Talent_FirstFragment extends android.support.v4.app.Fragment {
    private int[] arr_iv = {R.id.iv_home_main_bg_0, R.id.iv_home_main_bg_1, R.id.iv_home_main_bg_2, R.id.iv_home_main_bg_3, R.id.iv_home_main_bg_4, R.id.iv_home_main_bg_5,
            R.id.iv_home_main_bg_6, R.id.iv_home_main_bg_7, R.id.iv_home_main_bg_8, R.id.iv_home_main_bg_9, R.id.iv_home_main_bg_10, R.id.iv_home_main_bg_11,
            R.id.iv_home_main_bg_12, R.id.iv_home_main_bg_13};

    private int[] arr_bg = {R.drawable.pic_career
            , R.drawable.pic_study
            , R.drawable.pic_money
            , R.drawable.pic_it
            , R.drawable.pic_camera
            , R.drawable.pic_music
            , R.drawable.pic_design
            , R.drawable.pic_sports
            , R.drawable.pic_living
            , R.drawable.pic_beauty
            , R.drawable.pic_volunteer
            , R.drawable.pic_travel
            , R.drawable.pic_culture
            , R.drawable.pic_game };

    public Talent_FirstFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_first, container, false);
        loadBackgroundImages(layout);
        return layout;
    }

    private void loadBackgroundImages(LinearLayout layout) {
        for(int i = 0 ; i < arr_iv.length; i++){
            ImageView iv = (ImageView)layout.findViewById(arr_iv[i]);
            Glide.with(this).load(arr_bg[i]).into(iv);
        }
    }


}
