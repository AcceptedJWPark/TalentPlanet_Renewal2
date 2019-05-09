package accepted.talentplanet_renewal2.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import accepted.talentplanet_renewal2.R;

/**
 * Created by Accepted on 2019-05-05.
 */

public class Talent_SecondFragment extends android.support.v4.app.Fragment {


    public Talent_SecondFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_profile_fragment2, container, false);
        return layout;
    }


}
