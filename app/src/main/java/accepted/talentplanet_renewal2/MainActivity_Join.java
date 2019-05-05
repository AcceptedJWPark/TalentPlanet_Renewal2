package accepted.talentplanet_renewal2;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity_Join extends AppCompatActivity {

    TextView tv_toolbar;

    boolean malecheck;
    LinearLayout ll_malecheck;
    LinearLayout ll_femalecheck;

    ImageView img_malecheck;
    ImageView img_femalecheck;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        malecheck=true;
        setContentView(R.layout.activity_join);


        img_malecheck = findViewById(R.id.img_malecheck_join);
        img_femalecheck = findViewById(R.id.img_femalecheck_join);
        ll_malecheck = findViewById(R.id.ll_malecheck_join);
        ll_femalecheck = findViewById(R.id.ll_femalecheck_join);

        tv_toolbar=findViewById(R.id.tv_toolbar);
        tv_toolbar.setText("회원가입");
        ((ImageView)findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);
        findViewById(R.id.img_show1x15).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        ll_malecheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                malecheck = true;
                checkGender();
            }
        });

        ll_femalecheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                malecheck = false;
                checkGender();
            }
        });



    }

    public void checkGender()
    {
        if (malecheck)
        {
            img_malecheck.setImageResource(R.drawable.icon_check1on);
            img_femalecheck.setImageResource(R.drawable.icon_check1off);
        }
        else
        {
            img_malecheck.setImageResource(R.drawable.icon_check1off);
            img_femalecheck.setImageResource(R.drawable.icon_check1on);
        }
    }




}
