package accepted.talentplanet_renewal2.Profile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import accepted.talentplanet_renewal2.Home.MainActivity;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.TalentAdd.MainActivity_TalentAdd;
import accepted.talentplanet_renewal2.VolleySingleton;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

/**
 * Created by Accepted on 2019-07-19.
 */

public class customDialog_PointSend extends Dialog {

    LinearLayout ll_pointsendbg_popup;

    ImageView civ_user_profile;
    ImageView iv_cancel_pointsend;
    ImageView iv_icon1_popup;
    ImageView iv_gendericon_popup;

    TextView tv_username_popup;
    TextView tv_userbirth_popup;
    TextView tv_userdescript_popup;
    TextView tv_guide_popup;

    Button btn_reviewuser_popup;

    View [] view_Estimate = new View[10];
    int [] colorGradient = new int[10];

    Context mContext;

    private int score;
    private String MessageID;
    private String userName;
    private String imgResource;
    private TimeZone time= TimeZone.getTimeZone("Asia/Seoul");
    public SQLiteDatabase sqliteDatabase = null;
    String dbName = "/accepted.db";


    // 파라미터로 받아야할 값들
    private String isMentor;
    private String mentorID;
    private String menteeID;

    public customDialog_PointSend(@NonNull Context context, String mode, String targetID, String gender, Intent intent) {
        super(context);
        mContext = context;
        isMentor = mode;
        // 기본으로 표시될 점수
        score = 9;

        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.pointsend_popup);     //다이얼로그에서 사용할 레이아웃입니다.

        civ_user_profile = findViewById(R.id.civ_user_profile);
        iv_icon1_popup = findViewById(R.id.iv_icon1_popup);
        ll_pointsendbg_popup = findViewById(R.id.ll_pointsendbg_popup);
        iv_gendericon_popup = findViewById(R.id.iv_gendericon_popup);
        tv_username_popup = findViewById(R.id.tv_username_popup);
        tv_userbirth_popup = findViewById(R.id.tv_userbirth_popup);
        tv_userdescript_popup = findViewById(R.id.tv_userdescript_popup);

        // User Profile Image
        imgResource = intent.getStringExtra("S_FILE_PATH");
        if (imgResource != null && !imgResource.equals("NODATA")) {
            Glide.with(mContext).load(SaveSharedPreference.getImageUri() + imgResource).into(civ_user_profile);
        }
        // User Gender Icon
        if (intent.getBooleanExtra("inPerson",false) == false && intent.getStringExtra("userGender").equals("여")) {
            Glide.with(mContext).load(mContext.getResources().getDrawable(R.drawable.icon_female)).into((ImageView) findViewById(R.id.iv_gendericon_popup));
        }


        if (isMentor.equals("Y")) {
            MessageID = String.valueOf(intent.getIntExtra("MessageID", 0));

            ll_pointsendbg_popup.setBackgroundColor(mContext.getResources().getColor(R.color.color_mentor));

            iv_icon1_popup = findViewById(R.id.iv_icon1_popup);
            tv_guide_popup = findViewById(R.id.tv_guide_popup);
            iv_icon1_popup.setColorFilter(Color.WHITE);
            iv_gendericon_popup.setColorFilter(Color.WHITE);

            tv_username_popup.setTextColor(Color.WHITE);
            tv_userbirth_popup.setTextColor(Color.WHITE);
            tv_userdescript_popup.setTextColor(Color.WHITE);
            tv_guide_popup.setTextColor(Color.WHITE);

            ((TextView)findViewById(R.id.tv_text_pointsend)).setText("Student에게 포인트를 받습니다.");
            ((TextView)findViewById(R.id.btn_reviewuser_popup)).setText("포인트 받기");


            mentorID = SaveSharedPreference.getUserId(mContext);
            menteeID = targetID;

          } else {

            ll_pointsendbg_popup.setBackgroundColor(mContext.getResources().getColor(R.color.color_mentee));
            mentorID = targetID;
            menteeID = SaveSharedPreference.getUserId(mContext);


            ((TextView)findViewById(R.id.tv_text_pointsend)).setText("Teacher에게 포인트를 보냅니다.");
            ((TextView)findViewById(R.id.btn_reviewuser_popup)).setText("포인트 보내기");


        }

        // 상대방의 아이디
        userName = intent.getStringExtra("userName");
        tv_username_popup.setText(userName);

