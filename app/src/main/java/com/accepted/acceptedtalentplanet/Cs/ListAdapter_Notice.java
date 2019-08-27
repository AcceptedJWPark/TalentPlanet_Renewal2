package com.accepted.acceptedtalentplanet.Cs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import com.accepted.acceptedtalentplanet.Cs.Notice.NoticeData;
import com.accepted.acceptedtalentplanet.R;


public class ListAdapter_Notice extends BaseAdapter {

    private Context mContext;
    private ArrayList<NoticeData> noticeLsit;

    public ListAdapter_Notice(Context ctx, ArrayList<NoticeData> notice) {
        this.mContext = ctx;
        this.noticeLsit = notice;
    }

    @Override
    public int getCount() {
        return noticeLsit.size();
    }

    @Override
    public Object getItem(int position) {
        return noticeLsit.get(position);
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
            convertView = inflater.inflate(R.layout.customerservice_list_bg, parent, false);

            holder = new ViewHolder();
            holder.tv_noticeText = convertView.findViewById(R.id.tv_noticeText);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        NoticeData aData =  this.noticeLsit.get(position);

        holder.tv_noticeText.setText(aData.getNOTICE_TITLE());

        return convertView;
    }

    static class ViewHolder {
        TextView tv_noticeText;
    }
}
