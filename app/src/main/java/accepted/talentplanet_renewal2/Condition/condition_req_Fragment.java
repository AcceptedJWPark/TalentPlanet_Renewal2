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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import accepted.talentplanet_renewal2.R;


/**
 * Created by Accepted on 2019-05-05.
 */

public class condition_req_Fragment extends android.support.v4.app.Fragment {
    boolean ismymentor;
    String MentorID, MentorName, MentorBirth, MentorGender;
    String MenteeID, MenteeName, MenteeBirth, MenteeGender;
    String CreationDate;
    public condition_req_Fragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_condition_req_fragment, container, false);

        Button btn_cancel_mentor = ((Button)layout.findViewById(R.id.btn_cancel_mentor_req_condition));
        Button btn_next_mentor = ((Button)layout.findViewById(R.id.btn_next_mentor_req_condition));
        Button btn_cancel_mentee = ((Button)layout.findViewById(R.id.btn_cancel_mentee_req_condition));
        Button btn_next_mentee = ((Button)layout.findViewById(R.id.btn_next_mentee_req_condition));
        TextView tv_mentor = ((TextView)layout.findViewById(R.id.tv_mentor_req_condition));
        TextView tv_mentee = ((TextView)layout.findViewById(R.id.tv_mentee_req_condition));
        if(getArguments() != null) {
            CreationDate = getArguments().getString("CREATION_DATE");
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

        return layout;
    }

    public boolean ismymentor() {
        return ismymentor;
    }
    public void setIsmymentor(boolean ismymentor) {
        this.ismymentor = ismymentor;
    }


    public void conditionbtnBgr(TextView tvmentor, TextView tvmentee, final Button btnmentorCancel, final Button btnmentorNext, final Button btnmenteeCancel, final Button btnmenteeNext, LinearLayout layout) {
        if (ismymentor()) {

            ((TextView)layout.findViewById(R.id.tv_name_mentor_req_condition)).setText(MentorName);
            if(MentorGender.equals("남")) {
                ((ImageView) layout.findViewById(R.id.img_gender_mentor_req_condition)).setImageDrawable(getResources().getDrawable(R.drawable.icon_male));
            }else{
                ((ImageView) layout.findViewById(R.id.img_gender_mentor_req_condition)).setImageDrawable(getResources().getDrawable(R.drawable.icon_female));
            }
            ((TextView)layout.findViewById(R.id.tv_birth_mentor_req_condition)).setText(MentorBirth);

            ((ImageView)layout.findViewById(R.id.cimg_pic_mentor_req_condition)).setOnClickListener(new View.OnClickListener() {
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

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            try {
                Date cd = new Date(Long.parseLong(CreationDate));
                Calendar calendar = Calendar.getInstance();

                calendar.setTime(cd);

                calendar.add(Calendar.DATE, 3);

                SimpleDateFormat sdf2 = new SimpleDateFormat("yy/MM/dd HH:mm a");

                tvmentor.setText("Mentor (상대방)");
                tvmentee.setText("Mentee (나)");
                btnmentorCancel.setVisibility(View.GONE);
                btnmenteeCancel.setVisibility(View.GONE);
                unactivebgr(btnmentorNext);
                unactivebgr(btnmenteeNext);
                btnmentorNext.setText("진행 대기 중...");
                btnmenteeNext.setText(sdf2.format(calendar.getTime()) + " 자동 삭제");

            }catch(Exception e){
                e.printStackTrace();
            }

        }
        else
        {
            ((TextView)layout.findViewById(R.id.tv_name_mentee_req_condition)).setText(MenteeName);
            if(MenteeGender.equals("남")) {
                ((ImageView) layout.findViewById(R.id.img_gender_mentee_req_condition)).setImageDrawable(getResources().getDrawable(R.drawable.icon_male));
            }else{
                ((ImageView) layout.findViewById(R.id.img_gender_mentee_req_condition)).setImageDrawable(getResources().getDrawable(R.drawable.icon_female));
            }
            ((TextView)layout.findViewById(R.id.tv_birth_mentee_req_condition)).setText(MenteeBirth);

            ((ImageView)layout.findViewById(R.id.cimg_pic_mentee_req_condition)).setOnClickListener(new View.OnClickListener() {
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

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            try {
                Date cd = new Date(Long.parseLong(CreationDate));
                Calendar calendar = Calendar.getInstance();

                calendar.setTime(cd);

                calendar.add(Calendar.DATE, 3);

                SimpleDateFormat sdf2 = new SimpleDateFormat("yy/MM/dd HH:mm a");

                btnmentorCancel.setVisibility(View.VISIBLE);
                btnmenteeCancel.setVisibility(View.GONE);
                tvmentor.setText("Mentor (나)");
                tvmentee.setText("Mentee (상대방)");

                activebgr(btnmentorNext);
                subbgr(btnmenteeCancel);
                unactivebgr(btnmenteeNext);

                btnmentorNext.setText("진행");
                btnmenteeCancel.setText("삭제");
                btnmenteeNext.setText(sdf2.format(calendar.getTime()) + " 자동 삭제");

            }catch(Exception e){
                e.printStackTrace();
            }
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

}
