package accepted.talentplanet_renewal2.Cs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import accepted.talentplanet_renewal2.Home.MainActivity;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.VolleySingleton;

public class MainActivity_Qna extends AppCompatActivity {

    Context mContext;
    private Window window;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerservice_question_activity);


        mContext = getApplicationContext();
        ((TextView) findViewById(R.id.tv_toolbar_talentlist)).setText("문의하기");
        ((ImageView) findViewById(R.id.iv_toolbar_search_talentlist)).setVisibility(View.GONE);

        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));

        ((Button) findViewById(R.id.btn_requset_question)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String qTitle = ((TextView) findViewById(R.id.et_question_title)).getText().toString();
                final String qSummary = ((TextView) findViewById(R.id.et_qeustion_content)).getText().toString();
                requestQuestion(qTitle, qSummary);
            }
        });

        // 뒤로가기 이벤트
        ((ImageView) findViewById(R.id.img_back_toolbar_talentlist)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void requestQuestion(final String qTitle, final String qSummary) {
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Customer/requestQuestion.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("result").equals("success")){
                        Toast.makeText(mContext, "신청이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap();
                params.put("userID", SaveSharedPreference.getUserId(mContext));
                params.put("questionTitle", qTitle);
                params.put("questionSummary", qSummary);
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }
}
