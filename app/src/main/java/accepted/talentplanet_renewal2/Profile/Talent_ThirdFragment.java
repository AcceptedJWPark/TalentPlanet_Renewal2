package accepted.talentplanet_renewal2.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import accepted.talentplanet_renewal2.R;

/**
 * Created by Accepted on 2019-05-05.
 */

public class Talent_ThirdFragment extends android.support.v4.app.Fragment {


    public Talent_ThirdFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_profile_fragment3, container, false);

        ImageView editBtn = (ImageView) layout.findViewById(R.id.iv_edit_user_info);
        editBtn.setOnClickListener(new View.OnClickListener() {
            // 유저의 정보 수정 이벤트 부여
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , MainActivity_Detail.class);
                startActivity(intent);
            }
        });



        return layout;
    }


}
