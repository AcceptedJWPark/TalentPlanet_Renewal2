package accepted.talentplanet_renewal2.Profile;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
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
    private int nListCnt = 0;

    public ListAdapter_Talent(ArrayList<TalentObject_Home> userTalentList) {
        userTalent = userTalentList;
        nListCnt = userTalent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv;
        public ViewHolder(ImageView itemView) {
            super(itemView) ;
            iv = itemView.findViewById(R.id.iv_talent_image);
        }
    }

    @Override
    public ListAdapter_Talent.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView iv = (ImageView) LayoutInflater.from(parent.getContext()) .inflate(R.layout.list_talent_image, parent, false);

        ViewHolder vh = new ViewHolder(iv);
        return vh;
    }

    @Override
    public void onBindViewHolder(ListAdapter_Talent.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(userTalent.get(position).getBackgroundResourceID()).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return nListCnt;
    }
}
