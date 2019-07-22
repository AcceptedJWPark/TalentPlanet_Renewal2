package accepted.talentplanet_renewal2.Home;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import accepted.talentplanet_renewal2.R;

public class Fragment_PopTalent extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.home_pager, container, false);

        ImageView img1 = view.findViewById(R.id.iv_talent_img1_home);
        ImageView img2 = view.findViewById(R.id.iv_talent_img2_home);
        ImageView img3 = view.findViewById(R.id.iv_talent_img3_home);

        img1.setColorFilter(Color.parseColor("#bdbdbd"), PorterDuff.Mode.MULTIPLY);
        img2.setColorFilter(Color.parseColor("#bdbdbd"), PorterDuff.Mode.MULTIPLY);
        img3.setColorFilter(Color.parseColor("#bdbdbd"), PorterDuff.Mode.MULTIPLY);

        TextView tag1 = view.findViewById(R.id.tv_hashtag1_home);
        TextView tag2 = view.findViewById(R.id.tv_hashtag2_home);
        TextView tag3 = view.findViewById(R.id.tv_hashtag3_home);



        return view;
    }
}
