package accepted.talentplanet_renewal2.Condition;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Accepted on 2019-05-05.
 */

public class condition_proc_pagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<condition_proc_Fragment> arr_fragment;


    public condition_proc_pagerAdapter(FragmentManager fm, boolean ismentor, ArrayList<condition_proc_Fragment> arr_fragment) {
        super(fm);

        this.arr_fragment = arr_fragment;

        for(int i = 0; i< this.arr_fragment.size(); i++)
        {
            this.arr_fragment.get(i).setIsmymentor(ismentor);
        }
    }


    @Override
    public int getCount() {
        return arr_fragment.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public Fragment getItem(int position) {
        return arr_fragment.get(position);
    }


}
