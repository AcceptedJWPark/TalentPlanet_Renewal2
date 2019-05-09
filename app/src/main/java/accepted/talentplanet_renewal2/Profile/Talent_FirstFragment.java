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

import accepted.talentplanet_renewal2.Classes.TalentObject;
import accepted.talentplanet_renewal2.R;

import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Accepted on 2019-05-05.
 */

public class Talent_FirstFragment extends android.support.v4.app.Fragment {

    private ArrayList<TalentObject> arrTalent;
    public Talent_FirstFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_first, container, false);
        makeTestTalentArr();

        LinearLayout ll_container = layout.findViewById(R.id.ll_container);
        makeLayout(ll_container);
        return layout;
    }

    private void makeLayout(LinearLayout layout){
        LinearLayout root = (LinearLayout)layout.findViewById(R.id.ll_container);
        LinearLayout row = null;
        root.setMinimumWidth(MATCH_PARENT);
        root.setMinimumHeight(MATCH_PARENT);
        // row layout 의 Parameter 정의
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(MATCH_PARENT, 0);
        rowParams.weight = 1;
        rowParams.bottomMargin = convertDPtoInteger(3);

        // 하나의 객체의 Layout Parameter 정의
        LinearLayout.LayoutParams objectParams = new LinearLayout.LayoutParams(0, MATCH_PARENT);
        objectParams.weight = 1;

        // 객체 background image 의 Parameter 정의
        LinearLayout.LayoutParams bgImgParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        bgImgParams.gravity = CENTER;

        // 기본 Background View 의 Parameter 정의
        LinearLayout.LayoutParams basicViewParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);

        // 텍스트 및 돌출 이미지에 대한 LinearLayout Parameter 정의
        RelativeLayout.LayoutParams linearParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        linearParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        // 돌출 이미지에 대한 Parameter 정의
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(convertDPtoInteger(40), convertDPtoInteger(40));

        // 돌출 텍스트에 대한 Parameter 정의
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        textParams.topMargin = convertDPtoInteger(10);

        int rowNum = 0;

        for (int i = 0; i < arrTalent.size(); i++){
            if(i % 3 == 0){
                row = new LinearLayout(getContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
            }

            TalentObject obj = arrTalent.get(i);
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

            View basicView = new View(getActivity().getApplicationContext());
            basicView.setBackgroundColor(Color.parseColor("#68000000"));

            LinearLayout linear = new LinearLayout(getActivity().getApplicationContext());
            linear.setGravity(CENTER);
            linear.setOrientation(LinearLayout.VERTICAL);

            ImageView imgView = new ImageView(getActivity().getApplicationContext());
            imgView.setScaleType(ImageView.ScaleType.FIT_XY);
            imgView.setImageDrawable(getResources().getDrawable(obj.getIconResourceID()));

            TextView textView = new TextView(getActivity().getApplicationContext());
            textView.setText(obj.getTitle());
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(convertDPtoInteger(5));
            textView.setTypeface(Typeface.DEFAULT_BOLD);

            linear.addView(imgView, imgParams);
            linear.addView(textView, textParams);

            rl.addView(bgImgView, bgImgParams);
            rl.addView(basicView, basicViewParams);
            rl.addView(linear, linearParams);

            row.addView(rl, objectParams);
            if(i == arrTalent.size() - 1 || i % 3 == 2){
                root.addView(row, rowParams);
                rowNum++;
            }
        }

    }

    // dp 를 Int 값으로 변환
    private int convertDPtoInteger(int dp){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    // 테스트용 배열 생성 함수
    private void makeTestTalentArr(){
        arrTalent = new ArrayList<>();

        TalentObject career = new TalentObject("취업", R.drawable.pic_career, R.drawable.icon_career);
        TalentObject study = new TalentObject("학습", R.drawable.pic_study, R.drawable.icon_study);
        TalentObject money = new TalentObject("재테크", R.drawable.pic_money, R.drawable.icon_money);
        TalentObject it = new TalentObject("IT", R.drawable.pic_it, R.drawable.icon_it);
        TalentObject camera = new TalentObject("사진", R.drawable.pic_camera, R.drawable.icon_camera);
        TalentObject music = new TalentObject("음악", R.drawable.pic_music, R.drawable.icon_music);
        TalentObject design = new TalentObject("미술/디자인", R.drawable.pic_design, R.drawable.icon_design);
        TalentObject sports = new TalentObject("운동", R.drawable.pic_sports, R.drawable.icon_sports);
        TalentObject living = new TalentObject("생활", R.drawable.pic_living, R.drawable.icon_living);
        TalentObject beauty = new TalentObject("뷰티/패션", R.drawable.pic_beauty, R.drawable.icon_beauty);
        TalentObject volunteer = new TalentObject("사회봉사", R.drawable.pic_volunteer, R.drawable.icon_volunteer);
        TalentObject travel = new TalentObject("여행", R.drawable.pic_travel, R.drawable.icon_travel);
        TalentObject culture = new TalentObject("문화", R.drawable.pic_culture, R.drawable.icon_culture);
        TalentObject game = new TalentObject("게임", R.drawable.pic_game, R.drawable.icon_game);

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
