package accepted.talentplanet_renewal2.Profile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.VolleySingleton;

/**
 * Created by Accepted on 2019-07-19.
 */

public class customDialog_Description extends Dialog {

    Context mContext;

    RelativeLayout rl_editorhead_popup;

    ImageView iv_talent_img_edit;

    TextView tv_edittalent_title_popup;
    TextView tv_cancel_edittalent_popup;
    TextView tv_save_edittalent_popup;

    EditText et_edit_talent;
    int talentID = 0;
    private int code;

    public customDialog_Description(@NonNull Context context, String userDescription) {
        super(context);

        mContext = context;
        String flag = SaveSharedPreference.getPrefTalentFlag(mContext);
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.profile_edittalent_popup);     //다이얼로그에서 사용할 레이아웃입니다.

        rl_editorhead_popup = findViewById(R.id.rl_editorhead_popup);
        if (flag.equals("N")) {
            rl_editorhead_popup.setBackgroundColor( mContext.getResources().getColor(R.color.color_mentee));
        }

        iv_talent_img_edit = findViewById(R.id.iv_talent_img_edit);
        iv_talent_img_edit.setVisibility(View.GONE);

        et_edit_talent = findViewById(R.id.et_edit_talent);
        tv_edittalent_title_popup = findViewById(R.id.tv_edittalent_title_popup);

        if (userDescription != null && userDescription.length() != 0) {
            et_edit_talent.setText(userDescription);
        }

        tv_cancel_edittalent_popup = findViewById(R.id.tv_cancel_edittalent_popup);
        tv_cancel_edittalent_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();   //
            }
        });

        tv_save_edittalent_popup = findViewById(R.id.tv_save_edittalent_popup);
        tv_save_edittalent_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
    }

    private void test() {
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Profile/updateMyProfileInfo.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("result").equals("success")){

                        SaveSharedPreference.setPrefUserDescription(mContext, et_edit_talent.getText().toString());

                        Toast.makeText(mContext, "프로필 저장이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(mContext, MainActivity_Profile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("inPerson", true);
                        intent.putExtra("cateCode", String.valueOf(code));
                        mContext.startActivity(intent);

                        dismiss();
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
                params.put("PROFILE_DESCRIPTION", et_edit_talent.getText().toString());
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }
}
