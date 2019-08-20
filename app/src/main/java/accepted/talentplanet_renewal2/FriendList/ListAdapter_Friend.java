package accepted.talentplanet_renewal2.FriendList;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;

import java.util.ArrayList;
import com.mikhaellopez.circularimageview.CircularImageView;

public class ListAdapter_Friend extends BaseAdapter {

    LayoutInflater inflater = null;
    private ArrayList<ItemData_Friend> userData = null;
    private int nListCnt = 0;

    public ListAdapter_Friend(ArrayList<ItemData_Friend> adaptUserData) {
        userData = adaptUserData;
    }

    @Override
    public int getCount() {
        return userData.size();
    }

    @Override
    public Object getItem(int position) {
        return userData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        ViewHolder holder;
        convertView = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.friend_list_bg, parent, false);

            holder = new ViewHolder();
            holder.oTextTitle = (TextView) convertView.findViewById(R.id.userName);
            holder.oTextDate = (TextView) convertView.findViewById(R.id.userInfo);
            holder.tv_userdistance_friend = (TextView) convertView.findViewById(R.id.tv_userdistance_friend);
            holder.userBirth_friendlist = (TextView) convertView.findViewById(R.id.userBirth_friendlist);
            holder.userGender_friendlist = (ImageView) convertView.findViewById(R.id.userGender_friendlist);
            holder.user_profile= (CircularImageView) convertView.findViewById(R.id.user_profile);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.oTextTitle.setText(userData.get(position).getStrUserName());
        holder.oTextDate.setText(userData.get(position).getStrUserInfo());

        String flag = (String) userData.get(position).getBIRTH_FLAG();
        if (flag.equals("N")) {
            holder.userBirth_friendlist.setText(userData.get(position).getStrUserInfo());
        } else {
            holder.userBirth_friendlist.setText("비공개");
        }



        // 유저의 프로필
        String userThumb = userData.get(position).getS_FILE_PATH();
        if (!userThumb.equals("NODATA")) {
            Glide.with(context).load(SaveSharedPreference.getImageUri() + userThumb).into(holder.user_profile);
        }

        if (SaveSharedPreference.getPrefTalentFlag(context).equals("N")) {
            holder.user_profile.setBorderColor(context.getResources().getColor(R.color.color_mentee));
        }

        String userGender = userData.get(position).getStrUserGender();
        if (userGender.equals("여")) {
            holder.userGender_friendlist.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_female));
        }

        // 내 위치 관련
        Location myLocation = new Location("My point");
        try {
            final Double myGP_LAT = Double.parseDouble(SaveSharedPreference.getPrefUserGpLat(context));
            final Double myGP_LNG = Double.parseDouble(SaveSharedPreference.getPrefUserGpLng(context));
            myLocation.setLatitude(myGP_LAT);
            myLocation.setLongitude(myGP_LNG);
        }catch (NumberFormatException e ){

        }catch (Exception e) {

        }

        // 현재 받은 타 유저의 위치
        final Double aUserGP_LAT = Double.parseDouble(userData.get(position).getGP_LAT());
        final Double aUserGP_LNG = Double.parseDouble(userData.get(position).getGP_LNG());
        Location aUserLocation = new Location("Another point");

        if (aUserGP_LAT != null || aUserGP_LNG != null) {
            aUserLocation.setLatitude(aUserGP_LAT);
            aUserLocation.setLongitude(aUserGP_LNG);
            float distance = myLocation.distanceTo(aUserLocation);
//            String value = String.valueOf((int) distance);
            String value = String.format("%,d", (int) distance);
            String[] intToStr = value.split("\\,");

            // 현재 타 유저와의 거리
            holder.tv_userdistance_friend.setText(String.valueOf(intToStr[0]) + " km");
        } else {
            holder.tv_userdistance_friend.setText("위치\n정보\n없음");
        }

        return convertView;
    }

    static class ViewHolder {
        TextView oTextTitle;
        TextView oTextDate;
        TextView tv_userdistance_friend;
        TextView userBirth_friendlist;
        ImageView userGender_friendlist;
        CircularImageView user_profile;
    }
}
