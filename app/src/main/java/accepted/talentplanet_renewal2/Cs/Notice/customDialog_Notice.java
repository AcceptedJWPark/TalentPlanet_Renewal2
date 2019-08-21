package accepted.talentplanet_renewal2.Cs.Notice;


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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import accepted.talentplanet_renewal2.R;

public class customDialog_Notice extends Dialog {

    Context mContext;

    public  customDialog_Notice(@NonNull Context ctx, NoticeData data) {
        super(ctx);

        mContext = ctx;
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.customerservice_dialog_popup);     //다이얼로그에서 사용할 레이아웃입니다.

        ImageView iv_noticeclose_popup = findViewById(R.id.iv_noticeclose_popup);
        iv_noticeclose_popup.setColorFilter(Color.WHITE);

        TextView tv_noticetitle_popup = (TextView) findViewById(R.id.tv_noticetitle_popup);
        TextView tv_noticedate_popup = (TextView) findViewById(R.id.tv_noticedate_popup);
        TextView tv_noticesummary_popup = (TextView) findViewById(R.id.tv_noticesummary_popup);

        // 날짜 형식
        Long regDate = data.getCREATION_DATE();
        Date date = new Date(regDate);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String createDate = formatter.format(date);

        // 정보 입력
        tv_noticetitle_popup.setText(data.getNOTICE_TITLE());
        tv_noticedate_popup.setText(createDate);
        tv_noticesummary_popup.setText(data.getNOTICE_SUMMARY());

        iv_noticeclose_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
