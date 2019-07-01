package accepted.talentplanet_renewal2.SharingList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import accepted.talentplanet_renewal2.R;

import java.util.ArrayList;

/**
 * Created by Accepted on 2017-09-29.
 */

public class Adapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<ListItem> arrayList;
    private Spn_Adapter spn_Adapter = null;
    private ArrayList<MyModel> arrayList_Model;
    private boolean isClaimActivity = false;

    public Adapter(Context mContext, ArrayList<ListItem> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
            arrayList_Model = new ArrayList<>();
            arrayList_Model.add(new MyModel("보기"));
            arrayList_Model.add(new MyModel("Profile 보기"));
            arrayList_Model.add(new MyModel("신고 하기"));
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    public void setIsClaimActivity(boolean isClaimActivity){
        this.isClaimActivity = isClaimActivity;
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        final ViewHolder holder;
        view = null;

        if(view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.sharinglist_listviewbg, viewGroup, false);
            spn_Adapter = new Spn_Adapter(mContext, arrayList_Model);

            holder = new ViewHolder();

            holder.tv_Condition = view.findViewById(R.id.tv_Condition_SharingList);
            holder.tv_Name = view.findViewById(R.id.tv_Name_SharingList);
            holder.tv_Date = view.findViewById(R.id.tv_Date_SharingList);
            holder.tv_Talent1 = view.findViewById(R.id.tv_Talent1_SharingList);
            holder.tv_Talent2 = view.findViewById(R.id.tv_Talent2_SharingList);
            holder.tv_Talent3 = view.findViewById(R.id.tv_Talent3_SharingList);
            holder.tv_Txt = view.findViewById(R.id.tv_Txt_SharingList);
            holder.spn_ClaimProfile = view.findViewById(R.id.spn_ClaimProfile_SharingList);
            holder.rl_SpnContainer = view.findViewById(R.id.rl_SpnContainer_SharingList);
            holder.iv_ConditionIcon = view.findViewById(R.id.iv_ConditionIcon_SharingList);

            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);
            int Interesting_ListView_height = (int) (metrics.heightPixels * 0.10);
            ViewGroup.LayoutParams params1 = view.getLayoutParams();
            params1.height = Interesting_ListView_height;
            view.setLayoutParams(params1);

            holder.spn_ClaimProfile.setAdapter(spn_Adapter);
            holder.rl_SpnContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.spn_ClaimProfile.setFocusableInTouchMode(true);
                    holder.spn_ClaimProfile.performClick();
                }
            });

            view.setTag(holder);

        }else
        {
            holder=(ViewHolder) view.getTag();
        }

        TextView[] tags = new TextView[3];
        tags[0] = holder.tv_Talent1;
        tags[1] = holder.tv_Talent2;
        tags[2] = holder.tv_Talent3;

            //TODO:항목에 맞는 데이터 뿌려줘야 함
        holder.spn_ClaimProfile.setSelection(0, true);
        holder.spn_ClaimProfile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int spinner_position, long id) {

                    if (spinner_position == 0) {
                        return;
                    } else {
                        spinner_position = spinner_position - 1;
                    }

                        switch (spinner_position) {
                            case 1:
                                // 프로필 열기
//                                Intent i = new Intent(mContext, MainActivity.class);
//                                String str = (arrayList.get(position).getTalentFlag().equals("Mentor")) ? "Give": "Take";
//                                i.putExtra("TalentID", arrayList.get(position).getTalentID());
//                                i.putExtra("TalentFlag", str);
//                                mContext.startActivity(i);
                                break;
                            case 2:
                                if(isClaimActivity) {
                                    Intent intent = new Intent();
                                    Log.d("case2-1","claimClicked");

                                    intent.putExtra("name", arrayList.get(position).getUserName());
                                    intent.putExtra("hashtag", arrayList.get(position).getHashtag());
                                    intent.putExtra("matchingID", arrayList.get(position).getMatchingID());
                                    intent.putExtra("tarUserID", arrayList.get(position).getUserID());
                                    intent.putExtra("talentFlag", arrayList.get(position).getTalentFlag());
                                    intent.putExtra("status", arrayList.get(position).getMatchedFlag());
                                    ((Activity)mContext).setResult(Activity.RESULT_OK, intent);
                                    ((Activity)mContext).finish();

                                }else{
                                    Log.d("case2-2","claimClicked");
                                    Intent b = new Intent(mContext, accepted.talentplanet_renewal2.Cs.Claim.MainActivity.class);

                                    b.putExtra("isSelected", true);
                                    b.putExtra("name", arrayList.get(position).getUserName());
                                    b.putExtra("hashtag", arrayList.get(position).getHashtag());
                                    b.putExtra("matchingID", arrayList.get(position).getMatchingID());
                                    b.putExtra("tarUserID", arrayList.get(position).getUserID());
                                    b.putExtra("talentFlag", arrayList.get(position).getTalentFlag());
                                    b.putExtra("status", arrayList.get(position).getMatchedFlag());
                                    mContext.startActivity(b);

                                }
                                break;

                        }

                    holder.spn_ClaimProfile.setSelection(0, true);
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {


                }
            });

        holder.tv_Name.setText(arrayList.get(position).getUserName());
        holder.tv_Date.setText(arrayList.get(position).getCreationDate());
        String[] arrHashtag = arrayList.get(position).getHashtag().split("\\|");

        for(int i = 0; i < arrHashtag.length; i++){
            if(i < 3) {
                tags[i].setText(arrHashtag[i]);
            }else{
                break;
            }
        }

        for(int i = 2; i >= 0; i--){
            if(arrHashtag.length <= i){
                tags[i].setVisibility(View.GONE);
            }
        }

        switch (arrayList.get(position).getMatchedFlag()) {

            case "Y": {
                holder.tv_Condition.setText("진행중");
                holder.tv_Txt.setText("진행 중입니다.");
                holder.iv_ConditionIcon.setImageResource(R.drawable.icon_list_progress);
                break;
            }

            case "H": {
                holder.tv_Condition.setText("멘토 완료");
                holder.tv_Txt.setText("멘토가 재능 공유를 완료 하였습니다.");
                holder.iv_ConditionIcon.setImageResource(R.drawable.icon_list_complete);
                break;
            }

            case "C": {
                holder.tv_Condition.setText("공유 완료");
                holder.tv_Txt.setText("완료 하였습니다.");
                holder.iv_ConditionIcon.setImageResource(R.drawable.icon_list_complete);
                break;
            }

            case "X": {
                holder.tv_Condition.setText("진행 취소");
                holder.tv_Txt.setText("진행 취소하였습니다.");
                holder.iv_ConditionIcon.setImageResource(R.drawable.icon_list_cancel);
                break;
            }
        }

        return view;
    }

    static class ViewHolder
    {
        TextView tv_Condition;
        TextView tv_Name;
        TextView tv_Date;
        TextView tv_Talent1;
        TextView tv_Talent2;
        TextView tv_Talent3;
        TextView tv_Txt;
        ImageView iv_ConditionIcon;
        Spinner spn_ClaimProfile;
        RelativeLayout rl_SpnContainer;
    }

}
