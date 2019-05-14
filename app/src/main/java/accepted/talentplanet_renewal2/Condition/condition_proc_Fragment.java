package accepted.talentplanet_renewal2.Condition;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

public class condition_proc_Fragment extends android.support.v4.app.Fragment {
    boolean ismymentor;
    int condition;
    boolean ismentorComplete;

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

        conditionbtnBgr(tv_mentor, tv_mentee ,btn_cancel_mentor, btn_next_mentor,btn_cancel_mentee,btn_next_mentee);

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

    public void conditionbtnBgr(TextView tvmentor, TextView tvmentee, final Button btnmentorCancel, final Button btnmentorNext, final Button btnmenteeCancel, final Button btnmenteeNext) {
        if (ismymentor()) {
            tvmentor.setText("Mentor (상대방)");
            tvmentee.setText("Mentee (나)");

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
            }
        }
        else
        {
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

}
