package accepted.talentplanet_renewal2.Profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Accepted on 2019-05-05.
 */

public class talentlist_pagerAdapter extends FragmentStatePagerAdapter {
    public talentlist_pagerAdapter(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                return new Talent_FirstFragment();
            case 1:
                return new Talent_SecondFragment();
            case 2:
                return new Talent_ThirdFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
