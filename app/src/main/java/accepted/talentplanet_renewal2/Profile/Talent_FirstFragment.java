package accepted.talentplanet_renewal2.Profile;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import accepted.talentplanet_renewal2.R;

import static android.graphics.Color.WHITE;

/**
 * Created by Accepted on 2019-05-05.
 */

public class Talent_FirstFragment extends android.support.v4.app.Fragment {
    private int[] arr_iv = {R.id.iv_home_main_bg_0, R.id.iv_home_main_bg_1, R.id.iv_home_main_bg_2, R.id.iv_home_main_bg_3, R.id.iv_home_main_bg_4, R.id.iv_home_main_bg_5,
            R.id.iv_home_main_bg_6, R.id.iv_home_main_bg_7, R.id.iv_home_main_bg_8, R.id.iv_home_main_bg_9, R.id.iv_home_main_bg_10, R.id.iv_home_main_bg_11,
            R.id.iv_home_main_bg_12, R.id.iv_home_main_bg_13};


    private ImageView iv_talent[] = new ImageView[14];
    private TextView tv_talent[] = new TextView[14];
    private View v_talent[] = new View[14];
    private Boolean isTalent[] = new Boolean[14];




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
//        loadBackgroundImages(layout);

        for(int i=0; i<isTalent.length;i++)
        {
            isTalent[i] = false;
        }
        isTalent[1] = true;
        isTalent[2] = true;
        isTalent[4] = true;

        setViewId(layout);
        setTalentbgr();

        return layout;

    }

    private void loadBackgroundImages(LinearLayout layout) {
        for(int i = 0 ; i < arr_iv.length; i++){
            ImageView iv = (ImageView)layout.findViewById(arr_iv[i]);
            Glide.with(this).load(arr_bg[i]).into(iv);
        }
    }


    public void setViewId(LinearLayout layout)
    {
        iv_talent[0] = layout.findViewById(R.id.iv_talent1_profile);
        iv_talent[1] = layout.findViewById(R.id.iv_talent2_profile);
        iv_talent[2] = layout.findViewById(R.id.iv_talent3_profile);
        iv_talent[3] = layout.findViewById(R.id.iv_talent4_profile);
        iv_talent[4] = layout.findViewById(R.id.iv_talent5_profile);
        iv_talent[5] = layout.findViewById(R.id.iv_talent6_profile);
        iv_talent[6] = layout.findViewById(R.id.iv_talent7_profile);
        iv_talent[7] = layout.findViewById(R.id.iv_talent8_profile);
        iv_talent[8] = layout.findViewById(R.id.iv_talent9_profile);
        iv_talent[9] = layout.findViewById(R.id.iv_talent10_profile);
        iv_talent[10] = layout.findViewById(R.id.iv_talent11_profile);
        iv_talent[11] = layout.findViewById(R.id.iv_talent12_profile);
        iv_talent[12] = layout.findViewById(R.id.iv_talent13_profile);
        iv_talent[13] = layout.findViewById(R.id.iv_talent14_profile);

        tv_talent[0] = layout.findViewById(R.id.tv_talent1_profile);
        tv_talent[1] = layout.findViewById(R.id.tv_talent2_profile);
        tv_talent[2] = layout.findViewById(R.id.tv_talent3_profile);
        tv_talent[3] = layout.findViewById(R.id.tv_talent4_profile);
        tv_talent[4] = layout.findViewById(R.id.tv_talent5_profile);
        tv_talent[5] = layout.findViewById(R.id.tv_talent6_profile);
        tv_talent[6] = layout.findViewById(R.id.tv_talent7_profile);
        tv_talent[7] = layout.findViewById(R.id.tv_talent8_profile);
        tv_talent[8] = layout.findViewById(R.id.tv_talent9_profile);
        tv_talent[9] = layout.findViewById(R.id.tv_talent10_profile);
        tv_talent[10] = layout.findViewById(R.id.tv_talent11_profile);
        tv_talent[11] = layout.findViewById(R.id.tv_talent12_profile);
        tv_talent[12] = layout.findViewById(R.id.tv_talent13_profile);
        tv_talent[13] = layout.findViewById(R.id.tv_talent14_profile);

        v_talent[0] = layout.findViewById(R.id.v_talent1_profile);
        v_talent[1] = layout.findViewById(R.id.v_talent2_profile);
        v_talent[2] = layout.findViewById(R.id.v_talent3_profile);
        v_talent[3] = layout.findViewById(R.id.v_talent4_profile);
        v_talent[4] = layout.findViewById(R.id.v_talent5_profile);
        v_talent[5] = layout.findViewById(R.id.v_talent6_profile);
        v_talent[6] = layout.findViewById(R.id.v_talent7_profile);
        v_talent[7] = layout.findViewById(R.id.v_talent8_profile);
        v_talent[8] = layout.findViewById(R.id.v_talent9_profile);
        v_talent[9] = layout.findViewById(R.id.v_talent10_profile);
        v_talent[10] = layout.findViewById(R.id.v_talent11_profile);
        v_talent[11] = layout.findViewById(R.id.v_talent12_profile);
        v_talent[12] = layout.findViewById(R.id.v_talent13_profile);
        v_talent[13] = layout.findViewById(R.id.v_talent14_profile);
    }

    public void setTalentbgr()
    {
        int width_talent= (int) (getResources().getDimension(R.dimen.isTalent_pic));
        int width_nottalent= (int) (getResources().getDimension(R.dimen.isnotTalent_pic));
        LinearLayout.LayoutParams parms_talent = new LinearLayout.LayoutParams(width_talent,width_talent);
        LinearLayout.LayoutParams parms_nontalent = new LinearLayout.LayoutParams(width_nottalent,width_nottalent);



        for(int i=0; i<isTalent.length; i++)
            if(isTalent[i])
            {
                iv_talent[i].setColorFilter(getContext().getColor(R.color.txt_istalent));
                tv_talent[i].setTextColor(getContext().getColor(R.color.txt_istalent));
                v_talent[i].setBackgroundColor(WHITE);
                tv_talent[i].setTextSize(getResources().getDimension(R.dimen.isTalent_txt)/getResources().getDisplayMetrics().density);
                tv_talent[i].setTypeface(Typeface.DEFAULT_BOLD);
                iv_talent[i].setLayoutParams(parms_talent);
            }
            else
            {
                iv_talent[i].setColorFilter(getContext().getColor(R.color.txt_isnottalent));
                tv_talent[i].setTextColor(getContext().getColor(R.color.txt_isnottalent));
                v_talent[i].setBackgroundColor(WHITE);
                tv_talent[i].setTextSize(getResources().getDimension(R.dimen.isnotTalent_txt)/getResources().getDisplayMetrics().density);
                tv_talent[i].setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                iv_talent[i].setLayoutParams(parms_nontalent);

            }
    }





}
