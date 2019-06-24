package accepted.talentplanet_renewal2.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import accepted.talentplanet_renewal2.Classes.TalentObject_Home;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;

import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.CENTER;
import static android.view.View.GONE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Accepted on 2019-05-05.
 */

public class Talent_FirstFragment extends android.support.v4.app.Fragment {

    String isMentor;
    Context mContext;
    private ArrayList<TalentObject_Home> arrTalent;
    private Map<String, TalentObject_Home> talentMap;
    private LinearLayout layout;
    talentlist_viewpager vp;

    public Talent_FirstFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null) {
            isMentor = getArguments().getString("isMentor");
        }

        mContext = getActivity().getApplicationContext();

        layout = (LinearLayout) inflater.inflate(R.layout.activity_profile_fragment1, container, false);
        vp = container.findViewById(R.id.vp_profile_mentor);

        // 가상 데이터
        makeTestTalentArr();
        // 위의 데이터 재정렬
        getCateList();

//        makeLayout(layout);
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
            // 재능목록 하나
            final TalentObject_Home obj = arrTalent.get(i);

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

            // 유저 등록 재능 카테고리 리스트
            ArrayList<String> cateCodeArr = getArguments().getStringArrayList("cateCodeArr");
            if (cateCodeArr.contains(Integer.toString(obj.getCateCode()))) {
                //등록된 재능
                linear.setGravity(BOTTOM);
                linear.setOrientation(LinearLayout.VERTICAL);

                textView.setText(obj.getTitle());
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.isTalent_Txt));
                textView.setPadding(0,(int) getResources().getDimension(R.dimen.size_5dp),0, (int) getResources().getDimension(R.dimen.size_5dp));
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setBackgroundColor(Color.argb(200,27,27,99));
                textView.setGravity(CENTER);
            } else {
                // 그 외
                textView.setVisibility(GONE);
                bgImgView.setAlpha(0.1f);
            }

            linear.addView(textView, textParams);
            rl.addView(bgImgView, bgImgParams);
            rl.addView(linear, linearParams);
            final int talentIdx = i + 1;
            if (cateCodeArr.contains(Integer.toString(obj.getCateCode()))) {
                rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("talentIdx", String.valueOf(talentIdx) );
                        vp.setCurrentItem(talentIdx);
                    }
                });
            } else {
                rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 리스트상에서 비활성화 되어있는 재능들의 이벤트
                        Intent intent = new Intent(getActivity(), MainActivity_NewTalent.class);
                        intent.putExtra("talentName", obj.getTitle());
                        intent.putExtra("cateCode", Integer.toString(obj.getCateCode()));
                        intent.putExtra("talentFlag", isMentor);
                        intent.putExtra("imgSrc", obj.getBackgroundResourceID());
                        intent.putExtra("isMentor", isMentor);
                        startActivityForResult(intent, 3000);
                    }
                });
            }


            row.addView(rl, objectParams);
            if(i == arrTalent.size() - 1 || i % 3 == 2){
                root.addView(row, rowParams);
                rowNum++;
            }
        }
    }


    // 테스트용 배열 생성 함수
    private void makeTestTalentArr(){
        talentMap = new HashMap();

        TalentObject_Home career = new TalentObject_Home("취업", R.drawable.pic_career,R.drawable.icon_career, 0);
        TalentObject_Home study = new TalentObject_Home("학습", R.drawable.pic_study,R.drawable.icon_study, 0);
        TalentObject_Home money = new TalentObject_Home("재테크", R.drawable.pic_money,R.drawable.icon_money, 0);
        TalentObject_Home it = new TalentObject_Home("IT", R.drawable.pic_it,R.drawable.icon_it, 0);
        TalentObject_Home camera = new TalentObject_Home("사진", R.drawable.pic_camera,R.drawable.icon_camera, 0);
        TalentObject_Home music = new TalentObject_Home("음악", R.drawable.pic_music,R.drawable.icon_music, 0);
        TalentObject_Home design = new TalentObject_Home("미술/디자인", R.drawable.pic_design,R.drawable.icon_design, 0);
        TalentObject_Home sports = new TalentObject_Home("운동", R.drawable.pic_sports,R.drawable.icon_sports, 0);
        TalentObject_Home living = new TalentObject_Home("생활", R.drawable.pic_living,R.drawable.icon_living, 0);
        TalentObject_Home beauty = new TalentObject_Home("뷰티/패션", R.drawable.pic_beauty,R.drawable.icon_beauty, 0);
        TalentObject_Home volunteer = new TalentObject_Home("사회봉사", R.drawable.pic_volunteer,R.drawable.icon_volunteer, 0);
        TalentObject_Home travel = new TalentObject_Home("여행", R.drawable.pic_travel,R.drawable.icon_travel, 0);
        TalentObject_Home culture = new TalentObject_Home("문화", R.drawable.pic_culture,R.drawable.icon_culture, 0);
        TalentObject_Home game = new TalentObject_Home("게임", R.drawable.pic_game,R.drawable.icon_game, 0);

        talentMap.put(career.getTitle(), career);
        talentMap.put(study.getTitle(), study);
        talentMap.put(money.getTitle(), money);
        talentMap.put(it.getTitle(), it);
        talentMap.put(camera.getTitle(), camera);
        talentMap.put(music.getTitle(), music);
        talentMap.put(design.getTitle(), design);
        talentMap.put(sports.getTitle(), sports);
        talentMap.put(living.getTitle(), living);
        talentMap.put(beauty.getTitle(), beauty);
        talentMap.put(volunteer.getTitle(), volunteer);
        talentMap.put(travel.getTitle(), travel);
        talentMap.put(culture.getTitle(), culture);
        talentMap.put(game.getTitle(), game);

//        long seed = System.nanoTime();
//        Collections.shuffle(arrTalent, new Random(seed));
    }

    private void getCateList(){
        arrTalent = new ArrayList<>();
        RequestQueue postRequestQueue = Volley.newRequestQueue(mContext);
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "TalentSharing/getTalentCateList.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        TalentObject_Home talentObject = talentMap.get(obj.getString("CateName"));
                        talentObject.setCateCode((int)obj.getLong("CateCode"));
                        talentObject.setTalentCount((int)obj.getLong("RegistCount"));
                        arrTalent.add(talentObject);
                    }
                    if(isAdded()) {
                        makeLayout(layout);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(getActivity().getApplicationContext())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("TalentFlag", isMentor);
                params.put("UserID", SaveSharedPreference.getUserId(mContext));
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }
}
