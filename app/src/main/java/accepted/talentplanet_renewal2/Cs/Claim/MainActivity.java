package accepted.talentplanet_renewal2.Cs.Claim;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.VolleyMultipartRequest;
import accepted.talentplanet_renewal2.VolleySingleton;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static accepted.talentplanet_renewal2.SaveSharedPreference.hideKeyboard;

/**
 * Created by Accepted on 2017-10-31.
 */

public class MainActivity extends AppCompatActivity {

    private Spinner spn_ClaimType;
    private TextView tv_SharingList;
    private TextView tv_Txt;
    private EditText et_Claim;


    private View v_Divider;
    private TextView tv_TxtCount;
    private Context mContext;

    private Button btn_SaveClaim;
    private TextView tv_AttachFile;

    private String name, hashtag, tarUserID, talentFlag, status;
    private int matchingID;
    private final int GALLERY_CODE = 1112;
    private final int CLAIM_CODE = 1113;
    private boolean isSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerservice_claimactivity);

        isSelect = getIntent().getBooleanExtra("isSelected", false);
        if (isSelect) {
            name = getIntent().getStringExtra("name");
            hashtag = getIntent().getStringExtra("hashtag");
            tarUserID = getIntent().getStringExtra("tarUserID");
            matchingID = getIntent().getIntExtra("matchingID", -1);
            tarUserID = getIntent().getStringExtra("tarUserID");
            talentFlag = getIntent().getStringExtra("talentFlag");
            status = getIntent().getStringExtra("status");
        }
        mContext = getApplicationContext();


        spn_ClaimType = (Spinner) findViewById(R.id.spn_ClaimType_Claim);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.CustomerService_ClaimSpinnerList, R.layout.customerservice_claim_spinnertext);
        spn_ClaimType.setAdapter(adapter);

        tv_SharingList = (TextView) findViewById(R.id.tv_SharingList_Claim);
        tv_SharingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), accepted.talentplanet_renewal2.SharingList.MainActivity.class);
                intent.putExtra("isClaimActivity", true);
                startActivityForResult(intent, CLAIM_CODE);
            }
        });

        //TODO : ??
        if (isSelect) {
            String str = talentFlag;
            tv_Txt = (TextView) findViewById(R.id.tv_txt_Claim);
            String[] arrHashtag = hashtag.split("\\|");
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < arrHashtag.length; i++){
                if(i < 3 && !arrHashtag[i].isEmpty()){
                    if(i != 0){
                        sb.append(", ");
                    }

                    sb.append(arrHashtag);
                }
            }
            tv_Txt.setText("\"" + name + " " + str + " " + sb.toString() + "의 건" + "\"");
        }


        et_Claim = (EditText) findViewById(R.id.et_Content_Claim);
        tv_TxtCount = (TextView) findViewById(R.id.tv_TxtCount_Claim);
        et_Claim.setPrivateImeOptions("defaultInputmode=korean;");
        et_Claim.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v, mContext);
                }

            }
        });
        et_Claim.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_TxtCount.setText(String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        v_Divider = findViewById(R.id.v_Divider_Claim);
        tv_Txt = (TextView) findViewById(R.id.tv_txt_Claim);

        btn_SaveClaim = (Button) findViewById(R.id.btn_SaveClaim_Claim);
        btn_SaveClaim.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {

                                                 AlertDialog.Builder AlarmDeleteDialog = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.myDialog));
                                                 if (et_Claim.getText().length() == 0) {
                                                     Toast.makeText(mContext, "신고 내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
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
                                                 } else {
                                                     Toast.makeText(MainActivity.this,"공유 내역을 선택해주세요.",Toast.LENGTH_SHORT).show();
                                                     return;
                                                 }
                                             }
                                         });
    }


    public void requestClaim() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Customer/requestClaim_new.do", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {

                    JSONObject obj = new JSONObject(new String(response.data));
                        Toast.makeText(mContext, "신고가 정상적으로 접수되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(mContext, accepted.talentplanet_renewal2.Cs.MainActivity_Cs.class);
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

                switch(spn_ClaimType.getSelectedItem().toString()){
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

                params.put("matchingID", String.valueOf(matchingID));
                params.put("tarUserID", tarUserID);
                params.put("userID", SaveSharedPreference.getUserId(mContext));
                params.put("claimType", String.valueOf(claimType));
                params.put("claimSummary", et_Claim.getText().toString());
                params.put("status", status);
                return params;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData(){
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                //params.put("pic", new VolleyMultipartRequest.DataPart(imagename + ".png", getFileDataFromDrawable()));
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
                    tv_Txt = (TextView) findViewById(R.id.tv_txt_Claim);
                    tv_Txt.setText(data.getStringExtra("data"));
                    break;
                case CLAIM_CODE:
                    name = data.getStringExtra("name");
                    hashtag = data.getStringExtra("hashtag");
                    tarUserID = data.getStringExtra("tarUserID");
                    matchingID = data.getIntExtra("matchingID", -1);
                    tarUserID = data.getStringExtra("tarUserID");
                    talentFlag = data.getStringExtra("talentFlag");
                    status = data.getStringExtra("status");
                    isSelect = true;

                    String strStatus = null;
                    if(status.equals("Y"))
                    {
                        strStatus = "진행";
                    }else if(status.equals("H"))
                    {
                        strStatus = "멘토 완료";
                    }else if(status.equals("C"))
                    {
                        strStatus = "완료";
                    }else{
                        strStatus = "취소";
                    }

                    String str = talentFlag;
                    tv_Txt = (TextView) findViewById(R.id.tv_txt_Claim);

                    String[] arrHashtag = hashtag.split("\\|");
                    StringBuilder sb = new StringBuilder();
                    for(int i = 0; i < arrHashtag.length; i++){
                        if(i < 3 && !arrHashtag[i].isEmpty()){
                            if(i != 0){
                                sb.append(", ");
                            }

                            sb.append(arrHashtag[i]);
                        }
                    }
                    tv_Txt.setText("\"" + name + " " + str + " " + sb.toString() + "의 건" + "\"");
                    break;

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



