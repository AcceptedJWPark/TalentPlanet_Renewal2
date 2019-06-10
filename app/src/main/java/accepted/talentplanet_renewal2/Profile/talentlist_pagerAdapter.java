package accepted.talentplanet_renewal2.Profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Accepted on 2019-05-05.
 */

public class talentlist_pagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> views;

    public talentlist_pagerAdapter(FragmentManager fm) {
        super(fm);
        views = new ArrayList<>();
        views.add(new Talent_FirstFragment());
    }

    @Override
    public Fragment getItem(int position) {
        try {
            return views.get(position);
        }catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    public void addViews(Fragment fg){
        views.add(fg);
    }

    @Override
    public int getCount() {
        return views.size();
    }
}
