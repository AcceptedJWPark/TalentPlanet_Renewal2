package accepted.talentplanet_renewal2.TalentBox;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import accepted.talentplanet_renewal2.R;

public class ListAdapter_TalentBox extends BaseAdapter {

    private Context mContext;
    private ArrayList<TalentBoxObject_TalentBox> sendList;
    private int nSize;

    public ListAdapter_TalentBox(Context context, ArrayList<TalentBoxObject_TalentBox> list) {
        mContext = context;
        sendList = list;
        nSize = sendList.size();
    }

    @Override
    public int getCount() {
        return nSize;
    }

    @Override
    public Object getItem(int position) {
        return sendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_friend, parent, false);
        }
        // 필요한 View 설정
        ImageView user_profile = convertView.findViewById(R.id.user_profile);
        ImageView dmBtn = convertView.findViewById(R.id.dmBtn);
        TextView userName = convertView.findViewById(R.id.userName);
        TextView userInfo = convertView.findViewById(R.id.userInfo);
        View v_void_friend = convertView.findViewById(R.id.v_void_friend);

        dmBtn.setVisibility(View.GONE);
        v_void_friend.setVisibility(View.GONE);
        String requestType =sendList.get(position).getRequestType();
        String targetID;
        if (requestType.equals("send")) {
            targetID = sendList.get(position).getRECEIVER_ID();
            userName.setText(targetID + " 님에게 프로필을 보냈습니다.");
        } else if (requestType.equals("recive")) {
            targetID = sendList.get(position).getMentorID();
            userName.setText(targetID + " 님에게 프로필을 받았습니다.");
        }

        userName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);

        return convertView;
    }
}
