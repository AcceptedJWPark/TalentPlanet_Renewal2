package accepted.talentplanet_renewal2.Profile;

import android.content.Context;
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
    private String Name;
    private String profileText;
    private String targetUserID;
    private boolean inPerson = false;
    private String imgSrc;
    private String ProfileSendFlag;
    private String MatchingFlag;
    private Context mContext;

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
            Name = getArguments().getString("Name");
            profileText = getArguments().getString("profileText");
            CateCode = getArguments().getString("CateCode");
            isMentor = getArguments().getString("isMentor");
            inPerson = getArguments().getBoolean("inPerson");
            targetUserID = getArguments().getString("targetUserID");
            imgSrc = getArguments().getString("BackgroundID");
            ProfileSendFlag = getArguments().getString("ProfileSendFlag");
            MatchingFlag = getArguments().getString("MatchingFlag");
            Log.d("CheckFlag", ProfileSendFlag + " : " + MatchingFlag);
        }

        mContext = getActivity().getApplicationContext();

        layout = (LinearLayout) inflater.inflate(R.layout.activity_profile_fragment2, container, false);

        if (inPerson == false) {
            ((TextView) layout.findViewById(R.id.tv_user_data_edit)).setVisibility(View.GONE);
        }



        ImageView iv_profile_fragment2 = (ImageView) layout.findViewById(R.id.iv_profile_fragment2);
        Glide.with(mContext).load(getResources().getIdentifier(imgSrc,"drawable", getActivity().getPackageName())).into(iv_profile_fragment2);

        ((TextView) layout.findViewById(R.id.tv_profile_talant)).setText(profileText);

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
}
