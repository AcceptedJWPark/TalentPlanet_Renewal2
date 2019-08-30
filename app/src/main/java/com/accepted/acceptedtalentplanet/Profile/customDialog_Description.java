package com.accepted.acceptedtalentplanet.Profile;

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

import com.accepted.acceptedtalentplanet.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.accepted.acceptedtalentplanet.R;
import com.accepted.acceptedtalentplanet.SaveSharedPreference;
import com.accepted.acceptedtalentplanet.VolleySingleton;

/**
 * Created by Accepted on 2019-07-19.
 */

public class customDialog_Description extends Dialog {

    Context mContext;

    RelativeLayout rl_editorhead_popup;

    ImageView iv_talent_img_edit;
    ImageView iv_planetimage_popup;

    TextView tv_edittalent_title_popup;
    TextView tv_cancel_edittalent_popup;
    TextView tv_save_edittalent_popup;

    EditText et_edit_talent;
    int talentID = 0;
    private int code;
    private boolean inPerson;
    private boolean fromFriendFlag;
    private String friendTK;
    private String flag;
    private String userDescription;

    public customDialog_Description(@NonNull Context context, String description, boolean isUser) {
        super(context);

        mContext = context;
        flag = SaveSharedPreference.getPrefTalentFlag(mContext);
        inPerson = isUser;
        userDescription = description;
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.profile_edittalent_popup);     //다이얼로그에서 사용할 레이아웃입니다.
    }

    public void makeLayout() {
        rl_editorhead_popup = findViewById(R.id.rl_editorhead_popup);
        if (flag.equals("N")) {
            rl_editorhead_popup.setBackgroundColor( mContext.getResources().getColor(R.color.color_mentee));
        }

        iv_talent_img_edit = findViewById(R.id.iv_talent_img_edit);
        iv_talent_img_edit.setVisibility(View.GONE);

        et_edit_talent = findViewById(R.id.et_edit_talent);
        tv_edittalent_title_popup = findViewById(R.id.tv_edittalent_title_popup);

        et_edit_talent.setHint("");
        if (userDescription != null && userDescription.length() != 0 && !userDescription.equals("터치해서 자기소개를 입력해보세요.")) {
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

        if (!inPerson) {
            iv_planetimage_popup  = (ImageView) findViewById(R.id.iv_planetimage_popup);

            tv_cancel_edittalent_popup.setText("자기소개 보기");
            tv_save_edittalent_popup.setVisibility(View.GONE);
            tv_edittalent_title_popup.setVisibility(View.GONE);
            et_edit_talent.setVisibility(View.GONE);
            iv_planetimage_popup.setVisibility(View.GONE);

            ((ImageView)findViewById(R.id.iv_profileclose_popup)).setVisibility(View.VISIBLE);
            ((ImageView)findViewById(R.id.iv_profileclose_popup)).setColorFilter(Color.WHITE);
            ((ImageView)findViewById(R.id.iv_profileclose_popup)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            ((TextView)findViewById(R.id.tv_userdescript_talent)).setVisibility(View.VISIBLE);
            if (userDescription != null && userDescription.length() != 0) {
                ((TextView)findViewById(R.id.tv_userdescript_talent)).setText(userDescription);
            }

            if (fromFriendFlag) {
                if (friendTK.equals("N")) {
                    rl_editorhead_popup.setBackgroundColor( mContext.getResources().getColor(R.color.color_mentee));
                }
            }
        }
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

    public void setInPerson(boolean flag) {
        this.inPerson = flag;
    }

    public void setFriendFlag(boolean flag2) {
        this.fromFriendFlag = flag2;
    }

    public void setFriendTelantKind(String kind) {
        this.friendTK = kind;
    }

}
