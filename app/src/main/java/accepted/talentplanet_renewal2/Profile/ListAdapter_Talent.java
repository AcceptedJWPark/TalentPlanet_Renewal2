package accepted.talentplanet_renewal2.Profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import accepted.talentplanet_renewal2.Classes.TalentObject_Home;
import accepted.talentplanet_renewal2.R;

public class ListAdapter_Talent extends RecyclerView.Adapter<ListAdapter_Talent.ViewHolder> {

    private ArrayList<TalentObject_Home> userTalent = null;
    private String requestType;
    private boolean inPerson;
    private Activity mActivity;

    public ListAdapter_Talent(Activity activity, ArrayList<TalentObject_Home> userTalentList, String request, boolean state) {
        userTalent = userTalentList;
        requestType = request;
        inPerson = state;
        mActivity = activity;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        public ImageView iv;
        public TextView tv;
        public  View view;
        public ViewHolder(View itemView) {
            super(itemView);
//            iv = itemView.findViewById(R.id.iv_talent_image);
            tv = itemView.findViewById(R.id.tv_talent_name);
            view = itemView.findViewById(R.id.v_underline);
        }
    }

    @Override
    public ListAdapter_Talent.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()) .inflate(R.layout.list_talent_image, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ListAdapter_Talent.ViewHolder holder, final int position) {

        holder.tv.setText(userTalent.get(position).getTitle());
        holder.view.setVisibility(View.GONE);

        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView iv_talent_profile = mActivity.findViewById(R.id.iv_talent_profile);
                Glide.with(mActivity).load(userTalent.get(position).getBackgroundResourceID()).into(iv_talent_profile);

                TextView tv_tag_profile = mActivity.findViewById(R.id.tv_tag_profile);
                TextView tv_description_profile = mActivity.findViewById(R.id.tv_description_profile);

                holder.view.setVisibility(View.VISIBLE);

                if (!userTalent.get(position).getTitle().equals("추가")) {
                    tv_tag_profile.setVisibility(View.VISIBLE);
                    tv_description_profile.setVisibility(View.VISIBLE);

                    // 유저 재능내용을 가져오는 부분
                    String hashTagString = "";
                    String userText = userTalent.get(position).getTalentDescription();
                    String[] tagParse = userText.split(" ");

                    for (int i=0;i<tagParse.length;i++) {
                        String aTag = tagParse[i];
                        if (aTag.startsWith("#")) {
                            hashTagString += aTag + " ";
                        }
                    }

                    hashTagString.trim();

                    tv_tag_profile.setText(hashTagString);
                    tv_description_profile.setText(userText);
                } else {
                    tv_tag_profile.setVisibility(View.GONE);
                    tv_description_profile.setVisibility(View.GONE);
                }

//                Glide.with(mContext).load(userTalent.get(position).getBackgroundResourceID()).into(mContext);
//                TalentObject_Home item = userTalent.get(position);
//                Intent intent1 = new Intent(v.getContext(), MainActivity_TalentEdit.class);
//                intent1.putExtra("type", requestType);
//                intent1.putExtra("inPerson", inPerson);
//                intent1.putExtra("talentID", item.getTalentID());
//                intent1.putExtra("userID", item.getUserID());
//
//                if(position < getItemCount() - 1) {
//                    intent1.putExtra("CateCode", item.getCateCode());
//                    intent1.putExtra("type", requestType);
//                    intent1.putExtra("position", position);
//                    Intent parentIntent = ((Activity) v.getContext()).getIntent();
//                    intent1.putExtra("userName", parentIntent.getStringExtra("userName"));
//                    intent1.putExtra("userInfo", parentIntent.getStringExtra("userInfo"));
//                }
//                ((Activity)v.getContext()).startActivity(intent1);
////                ((Activity)v.getContext()).startActivityForResult(intent1, 3000);
//                ((Activity)v.getContext()).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userTalent.size();
    }
}
