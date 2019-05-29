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

public class condition_req_Fragment extends android.support.v4.app.Fragment {
    boolean ismymentor;

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

        conditionbtnBgr(tv_mentor, tv_mentee, btn_cancel_mentor,btn_next_mentor,btn_cancel_mentee,btn_next_mentee);

        return layout;
    }

    public boolean ismymentor() {
        return ismymentor;
    }
    public void setIsmymentor(boolean ismymentor) {
        this.ismymentor = ismymentor;
    }


    public void conditionbtnBgr(TextView tvmentor, TextView tvmentee, final Button btnmentorCancel, final Button btnmentorNext, final Button btnmenteeCancel, final Button btnmenteeNext) {
        if (ismymentor()) {

            tvmentor.setText("Mentor (상대방)");
            tvmentee.setText("Mentee (나)");
            btnmentorCancel.setVisibility(View.GONE);
            btnmenteeCancel.setVisibility(View.GONE);
            unactivebgr(btnmentorNext);
            unactivebgr(btnmenteeNext);
            btnmentorNext.setText("진행 대기 중...");
            btnmenteeNext.setText("19/05/13 17:00PM 자동 삭제");
        }
        else
        {
            btnmentorCancel.setVisibility(View.VISIBLE);
            btnmenteeCancel.setVisibility(View.GONE);
            tvmentor.setText("Mentor (나)");
            tvmentee.setText("Mentee (상대방)");

            activebgr(btnmentorNext);
            subbgr(btnmenteeCancel);
            unactivebgr(btnmenteeNext);

            btnmentorNext.setText("진행");
            btnmenteeCancel.setText("삭제");
            btnmenteeNext.setText("19/05/13 17:00PM 자동 삭제");
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
