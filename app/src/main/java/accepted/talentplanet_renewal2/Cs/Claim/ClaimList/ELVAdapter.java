package accepted.talentplanet_renewal2.Cs.Claim.ClaimList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import accepted.talentplanet_renewal2.R;

import java.util.ArrayList;

/**
 * Created by Accepted on 2017-10-31.
 */

public class ELVAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<ListItem_Question> arrayList_Parent;
    private ArrayList<ArrayList<ListItem_Answer>> arrayList_Child;

    public ELVAdapter(Context context, ArrayList<ListItem_Question> arrayList_Parent, ArrayList<ArrayList<ListItem_Answer>> arrayChild)
    {
        super();
        this.mContext = context;
        this.arrayList_Parent = arrayList_Parent;
        this.arrayList_Child = arrayChild;
    }


    @Override
    public int getGroupCount() {
        return arrayList_Parent.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return arrayList_Child.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return arrayList_Parent.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return arrayList_Child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;

        String QuestionTitle = arrayList_Parent.get(groupPosition).getTitle();
        String isAnswered = arrayList_Parent.get(groupPosition).getIsAnswer();
        String date = arrayList_Parent.get(groupPosition).getRegistDate();

        if(v==null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=(LinearLayout) inflater.inflate(R.layout.customerservice_claimlist_parentbg,  parent,false);

        }

        TextView tv_Title_ClaimList = (TextView) v.findViewById(R.id.tv_ClaimList_Title);
        TextView tv_Condition_ClaimList = (TextView) v.findViewById(R.id.tv_ClaimList_Condition);
        TextView tv_Date_ClaimList = (TextView) v.findViewById(R.id.tv_ClaimList_Date);

        //
        tv_Title_ClaimList.setText(QuestionTitle);
        tv_Date_ClaimList.setText(date);
        tv_Condition_ClaimList.setText(isAnswered);
        return v;


    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        View v = convertView;

        String Name = arrayList_Child.get(groupPosition).get(childPosition).getname();
        String Type = arrayList_Child.get(groupPosition).get(childPosition).getclaimType();
        String Date = arrayList_Child.get(groupPosition).get(childPosition).getdate();
        String content_Claim = arrayList_Child.get(groupPosition).get(childPosition).getContent_claim();
        String content_Answer = arrayList_Child.get(groupPosition).get(childPosition).getanswer();

        if(v==null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=(LinearLayout) inflater.inflate(R.layout.customerservice_claimlist_childbg, null);
        }
        TextView tv_Name = (TextView) v.findViewById(R.id.tv_Name_ClaimList);
        TextView tv_ClaimType = (TextView) v.findViewById(R.id.tv_ClaimType_ClaimList);
        TextView tv_Date = (TextView) v.findViewById(R.id.tv_Date_ClaimList);
        TextView tv_Claim = (TextView) v.findViewById(R.id.tv_Claim_ClaimList);
        TextView tv_Answer = v.findViewById(R.id.tv_Answer_ClaimList);

        tv_Name.setText(Name);
        tv_ClaimType.setText(Type);
        tv_Date.setText(Date);
        tv_Claim.setText(content_Claim);
        tv_Answer.setText(content_Answer);

        return v;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}

