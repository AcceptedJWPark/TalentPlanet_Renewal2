package accepted.talentplanet_renewal2.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import accepted.talentplanet_renewal2.Classes.TalentObject_Home;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;

/**
 * Created by Accepted on 2019-05-05.
 */

public class Talent_SecondFragment extends android.support.v4.app.Fragment {

    LinearLayout layout;

    private String CateCode;
    private String isMentor;
    private Map<String, TalentObject_Home> talentMap;
    private ArrayList<TalentObject_Home> arrTalent;
    private String talentFlag;
    private String talentName;
    private boolean inPerson;
    private int imgSrc;

    public Talent_SecondFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            CateCode = getArguments().getString("CateCode");
            isMentor = getArguments().getString("isMentor");
            inPerson = getArguments().getBoolean("isMentor");
        }

        // 카테고리 정보
        makeTestTalentArr();
        getCateList();

        layout = (LinearLayout) inflater.inflate(R.layout.activity_profile_fragment2, container, false);
        ((TextView) layout.findViewById(R.id.tv_profile_talant)).setText(getArguments().getString("profileText"));

        Log.d("inPerson", String.valueOf(inPerson));
        if (!inPerson) {
            ((TextView) layout.findViewById(R.id.tv_user_data_edit)).setVisibility(View.GONE);
        }

        TextView editBtn = (TextView) layout.findViewById(R.id.tv_user_data_edit);
        editBtn.setOnClickListener(new View.OnClickListener() {
            // 유저의 정보 수정 이벤트 부여
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , MainActivity_Detail.class);
                intent.putExtra("cateCode", CateCode);
                intent.putExtra("isMentor", isMentor);
                startActivityForResult(intent, 3000);
            }
        });

        return layout;
    }

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
        RequestQueue postRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
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
                    for (int i=0;i<arrTalent.size();i++) {
                        String aCateCode = Integer.toString(arrTalent.get(i).getCateCode());
                        if(aCateCode == CateCode) {
                            talentName = arrTalent.get(i).getTitle();
                            imgSrc = arrTalent.get(i).getBackgroundResourceID();
                            // 재능별 백그라운드 이미지 변경
                            ImageView iv_profile_fragment2 = (ImageView) layout.findViewById(R.id.iv_profile_fragment2);
                            Glide.with(getActivity().getApplicationContext()).load(imgSrc).into(iv_profile_fragment2);
                        }
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
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }
}
