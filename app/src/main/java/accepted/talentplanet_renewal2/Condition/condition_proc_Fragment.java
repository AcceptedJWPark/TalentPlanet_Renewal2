package accepted.talentplanet_renewal2.Condition;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.VolleySingleton;


/**
 * Created by Accepted on 2019-05-05.
 */

public class condition_proc_Fragment extends android.support.v4.app.Fragment {
    boolean ismymentor;
    int condition;
    boolean ismentorComplete;
    String MentorID, MentorName, MentorBirth, MentorGender;
    String MenteeID, MenteeName, MenteeBirth, MenteeGender;
    String Code;

    public condition_proc_Fragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_condition_proc_fragment, container, false);

        Button btn_next_mentor = ((Button)layout.findViewById(R.id.btn_next_mentor_proc_condition));
        Button btn_cancel_mentor = ((Button)layout.findViewById(R.id.btn_cancel_mentor_proc_condition));
        Button btn_next_mentee = ((Button)layout.findViewById(R.id.btn_next_mentee_proc_condition));
        Button btn_cancel_mentee = ((Button)layout.findViewById(R.id.btn_cancel_mentee_proc_condition));
        TextView tv_mentor = ((TextView)layout.findViewById(R.id.tv_mentor_proc_condition));
        TextView tv_mentee = ((TextView)layout.findViewById(R.id.tv_mentee_proc_condition));

        if(getArguments() != null) {
            Code = getArguments().getString("Code");
            if (ismymentor) {
                MentorID = getArguments().getString("MentorID");
                MentorName = getArguments().getString("MentorName");
                MentorBirth = getArguments().getString("MentorBirth");
                MentorGender = getArguments().getString("MentorGender");
            } else {
                MenteeID = getArguments().getString("MenteeID");
                MenteeName = getArguments().getString("MenteeName");
                MenteeBirth = getArguments().getString("MenteeBirth");
                MenteeGender = getArguments().getString("MenteeGender");
            }
        }

        conditionbtnBgr(tv_mentor, tv_mentee, btn_cancel_mentor,btn_next_mentor,btn_cancel_mentee,btn_next_mentee, layout);

        Log.d("condition", String.valueOf(getCondition()));
        return layout;
    }

    public boolean ismymentor() {
        return ismymentor;
    }

    public void setIsmymentor(boolean ismymentor) {
        this.ismymentor = ismymentor;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }


    public boolean isIsmentorComplete() {
        return ismentorComplete;
    }

    public void setIsmentorComplete(boolean ismentorComplete) {
        this.ismentorComplete = ismentorComplete;
    }

    public void conditionbtnBgr(TextView tvmentor, TextView tvmentee, final Button btnmentorCancel, final Button btnmentorNext, final Button btnmenteeCancel, final Button btnmenteeNext, LinearLayout layout) {
        if (ismymentor()) {
            tvmentor.setText("Mentor (상대방)");
            tvmentee.setText("Mentee (나)");
            ((TextView)layout.findViewById(R.id.tv_name_mentor_proc_condition)).setText(MentorName);
            if(MentorGender.equals("남")) {
                ((ImageView) layout.findViewById(R.id.img_gender_mentor_proc_condition)).setImageDrawable(getResources().getDrawable(R.drawable.icon_male));
            }else{
                ((ImageView) layout.findViewById(R.id.img_gender_mentor_proc_condition)).setImageDrawable(getResources().getDrawable(R.drawable.icon_female));
            }
            ((TextView)layout.findViewById(R.id.tv_birth_mentor_proc_condition)).setText(MentorBirth);

            ((ImageView)layout.findViewById(R.id.cimg_pic_mentor_proc_condition)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), accepted.talentplanet_renewal2.Profile.MainActivity_Profile.class);
                    SimpleDateFormat sdf = new SimpleDateFormat("YYYY");
                    int age = Integer.parseInt(sdf.format(new Date())) - Integer.parseInt(MentorBirth.split("-")[0]) + 1;
                    String userInfo = MentorBirth + " / " + age + "세";

                    intent.putExtra("userName", MentorName);
                    intent.putExtra("userInfo", userInfo);
                    intent.putExtra("targetUserID", MentorID);
                    intent.putExtra("userID", MentorID);
                    startActivity(intent);
                }
            });

            if (!ismentorComplete) {
                unactivebgr(btnmentorNext);
                unactivebgr(btnmenteeNext);
                btnmentorCancel.setVisibility(View.GONE);
                btnmenteeCancel.setVisibility(View.GONE);
                btnmentorNext.setText("상대방 완료를 기다립니다.");
                btnmenteeNext.setText("완료");

            } else {
                unactivebgr(btnmentorNext);
                activebgr(btnmenteeNext);
                subbgr(btnmentorCancel);

                btnmentorCancel.setVisibility(View.VISIBLE);
                btnmenteeCancel.setVisibility(View.GONE);

                btnmentorNext.setText("완료 대기 중...");
                btnmentorCancel.setText("신고하기");
                btnmenteeNext.setText("완료");
                btnmenteeNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateMatchedFlag("C");
                    }
                });
            }
        }
        else
        {
            ((TextView)layout.findViewById(R.id.tv_name_mentee_proc_condition)).setText(MenteeName);
            if(MenteeGender.equals("남")) {
                ((ImageView) layout.findViewById(R.id.img_gender_mentee_proc_condition)).setImageDrawable(getResources().getDrawable(R.drawable.icon_male));
            }else{
                ((ImageView) layout.findViewById(R.id.img_gender_mentee_proc_condition)).setImageDrawable(getResources().getDrawable(R.drawable.icon_female));
            }
            ((TextView)layout.findViewById(R.id.tv_birth_mentee_proc_condition)).setText(MenteeBirth);

            ((ImageView)layout.findViewById(R.id.cimg_pic_mentee_proc_condition)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), accepted.talentplanet_renewal2.Profile.MainActivity_Profile.class);
                    SimpleDateFormat sdf = new SimpleDateFormat("YYYY");
                    int age = Integer.parseInt(sdf.format(new Date())) - Integer.parseInt(MenteeBirth.split("-")[0]) + 1;
                    String userInfo = MenteeBirth + " / " + age + "세";

                    intent.putExtra("userName", MenteeName);
                    intent.putExtra("userInfo", userInfo);
                    intent.putExtra("targetUserID", MenteeID);
                    intent.putExtra("userID", MenteeID);
                    startActivity(intent);
                }
            });

            tvmentor.setText("Mentor (나)");
            tvmentee.setText("Mentee (상대방)");
            btnmentorCancel.setVisibility(View.GONE);
            btnmenteeCancel.setVisibility(View.GONE);
            activebgr(btnmentorNext);
            unactivebgr(btnmenteeNext);
            btnmentorNext.setText("완료");
            btnmenteeNext.setText("회원님의 완료를 기다립니다.");

            btnmentorNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateMatchedFlag("H");
                    btnmenteeCancel.setVisibility(View.VISIBLE);
                    unactivebgr(btnmentorNext);
                    subbgr(btnmenteeCancel);
                    btnmenteeCancel.setText("신고하기");
                    btnmentorNext.setText("완료");
                    btnmenteeNext.setText("완료 대기 중...");
                }
            });
        }
    }

    public void unactivebgr(Button btn)
    {
        btn.setBackgroundResource(R.drawable.bgr_unactivebtn);
        btn.setTextColor(Color.argb(255, 190, 190, 190));
    }

    public void subbgr(Button btn)
    {
        btn.setBackgroundResource(R.drawable.bgr_subbutton2);
        btn.setTextColor(getResources().getColor(R.color.bgr_mainColor));
    }
    public void activebgr(Button btn)
    {
        btn.setBackgroundResource(R.drawable.bgr_clickedbtn);
        btn.setTextColor(Color.WHITE);
    }

    public void updateMatchedFlag(final String matchedFlag) {
        RequestQueue postRequestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "TalentCondition/updateMatchedFlag.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("result").equals("success")){
                        Toast.makeText(getContext(), "완료되었습니다.", Toast.LENGTH_SHORT);
                    }else{
                        Toast.makeText(getContext(), "실패했습니다.", Toast.LENGTH_SHORT);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(getContext())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("MatchedFlag", matchedFlag);
                params.put("Code", Code);
                return params;
            }
        };
        postRequestQueue.add(postJsonRequest);
    }

}
