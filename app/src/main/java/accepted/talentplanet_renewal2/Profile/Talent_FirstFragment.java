package accepted.talentplanet_renewal2.Profile;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import accepted.talentplanet_renewal2.R;

/**
 * Created by Accepted on 2019-05-05.
 */

public class Talent_FirstFragment extends android.support.v4.app.Fragment {


    public Talent_FirstFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_first, container, false);
        return layout;
    }


}
