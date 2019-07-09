package accepted.talentplanet_renewal2.SharingList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import accepted.talentplanet_renewal2.R;

import java.util.List;

/**
 * Created by kwonhong on 2017-12-16.
 */

public class Spn_Adapter extends ArrayAdapter<MyModel> {

    public Spn_Adapter(Context context, List<MyModel> items) {
        super(context, R.layout.sharinglist_spinnertext, items);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            return initialSelection();
        }
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            return initialSelection();
        }
        return getCustomView(position, convertView, parent);
    }


    @Override
    public int getCount() {
        return super.getCount() + 1; // Adjust for initial selection item
    }

    private View initialSelection() {

        TextView view = new TextView(getContext());

        // Hidden when the dropdown is opened
            view.setHeight(0);


        return view;
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        // Distinguish "real" spinner items (that can be reused) from initial selection item
        View row = convertView != null && !(convertView instanceof TextView)
                ? convertView :
                LayoutInflater.from(getContext()).inflate(R.layout.sharinglist_spinnertext, parent, false);
        if(position != 0)
            position = position - 1; // Adjust for initial selection item
        MyModel item = getItem(position);
        TextView tv = (TextView)row;
        if(position == 0)
            tv.setHeight(0);
        tv.setText(item.getspinnerItem1());
        // ... Resolve views & populate with data ...

        return row;
    }

}