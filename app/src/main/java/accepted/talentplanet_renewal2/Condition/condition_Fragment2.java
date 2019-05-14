package accepted.talentplanet_renewal2.Condition;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import accepted.talentplanet_renewal2.R;


/**
 * Created by Accepted on 2019-05-05.
 */

public class condition_Fragment2 extends android.support.v4.app.Fragment {
    boolean ismymentor;
    int condition;
    boolean ismentorComplete = false;

    public condition_Fragment2() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_condition_fragment, container, false);

        Button btn_cancel_mentor = ((Button)layout.findViewById(R.id.btn_cancel_mentor_condition));
        Button btn_next_mentor = ((Button)layout.findViewById(R.id.btn_next_mentor_condition));
        Button btn_cancel_mentee = ((Button)layout.findViewById(R.id.btn_cancel_mentee_condition));
        Button btn_next_mentee = ((Button)layout.findViewById(R.id.btn_next_mentee_condition));
        TextView tv_mentor = ((TextView)layout.findViewById(R.id.tv_mentor_condition));
        TextView tv_mentee = ((TextView)layout.findViewById(R.id.tv_mentee_condition));

        conditionbtnBgr(tv_mentor, tv_mentee, btn_cancel_mentor,btn_next_mentor,btn_cancel_mentee,btn_next_mentee);

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

    public void conditionbtnBgr(TextView tvmentor, TextView tvmentee, final Button btnmentorCancel, final Button btnmentorNext, final Button btnmenteeCancel, final Button btnmenteeNext) {
        if (ismymentor) {

            tvmentor.setText("Mentor (상대방)");
            tvmentee.setText("Mentee (나)");
            btnmentorCancel.setVisibility(View.GONE);
            unactivebgr(btnmentorNext);

            if (condition == 0) {
                btnmenteeCancel.setVisibility(View.GONE);
                unactivebgr(btnmenteeNext);
                btnmentorNext.setText("진행 대기 중...");
                btnmenteeNext.setText("19/05/13 17:00PM 자동 삭제");
            } else
                {
                if (!ismentorComplete) {
                    btnmenteeCancel.setVisibility(View.VISIBLE);
                    subbgr(btnmenteeCancel);
                    unactivebgr(btnmenteeNext);
                    btnmenteeNext.setText("완료");
                    btnmenteeCancel.setText("신고하기");
                    btnmentorNext.setText("상대방 완료를 기다립니다.");

                } else {
                    activebgr(btnmenteeNext);
                    btnmentorNext.setText("회원님의 완료를 기다립니다.");
                    btnmenteeNext.setText("완료");
                    btnmenteeCancel.setText("신고하기");
                }
            }
        }
        else
        {
            tvmentor.setText("Mentor (나)");
            tvmentee.setText("Mentee (상대방)");
            unactivebgr(btnmenteeNext);

            if(condition==0) {
                btnmenteeCancel.setVisibility(View.GONE);
                btnmentorCancel.setVisibility(View.VISIBLE);
                subbgr(btnmentorCancel);
                activebgr(btnmentorNext);
                btnmentorNext.setText("진행");
                btnmentorCancel.setText("삭제");
                btnmenteeNext.setText("19/05/13 17:00PM 자동 삭제");
            }
            else
            {
                btnmenteeCancel.setVisibility(View.GONE);
                btnmentorCancel.setVisibility(View.GONE);
                activebgr(btnmentorNext);
                btnmentorNext.setText("완료");
                btnmenteeNext.setText("회원님의 완료를 기다립니다.");

            btnmentorNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
