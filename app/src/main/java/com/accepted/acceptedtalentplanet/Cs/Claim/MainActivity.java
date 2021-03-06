package com.accepted.acceptedtalentplanet.Cs.Claim;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.accepted.acceptedtalentplanet.R;
import com.accepted.acceptedtalentplanet.SaveSharedPreference;
import com.accepted.acceptedtalentplanet.VolleyMultipartRequest;
import com.accepted.acceptedtalentplanet.VolleySingleton;

import com.accepted.acceptedtalentplanet.SaveSharedPreference;
import com.accepted.acceptedtalentplanet.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.accepted.acceptedtalentplanet.SaveSharedPreference.hideKeyboard;

/**
 * Created by Accepted on 2017-10-31.
 */

public class MainActivity extends AppCompatActivity {

    private EditText et_Claim;


    private Context mContext;

    private Button btn_SaveClaim;


    private ImageView[] iv_claimSelect = new ImageView[5];
    private LinearLayout[] ll_claimSelect = new LinearLayout[5];
    private boolean[] claimSelected = new boolean[5];

    private boolean isConfirm;
    private ImageView iv_confirmClaim;
    private LinearLayout ll_confirmClaim;
    private TextView tv_SharingList_Claim;
    private CircularImageView civ_user_claim;

    private String claimChecked;

    private String userName, hashtag, tarUserID, talentFlag, sFilePath;

