package accepted.talentplanet_renewal2.Profile;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import accepted.talentplanet_renewal2.R;

/**
 * Created by Accepted on 2019-07-19.
 */

public class customDialog_Profile extends Dialog {

    TextView tv_cancel_edittalent_popup;

    public customDialog_Profile(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.profile_edittalent_popup);     //다이얼로그에서 사용할 레이아웃입니다.

        tv_cancel_edittalent_popup = findViewById(R.id.tv_cancel_edittalent_popup);
        tv_cancel_edittalent_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();   //
            }
        });
    }
}
