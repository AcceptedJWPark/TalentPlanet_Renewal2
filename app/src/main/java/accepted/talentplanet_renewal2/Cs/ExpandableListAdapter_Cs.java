package accepted.talentplanet_renewal2.Cs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import accepted.talentplanet_renewal2.R;

public class ExpandableListAdapter_Cs extends BaseExpandableListAdapter {

    LayoutInflater inflater = null;
    private ArrayList<NoticeData> _listData;

    public ExpandableListAdapter_Cs(ArrayList<NoticeData> commandMap) {
        _listData = commandMap;
    }

    @Override
    public int getGroupCount() {
        return _listData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return _listData.get(groupPosition).text.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return _listData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return _listData.get(groupPosition).text;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
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
            convertView = inflater.inflate(R.layout.customerservice_exlist_bg, parent, false);
        }

        // ParentList의 Layout 연결 후, 해당 layout 내 TextView를 연결
        TextView parentText = (TextView)convertView.findViewById(R.id.tv_ctgrName);
        TextView regDateText = (TextView)convertView.findViewById(R.id.tv_regDate);
        parentText.setText(_listData.get(groupPosition).title);
        regDateText.setVisibility(View.VISIBLE);
        regDateText.setText(_listData.get(groupPosition).regDate);
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
            convertView = inflater.inflate(R.layout.customerservice_list_bg, null);
        }

        // ParentList의 Layout 연결 후, 해당 layout 내 TextView를 연결
        TextView parentText = (TextView)convertView.findViewById(R.id.tv_noticeText);
        parentText.setText(_listData.get(groupPosition).text.get(childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
