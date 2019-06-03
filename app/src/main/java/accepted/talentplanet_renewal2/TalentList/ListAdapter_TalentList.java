package accepted.talentplanet_renewal2.TalentList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import accepted.talentplanet_renewal2.R;

public class ListAdapter_TalentList extends BaseAdapter {

    private Context mContext;
    private ArrayList<UserData_TalentList> userList;

    public ListAdapter_TalentList(Context context, ArrayList<UserData_TalentList> userDatas) {
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
            convertView = inflater.inflate(R.layout.list_talent_user, parent, false);
        }
        // View
        ImageView user_profile = (ImageView) convertView.findViewById(R.id.user_profile);
        TextView userName = (TextView) convertView.findViewById(R.id.userName);
        TextView userInfo = (TextView) convertView.findViewById(R.id.userInfo);
        TextView tv_talentTag_1 = (TextView) convertView.findViewById(R.id.tv_talentTag_1);
        TextView tv_talentTag_2 = (TextView) convertView.findViewById(R.id.tv_talentTag_2);
        TextView tv_talentTag_3 = (TextView) convertView.findViewById(R.id.tv_talentTag_3);
        TextView tv_userDistance = (TextView) convertView.findViewById(R.id.tv_userDistance);
        // Data
        UserData_TalentList aItem = this.userList.get(position);
        userName.setText(aItem.getUserName());
        userInfo.setText(aItem.getUserGender() + " / " + aItem.getUserAge() + "세");

        // 태그관련
//        String[] tagList = aItem.getUserTag();
//        if (tagList.length > 0) {
//            for (int i=0; i<tagList.length; i++) {
//                String nowTag = tagList[i];
//                if (i == 0) {
//                    tv_talentTag_1.setText(nowTag);
//                } else if (i == 1) {
//                    tv_talentTag_2.setText(nowTag);
//                } else if (i == 2) {
//                    tv_talentTag_3.setText(nowTag);
//                }
//            }
//        }


        return convertView;
    }
}