        if (intent.getStringExtra("BIRTH_FLAG").equals("N")) {
            String userBirth = intent.getStringExtra("userInfo");

            tv_userbirth_popup.setText(userBirth.replaceAll("-", "."));
        } else {
            tv_userbirth_popup.setText("비공개");
        }

        tv_userdescript_popup.setText(intent.getStringExtra("userDescription"));

        view_Estimate[0] = findViewById(R.id.v1_estimate_pointsend);
        view_Estimate[1] = findViewById(R.id.v2_estimate_pointsend);
        view_Estimate[2] = findViewById(R.id.v3_estimate_pointsend);
        view_Estimate[3] = findViewById(R.id.v4_estimate_pointsend);
        view_Estimate[4] = findViewById(R.id.v5_estimate_pointsend);
        view_Estimate[5] = findViewById(R.id.v6_estimate_pointsend);
        view_Estimate[6] = findViewById(R.id.v7_estimate_pointsend);
        view_Estimate[7] = findViewById(R.id.v8_estimate_pointsend);
        view_Estimate[8] = findViewById(R.id.v9_estimate_pointsend);
        view_Estimate[9] = findViewById(R.id.v10_estimate_pointsend);

        colorGradient[0] = Color.parseColor("#ff6666");
        colorGradient[1] = Color.parseColor("#ff6f65");
        colorGradient[2] = Color.parseColor("#ff7664");
        colorGradient[3] = Color.parseColor("#ff7d63");
        colorGradient[4] = Color.parseColor("#ff8363");
        colorGradient[5] = Color.parseColor("#ff8962");
        colorGradient[6] = Color.parseColor("#ff8e62");
        colorGradient[7] = Color.parseColor("#ff9462");
        colorGradient[8] = Color.parseColor("#ff9a61");
        colorGradient[9] = Color.parseColor("#ffa061");


        for(int i=0; i<view_Estimate.length; i++)
        {
            final int finalI = i;
            view_Estimate[i].setBackgroundColor(colorGradient[i]);
            view_Estimate[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int j=0; j<view_Estimate.length; j++)
                    {
                        if(j<= finalI)
                        {
                            view_Estimate[j].setBackgroundColor(colorGradient[j]);
                            score = j;
                        }else
                        {
                            view_Estimate[j].setBackgroundColor(WHITE);
                        }
                    }
                }
            });
        }


        btn_reviewuser_popup = findViewById(R.id.btn_reviewuser_popup);
        btn_reviewuser_popup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                newSendInterest();


            }
        });

        iv_cancel_pointsend = findViewById(R.id.iv_cancel_pointsend);
        iv_cancel_pointsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();   //
            }
        });
    }

    private void newSendInterest() {
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "TalentSharing/newSendInterest.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("result").equals("success")){

                        int userPoint = 0;

                        if (isMentor.equals("Y")) {
                            userPoint = SaveSharedPreference.getTalentPoint(mContext) + 1;
                            sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(mContext.getFilesDir() + dbName, null);
                            sqliteDatabase.execSQL("UPDATE TB_CHAT_LOG SET POINT_SEND_FLAG = '1' WHERE MESSAGE_ID = '" + MessageID + "'");
                        } else if (isMentor.equals("N")) {
                            userPoint = SaveSharedPreference.getTalentPoint(mContext) - 1;
                            int roomID = SaveSharedPreference.makeChatRoom(mContext, mentorID, userName, imgResource);
                            final Date date = new Date();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd,a hh:mm:ss");
                            simpleDateFormat.setTimeZone(time);
                            final String nowDateStr = simpleDateFormat.format(date);
                            sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(mContext.getFilesDir() + dbName, null);
                            sqliteDatabase.execSQL("INSERT INTO TB_CHAT_LOG(MESSAGE_ID, ROOM_ID, MASTER_ID, USER_ID, CONTENT, CREATION_DATE, POINT_MSG_FLAG) VALUES (" + obj.getString("MESSAGE_ID") + ","+roomID+" ,'" + SaveSharedPreference.getUserId(mContext) + "', '"+SaveSharedPreference.getUserId(mContext)+"','포인트 보내기','"+nowDateStr+"', '1')");
                        }

                        SaveSharedPreference.setPrefTalentPoint(mContext, userPoint);

                        Toast.makeText(mContext, "평가가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                params.put("MenteeID", menteeID);
                params.put("MentorID", mentorID);
                params.put("user", SaveSharedPreference.getUserId(mContext));
                params.put("isMentor", isMentor);
                params.put("Score", String.valueOf(score));
                Log.d("params : ", params.toString());
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }

}
