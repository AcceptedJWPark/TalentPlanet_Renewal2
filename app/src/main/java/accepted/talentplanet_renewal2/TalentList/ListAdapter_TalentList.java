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
            convertView = inflater.inflate(R.layout.talentlist_bg, parent, false);
        }
        // View
//        ImageView user_profile = (ImageView) convertView.findViewById(R.id.user_profile);
        TextView userName_talentlist = (TextView) convertView.findViewById(R.id.userName_talentlist);
        TextView userBirth_talentlist = (TextView) convertView.findViewById(R.id.userBirth_talentlist);
        TextView hashTag_talentlist = (TextView) convertView.findViewById(R.id.hashTag_talentlist);

        // Data
        UserData_TalentList aItem = this.userList.get(position);
        userName_talentlist.setText(aItem.getUserName());
        userBirth_talentlist.setText(aItem.getUserGender() + " / " + aItem.getUserAge() + "세");
//
//        // 태그 관련
//        String strHashtags = aItem.getHashtag();
//        StringBuilder hashtag = new StringBuilder();
//        String[] hashtags = strHashtags.split("\\|");
//
//        for(int i = 0 ; i < hashtags.length; i++){
//            if(hashtags[i] == null || hashtags[i].isEmpty()){
//                continue;
//            }
//
//            hashtag.append("#").append(hashtags[i]).append(" ");
//        }
        return convertView;
    }
}
