package com.accepted.acceptedtalentplanet.Home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ViewPager_PopTalent extends FragmentStatePagerAdapter {

    private Context mContext;
    private ArrayList<HotTagItem> items;

    public ViewPager_PopTalent(FragmentManager fm, Context context, ArrayList items) {
        super(fm);
        mContext = context;
        this.items = items;
    }


    @Override
    public Fragment getItem(int position) {
        ArrayList<HotTagItem> tagItems = new ArrayList();
        for(int i = 0; i <= 2; i++){
            if(position * 3 + i < items.size())
                tagItems.add(items.get(position * 3 + i));
        }
        Fragment_PopTalent view = new Fragment_PopTalent();

        Bundle args = new Bundle();
        args.putSerializable("tagItems", tagItems);
        view.setArguments(args);

        return view;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
