package accepted.talentplanet_renewal2.Profile;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import accepted.talentplanet_renewal2.Classes.TalentObject_Home;
import accepted.talentplanet_renewal2.R;

public class SpinnerAdapter_Talent extends BaseAdapter {
    ArrayList<TalentObject_Home> items;
    LayoutInflater inflater;
    Context mContext;

    SpinnerAdapter_Talent(ArrayList<TalentObject_Home> items, Context mContext){
        this.items = items;
        this.mContext = mContext;
        inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = inflater.inflate(R.layout.profile_spinner_selecteditem, parent, false);
        }

        if(items.get(position) != null){
            TalentObject_Home item = items.get(position);
            ((TextView)convertView.findViewById(R.id.tv_spinner_text)).setTextColor(Color.parseColor("#ffffff"));
            ((TextView)convertView.findViewById(R.id.tv_spinner_text)).setText(item.getTitle());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = inflater.inflate(R.layout.spinner_item_menubar, parent, false);
        }

        if(items.get(position) != null){
            TalentObject_Home item = items.get(position);
            ((TextView)convertView.findViewById(R.id.tv_spinner_text)).setText(item.getTitle());
        }

        return convertView;
    }

    @Override
    public TalentObject_Home getItem(int position){
        return items.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }
}
