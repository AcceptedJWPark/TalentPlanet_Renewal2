package accepted.talentplanet_renewal2.Profile;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

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

/**
 * Created by Accepted on 2019-05-05.
 */

public class Talent_FirstFragment extends android.support.v4.app.Fragment {

    private ArrayList<TalentObject_Profile> arrTalent;
    public Talent_FirstFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_profile_fragment1, container, false);
        makeTestTalentArr();

        makeLayout(layout);
        return layout;
    }

    private void makeLayout(LinearLayout layout){
        LinearLayout root = (LinearLayout)layout.findViewById(R.id.ll_container_profile);
        LinearLayout row = null;
        root.setMinimumWidth(MATCH_PARENT);
        root.setMinimumHeight(MATCH_PARENT);
        // row layout 의 Parameter 정의
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(MATCH_PARENT, 0);
        rowParams.weight = 1;
        rowParams.bottomMargin = (int) getResources().getDimension(R.dimen.size_5dp);

        // 하나의 객체의 Layout Parameter 정의
        LinearLayout.LayoutParams objectParams = new LinearLayout.LayoutParams(0, MATCH_PARENT);
        objectParams.weight = 1;
        objectParams.rightMargin = (int) getResources().getDimension(R.dimen.size_5dp);

        // 객체 background image 의 Parameter 정의
        LinearLayout.LayoutParams bgImgParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        bgImgParams.gravity = CENTER;

        // 텍스트 및 돌출 이미지에 대한 LinearLayout Parameter 정의
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);

        // 돌출 텍스트에 대한 Parameter 정의
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);

        int rowNum = 0;

        for (int i = 0; i < arrTalent.size(); i++){
            if(i % 3 == 0){
                row = new LinearLayout(getContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
            }

            TalentObject_Profile obj = arrTalent.get(i);
            RelativeLayout rl = new RelativeLayout(getActivity().getApplicationContext());

            ImageView bgImgView = new ImageView(getActivity().getApplicationContext());
            //bgImgView.setBackground(getResources().getDrawable(R.drawable.pic_career));
            Glide.with(this).load(obj.getBackgroundResourceID()).into(new ViewTarget<ImageView, GlideDrawable>(bgImgView) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation anim) {
                    ImageView myView = this.view;
                    // Set your resource on myView and/or start your animation here.
                    myView.setBackground(resource);
                }
            });

            LinearLayout linear = new LinearLayout(getActivity().getApplicationContext());
            TextView textView = new TextView(getActivity().getApplicationContext());
            View basicView = new View(getActivity().getApplicationContext());

            textView.setVisibility(GONE);
            bgImgView.setAlpha(0.1f);

            //등록된 재능
            if(i % 4 == 0) {
//                linear.setGravity(BOTTOM);
//                linear.setOrientation(LinearLayout.VERTICAL);
//
//                textView.setText(obj.getTitle());
//                textView.setTextColor(Color.WHITE);
//                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.isTalent_Txt));
//                textView.setPadding(0,(int) getResources().getDimension(R.dimen.size_5dp),0, (int) getResources().getDimension(R.dimen.size_5dp));
//                textView.setTypeface(Typeface.DEFAULT_BOLD);
//                textView.setBackgroundColor(Color.argb(200,27,27,99));
//                textView.setGravity(CENTER);
            } else {
                //등록이 안된 재능
                textView.setVisibility(GONE);
                bgImgView.setAlpha(0.1f);
            }

            linear.addView(textView, textParams);
            rl.addView(bgImgView, bgImgParams);
            rl.addView(linear, linearParams);

            row.addView(rl, objectParams);
            if(i == arrTalent.size() - 1 || i % 3 == 2){
                root.addView(row, rowParams);
                rowNum++;
            }
        }
    }


    // 테스트용 배열 생성 함수
    private void makeTestTalentArr(){
        arrTalent = new ArrayList<>();

        TalentObject_Profile career = new TalentObject_Profile("취업", R.drawable.pic_career);
        TalentObject_Profile study = new TalentObject_Profile("학습", R.drawable.pic_study);
        TalentObject_Profile money = new TalentObject_Profile("재테크", R.drawable.pic_money);
        TalentObject_Profile it = new TalentObject_Profile("IT", R.drawable.pic_it);
        TalentObject_Profile camera = new TalentObject_Profile("사진", R.drawable.pic_camera);
        TalentObject_Profile music = new TalentObject_Profile("음악", R.drawable.pic_music);
        TalentObject_Profile design = new TalentObject_Profile("미술/디자인", R.drawable.pic_design);
        TalentObject_Profile sports = new TalentObject_Profile("운동", R.drawable.pic_sports);
        TalentObject_Profile living = new TalentObject_Profile("생활", R.drawable.pic_living);
        TalentObject_Profile beauty = new TalentObject_Profile("뷰티/패션", R.drawable.pic_beauty);
        TalentObject_Profile volunteer = new TalentObject_Profile("사회봉사", R.drawable.pic_volunteer);
        TalentObject_Profile travel = new TalentObject_Profile("여행", R.drawable.pic_travel);
        TalentObject_Profile culture = new TalentObject_Profile("문화", R.drawable.pic_culture);
        TalentObject_Profile game = new TalentObject_Profile("게임", R.drawable.pic_game);

        arrTalent.add(career);
        arrTalent.add(study);
        arrTalent.add(money);
        arrTalent.add(it);
        arrTalent.add(camera);
        arrTalent.add(music);
        arrTalent.add(design);
        arrTalent.add(sports);
        arrTalent.add(living);
        arrTalent.add(beauty);
        arrTalent.add(volunteer);
        arrTalent.add(travel);
        arrTalent.add(culture);
        arrTalent.add(game);

        long seed = System.nanoTime();
        Collections.shuffle(arrTalent, new Random(seed));
    }
}
