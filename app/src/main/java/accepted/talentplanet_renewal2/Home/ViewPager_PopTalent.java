package accepted.talentplanet_renewal2.Home;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

public class ViewPager_PopTalent extends FragmentStatePagerAdapter {

    private Context mContext;

    public ViewPager_PopTalent(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment_PopTalent view = new Fragment_PopTalent();

        return view;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
