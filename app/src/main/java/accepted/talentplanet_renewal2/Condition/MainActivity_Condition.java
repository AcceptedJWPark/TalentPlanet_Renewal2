package accepted.talentplanet_renewal2.Condition;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.VolleySingleton;

import static android.graphics.Color.WHITE;


public class MainActivity_Condition extends AppCompatActivity {

    TextView tv_toolbar;
    Context mContext;
    ViewPager vp_req_condition;
    ViewPager vp_proc_condition;

    condition_req_pagerAdapter adapter_req;
    condition_proc_pagerAdapter adapter_proc;

    ArrayList<condition_req_Fragment> arr_req_fragment;
    ArrayList<condition_proc_Fragment> arr_proc_fragment;

    boolean ismymentor;

    Button btn_req_condition;
    Button btn_proc_condition;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);

        mContext = getApplicationContext();
        tv_toolbar = findViewById(R.id.tv_toolbar);

        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);
        ((ImageView) findViewById(R.id.img_open_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIntent().removeExtra("ismyMentor");
                finish();
            }
        });

        intent = getIntent();
        String str;
        str = intent.getExtras().getString("ismyMentor");

        if(str.equals("1"))
        {
            ismymentor=true;
        }
        else if(str.equals("2"))
        {
            ismymentor=false;
        }

        findViewById(R.id.img_show1x15).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        btn_req_condition = findViewById(R.id.btn_req_condition);
        btn_proc_condition = findViewById(R.id.btn_proc_condition);


        vp_req_condition = findViewById(R.id.vp_req_condition);
        vp_proc_condition = findViewById(R.id.vp_proc_condition);

        if(ismymentor)
        {
            tv_toolbar.setText("My Mentor");
            getMyMentor();
        }

        else
        {
            tv_toolbar.setText("My Mentee");
            getMyMentee();
        }


        btn_req_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClickedbgr(btn_req_condition);
                btnUnClickedbgr(btn_proc_condition);
                vp_req_condition.setVisibility(View.VISIBLE);
                vp_proc_condition.setVisibility(View.GONE);
            }
        });

        btn_proc_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClickedbgr(btn_proc_condition);
                btnUnClickedbgr(btn_req_condition);
                vp_proc_condition.setVisibility(View.VISIBLE);
                vp_req_condition.setVisibility(View.GONE);
            }
        });

    }

    public void btnClickedbgr(Button btn)
    {
        btn.setBackgroundColor(getResources().getColor(R.color.bgr_mainColor));
        btn.setTextColor(WHITE);
        btn.setTypeface(null, Typeface.BOLD);
    }

    public void btnUnClickedbgr(Button btn)
    {
        btn.setBackgroundColor(getResources().getColor(R.color.bgr_gray));
        btn.setTextColor(getResources().getColor(R.color.txt_gray));
        btn.setTypeface(null, Typeface.NORMAL);
    }

    public class viewpagerChangeListener implements ViewPager.OnPageChangeListener{

        protected PagerAdapter adapter;
        protected TextView textView;

        public viewpagerChangeListener(PagerAdapter adapter, TextView textView) {
            this.adapter = adapter;
            this.textView = textView;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            textView.setText(position+1+"/"+adapter.getCount());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public void getMyMentor() {
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "TalentCondition/getMyMentor.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    arr_proc_fragment = new ArrayList<>();
                    arr_req_fragment = new ArrayList<>();

                    JSONArray array = new JSONArray(response);
                    for(int i = 0; i < array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
                        Bundle bundle = new Bundle();

                        bundle.putString("MATCHED_FLAG", obj.getString("MATCHED_FLAG"));
                        bundle.putString("MentorID", obj.getString("MentorID"));
                        bundle.putString("MentorName", obj.getString("MentorName"));
                        bundle.putString("MentorBirth", obj.getString("MentorBirth"));
                        bundle.putString("MentorGender", obj.getString("MentorGender"));
                        bundle.putString("CREATION_DATE", obj.getString("CREATION_DATE"));
                        bundle.putString("Code", obj.getString("Code"));

                        if(obj.getString("MATCHED_FLAG").equals("N")){
                            condition_req_Fragment fragment = new condition_req_Fragment();
                            fragment.setArguments(bundle);
                            arr_req_fragment.add(fragment);
                        }else if(obj.getString("MATCHED_FLAG").equals("Y")){
                            condition_proc_Fragment fragment = new condition_proc_Fragment();
                            fragment.setArguments(bundle);
                            arr_proc_fragment.add(fragment);
                        }
                    }

                    adapter_req = new condition_req_pagerAdapter(getSupportFragmentManager(),ismymentor, arr_req_fragment);
                    adapter_proc = new condition_proc_pagerAdapter(getSupportFragmentManager(),ismymentor, arr_proc_fragment);
                    vp_req_condition.setAdapter(adapter_req);
                    vp_proc_condition.setAdapter(adapter_proc);

                    if(arr_req_fragment.size() > 0) {
                        ((TextView) findViewById(R.id.tv_series_condition)).setText("1/"+arr_req_fragment.size());
                    }

                    vp_req_condition.setOnPageChangeListener(new viewpagerChangeListener(adapter_req,(TextView)findViewById(R.id.tv_series_condition)));
                    vp_proc_condition.setOnPageChangeListener(new viewpagerChangeListener(adapter_proc,(TextView)findViewById(R.id.tv_series_condition)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("UserID", SaveSharedPreference.getUserId(mContext));
                return params;
            }
        };
        postRequestQueue.add(postJsonRequest);
    }

    public void getMyMentee() {
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "TalentCondition/getMyMentee.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    arr_proc_fragment = new ArrayList<>();
                    arr_req_fragment = new ArrayList<>();

                    JSONArray array = new JSONArray(response);
                    for(int i = 0; i < array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
                        Bundle bundle = new Bundle();

                        bundle.putString("MATCHED_FLAG", obj.getString("MATCHED_FLAG"));
                        bundle.putString("MenteeID", obj.getString("MenteeID"));
                        bundle.putString("MenteeName", obj.getString("MenteeName"));
                        bundle.putString("MenteeBirth", obj.getString("MenteeBirth"));
                        bundle.putString("MenteeGender", obj.getString("MenteeGender"));
                        bundle.putString("CREATION_DATE", obj.getString("CREATION_DATE"));
                        bundle.putString("Code", obj.getString("Code"));

                        if(obj.getString("MATCHED_FLAG").equals("N")){
                            condition_req_Fragment fragment = new condition_req_Fragment();
                            fragment.setArguments(bundle);
                            arr_req_fragment.add(fragment);
                        }else if(obj.getString("MATCHED_FLAG").equals("Y") || obj.getString("MATCHED_FLAG").equals("H")){
                            condition_proc_Fragment fragment = new condition_proc_Fragment();
                            fragment.setArguments(bundle);
                            arr_proc_fragment.add(fragment);
                        }
                    }

                    adapter_req = new condition_req_pagerAdapter(getSupportFragmentManager(),ismymentor, arr_req_fragment);
                    adapter_proc = new condition_proc_pagerAdapter(getSupportFragmentManager(),ismymentor, arr_proc_fragment);
                    vp_req_condition.setAdapter(adapter_req);
                    vp_proc_condition.setAdapter(adapter_proc);
                    if(arr_req_fragment.size() > 0) {
                        ((TextView) findViewById(R.id.tv_series_condition)).setText("1/"+arr_req_fragment.size());
                    }
                    vp_req_condition.setOnPageChangeListener(new viewpagerChangeListener(adapter_req,(TextView)findViewById(R.id.tv_series_condition)));
                    vp_proc_condition.setOnPageChangeListener(new viewpagerChangeListener(adapter_proc,(TextView)findViewById(R.id.tv_series_condition)));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                params.put("UserID", SaveSharedPreference.getUserId(mContext));
                return params;
            }
        };
        postRequestQueue.add(postJsonRequest);
    }
}
