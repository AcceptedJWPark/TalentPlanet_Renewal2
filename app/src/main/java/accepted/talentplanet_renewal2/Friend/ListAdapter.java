package accepted.talentplanet_renewal2.Friend;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import accepted.talentplanet_renewal2.R;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    LayoutInflater inflater = null;
    private ArrayList<ItemData> userData = null;
    private int nListCnt = 0;

    public ListAdapter(ArrayList<ItemData> adaptUserData) {
        userData = adaptUserData;
        nListCnt = userData.size();
    }

    @Override
    public int getCount() {
        return nListCnt;
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
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.friend_list_item, parent, false);
        }

        TextView oTextTitle = (TextView) convertView.findViewById(R.id.userName);
        TextView oTextDate = (TextView) convertView.findViewById(R.id.userInfo);

        oTextTitle.setText(userData.get(position).strUserName);
        oTextDate.setText(userData.get(position).strUserInfo);
        return convertView;
    }
}
