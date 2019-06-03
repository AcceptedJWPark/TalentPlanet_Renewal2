package accepted.talentplanet_renewal2.Profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.volokh.danylo.hashtaghelper.HashTagHelper;

import accepted.talentplanet_renewal2.R;

public class MainActivity_Detail extends AppCompatActivity {

    TextView hashTextView;
    HashTagHelper mEditTextHashTagHelper;
    Button btn_save_detail;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__detail);

        // 기본 변수 선언
        hashTextView = (TextView) findViewById(R.id.et_detail);
        btn_save_detail = (Button) findViewById(R.id.btn_save_detail);
        ((TextView)findViewById(R.id.tv_toolbar)).setText("상세정보");
        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);

        hashTextView.requestFocus();
        // Hashtag Helper
        mEditTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), null);
        mEditTextHashTagHelper.handle(hashTextView);

        //키보드 보이게 하는 부분
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        findViewById(R.id.img_show1x15).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        // 완료 버튼
        btn_save_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTalent();
            }
        });

        // 뒤로가기 이벤트
        ((ImageView) findViewById(R.id.img_open_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

                immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                finish();
            }
        });
    }
    
    private void saveTalent(){
        // to-do 저장 로직

        // 저장 성공 시
        Intent resultIntent = new Intent();
        resultIntent.putExtra("ProfileText", hashTextView.getText().toString());
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
