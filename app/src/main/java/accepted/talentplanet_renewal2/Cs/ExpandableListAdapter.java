package accepted.talentplanet_renewal2.Cs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import accepted.talentplanet_renewal2.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    LayoutInflater inflater = null;
    private Context mContext;
    private ArrayList<HashMap<String, Object>> _listData;

    public ExpandableListAdapter(ArrayList<HashMap<String,Object>> commandMap) {
        _listData = commandMap;
    }

    @Override
    public int getGroupCount() {
        return _listData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return _listData.get(groupPosition).get("title");
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return _listData.get(groupPosition).get("text");
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Log.d(this.getClass().getName(), "여기까지");
        if (convertView == null) {
            final Context context = parent.getContext();
            if (inflater == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.cs_list_item, null);
        }

        // ParentList의 Layout 연결 후, 해당 layout 내 TextView를 연결
        TextView parentText = (TextView)convertView.findViewById(R.id.tv_ctgrName);
        parentText.setText(_listData.get(groupPosition).get("title").toString());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Log.d(this.getClass().getName(), "여기까지");
        if (convertView == null) {
            final Context context = parent.getContext();
            if (inflater == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.cs_child_list_item, null);
        }

        // ParentList의 Layout 연결 후, 해당 layout 내 TextView를 연결
        TextView parentText = (TextView)convertView.findViewById(R.id.tv_ctgrName);
        parentText.setText(_listData.get(groupPosition).get("title").toString());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
