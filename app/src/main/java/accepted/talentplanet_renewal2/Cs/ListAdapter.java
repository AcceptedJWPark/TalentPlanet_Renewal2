package accepted.talentplanet_renewal2.Cs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import accepted.talentplanet_renewal2.R;

import java.util.ArrayList;


public class ListAdapter extends BaseAdapter {

    LayoutInflater inflater = null;
    private ArrayList<NoticeData> _listData;
    private int _dataLeng = 0;

    public ListAdapter(ArrayList<NoticeData> _listData) {
        this._listData = _listData;
        this._dataLeng = this._listData.size();
    }

    @Override
    public int getCount() {
        return this._dataLeng;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)  {
            final Context context = parent.getContext();
            if (inflater == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.cs_list_item, parent, false);
        }

        TextView oTextTitle = (TextView) convertView.findViewById(R.id.tv_ctgrName);

        oTextTitle.setText(this._listData.get(position).title);

        return convertView;
    }
}
