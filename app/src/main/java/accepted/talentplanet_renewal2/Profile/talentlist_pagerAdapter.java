package accepted.talentplanet_renewal2.Profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Accepted on 2019-05-05.
 */

public class talentlist_pagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> views;
    private Talent_FirstFragment firstPage;
    private ArrayList<String> CateCodeArr = new ArrayList<String>();
    private String isMentor;

    public talentlist_pagerAdapter(FragmentManager fm) {
        super(fm);
        views = new ArrayList<>();
        firstPage = new Talent_FirstFragment();
        views.add(firstPage);
    }

    @Override
    public Fragment getItem(int position) {
        try {
            return views.get(position);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void addViews(Fragment fg){
        views.add(fg);
        if (fg.getArguments() != null) {
            CateCodeArr.add(fg.getArguments().getString("CateCode"));
            // 코드를 오름차순으로 정렬, FirstFragment 순서 맞춤
            Collections.sort(views, new Comparator<Fragment>() {
                @Override
                public int compare(Fragment o1, Fragment o2) {
                    String code1 = o1.getArguments().getString("CateCode");
                    Bundle codeP = o2.getArguments();
                    if (codeP != null) {
                        String code2 = codeP.getString("CateCode");
                        return Integer.parseInt(code1) - Integer.parseInt(code2);
                    }
                    return 0;
                }
            });
        }
    }

    public void startPager(String request) {

        isMentor = request;
        Bundle bundle = new Bundle();
        bundle.putStringArrayList ("cateCodeArr", CateCodeArr);
        bundle.putString("isMentor", isMentor);
        firstPage.setArguments(bundle);
    }

    @Override
    public int getCount() {
        return views.size();
    }
}
