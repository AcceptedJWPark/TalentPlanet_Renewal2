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
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import accepted.talentplanet_renewal2.Classes.TalentObject_Home;
import accepted.talentplanet_renewal2.R;

public class ListAdapter_Talent extends RecyclerView.Adapter<ListAdapter_Talent.ViewHolder> {

    private ArrayList<TalentObject_Home> userTalent = null;

    public ListAdapter_Talent(ArrayList<TalentObject_Home> userTalentList) {
        userTalent = userTalentList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv;
        public ViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv_talent_image);
        }
    }

    @Override
    public ListAdapter_Talent.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()) .inflate(R.layout.list_talent_image, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ListAdapter_Talent.ViewHolder holder, final int position) {
        Glide.with(holder.itemView.getContext()).load(userTalent.get(position).getBackgroundResourceID()).into(holder.iv);
        Log.d("position", userTalent.get(position).getTitle());

        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(v.getContext(), MainActivity_TalentEdit.class);
                intent1.putExtra("type","MENTOR");
                if(position < getItemCount() - 1){
                    TalentObject_Home item = userTalent.get(position);
                    intent1.putExtra("CateCode", item.getCateCode());
                }
                ((Activity)v.getContext()).startActivity(intent1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userTalent.size();
    }
}
