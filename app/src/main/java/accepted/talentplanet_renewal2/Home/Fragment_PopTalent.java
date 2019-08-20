package accepted.talentplanet_renewal2.Home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.Search.MainActivity_Search;

public class Fragment_PopTalent extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        // 파라미터 처리
        ArrayList<HotTagItem> items = (ArrayList)getArguments().getSerializable("tagItems");
        Log.d("Tag ArrayList", items.get(0).getTag());
        ImageView[] imgs = new ImageView[3];
        TextView[] tags = new TextView[3];
        View view = inflater.inflate(R.layout.home_pager, container, false);

        imgs[0] = view.findViewById(R.id.iv_talent_img1_home);
        imgs[1] = view.findViewById(R.id.iv_talent_img2_home);
        imgs[2] = view.findViewById(R.id.iv_talent_img3_home);

        tags[0] = view.findViewById(R.id.tv_hashtag1_home);
        tags[1] = view.findViewById(R.id.tv_hashtag2_home);
        tags[2] = view.findViewById(R.id.tv_hashtag3_home);

        for (int i = 0; i < items.size(); i++){
            final String tag = items.get(i).getTag();
            imgs[i].setImageResource(getResources().getIdentifier(items.get(i).getBackgroundID(), "drawable", getActivity().getPackageName()));
            imgs[i].setColorFilter(Color.parseColor("#bdbdbd"), PorterDuff.Mode.MULTIPLY);
            tags[i].setText("#"+ tag);
            imgs[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), MainActivity_Search.class);
                    i.putExtra("searchTag", tag);
                    startActivity(i);
                }
            });
        }

        return view;
    }
}
