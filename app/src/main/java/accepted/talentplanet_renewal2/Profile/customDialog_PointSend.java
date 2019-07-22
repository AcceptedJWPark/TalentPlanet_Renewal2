package accepted.talentplanet_renewal2.Profile;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import accepted.talentplanet_renewal2.R;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

/**
 * Created by Accepted on 2019-07-19.
 */

public class customDialog_PointSend extends Dialog {

    ImageView iv_cancel_pointsend;
    View [] view_Estimate = new View[10];
    int [] colorGradient = new int[10];


    public customDialog_PointSend(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.pointsend_popup);     //다이얼로그에서 사용할 레이아웃입니다.

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
            view_Estimate[i].setBackgroundColor(WHITE);
            final int finalI = i;
            view_Estimate[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int j=0; j<view_Estimate.length; j++)
                    {
                        if(j<= finalI)
                        {
                            view_Estimate[j].setBackgroundColor(colorGradient[j]);
                        }else
                        {
                            view_Estimate[j].setBackgroundColor(WHITE);
                        }
                    }
                }
            });
        }


        iv_cancel_pointsend = findViewById(R.id.iv_cancel_pointsend);
        iv_cancel_pointsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();   //
            }
        });
    }
}
