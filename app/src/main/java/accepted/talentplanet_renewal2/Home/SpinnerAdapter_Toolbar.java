package accepted.talentplanet_renewal2.Home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import accepted.talentplanet_renewal2.R;

public class SpinnerAdapter_Toolbar extends BaseAdapter {
    ArrayList<SpinnerData_Toolbar> items;
    LayoutInflater inflater;
    Context mContext;

    SpinnerAdapter_Toolbar(ArrayList<SpinnerData_Toolbar> items, Context mContext){
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
            convertView = inflater.inflate(R.layout.spinner_item_toolbar_selected, parent, false);
        }

        if(items.get(position) != null){
            SpinnerData_Toolbar item = items.get(position);
            ((ImageView)convertView.findViewById(R.id.iv_spinner_icon)).setImageDrawable(mContext.getDrawable(item.getImageID()));
            ((TextView)convertView.findViewById(R.id.tv_spinner_text)).setText(item.getText());
            ((ImageView)convertView.findViewById(R.id.iv_spinner_arrow)).setImageDrawable(mContext.getDrawable(item.getArrowID()));
            if(item.getText().equals("Teacher Planet")){
                ((TextView)convertView.findViewById(R.id.tv_spinner_text)).setTextColor(Color.parseColor("#28364A"));
            }else{
                ((TextView)convertView.findViewById(R.id.tv_spinner_text)).setTextColor(Color.parseColor("#FFC35E"));
            }
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = inflater.inflate(R.layout.spinner_item_toolbar, parent, false);
        }

        if(items.get(position) != null){
            SpinnerData_Toolbar item = items.get(position);
            ((ImageView)convertView.findViewById(R.id.iv_spinner_icon)).setImageDrawable(mContext.getDrawable(item.getImageID()));
            ((TextView)convertView.findViewById(R.id.tv_spinner_text)).setText(item.getText());
            if(item.getText().equals("Teacher Planet")){
                ((TextView)convertView.findViewById(R.id.tv_spinner_text)).setTextColor(Color.parseColor("#28364A"));
            }else{
                ((TextView)convertView.findViewById(R.id.tv_spinner_text)).setTextColor(Color.parseColor("#FFC35E"));
            }
        }

        return convertView;
    }

    @Override
    public SpinnerData_Toolbar getItem(int position){
        return items.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }
}