    private final int GALLERY_CODE = 1112;
    private final int CLAIM_CODE = 1113;
    private boolean isSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerservice_claimactivity);

        mContext = getApplicationContext();

        ((ImageView)findViewById(R.id.img_open_dl)).setVisibility(View.GONE);
        ((ImageView)findViewById(R.id.img_back_toolbar)).setVisibility(View.VISIBLE);

        ((ImageView)findViewById(R.id.img_back_toolbar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((ImageView)findViewById(R.id.img_search_talentadd)).setVisibility(View.GONE);

        ((TextView)findViewById(R.id.tv_toolbar)).setText("신고하기");
        ((TextView)findViewById(R.id.tv_toolbar)).setVisibility(View.VISIBLE);
        ((Spinner)findViewById(R.id.sp_toolbar)).setVisibility(View.GONE);

        ((ImageView)findViewById(R.id.img_rightbtn)).setVisibility(View.GONE);

        civ_user_claim = findViewById(R.id.civ_user_claim);

        tv_SharingList_Claim = findViewById(R.id.tv_SharingList_Claim);
        tv_SharingList_Claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, com.accepted.acceptedtalentplanet.Messanger.List.MainActivity.class);
                i.putExtra("isClaim", true);
                startActivityForResult(i, 100);
            }
        });



        isSelect = getIntent().getBooleanExtra("isSelected", false);
        if (isSelect) {
            tarUserID = getIntent().getStringExtra("userID");
            userName = getIntent().getStringExtra("userName");
            tv_SharingList_Claim.setText(userName);
            if(!getIntent().getStringExtra("S_FILE_PATH").equals("NODATA"))
                Glide.with(mContext).load(SaveSharedPreference.getImageUri() + getIntent().getStringExtra("S_FILE_PATH")).into(civ_user_claim);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.rgb(255, 102, 102));
        }

        et_Claim = (EditText) findViewById(R.id.et_Content_Claim);
        //et_Claim.setPrivateImeOptions("defaultInputmode=korean;");
        et_Claim.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    SaveSharedPreference.hideKeyboard(v, mContext);
                }

            }
        });

        claimChecked = "";

        iv_claimSelect[0] = findViewById(R.id.iv1_claim);
        iv_claimSelect[1] = findViewById(R.id.iv2_claim);
        iv_claimSelect[2] = findViewById(R.id.iv3_claim);
        iv_claimSelect[3] = findViewById(R.id.iv4_claim);
        iv_claimSelect[4] = findViewById(R.id.iv5_claim);

        ll_claimSelect[0] = findViewById(R.id.ll1_claim);
        ll_claimSelect[1] = findViewById(R.id.ll2_claim);
        ll_claimSelect[2] = findViewById(R.id.ll3_claim);
        ll_claimSelect[3] = findViewById(R.id.ll4_claim);
        ll_claimSelect[4] = findViewById(R.id.ll5_claim);

        for(int i = 0; i< ll_claimSelect.length; i++)
        {
            claimSelected[i] = false;

            final int finalI = i;
            ll_claimSelect[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(finalI==0)
                    {
                        claimChecked="금품 요구";
                    }else if(finalI==1)
                    {
                        claimChecked="폭언 및 욕설";
                    }else if(finalI==2)
                    {
                        claimChecked="No-Show";
                    }else if(finalI==3)
                    {
                        claimChecked="허위 광고";
                    }else
                    {
                        claimChecked="기타";
                    }

                    for(int j = 0; j< ll_claimSelect.length; j++)
                    {
                        if(j== finalI)
                        {
                            claimSelected[j] = true;
                            iv_claimSelect[j].setImageResource(R.drawable.icon_checkbox_selected);
                        }else
                        {
                            claimSelected[j] = false;
                            iv_claimSelect[j].setImageResource(R.drawable.icon_checkbox_unselected);
                        }
                    }
                }
            });
        }


        isConfirm = false;
        iv_confirmClaim = findViewById(R.id.iv_confirmClaim);
        ll_confirmClaim = findViewById(R.id.ll_confirmClaim);

        ll_confirmClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConfirm)
                {
                    isConfirm = true;
                    iv_confirmClaim.setImageResource(R.drawable.icon_checkbox_selected);
                }else
                {
                    isConfirm = false;
                    iv_confirmClaim.setImageResource(R.drawable.icon_checkbox_unselected);
                }
            }
        });





        btn_SaveClaim = (Button) findViewById(R.id.btn_SaveClaim_Claim);
        btn_SaveClaim.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {

                                                 AlertDialog.Builder AlarmDeleteDialog = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.myDialog));
                                                 if (claimChecked.equals("")) {
                                                     Toast.makeText(mContext, "신고 유형을 선택해주세요.", Toast.LENGTH_SHORT).show();
                                                     return;
                                                 } else if (et_Claim.getText().length() == 0) {
                                                     Toast.makeText(mContext, "신고 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                                                     return;
                                                 } else if (!isConfirm) {
                                                     Toast.makeText(mContext, "동의하시면 체크해주세요.", Toast.LENGTH_SHORT).show();
                                                     return;
                                                 } else if (isSelect) {
                                                     AlarmDeleteDialog.setMessage("신고하시겠습니까?")
                                                             .setPositiveButton("신고하기", new DialogInterface.OnClickListener() {
                                                                 @Override
                                                                 public void onClick(DialogInterface dialog, int which) {

                                                                     requestClaim();
                                                                     dialog.cancel();
                                                                     finish();
                                                                 }
                                                             })
                                                             .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                                                                 @Override
                                                                 public void onClick(DialogInterface dialog, int which) {
                                                                     dialog.cancel();
                                                                 }
                                                             });
                                                     AlertDialog alertDialog = AlarmDeleteDialog.create();
                                                     alertDialog.show();
                                                     alertDialog.getButton((DialogInterface.BUTTON_NEGATIVE)).setTextColor(getResources().getColor(R.color.loginPasswordLost));
                                                     alertDialog.getButton((DialogInterface.BUTTON_POSITIVE)).setTextColor(getResources().getColor(R.color.loginPasswordLost));
                                                 } else
                                                     {
                                                     Toast.makeText(MainActivity.this,"대상을 선택해주세요.",Toast.LENGTH_SHORT).show();
                                                     return;
                                                 }
                                             }
                                         });
    }


    public void requestClaim() {

        StringRequest volleyMultipartRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Customer/requestClaim_new.do", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject obj = new JSONObject(response);
                        Toast.makeText(mContext, "신고가 정상적으로 접수되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(mContext, com.accepted.acceptedtalentplanet.Cs.MainActivity_Cs.class);
                        startActivity(i);
                        finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap();
                String strStatus;
                int claimType;

                switch(claimChecked){
                    case "금품 요구":
                        claimType = 1;
                        break;
                    case "폭언 및 욕설":
                        claimType = 2;
                        break;
                    case "No-Show":
                        claimType = 3;
                        break;
                    case "허위 광고":
                        claimType = 4;
                        break;
                    default:
                        claimType = 5;
                }

                params.put("tarUserID", tarUserID);
                params.put("userID", SaveSharedPreference.getUserId(mContext));
                params.put("claimType", String.valueOf(claimType));
                params.put("claimSummary", et_Claim.getText().toString());

                return params;
            }
        };


        VolleySingleton.getInstance(mContext).getRequestQueue().add(volleyMultipartRequest);

    }

    private void selectGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch(requestCode){
                case 100:
                    // 메신저에서 선택하는 경우
                    if(!data.getStringExtra("S_FILE_PATH").equals("NODATA"))
                        Glide.with(mContext).load(SaveSharedPreference.getImageUri() + data.getStringExtra("S_FILE_PATH")).into(civ_user_claim);
                    tv_SharingList_Claim.setText(data.getStringExtra("userName"));
                    tarUserID = data.getStringExtra("userID");
                    isSelect = true;
                    break;
                case GALLERY_CODE:
                    //sendPicture(data.getData());

                    Uri uri = data.getData();
                    ClipData clipData = data.getClipData();

                    if(clipData != null){
                        clipData.getItemCount();
                        Uri urione = clipData.getItemAt(0).getUri();
                    }
                    else if(uri != null){

                    }
                    break;
                case 1:
                    break;
                case CLAIM_CODE:
                    userName = data.getStringExtra("name");
                    hashtag = data.getStringExtra("hashtag");
                    tarUserID = data.getStringExtra("tarUserID");
                    tarUserID = data.getStringExtra("tarUserID");
                    talentFlag = data.getStringExtra("talentFlag");
                    isSelect = true;

            }
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void sendPicture(Uri imgUri){
        String imagePath = getRealPathFromURI(imgUri);
        Log.d("image Path = ", imagePath);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        }catch (IOException e){
            e.printStackTrace();
        }

        int exifOrientation;
        int exifDegree;

        if(exif != null){
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegrees(exifOrientation);
        }else{
            exifDegree = 0;
        }

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imagePath, options); //MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);

            options.inSampleSize = setSimpleSize(options, 960, 720);

            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            bitmap = rotate(bitmap, exifDegree);
            if(bitmap == null){
                Log.d("bitmap = ", "null");
            }

            //uploadBitmap(bitmap);
        }catch (Exception e){
            e.printStackTrace();//bitmap = rotate(bitmap, exifDegree);
        }

    }

    private int setSimpleSize(BitmapFactory.Options options, int requestWidth, int requestHeight){
        // 이미지 사이즈를 체크할 원본 이미지 가로/세로 사이즈를 임시 변수에 대입.
        int originalWidth = options.outWidth;
        int originalHeight = options.outHeight;

        // 원본 이미지 비율인 1로 초기화
        int size = 1;

        // 해상도가 깨지지 않을만한 요구되는 사이즈까지 2의 배수의 값으로 원본 이미지를 나눈다.
        while(requestWidth < originalWidth || requestHeight < originalHeight){
            originalWidth = originalWidth / 2;
            originalHeight = originalHeight / 2;

            size = size * 2;
        }
        return size;
    }

    private int exifOrientationToDegrees(int exifOrientation){
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90){
            return 90;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180){
            return 180;
        }else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270){
            return 270;
        }

        return 0;
    }
    private Bitmap rotate(Bitmap src, float degree){
        Matrix matrix = new Matrix();

        matrix.postRotate(degree);

        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    private String getRealPathFromURI(Uri contentUri){
        int column_index = 0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }

}



