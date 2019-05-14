package accepted.talentplanet_renewal2.Condition;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Accepted on 2019-05-05.
 */

public class condition_pagerAdapter extends FragmentStatePagerAdapter {

    boolean ismentor;
    int condition;
    int cnttalent;
    condition_Fragment[] condition_fragment = new condition_Fragment[2];


    public condition_pagerAdapter(FragmentManager fm) {
        super(fm);

        condition_fragment[0] = new condition_Fragment();
        condition_fragment[1] = new condition_Fragment();
    }


    @Override
    public int getCount() {
        return cnttalent;
    }



    @Override
    public Fragment getItem(int position) {

        switch(position)
        {
            case 0:
                return condition_fragment[0];
            case 1:
                return condition_fragment[1];
            default:
                return null;
        }
    }




    public boolean isIsmentor() {
        return ismentor;
    }

    public void setIsmentor(boolean ismentor) {
        this.ismentor = ismentor;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public int getCnttalent() {
        return cnttalent;
    }

    public void setCnttalent(int cnttalent) {
        this.cnttalent = cnttalent;
    }


}
