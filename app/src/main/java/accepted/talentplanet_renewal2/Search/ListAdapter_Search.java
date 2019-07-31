package accepted.talentplanet_renewal2.Search;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;


public class ListAdapter_Search extends BaseAdapter {

    Context mContext;
    ArrayList<Map<String, Object>> itemArr;

    public ListAdapter_Search(Context context, ArrayList<Map<String, Object>> paramArr) {
        mContext = context;
        itemArr = paramArr;
    }

    @Override
    public int getCount() {
        return itemArr.size();
    }

    @Override
    public Object getItem(int position) {
        return itemArr.get(position);
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
        TextView userName_talentlist = (TextView) convertView.findViewById(R.id.userName_talentlist);
        TextView userBirth_talentlist = (TextView) convertView.findViewById(R.id.userBirth_talentlist);
        TextView hashTag_talentlist = (TextView) convertView.findViewById(R.id.hashTag_talentlist);
        TextView tv_userDistance_talentlist = (TextView) convertView.findViewById(R.id.tv_userDistance_talentlist);

        // 유저 정보 맵
        Map<String, Object> aUserData = itemArr.get(position);

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
        final Double aUserGP_LAT = (Double) aUserData.get("GP_LAT");
        final Double aUserGP_LNG = (Double) aUserData.get("GP_LNG");
        Location aUserLocation = new Location("Another point");

        if (aUserGP_LAT != null || aUserGP_LNG != null) {
            aUserLocation.setLatitude(aUserGP_LAT);
            aUserLocation.setLongitude(aUserGP_LNG);
            float distance = myLocation.distanceTo(aUserLocation);
//            String value = String.valueOf((int) distance);
            String value = String.format("%,d", (int) distance);
            String[] intToStr = value.split("\\,");

            // 현재 타 유저와의 거리
            tv_userDistance_talentlist.setText(String.valueOf(intToStr[0]) + " km");
        } else {
            tv_userDistance_talentlist.setText("위치\n정보\n없음");
        }

        // Data
        userName_talentlist.setText((String) aUserData.get("userName"));

        String flag = (String) aUserData.get("birthFlag");
        if (flag.equals("N")) {
            userBirth_talentlist.setText((String) aUserData.get("userBirth"));
        } else {
            userBirth_talentlist.setText("비공개");
        }

        // 태그 관련
        String talentName = (String) aUserData.get("talentName");
        String searchText = (String) aUserData.get("searchTxt");

        hashTag_talentlist.setText(talentName + " > "+searchText);

        return convertView;
    }
}
