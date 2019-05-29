package accepted.talentplanet_renewal2.Condition;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by Accepted on 2019-05-05.
 */

public class condition_req_pagerAdapter extends FragmentStatePagerAdapter {

    condition_req_Fragment[] condition_req_fragment = new condition_req_Fragment[2];


    public condition_req_pagerAdapter(FragmentManager fm, boolean ismentor) {
        super(fm);

        condition_req_fragment[0] = new condition_req_Fragment();
        condition_req_fragment[1] = new condition_req_Fragment();

        for(int i = 0; i< condition_req_fragment.length; i++)
        {
            condition_req_fragment[i].setIsmymentor(ismentor);
        }
    }


    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public Fragment getItem(int position) {

        switch(position)
        {
            case 0:
                return condition_req_fragment[0];
            case 1:
                return condition_req_fragment[1];
            default:
                return null;
        }
    }


}
