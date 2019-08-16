package accepted.talentplanet_renewal2.TalentList;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;

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
        ViewHolder holder;
        convertView = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.talentlist_bg, parent, false);

            // View
            holder = new ViewHolder();
            holder.user_profile = convertView.findViewById(R.id.civ_user_profile);
            holder.userName_talentlist = (TextView) convertView.findViewById(R.id.userName_talentlist);
            holder.userBirth_talentlist = (TextView) convertView.findViewById(R.id.userBirth_talentlist);
            holder.hashTag_talentlist = (TextView) convertView.findViewById(R.id.hashTag_talentlist);
            holder.tv_userDistance_talentlist = (TextView) convertView.findViewById(R.id.tv_userDistance_talentlist);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        UserData_TalentList aItem = this.userList.get(position);

        // 내 위치 관련
        Location myLocation = new Location("My point");
        try {
            final Double myGP_LAT = Double.parseDouble(SaveSharedPreference.getPrefUserGpLat(mContext));
            final Double myGP_LNG = Double.parseDouble(SaveSharedPreference.getPrefUserGpLng(mContext));
            myLocation.setLatitude(myGP_LAT);
            myLocation.setLongitude(myGP_LNG);
        }catch (NumberFormatException e ){

        }catch (Exception e) {

        }

        // 현재 받은 타 유저의 위치
        final Double aUserGP_LAT = aItem.getGP_LAT();
        final Double aUserGP_LNG = aItem.getGP_LNG();
        Location aUserLocation = new Location("Another point");

        if (aUserGP_LAT != null || aUserGP_LNG != null) {
            aUserLocation.setLatitude(aUserGP_LAT);
            aUserLocation.setLongitude(aUserGP_LNG);
            float distance = myLocation.distanceTo(aUserLocation);
//            String value = String.valueOf((int) distance);
            String value = String.format("%,d", (int) distance);
            String[] intToStr = value.split("\\,");

            // 현재 타 유저와의 거리
            holder.tv_userDistance_talentlist.setText(String.valueOf(intToStr[0]) + " km");
        } else {
            holder.tv_userDistance_talentlist.setText("위치\n정보\n없음");
        }

        // 유저의 프로필
        String userThumb = aItem.getThumbPath();
        if (!userThumb.equals("NODATA")) {
            Glide.with(mContext).load(SaveSharedPreference.getImageUri() + userThumb).into(holder.user_profile);
        }

        if (SaveSharedPreference.getPrefTalentFlag(mContext).equals("N")) {
            holder.user_profile.setBorderColor(mContext.getResources().getColor(R.color.color_mentee));
        }

        // Data
        holder.userName_talentlist.setText(aItem.getUserName());

        if(aItem.getBirthFlag().equals("Y")){
            holder.userBirth_talentlist.setText("비공개");
        }else{
            holder.userBirth_talentlist.setText(aItem.getUserBirth());
        }

        // 태그 관련
        String descript = aItem.getDescription();
        String strHashtags = aItem.getHashtag();
        String[] hashtags = strHashtags.split("\\|");

        StringBuilder hashtag = new StringBuilder();

        for(int i = 0 ; i < hashtags.length; i++){
            if(hashtags[i] == null || hashtags[i].isEmpty()){
                continue;
            }

            hashtag.append("#").append(hashtags[i]).append(" ");
        }

        holder.hashTag_talentlist.setText(hashtag);

        return convertView;
    }

    static class ViewHolder {
        CircularImageView user_profile;
        TextView userName_talentlist;
        TextView userBirth_talentlist;
        TextView hashTag_talentlist;
        TextView tv_userDistance_talentlist;
    }

}
