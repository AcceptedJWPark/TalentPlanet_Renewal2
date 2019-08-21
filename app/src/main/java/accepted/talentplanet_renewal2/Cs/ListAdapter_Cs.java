package accepted.talentplanet_renewal2.Cs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import accepted.talentplanet_renewal2.R;

import java.util.ArrayList;
import java.util.Arrays;


public class ListAdapter_Cs extends BaseAdapter {

    LayoutInflater inflater = null;
    private ArrayList<String> _listData = new ArrayList<String >(Arrays.asList("공지사항", "개인정보 이용동의", "정보 이용동의", "신고 리스트", "질문과 답변", "회원탈퇴"));
    private int _dataLeng = 0;

    public ListAdapter_Cs() {
        this._dataLeng = this._listData.size();
    }

    @Override
    public int getCount() {
        return this._dataLeng;
    }

    @Override
    public Object getItem(int position) {
        Object result = _listData.get(position);
        return result;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)  {
            final Context context = parent.getContext();
            if (inflater == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.customerservice_exlist_bg, parent, false);
        }

        TextView oTextTitle = (TextView) convertView.findViewById(R.id.tv_ctgrName);
        oTextTitle.setText(this._listData.get(position));

        return convertView;
    }
}
