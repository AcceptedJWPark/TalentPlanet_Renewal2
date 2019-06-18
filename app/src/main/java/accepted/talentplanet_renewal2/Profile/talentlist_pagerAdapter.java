package accepted.talentplanet_renewal2.Profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

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
        if (fg.getArguments() != null) {
            CateCodeArr.add(fg.getArguments().getString("CateCode").toString());
        }
        views.add(fg);
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
