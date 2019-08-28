package accepted.talentplanet_renewal2.AddCategory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import accepted.talentplanet_renewal2.Classes.TalentObject_Home;
import accepted.talentplanet_renewal2.Home.MainActivity;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.VolleySingleton;

import static android.graphics.Color.WHITE;


public class MainActivity_AddCategory extends AppCompatActivity {

    Context mContext;

    LinearLayout ll_addcategory1;
    LinearLayout ll_addcategory2;

    ImageView iv_addcategory1;
    ImageView iv_addcategory2;

    boolean isteacher_category;
    boolean isstudent_category;

    EditText et_addcategory_title;
    EditText et_addcategory_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcategory_activity);

        mContext = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        }



        ((ImageView)findViewById(R.id.img_back_toolbar_talentlist)).setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                finish();
            }
        });

        ((TextView)findViewById(R.id.tv_toolbar_talentlist)).setText("카테고리 신청");
        ((ImageView)findViewById(R.id.iv_toolbar_search_talentlist)).setVisibility(View.GONE);

        ll_addcategory1 = findViewById(R.id.ll1_addcategory);
        ll_addcategory2 = findViewById(R.id.ll2_addcategory);
        iv_addcategory1 = findViewById(R.id.iv1_addcategory);
        iv_addcategory2 = findViewById(R.id.iv2_addcategory);

        et_addcategory_title = findViewById(R.id.et_addcategory_title);
        et_addcategory_content = findViewById(R.id.et_addcategory_content);

        isteacher_category=false;
        isstudent_category=false;

        ll_addcategory1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isteacher_category)
                {
                    iv_addcategory1.setImageResource(R.drawable.icon_checkbox_selected);
                    isteacher_category=true;
                }else
                {
                    iv_addcategory1.setImageResource(R.drawable.icon_checkbox_unselected);
                    isteacher_category=false;
                }
            }
        });

        ll_addcategory2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isstudent_category)
                {
                    iv_addcategory2.setImageResource(R.drawable.icon_checkbox_selected);
                    isstudent_category=true;
                }else
                {
                    iv_addcategory2.setImageResource(R.drawable.icon_checkbox_unselected);
                    isstudent_category=false;
                }
            }
        });

        ((Button)findViewById(R.id.btn_SaveClaim_addCategory)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isteacher_category&&!isstudent_category) {
                    Toast.makeText(mContext,"누구의 카테고리인지 선택해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(et_addcategory_title.length()==0) {
                    Toast.makeText(mContext,"카테고리 명을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(et_addcategory_content.length()==0) {
                    Toast.makeText(mContext,"카테고리의 설명을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }

                requestNewCategory();
            }
        });
    }

    private void requestNewCategory() {
        RequestQueue postRequestQueue = Volley.newRequestQueue(mContext);
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Customer/requestNewCategory.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("reuslt").equals("success")){
                        Toast.makeText(mContext,"신청이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity_AddCategory.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();

                String TALENT_FLAG = "";
                String CATEGORY_NAME = "";
                String CATEGORY_CONTENT = "";
                if(isteacher_category) {
                    TALENT_FLAG = "Y";
                } else if (isstudent_category) {
                    TALENT_FLAG = "N";
                }
                CATEGORY_NAME = et_addcategory_title.getText().toString();
                CATEGORY_CONTENT = et_addcategory_content.getText().toString();

                params.put("USER_ID", SaveSharedPreference.getUserId(mContext));
                params.put("TALENT_FLAG", TALENT_FLAG);
                params.put("CATEGORY_NAME", CATEGORY_NAME);
                params.put("CATEGORY_CONTENT", CATEGORY_CONTENT);

                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }
}
