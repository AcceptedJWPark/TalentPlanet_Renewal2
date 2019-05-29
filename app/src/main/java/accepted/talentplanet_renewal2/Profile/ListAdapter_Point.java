package accepted.talentplanet_renewal2.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import accepted.talentplanet_renewal2.R;

public class ListAdapter_Point extends BaseAdapter {

    LayoutInflater inflater = null;
    private ArrayList<ItemData_Profile> userData = null;
    private int nListCnt = 0;

    public ListAdapter_Point(ArrayList<ItemData_Profile> adaptUserData) {
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
            convertView = inflater.inflate(R.layout.list_point, parent, false);
        }

        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name_point_profile);
        TextView tv_userinfo = (TextView) convertView.findViewById(R.id.tv_userinfo_point_profile);
        TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date_point_profile);
        TextView tv_mentormentee = (TextView) convertView.findViewById(R.id.tv_mentormentee_point_profile);
        TextView tv_point = (TextView) convertView.findViewById(R.id.tv_point_point_profile);


        tv_name.setText(userData.get(position).name);
        tv_userinfo.setText(userData.get(position).userinfo);
        tv_date.setText(userData.get(position).date);
        tv_point.setText(userData.get(position).point);

        if(userData.get(position).ismentor)
        {
            tv_mentormentee.setText("Mentor");
            tv_point.setTextColor(context.getResources().getColor(R.color.bgr_mainColor));
            tv_mentormentee.setTextColor(context.getResources().getColor(R.color.bgr_mainColor));
        }else
        {
            tv_mentormentee.setText("Mentee");
            tv_point.setTextColor(context.getResources().getColor(R.color.txt_minus));
            tv_mentormentee.setTextColor(context.getResources().getColor(R.color.txt_minus));
        }


        return convertView;
    }
}
