package accepted.talentplanet_renewal2.Condition;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by Accepted on 2019-05-05.
 */

public class condition_proc_pagerAdapter extends FragmentStatePagerAdapter {

    condition_proc_Fragment[] condition_proc_fragment = new condition_proc_Fragment[2];


    public condition_proc_pagerAdapter(FragmentManager fm, boolean ismentor) {
        super(fm);

        condition_proc_fragment[0] = new condition_proc_Fragment();
        condition_proc_fragment[1] = new condition_proc_Fragment();

        condition_proc_fragment[0].setIsmentorComplete(false);
        condition_proc_fragment[1].setIsmentorComplete(true);

        for(int i = 0; i< condition_proc_fragment.length; i++)
        {
            condition_proc_fragment[i].setIsmymentor(ismentor);
        }
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public Fragment getItem(int position) {

        switch(position)
        {
            case 0:
                return condition_proc_fragment[0];
            case 1:
                return condition_proc_fragment[1];
            default:
                return null;
        }
    }


}
