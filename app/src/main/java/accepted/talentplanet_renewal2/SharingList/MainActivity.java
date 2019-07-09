package accepted.talentplanet_renewal2.SharingList;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Created by Accepted on 2017-09-29.
 */

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private ListView listView_Give;
    private ListView listView_Take;
    private ArrayList<ListItem> arrayList;
    private ArrayList<ListItem> arrayList_Original;
    private Adapter adapter;
    private LinearLayout ll_PreContainer;

    private boolean isGiveTalent = true;

    private Button btn_SelectMentor;
    private Button btn_SelectMentee;

    private boolean isClaimActivity = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharinglist_activity);

        mContext = getApplicationContext();

        isClaimActivity = getIntent().getBooleanExtra("isClaimActivity", false);
        Log.d("Claim Flag = ", isClaimActivity + "");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null)
        {notificationManager.cancel(0);}


        listView_Give = (ListView) findViewById(R.id.listView_Give_SharingList);
        listView_Take = (ListView) findViewById(R.id.listView_Take_SharingList);


        getSharingList();

        btn_SelectMentor = (Button) findViewById(R.id.btn_SelectMentor_SharingList);
        btn_SelectMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 16) {
                    btn_SelectMentor.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bgr_giveortake_clicked));
                    btn_SelectMentee.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bgr_giveortake_unclicked));
                }else{
                    btn_SelectMentor.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.bgr_giveortake_clicked));
                    btn_SelectMentee.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.bgr_giveortake_unclicked));
                }
                btn_SelectMentor.setPaintFlags(btn_SelectMentor.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                btn_SelectMentor.setTextColor(getResources().getColor(R.color.textcolor_giveortake_clicked));
                btn_SelectMentee.setTextColor(getResources().getColor(R.color.textcolor_giveortake_unclicked));
                btn_SelectMentee.setPaintFlags(btn_SelectMentee.getPaintFlags() &~ Paint.FAKE_BOLD_TEXT_FLAG);

                arrayList.clear();
                for(ListItem item : arrayList_Original){
                    if(item.getTalentFlag().equals("Mentor"))
                        arrayList.add(item);
                }
                adapter = new Adapter(MainActivity.this, arrayList);
                adapter.setIsClaimActivity(isClaimActivity);
                listView_Give.setAdapter(adapter);

                listView_Give.setVisibility(View.VISIBLE);
                listView_Take.setVisibility(View.GONE);
            }
        });
        btn_SelectMentee = (Button) findViewById(R.id.btn_SelectMentee_SharingList);
        btn_SelectMentee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= 16) {
                    btn_SelectMentee.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bgr_giveortake_clicked));
                    btn_SelectMentor.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bgr_giveortake_unclicked));
                }else{
                    btn_SelectMentee.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.bgr_giveortake_clicked));
                    btn_SelectMentor.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.bgr_giveortake_unclicked));
                }
                btn_SelectMentee.setPaintFlags(btn_SelectMentor.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                btn_SelectMentee.setTextColor(getResources().getColor(R.color.textcolor_giveortake_clicked));
                btn_SelectMentor.setTextColor(getResources().getColor(R.color.textcolor_giveortake_unclicked));
                btn_SelectMentor.setPaintFlags(btn_SelectMentee.getPaintFlags() &~ Paint.FAKE_BOLD_TEXT_FLAG);

                arrayList.clear();
                for(ListItem item : arrayList_Original){
                    if(item.getTalentFlag().equals("Mentee"))
                        arrayList.add(item);
                }
                adapter = new Adapter(MainActivity.this, arrayList);
                adapter.setIsClaimActivity(isClaimActivity);
                listView_Take.setAdapter(adapter);

                listView_Take.setVisibility(View.VISIBLE);
                listView_Give.setVisibility(View.GONE);
            }
        });


    }

    public void getSharingList() {
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "SharingList/getSharingList_new.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray obj = new JSONArray(response);
                    arrayList_Original = new ArrayList<>();
                    arrayList = new ArrayList<>();
                    for (int index = 0; index < obj.length(); index++) {
                        JSONObject o = obj.getJSONObject(index);

                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd hh:mm", Locale.ENGLISH);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Log.d("date = ", o.getString("CREATION_DATE"));
                        ParsePosition pos = new ParsePosition(0);

                        Date date = new java.sql.Date(Long.parseLong(o.getString("CREATION_DATE")));
                        String dateStr = sdf2.format(date);

                        int TalentType = (o.getString("TALENT_FLAG").equals("Mentee"))?2: 1;

                        ListItem target = new ListItem(o.getString("MATCHED_FLAG"), o.getString("USER_NAME"), o.getString("USER_ID"), (o.has("HASHTAG")) ? o.getString("HASHTAG"):"", o.getInt("MATCHING_ID"), o.getString("S_FILE_PATH"), o.getString("FILE_PATH"), o.getInt("TALENT_CATE_CODE"), dateStr, o.getString("TALENT_FLAG"));
                        arrayList_Original.add(0,target);
                        if(isGiveTalent){
                            if(TalentType == 1){
                                arrayList.add(0,target);
                            }
                        }else{
                            if(TalentType == 2){
                                arrayList.add(0,target);
                            }
                        }

                    }
                    adapter = new Adapter(MainActivity.this, arrayList);
                    adapter.setIsClaimActivity(isClaimActivity);
                    listView_Give.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("userID", SaveSharedPreference.getUserId(mContext));
                return params;
            }
        };


        postRequestQueue.add(postJsonRequest);

    }



}



