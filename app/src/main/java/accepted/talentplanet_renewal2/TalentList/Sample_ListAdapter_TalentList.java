package accepted.talentplanet_renewal2.TalentList;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import accepted.talentplanet_renewal2.R;

public class Sample_ListAdapter_TalentList extends BaseAdapter {

    private Context mContext;
    private ArrayList<Sample_UserData_TalentList> userList;

    public Sample_ListAdapter_TalentList(Context context, ArrayList<Sample_UserData_TalentList> userDatas) {
        this.mContext = context;
        this.userList = userDatas;
    }

    @Override
    public int getCount() {
        return this.userList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.talentlist_bg, parent, false);
        }
        // View
        TextView userName = (TextView) convertView.findViewById(R.id.userName_talentlist);
        TextView userBirth = (TextView) convertView.findViewById(R.id.userBirth_talentlist);
        TextView userHash = (TextView) convertView.findViewById(R.id.hashTag_talentlist);
        ImageView userGender = (ImageView) convertView.findViewById(R.id.userGender_talentlist);
        TextView tv_userDistance = (TextView) convertView.findViewById(R.id.tv_userDistance_talentlist);


        userName.setText(this.userList.get(position).getUserName());
        userBirth.setText(this.userList.get(position).getUserAge());
        userHash.setText(this.userList.get(position).getHashtag());
        tv_userDistance.setText(this.userList.get(position).getDistance());



        if(this.userList.get(position).getUserGender().equals("남성"))
        {
            userGender.setImageResource(R.drawable.icon_male);
        }else
        {
            userGender.setImageResource(R.drawable.icon_female);
        }
        return convertView;
    }
}
