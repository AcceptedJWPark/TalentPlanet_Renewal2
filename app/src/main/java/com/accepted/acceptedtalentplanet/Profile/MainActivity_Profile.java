package com.accepted.acceptedtalentplanet.Profile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.accepted.acceptedtalentplanet.GeoPoint;
import com.accepted.acceptedtalentplanet.Messanger.Chatting.MainActivity;
import com.accepted.acceptedtalentplanet.MyTalent;
import com.accepted.acceptedtalentplanet.PermissionUtil;
import com.accepted.acceptedtalentplanet.SaveSharedPreference;
import com.accepted.acceptedtalentplanet.TalentAdd.MainActivity_TalentAdd;
import com.accepted.acceptedtalentplanet.VolleyMultipartRequest;
import com.accepted.acceptedtalentplanet.VolleySingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.accepted.acceptedtalentplanet.BuildConfig;
import com.accepted.acceptedtalentplanet.Classes.TalentObject_Home;
import com.accepted.acceptedtalentplanet.GeoPoint;
import com.accepted.acceptedtalentplanet.MyTalent;
import com.accepted.acceptedtalentplanet.PermissionUtil;
import com.accepted.acceptedtalentplanet.R;
import com.accepted.acceptedtalentplanet.SaveSharedPreference;
import com.accepted.acceptedtalentplanet.TalentAdd.MainActivity_TalentAdd;
import com.accepted.acceptedtalentplanet.VolleyMultipartRequest;
import com.accepted.acceptedtalentplanet.VolleySingleton;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class MainActivity_Profile extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        GoogleMap.OnMapClickListener{

    TextView tv_toolbarprofle;
    Context mContext;

    String messageFilePath;
    String messageUserID;
    String messageUserName;

    private double Lat;
    private double Lng;

    RecyclerView lv_mentor_profile;
    RecyclerView lv_mentee_profile;
    private ArrayList<ItemData_Profile> userDate = new ArrayList<>();

    private ArrayList<TalentObject_Home> mentorTalentList;
    private ArrayList<TalentObject_Home> menteeTalentList;

    private String targetUserID;
    private String cateCode;
//    private TextView tv_profile_mentor_count, tv_profile_mentee_count;
    private TextView tv_profile_description;
    private TextView tv_birth_profile;
    private Spinner sp_talent_profile;
    private TextView tv_addr_profile;
//    private TextView tv_profile_point;

    private boolean isDetailprofile;

    // 지도 관련 변수
    private String keyword1, keyword2, keyword3;
    private String location;
    private boolean isRegisted;
    private boolean isHavingData;
    private MyTalent data;

    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private GoogleMap gMap;

    private Location mCurrentLocation;
    private boolean mLocationPermissionGranted = false;
    private LocationRequest mLocationRequest;

    private GoogleApiClient mGoogleApiClient = null;

    private Activity mActivity;

    private ImageView img_gender_profile;

    private ImageView iv_cimg_pic_profile;
    private Uri photoUri;
    private String currentPhotoPath;
    private String mImageCaptureName;
    private boolean inPerson;
    private final int CAMERA_CODE = 1111;
    private final int GALLERY_CODE = 1112;
    private String mode;
    private String userTalentDescript;

    // 리스트 뷰를 초기화 하기 위한 변수
    private String listReset;
    private boolean fromFriend;

    private String userID;

    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider";

    // 친구 관련 변수
    private boolean friendFlag;

    //프로필 수정 다이얼로그
    customDialog_Profile cd_profile;
    customDialog_PointSend cd_PointSend;
    customDialog_Description cd_Description;

    SpinnerAdapter_Talent adapter_talent;

    // 새로운 재능인지 아닌지
    private boolean isNewTalent;
    private String listCateCode;
    private String title;
    private int bgID;
    private int spinnerIdx;

    //
    private int talentCnt;


    View [] view_Estimate = new View[10];
    int [] colorGradient = new int[10];
    double averageScore = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        mContext = getApplicationContext();

        img_gender_profile = findViewById(R.id.img_gender_profile);
        tv_profile_description = findViewById(R.id.tv_profile_description);
        tv_birth_profile = findViewById(R.id.tv_birth_profile);

        sp_talent_profile = findViewById(R.id.sp_talent_profile);

        mentorTalentList = new ArrayList<>();
        menteeTalentList = new ArrayList<>();

        tv_toolbarprofle = findViewById(R.id.tv_toolbarprofle);
        mActivity = this;

        // 받은 값 구현
        Intent intent = new Intent(this.getIntent());
        inPerson = intent.getBooleanExtra("inPerson", false);
        Log.d("inPerson_Profile", inPerson + "");
        isNewTalent = intent.getBooleanExtra("isNewTalent", false);
        listCateCode = intent.getStringExtra("cateCode");
        userTalentDescript = intent.getStringExtra("userTalentDescription");
        fromFriend = intent.getBooleanExtra("fromFriend",false);
        mode = SaveSharedPreference.getPrefTalentFlag(mContext);

        //statusbar 변경
        if(fromFriend){
            boolean isMentor = intent.getBooleanExtra("isMentor",false);
            if (isMentor) {
                mode = "Y";
            } else {
                mode = "N";
            }
        }

        // 재능 등록 개수 확인
        getTalentCount();

        if (mode.equals("Y")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
            }

            changeToMentee();

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
            }
        }

        if (inPerson) {
            getMyProfileInfo_new();
            // 유저가 로그인했을 경우
            String userName = SaveSharedPreference.getUserName(mContext);
            String userInfo = SaveSharedPreference.getPrefUserBirth(mContext);
            String userMent = SaveSharedPreference.getPrefUserDescription(mContext);
            String gender = SaveSharedPreference.getPrefUserGender(mContext);
            // 주소
            String lat = SaveSharedPreference.getPrefUserGpLat(mContext);
            String lng = SaveSharedPreference.getPrefUserGpLng(mContext);
            double Lat = 0;
            double Lng = 0;

            ((TextView)findViewById(R.id.tv_isteacher_profile)).setVisibility(GONE);

            if (!lat.equals("") || !lng.equals("")) {
                Lat = Double.parseDouble(lat);
                Lng = Double.parseDouble(lng);
            }

            userID = SaveSharedPreference.getUserId(mContext);

            ((TextView)findViewById(R.id.tv_name_profile)).setText(userName);
            ((TextView)findViewById(R.id.tv_birth_profile)).setText(userInfo);

            if (userMent != null && !userMent.equals("")) {
                ((TextView)findViewById(R.id.tv_profile_description)).setText(userMent);
            } else {
                ((TextView)findViewById(R.id.tv_profile_description)).setText("터치해서 자기소개를 입력해보세요.");
                ((TextView)findViewById(R.id.tv_profile_description)).setBackgroundResource(R.drawable.white_dash_line);
            }

            if (lat.equals("0") || lng.equals("0")) {
                ((ImageView)findViewById(R.id.img_mappointer)).setVisibility(GONE);
                ((TextView)findViewById(R.id.tv_addr_profile)).setText("터치해서 위치를 등록해보세요.");
                ((TextView)findViewById(R.id.tv_addr_profile)).setBackgroundResource(R.drawable.white_dash_line);
            } else {
                final Geocoder geocoder = new Geocoder(mContext);
                try {
                    List<Address> list = geocoder.getFromLocation(Lat,Lng,10);
                    if (list.size()==0) {
                        ((TextView)findViewById(R.id.tv_addr_profile)).setText("해당되는 주소 정보는 없습니다");
                    } else {
                        String[] addr = list.get(0).getAddressLine(0).split(" ");

                        ((TextView)findViewById(R.id.tv_addr_profile)).setText(addr[1]+" "+addr[2]+" "+addr[3]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("입출력오류", "입출력 오류 - 서버에서 주소변환시 에러발생");
                }
            }

            ((TextView)findViewById(R.id.tv_addr_profile)).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MapsActivity.class);
                    intent.putExtra("GP_LAT", SaveSharedPreference.getPrefUserGpLat(mContext));
                    intent.putExtra("GP_LNG", SaveSharedPreference.getPrefUserGpLng(mContext));
                    startActivityForResult(intent, 2030);
                }
            });

            if (gender.equals("남")) {
                ((ImageView)findViewById(R.id.img_gender_profile)).setImageDrawable(getResources().getDrawable(R.drawable.icon_male));
            } else {
                ((ImageView)findViewById(R.id.img_gender_profile)).setImageDrawable(getResources().getDrawable(R.drawable.icon_female));
            }

            ((ImageView)findViewById(R.id.iv_message_profile)).setVisibility(View.GONE);
            ((ImageView)findViewById(R.id.img_addfriend_toolbarprofile)).setVisibility(View.GONE);
            ((ImageView)findViewById(R.id.iv_share_profile)).setVisibility(View.GONE);

            ((ImageView)findViewById(R.id.iv_edittalent_profile)).setColorFilter(Color.parseColor("#ffffff"));
            ((ImageView)findViewById(R.id.iv_deltalent_profile)).setColorFilter(Color.parseColor("#ffffff"));

            ((TextView)findViewById(R.id.tv_profile_description)).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    updateMyProfileInfo();
                }
            });

            iv_cimg_pic_profile = findViewById(R.id.cimg_pic_profile);

            String imgResource = SaveSharedPreference.getMyPicturePath();
            if (imgResource != null && !imgResource.equals("NODATA")) {
                Glide.with(mContext).load(SaveSharedPreference.getImageUri() + SaveSharedPreference.getMyPicturePath())
                        .into(iv_cimg_pic_profile);
            }

            // 프로필 사진 관련
            final android.support.v7.app.AlertDialog.Builder AlarmDeleteDialog = new android.support.v7.app.AlertDialog.Builder(MainActivity_Profile.this);

            iv_cimg_pic_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT > 22) {
                        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, 1);
                    }
                    AlarmDeleteDialog.setMessage("사진을 가져올 곳을 선택해주세요.")
                            .setPositiveButton("카메라", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    selectPhoto();
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("앨범", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    selectGallery();
                                    dialog.cancel();
                                }
                            });
                    android.support.v7.app.AlertDialog alertDialog = AlarmDeleteDialog.create();
                    alertDialog.show();
                }
            });

            changeOpenFlagImage();

            if (mode.equals("Y")) {
                ((ImageView) findViewById(R.id.iv_birthopen_profile)).setColorFilter(mContext.getResources().getColor(R.color.color_mentee));
                ((ImageView) findViewById(R.id.iv_birthclose_profile)).setColorFilter(mContext.getResources().getColor(R.color.color_mentee));
            }

        } else {
            userID = intent.getStringExtra("userID");
            targetUserID = userID;

            getProfileData(intent);

            ((TextView)findViewById(R.id.tv_profile_description)).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    showProfileInfo();
                }
            });
        }


        ((ImageView)findViewById(R.id.iv_addtalent_profile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd_profile.show();
            }
        });

        ((TextView)findViewById(R.id.tv_description_profile)).setMovementMethod(new ScrollingMovementMethod());
        ((TextView)findViewById(R.id.tv_tag_profile)).setMovementMethod(new ScrollingMovementMethod());

        //재능 수정
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        double width = dm.widthPixels; //디바이스 화면 너비
        double height = dm.heightPixels; //디바이스 화면 높이

        if (intent.getStringExtra("userName") != null) {
            cateCode = intent.getStringExtra("cateCode");
            targetUserID = intent.getStringExtra("userID");
            messageUserID = targetUserID;
            messageUserName = intent.getStringExtra("userName");
            messageFilePath = intent.getStringExtra("FILE_PATH");
        }


        if (isNewTalent) {
            String code = intent.getStringExtra("Code");
            title = intent.getStringExtra("talentTitle");
            bgID = intent.getIntExtra("backgroundID", 0);

            addNewTalent(mode, bgID, Integer.parseInt(code), "");
        }else {
            getAllTalent(mode);
        }

        wasItShared(targetUserID);



        // 뒤로가기 이벤트
        ((ImageView) findViewById(R.id.img_back_toolbarprofile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        view_Estimate[0] = findViewById(R.id.v1_estimate_profile);
        view_Estimate[1] = findViewById(R.id.v2_estimate_profile);
        view_Estimate[2] = findViewById(R.id.v3_estimate_profile);
        view_Estimate[3] = findViewById(R.id.v4_estimate_profile);
        view_Estimate[4] = findViewById(R.id.v5_estimate_profile);
        view_Estimate[5] = findViewById(R.id.v6_estimate_profile);
        view_Estimate[6] = findViewById(R.id.v7_estimate_profile);
        view_Estimate[7] = findViewById(R.id.v8_estimate_profile);
        view_Estimate[8] = findViewById(R.id.v9_estimate_profile);
        view_Estimate[9] = findViewById(R.id.v10_estimate_profile);

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

        // 점수계산된값
        String userPoint = "";
        if (inPerson) {
            userPoint = SaveSharedPreference.getPrefUserScore(mContext);
        } else {
            userPoint = intent.getStringExtra("Score") == null ? "" : intent.getStringExtra("Score");
        }

        if (!userPoint.equals("")) {
            averageScore = Double.parseDouble(userPoint) + 1;
        }

        Log.d("AverageScore", averageScore + "");
        for(int i=0; i<Math.round(averageScore); i++)
        {
            final int finalI = i;
            view_Estimate[i].setBackgroundColor(colorGradient[i]);
        }

        // 지도
        // 07/18 11:32

        // 07/18 11:32
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();

//        fragmentManager = getFragmentManager();
//        mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.frg_Map_TalentRegister);
//        mapFragment.getMapAsync(this);

//        createLocationRequest();

        // 07/18 11:32
//        if(!inPerson){
//               mapFragment.getView().setOnClickListener(null);
//        }





//        if (inPerson) {
//            tv_birth_profile.setText(SaveSharedPreference.getPrefUserBirth(mContext));
//            tv_profile_description.setText(SaveSharedPreference.getPrefUserDescription(mContext));
//            tv_profile_description.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity_Profile.this);
//                    ad.setTitle("자기소개 변경");
//                    ad.setMessage("변경할 내용을 입력해 주세요.");
//
//                    final EditText et = new EditText(MainActivity_Profile.this);
//
//                    et.setText(SaveSharedPreference.getPrefUserDescription(mContext));
//                    ad.setView(et);
//
//                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            String txt = et.getText().toString();
//                            updateMyProfileInfo(txt);
//                            tv_profile_description.setText(txt);
//                            SaveSharedPreference.setPrefUserDescription(mContext, txt);
//                        }
//                    });
//
//                    ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//                    ad.show();
//                }
//            });
//
//            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
//            mCurrentLocation = new Location("");
//            mCurrentLocation.setLatitude(Double.parseDouble(SaveSharedPreference.getPrefUserGpLat(mContext)));
//            mCurrentLocation.setLongitude(Double.parseDouble(SaveSharedPreference.getPrefUserGpLng(mContext)));
//            Glide.with(mContext).load(SaveSharedPreference.getImageUri() + SaveSharedPreference.getMyThumbPicturePath()).into((ImageView) findViewById(R.id.cimg_pic_profile));
//        }
//        getProfileData();

        isDetailprofile = true;

//        ((TextView)findViewById(R.id.tv_showdetail_profile)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isDetailprofile)
//                {
//                    ((TextView)findViewById(R.id.tv_showdetail_profile)).setText("간략히 보기");
//                    ((TextView) findViewById(R.id.tv_description_profile)).setMaxLines(Integer.MAX_VALUE);
//                    isDetailprofile = false;
//                }else
//                {
//                    ((TextView)findViewById(R.id.tv_showdetail_profile)).setText("자세히 보기");
//                    ((TextView) findViewById(R.id.tv_description_profile)).setMaxLines(2);
//                    isDetailprofile = true;
//                }
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void changeToMentee() {
        LinearLayout ll_status_bg_profile = findViewById(R.id.ll_status_bg_profile);

        TextView tv_name_profile = findViewById(R.id.tv_name_profile);
        TextView tv_birth_profile = findViewById(R.id.tv_birth_profile);
        TextView tv_addr_profile = findViewById(R.id.tv_addr_profile);
        TextView tv_profile_description = findViewById(R.id.tv_profile_description);

        ImageView img_gender_profile = findViewById(R.id.img_gender_profile);
        ImageView img_mappointer = findViewById(R.id.img_mappointer);
        ImageView iv_message_profile = findViewById(R.id.iv_message_profile);
        ImageView iv_share_profile = findViewById(R.id.iv_share_profile);
        ImageView img_addfriend_toolbarprofile = findViewById(R.id.img_addfriend_toolbarprofile);
        ImageView img_delfriend_toolbarprofile = findViewById(R.id.img_delfriend_toolbarprofile);


        String white = "#ffffff";

        ll_status_bg_profile.setBackgroundColor(getResources().getColor(R.color.color_mentor));
        tv_name_profile.setTextColor(Color.parseColor(white));
        tv_birth_profile.setTextColor(Color.parseColor(white));
        tv_addr_profile.setTextColor(Color.parseColor(white));
        tv_profile_description.setTextColor(Color.parseColor(white));

        img_gender_profile.setColorFilter(getResources().getColor(R.color.color_mentee));
        img_mappointer.setColorFilter(getResources().getColor(R.color.color_mentee));
        iv_message_profile.setColorFilter(getResources().getColor(R.color.color_mentee));
        img_addfriend_toolbarprofile.setColorFilter(getResources().getColor(R.color.color_mentee));
        img_delfriend_toolbarprofile.setColorFilter(getResources().getColor(R.color.color_mentee));
    }

    private void getProfileData(final Intent intent){
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SaveSharedPreference.getServerIp() + "Profile/getMyProfileInfo_new.do",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject profileData = new JSONObject(response);

                            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
                            double width = dm.widthPixels; //디바이스 화면 너비
                            double height = dm.heightPixels; //디바이스 화면 높이

                            String strLat = "";
                            String strLng = "";
                            if (profileData.has("GP_LAT")) {
                                strLat = (String) profileData.get("GP_LAT");
                                Lat = Double.parseDouble(strLat);
                            }
                            if (profileData.has("GP_LNG")) {
                                strLng = (String) profileData.get("GP_LNG");
                                Lng = Double.parseDouble(strLng);
                        }

                        final String bigImg = (String) profileData.get("FILE_PATH");

                        String userName = (String) profileData.get("USER_NAME");
                        String gender = (String) profileData.get("GENDER");
                        String birthFlag = (String) profileData.get("BIRTH_FLAG");
                        String imgResource = (String ) profileData.get("FILE_PATH");
                        String userInfo = (String) profileData.get("USER_BIRTH");
                        String userDescription = profileData.has("PROFILE_DESCRIPTION") ? (String) profileData.get("PROFILE_DESCRIPTION") : "";

                        intent.putExtra("userName", userName);
                        intent.putExtra("userGender", gender);
                        intent.putExtra("BIRTH_FLAG", birthFlag);
                        intent.putExtra("userInfo", userInfo);
                        intent.putExtra("S_FILE_PATH", (String)profileData.get("S_FILE_PATH"));
                        intent.putExtra("userDescription", userDescription);

                        ((TextView)findViewById(R.id.tv_name_profile)).setText(userName);
                        ((TextView)findViewById(R.id.tv_birth_profile)).setText(userInfo.replaceAll("-", "\\."));
                        ((TextView)findViewById(R.id.tv_profile_description)).setText(userDescription);

                        ((TextView)findViewById(R.id.tv_isteacher_profile)).setVisibility(VISIBLE);
                        ((RelativeLayout) findViewById(R.id.rl_edittalent_profile)).setVisibility(View.GONE);
                        ((RelativeLayout) findViewById(R.id.rl_deltalent_profile)).setVisibility(View.GONE);

                        if (gender.equals("남")) {
                            img_gender_profile.setImageDrawable(getResources().getDrawable(R.drawable.icon_male));
                        } else {
                            img_gender_profile.setImageDrawable(getResources().getDrawable(R.drawable.icon_female));
                        }



                        //  타유저의 프로필에서 주소 표시를 위한 geocoder
                        if (Lat != 0.0 && Lng != 0.0) {
                            final Geocoder geocoder = new Geocoder(mContext);
                            try {
                                List<Address> list = geocoder.getFromLocation(Lat,Lng,10);
                                if (list.size()==0) {
                                    ((TextView)findViewById(R.id.tv_addr_profile)).setText("해당되는 주소 정보는 없습니다");
                                } else {
                                    String[] addr = list.get(0).getAddressLine(0).split(" ");

                                    ((TextView)findViewById(R.id.tv_addr_profile)).setText(addr[1]+" "+addr[2]+" "+addr[3]);

                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("입출력오류", "입출력 오류 - 서버에서 주소변환시 에러발생");
                            }

                            // 타인이 자신의 주소를 볼경우
                            ((TextView)findViewById(R.id.tv_addr_profile)).setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, MapsActivity.class);
                                    intent.putExtra("GP_LAT", Lat);
                                    intent.putExtra("GP_LNG", Lng);
                                    intent.putExtra("isUser", true);
                                    startActivityForResult(intent, 2030);
                                }
                            });
                        } else {
                            ((TextView)findViewById(R.id.tv_addr_profile)).setText("위치 정보 없음");
                        }

                        //포인트 지급
                        if (!fromFriend) {
                            mode = SaveSharedPreference.getPrefTalentFlag(mContext);
                        }

                        cd_PointSend = new customDialog_PointSend(MainActivity_Profile.this, mode, userID, gender, intent);
                        WindowManager.LayoutParams wm2 = cd_PointSend.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
                        wm2.copyFrom(cd_PointSend.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
                        wm2.width = (int) (width / 1.1);
                        wm2.height = (int) (height / 1.1);

                        ((ImageView)findViewById(R.id.iv_share_profile)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int userPoint = SaveSharedPreference.getTalentPoint(mContext);
                                if (userPoint <= 0) {
                                    Toast.makeText(mContext, "현재 포인트가 없어 공유하실 수 없습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    cd_PointSend.show();
                                }
                            }
                        });

                        // 타 유저의 공개여부에 맞게
                        checkUserOpenData(birthFlag);

                        if (inPerson) {
                            // 자신이 타유저를 볼때 반전되는 플래그
                            if (mode.equals("Y")) {
                                mode = "N";
                            } else {
                                mode = "Y";
                            }
                        }

                        iv_cimg_pic_profile = findViewById(R.id.cimg_pic_profile);

                        ((ImageView)findViewById(R.id.iv_message_profile)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int roomID = SaveSharedPreference.makeChatRoom(mContext, messageUserID, messageUserName, messageFilePath);
                                if (roomID < 0) {
                                    return;
                                }
                                Intent i = new Intent(mContext, com.accepted.acceptedtalentplanet.Messanger.Chatting.MainActivity.class);
                                i.putExtra("userID", messageUserID);
                                i.putExtra("roomID", roomID);
                                i.putExtra("userName", messageUserName);
                                startActivity(i);
                                finish();
                            }
                        });



                        if (!imgResource.equals("NODATA")) {
                            Glide.with(mContext).load(SaveSharedPreference.getImageUri() + imgResource)
                                    .asBitmap()
                                    .format(DecodeFormat.PREFER_ARGB_8888)
                                    .into(iv_cimg_pic_profile);
                            // 타인의 프로필을 볼 경우 전체화면 식으로
                            iv_cimg_pic_profile.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, ImageActivity.class);
                                    intent.putExtra("bigImg", bigImg);
                                    startActivity(intent);
                                }
                            });
                        }

                        // 타인이 자신의 주소를 볼경우
//                        ((TextView)findViewById(R.id.tv_addr_profile)).setOnClickListener(new View.OnClickListener(){
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(mContext, MapsActivity.class);
//                                intent.putExtra("GP_LAT", Lat);
//                                intent.putExtra("GP_LNG", Lng);
//                                intent.putExtra("isUser", true);
//                                startActivityForResult(intent, 2030);
//                            }
//                        });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(this.getClass().getName(), "test 1 [error]: " +error);
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userID", userID);
                return params;
            }
        };

        // Add StringRequest to the RequestQueue
        requestQueue.add(stringRequest);
    }

    public void getAllTalent(final String talentFlag) {
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        final StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SaveSharedPreference.getServerIp() + "Profile/getAllMyTalent.do",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray talentArr = new JSONArray(response);
                            if(talentArr.length() == 0){
                                if(talentFlag.equals("Y")) {
                                    Toast.makeText(mContext, "Teacher 재능을 등록해주세요.", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(mContext, "Student 재능을 등록해주세요.", Toast.LENGTH_SHORT).show();
                                }
                                Intent intent = new Intent(mContext, MainActivity_TalentAdd.class);
                                intent.putExtra("TalentFlag", talentFlag);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            mentorTalentList = new ArrayList<TalentObject_Home>();
                            menteeTalentList = new ArrayList<TalentObject_Home>();
                            for(int i = 0; i < talentArr.length(); i++){
                                JSONObject obj = talentArr.getJSONObject(i);
                                TalentObject_Home item = new TalentObject_Home(obj.getString("Name"), getResources().getIdentifier(obj.getString("BackgroundID"), "drawable", getPackageName()), getResources().getIdentifier(obj.getString("IconID"), "drawable", getPackageName()), 0, obj.getString("TalentID"));
                                item.setCateCode((int)obj.getLong("Code"));
                                item.setUserID(obj.getString("UserID"));
                                item.setTalentDescription(obj.getString("TalentDescription"));
                                item.setTalentFlag(obj.getString("TalentFlag"));

                                ((RelativeLayout)findViewById(R.id.rl_picarea_profile)).setVisibility(VISIBLE);
                                ((LinearLayout)findViewById(R.id.ll_introarea_profile)).setVisibility(VISIBLE);

                                // 최초 로딩시 첫 재능을 보여주기 위한 부분
                                if (i == 0) {
                                    Glide.with(mActivity).load(item.getBackgroundResourceID()).into((ImageView)findViewById(R.id.iv_talent_profile));

                                    findViewById(R.id.tv_tag_profile).setVisibility(VISIBLE);
                                    findViewById(R.id.tv_description_profile).setVisibility(VISIBLE);
                                    ((TextView)findViewById(R.id.tv_notalent1_profile)).setVisibility(GONE);
                                    ((TextView)findViewById(R.id.tv_notalent2_profile)).setVisibility(GONE);
                                    ((TextView)findViewById(R.id.tv_notalent3_profile)).setVisibility(GONE);
                                    ((ImageView)findViewById(R.id.iv_notalent_profile)).setVisibility(GONE);
                                    ((ImageView)findViewById(R.id.iv_addtalent_profile)).setVisibility(GONE);

                                    // 유저 재능내용을 가져오는 부분
                                    String hashTagString = "";
                                    String userText = item.getTalentDescription();
                                    userText = userText.replaceAll("#", " #");
                                    String[] tagParse = userText.split(" ");

                                    for (int j = 0; j < tagParse.length; j++) {
                                        String aTag = tagParse[j];
                                        if (aTag.startsWith("#")) {
                                            hashTagString += aTag + " ";
                                        }
                                    }

                                    userText.trim();
                                    hashTagString.trim();

                                    ((TextView) findViewById(R.id.tv_tag_profile)).setText(hashTagString);
                                    ((TextView) findViewById(R.id.tv_description_profile)).setText(userText);
                                }

                                if (listCateCode != null && listCateCode.equals(String.valueOf(item.getCateCode()))) {
                                    spinnerIdx = i;
                                }

                                // 모드별 유저의 재능 리스트 가져오기
                                if(talentFlag.equals("Y")) {
                                    mentorTalentList.add(item);
                                    adapter_talent = new SpinnerAdapter_Talent(mentorTalentList, mContext);
                                }else if(talentFlag.equals("N")) {
                                    menteeTalentList.add(item);
                                    adapter_talent = new SpinnerAdapter_Talent(menteeTalentList, mContext);
                                }

                                // 스피너에 추가

                                sp_talent_profile.setAdapter(adapter_talent);
                                sp_talent_profile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        if(talentFlag.equals("Y")) {
                                            Log.d("selected position", position + "");
                                            Glide.with(mActivity).load(mentorTalentList.get(position).getBackgroundResourceID()).into((ImageView)findViewById(R.id.iv_talent_profile));

                                            String hashTagString = "";
                                            String userText = mentorTalentList.get(position).getTalentDescription();
//                                            userText = userText.replaceAll("#", " #");
                                            String[] tagParse = userText.split(" ");

                                            for (int j=0;j<tagParse.length;j++) {
                                                String aTag = tagParse[j];
                                                if (aTag.startsWith("#")) {
                                                    hashTagString += aTag + " ";
                                                }
                                            }

                                            userText.trim();
                                            hashTagString.trim();

                                           ((TextView)findViewById(R.id.tv_tag_profile)).setText(hashTagString);
                                           ((TextView)findViewById(R.id.tv_description_profile)).setText(userText);

                                            cd_profile = new customDialog_Profile(mActivity, userText, mentorTalentList.get(position).getBackgroundResourceID(), mentorTalentList.get(position).getTalentFlag(), mentorTalentList.get(position).getCateCode());

                                        } else if(talentFlag.equals("N")) {
                                            Glide.with(mActivity).load(menteeTalentList.get(position).getBackgroundResourceID()).into((ImageView)findViewById(R.id.iv_talent_profile));

                                            String hashTagString = "";
                                            String userText = menteeTalentList.get(position).getTalentDescription();
//                                            userText = userText.replaceAll("#", " #");
                                            String[] tagParse = userText.split(" ");

                                            for (int j=0;j<tagParse.length;j++) {
                                                String aTag = tagParse[j];
                                                if (aTag.startsWith("#")) {
                                                    hashTagString += aTag + " ";
                                                }
                                            }

                                            userText.trim();
                                            hashTagString.trim();

                                            ((TextView)findViewById(R.id.tv_tag_profile)).setText(hashTagString);
                                            ((TextView)findViewById(R.id.tv_description_profile)).setText(userText);

                                            cd_profile = new customDialog_Profile(mActivity, userText, menteeTalentList.get(position).getBackgroundResourceID(), menteeTalentList.get(position).getTalentFlag(), menteeTalentList.get(position).getCateCode());

                                        }

                                        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
                                        double width = dm.widthPixels; //디바이스 화면 너비
                                        double height = dm.heightPixels; //디바이스 화면 높이

                                        WindowManager.LayoutParams wm = cd_profile.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
                                        wm.copyFrom(cd_profile.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
                                        wm.width = (int) (width / 1.2);  //
                                        wm.height = (int) (height / 1.2);  //

                                        // 재능 수정 버튼
                                        ((RelativeLayout)findViewById(R.id.rl_edittalent_profile)).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (!mActivity.isFinishing()) {
                                                    cd_profile.show();
                                                }
                                            }
                                        });

                                        // 재능 삭제 버튼
                                        if (talentFlag.equals("Y")) {
                                            final int nowTalentCode = mentorTalentList.get(position).getCateCode();
                                            ((RelativeLayout)findViewById(R.id.rl_deltalent_profile)).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    boolean checkCount = false;

                                                    if (0 == talentCnt - 1) {
                                                        Toast.makeText(mContext, "재능은 최소 한 가지가 있어야 합니다.", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        checkCount = true;
                                                        talentCnt = talentCnt - 1;
                                                    }

                                                    if (checkCount) {
                                                        disableUserTalent(String.valueOf(nowTalentCode));
                                                    }
                                                }
                                            });
                                        } else {
                                            final int nowTalentCode = menteeTalentList.get(position).getCateCode();
                                            ((RelativeLayout)findViewById(R.id.rl_deltalent_profile)).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    boolean checkCount = false;

                                                    if (0 == talentCnt - 1) {
                                                        Toast.makeText(mContext, "재능은 최소 한 가지가 있어야 합니다.", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        checkCount = true;
                                                        talentCnt = talentCnt - 1;
                                                    }

                                                    if (checkCount) {
                                                        disableUserTalent(String.valueOf(nowTalentCode));
                                                    }
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) { }
                                });

                                Log.d("checkidx", String.valueOf(spinnerIdx));
                                if (spinnerIdx > 0) {
                                    sp_talent_profile.setSelection(spinnerIdx);
                                }

                                friendFlag = obj.getInt("FriendFlag") > 0;
                                if (!inPerson) {
                                    if (friendFlag) {
                                        ((ImageView) findViewById(R.id.img_addfriend_toolbarprofile)).setVisibility(View.GONE);
                                        ((ImageView) findViewById(R.id.img_delfriend_toolbarprofile)).setVisibility(View.VISIBLE);
                                    } else {
                                        ((ImageView) findViewById(R.id.img_addfriend_toolbarprofile)).setVisibility(View.VISIBLE);
                                        ((ImageView) findViewById(R.id.img_delfriend_toolbarprofile)).setVisibility(View.GONE);
                                    }

                                    ((ImageView) findViewById(R.id.img_addfriend_toolbarprofile)).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            addFriend();
                                        }
                                    });
                                    ((ImageView) findViewById(R.id.img_delfriend_toolbarprofile)).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            delFriend();
                                        }
                                    });
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(this.getClass().getName(), "getAllTalent  [error]: " +error);
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String flag = "";
                if (talentFlag.equals("Y")) {
                    flag = "N";
                } else if (talentFlag.equals("N")) {
                    flag = "Y";
                }

                if (inPerson) {
                    flag = talentFlag;
                }
                params.put("UserID", userID);
                params.put("CheckUserID", SaveSharedPreference.getUserId(mContext));
                params.put("TalentFlag", flag);
                return params;
            }
        };

        // Add StringRequest to the RequestQueue
        requestQueue.add(stringRequest);
    }

    private void addNewTalent(String talentFlag, int backgroundID, int cateCode, String description) {
        Log.d("talentFlag:", talentFlag);
//        Log.d("backgroundID:", backgroundID);
//        Log.d("cateCode:", cateCode);
        Log.d("description:", description);

        cd_profile = new customDialog_Profile(mActivity, description, backgroundID, talentFlag, cateCode);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        double width = dm.widthPixels; //디바이스 화면 너비
        double height = dm.heightPixels; //디바이스 화면 높이

        WindowManager.LayoutParams wm = cd_profile.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
        wm.copyFrom(cd_profile.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = (int) (width / 1.2);  //
        wm.height = (int) (height / 1.2);  //
    }

    private void makeEmptyProfile(int bgID, String title) {
        Glide.with(mActivity).load(bgID).into((ImageView)findViewById(R.id.iv_talent_profile));

        TalentObject_Home obj = new TalentObject_Home();

        ((TextView)findViewById(R.id.tv_tag_profile)).setText("");
        ((TextView)findViewById(R.id.tv_description_profile)).setText("");
    }


    // 여기 아래부터는 지도 관련
    @Override
    public void onLocationChanged(Location location){
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

        Log.d("location changed", currentPosition.toString());

        mCurrentLocation = location;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> list = null;
        try{
            list = geocoder.getFromLocation(currentPosition.latitude, currentPosition.longitude, 1);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(list == null){
            Log.d("주소찾기", "실패");
        }else if(list.size() > 0){
            Address addr = list.get(0);

            Log.d("MyLocation", "location: " + mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude() + ", " + addr.getAddressLine(0) + "," + addr.toString());
            setCurrentLocation(mCurrentLocation,"현재위치" , addr.getAddressLine(0));

        }



    }

    @Override
    protected void onStart(){
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected() == false){
            mGoogleApiClient.connect();
        }

        super.onStart();
    }

    @Override
    protected void onStop(){
//        if(mGoogleApiClient.isConnected()){
//            mGoogleApiClient.disconnect();
//        }

        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if(hasFineLocationPermission == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2002);
            }
        }else{

            mLocationPermissionGranted = true;
        }
        if(mCurrentLocation == null)
            getCurrentLocation();
        updateLocationUI();
    }

    @Override
    public  void onConnectionFailed(ConnectionResult connectionResult){
        Log.d("failed connection", "failed");
    }

    @Override
    public void onConnectionSuspended(int cause){
        if(cause == CAUSE_NETWORK_LOST)
            Log.e("suspended", "connection lost");
        else if (cause == CAUSE_SERVICE_DISCONNECTED)
            Log.e("suspended", "service disconnected");
    }


    @Override
    public void onMapReady(final GoogleMap map){
        gMap = map;

        //if(!inPerson) return;

        gMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                getCurrentLocation();
                return true;
            }
        });
        gMap.setOnMapClickListener(this);
        Log.d("isHavingData", isHavingData+"");
        if(isHavingData){

            GeoPoint geoPoint = data.getArrGeoPoint();
            Log.d("having data", "data = " + geoPoint.getLng() + ", " + geoPoint.getLat());

            if (geoPoint != null && geoPoint.getLng() != 0 && location != null) {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                mCurrentLocation = new Location("");
                mCurrentLocation.setLatitude(geoPoint.getLat());
                mCurrentLocation.setLongitude(geoPoint.getLng());

                List<Address> list = null;
                try{
                    list = geocoder.getFromLocation(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude() , 1);
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(list == null){
                    Log.d("주소찾기", "실패");
                }else if(list.size() > 0){
                    Address addr = list.get(0);

                    Log.d("MyLocation", "location: " + mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude() + ", " + addr.getAddressLine(0) + "," + addr.toString());
                    setCurrentLocation(mCurrentLocation,"기존위치" , addr.getAddressLine(0));
                }

            }
        }else{
            mCurrentLocation = new Location("");
            mCurrentLocation.setLatitude(37.567759717923146);
            mCurrentLocation.setLongitude(126.98008608072996);
            LatLng latlng = new LatLng(37.567759717923146,126.98008608072996 );
            gMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            gMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        Point screenPt = gMap.getProjection().toScreenLocation(point);

        LatLng latLng = gMap.getProjection().fromScreenLocation(screenPt);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        mCurrentLocation = new Location("");
        mCurrentLocation.setLatitude(point.latitude);
        mCurrentLocation.setLongitude(point.longitude);
        List<Address> list = null;
        try{
            list = geocoder.getFromLocation(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude() , 1);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(list == null){
            Log.d("주소찾기", "실패");
        }else if(list.size() > 0){
            Address addr = list.get(0);

            Log.d("MyLocation", "location: " + mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude() + ", " + addr.getAddressLine(0) + "," + addr.toString());
            setCurrentLocation(mCurrentLocation,"선택위치" , addr.getAddressLine(0));

        }
    }





    private void setCurrentLocation(Location location, String name, final String addr){
        gMap.clear();

        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlng);
        markerOptions.title(name);
        markerOptions.snippet(addr);
        Marker marker = gMap.addMarker(markerOptions);
        if (inPerson){
            gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    final AlertDialog.Builder ProgressorCancelPopup = new AlertDialog.Builder(MainActivity_Profile.this);
                    Log.d("geo icon_point", mCurrentLocation.getLatitude() +", "+ mCurrentLocation.getLongitude());

                    ProgressorCancelPopup.setMessage("선택한 위치가 \"" + marker.getSnippet() + "\"가 맞습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = ProgressorCancelPopup.create();
                    alertDialog.show();
                }
            });
        }
        marker.showInfoWindow();
        Log.d("Current Loc", location.getLatitude()+", "+location.getLongitude());

        gMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }




    private void updateLocationUI(){
        try {
            Log.d("UpdateLocationUI", "gMap is null: " + (gMap == null) + ", permission = " + mLocationPermissionGranted);
            if (gMap == null) return;
            if (mLocationPermissionGranted) {
                gMap.setMyLocationEnabled(true);
                gMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                gMap.setMyLocationEnabled(false);
                gMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        mLocationPermissionGranted = false;
        if(requestCode == 2002){
            if(PermissionUtil.verifyPermission(grantResults)){
                mLocationPermissionGranted = true;
            }
        }
        updateLocationUI();
    }


    private void getCurrentLocation() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            updateLocationUI();
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    // 프로필 사진 관련
    public void saveMyProfile(){
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Profile/saveMyProfileInfo.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.d("result = ", obj.toString());
                    if(obj.getString("result").equals("success")){
                        Toast.makeText(mContext, "프로필 저장이 완료되었습니다.", Toast.LENGTH_SHORT).show();
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

                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }

    private void selectPhoto(){
        String state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(state)){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {

                    File photoFile = null;
                    try{
                        photoFile = createImageFile();
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                    if(photoFile != null){
                        photoUri = FileProvider.getUriForFile(mContext, AUTHORITY, photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, CAMERA_CODE);
                    }
            }
        }
    }

    private File createImageFile() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory() + "/path/");
        if(!dir.exists()){
            dir.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageCaptureName = timeStamp + ".png";

        File storageDir = new File(Environment.getExternalStorageDirectory() + "/path/" + mImageCaptureName);
        currentPhotoPath = storageDir.getAbsolutePath();

        return storageDir;
    }

    private void selectGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Log.d("requestCode", String.valueOf(requestCode));
            switch(requestCode){
                case GALLERY_CODE:
                    sendPicture(data.getData());
                    break;
                case CAMERA_CODE:
                    getPictureForPhoto();
                    break;
                case 2030:
                    final Geocoder geocoder = new Geocoder(mContext);
                    try {
                        List<Address> list = geocoder.getFromLocation(Double.parseDouble(SaveSharedPreference.getPrefUserGpLat(mContext)),Double.parseDouble(SaveSharedPreference.getPrefUserGpLng(mContext)),10);
                        if (list.size()==0) {
                            ((TextView)findViewById(R.id.tv_addr_profile)).setText("해당되는 주소 정보는 없습니다");
                        } else {
                            String[] addr = list.get(0).getAddressLine(0).split(" ");
                            ((TextView)findViewById(R.id.tv_addr_profile)).setBackgroundResource(0);
                            ((TextView)findViewById(R.id.tv_addr_profile)).setText(addr[1]+" "+addr[2]+" "+addr[3]);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("입출력오류", "입출력 오류 - 서버에서 주소변환시 에러발생");
                    }
                    break;
                default:
                    break;
            }
        }
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
            iv_cimg_pic_profile.setImageBitmap(bitmap);

            uploadBitmap(bitmap);
        }catch (Exception e){
            e.printStackTrace();//bitmap = rotate(bitmap, exifDegree);
        }

    }

    private void getPictureForPhoto(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, options);

        options.inSampleSize = setSimpleSize(options, 960, 720);

        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, options);

        ExifInterface exif = null;
        try{
            exif = new ExifInterface(currentPhotoPath);
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

        bitmap = rotate(bitmap, exifDegree);

        iv_cimg_pic_profile.setImageBitmap(bitmap);
        uploadBitmap(bitmap);
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


    public byte[] getFileDataFromDrawable(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap){
        final String tags = "UserProfilePicture";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Profile/savePicture.do", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    JSONObject obj = new JSONObject(new String(response.data));
                    SaveSharedPreference.setMyPicturePath(obj.getString("FILE_PATH"), obj.getString("FILE_PATH"));

//                    Glide.with(mContext).load(SaveSharedPreference.getImageUri() + SaveSharedPreference.getMyThumbPicturePath()).into((ImageView) findViewById(R.id.cimg_pic_dl));
                    Glide.with(mContext).load(SaveSharedPreference.getImageUri() + SaveSharedPreference.getMyThumbPicturePath()).into((ImageView) findViewById(R.id.cimg_pic_profile));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tags", tags);
                params.put("userID", SaveSharedPreference.getUserId(mContext));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData(){
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new VolleyMultipartRequest.DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        try {
            for(Map.Entry<String, String> elem : volleyMultipartRequest.getHeaders().entrySet()){
                Log.d("header = " , String.format("키 : %s, 값 : %s", elem.getKey(), elem.getValue()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        VolleySingleton.getInstance(mContext).getRequestQueue().add(volleyMultipartRequest);

    }

    public int getStatusBarHeight(){
        int statusBarHeight = 0;
        int screenSizeType = ((mContext).getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);
        if(screenSizeType != Configuration.SCREENLAYOUT_SIZE_XLARGE){
            int resourceId = mContext.getResources().getIdentifier("status_bar_height","dimen","android");
            if(resourceId>0)
            {
                statusBarHeight = mContext.getResources().getDimensionPixelOffset(resourceId);
            }
        }
        return statusBarHeight;
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

    public void addFriend(){
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "FriendList/updateFriendList_new.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("result").equals("success")){
                        ((ImageView) findViewById(R.id.img_addfriend_toolbarprofile)).setVisibility(View.GONE);
                        ((ImageView) findViewById(R.id.img_delfriend_toolbarprofile)).setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, "친구 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
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
                params.put("friendID", targetUserID);
                params.put("updateFlag", "I");
                String flag = "Y";
                if (mode.equals("Y")) {
                    flag = "N";
                }
                params.put("talentFlag", mode);
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }

    public void delFriend(){
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "FriendList/updateFriendList_new.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("result").equals("success")){
                        ((ImageView) findViewById(R.id.img_addfriend_toolbarprofile)).setVisibility(View.VISIBLE);
                        ((ImageView) findViewById(R.id.img_delfriend_toolbarprofile)).setVisibility(View.GONE);
                        Toast.makeText(mContext, "친구 삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
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
                params.put("friendID", targetUserID);
                params.put("updateFlag", "D");

                params.put("talentFlag", mode);
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }

    public void updateMyProfileInfo(){
        String text = ((TextView) findViewById(R.id.tv_profile_description)).getText().toString();

        cd_Description = new customDialog_Description(MainActivity_Profile.this, text, true);
        cd_Description.makeLayout();
        cd_Description.show();
    }

    public void showProfileInfo(){
        String text = ((TextView) findViewById(R.id.tv_profile_description)).getText().toString();

        cd_Description = new customDialog_Description(MainActivity_Profile.this, text, false);
        cd_Description.setFriendFlag(fromFriend);
        cd_Description.setFriendTelantKind(mode);
        cd_Description.makeLayout();
        cd_Description.show();
    }

    public void updateMyLocation(final String lat, final String lng){
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Profile/updateMyLocation.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("result").equals("success")){
                        Toast.makeText(mContext, "선택 지역으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
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
                params.put("GP_LAT", lat);
                params.put("GP_LNG", lng);
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }

    public void disableUserTalent(final String Code){

        AlertDialog.Builder dialog = new AlertDialog.Builder((MainActivity_Profile.this));
        dialog.setTitle("해당 재능 삭제")
                .setMessage("삭제하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
                        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Hashtag/deleteTalent.do", new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response){
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if(obj.getString("result").equals("success")){
                                        Toast.makeText(mContext, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                        (MainActivity_Profile.this).finish();
                                        Intent intent = new Intent(mContext, MainActivity_TalentAdd.class);
                                        startActivity(intent);
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
                                params.put("UserID", SaveSharedPreference.getUserId(mContext));
                                params.put("TalentFlag", SaveSharedPreference.getPrefTalentFlag(mContext));
                                params.put("TalentCateCode", Code);
                                return params;
                            }
                        };

                        postRequestQueue.add(postJsonRequest);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
        dialog.create();
        dialog.show();
    }

    public void saveMyProfileInfo(final String birth, final int mentIdx){
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Profile/saveMyProfileInfo.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("result").equals("success")){
                        switch (mentIdx) {
                            case 1:
                                Toast.makeText(mContext, "생일을 공개로 설정했습니다.", Toast.LENGTH_SHORT).show();
                                ((TextView) findViewById(R.id.tv_birth_profile)).setText(SaveSharedPreference.getPrefUserBirth(mContext));
                                break;
                            case 2:
                                Toast.makeText(mContext, "생일을 비공개로 설정했습니다.", Toast.LENGTH_SHORT).show();
                                ((TextView) findViewById(R.id.tv_birth_profile)).setText("비공개");
                                break;
                        }
                        // 변경값으로 저장
                        SaveSharedPreference.setPrefUserBirthFlag(mContext, birth);

                        String addrFlag = SaveSharedPreference.getPrefUserAddrFlag(mContext);
                        String birthFlag = SaveSharedPreference.getPrefUserBirthFlag(mContext);

                        changeOpenFlagImage();
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
                params.put("birthFlag", birth);
                params.put("addrFlag", "");
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }

    public void changeOpenFlagImage() {
        // 공개 또는 비공개 기능
        final String birthFlag = SaveSharedPreference.getPrefUserBirthFlag(mContext);

        if (birthFlag.equals("Y")) {
            ((ImageView) findViewById(R.id.iv_birthopen_profile)).setVisibility(VISIBLE);
            ((ImageView) findViewById(R.id.iv_birthclose_profile)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tv_birth_profile)).setText("비공개");
            ((ImageView) findViewById(R.id.iv_birthopen_profile)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveMyProfileInfo("N",1);
                }
            });
        } else if (birthFlag.equals("N")) {
            ((ImageView) findViewById(R.id.iv_birthopen_profile)).setVisibility(View.GONE);
            ((ImageView) findViewById(R.id.iv_birthclose_profile)).setVisibility(VISIBLE);

            ((ImageView) findViewById(R.id.iv_birthclose_profile)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveMyProfileInfo("Y",2);
                }
            });
        }
    }

    public void checkUserOpenData(final  String birthFlag) {
        if (birthFlag.equals("Y")) {
            ((TextView)findViewById(R.id.tv_birth_profile)).setText("비공개");
        }
    }

    public void wasItShared(final String targetID){
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Profile/wasItShared.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("isTrue").equals("1")){
                        ((ImageView)findViewById(R.id.iv_share_profile)).setVisibility(VISIBLE);
                    }else {
                        ((ImageView)findViewById(R.id.iv_share_profile)).setVisibility(View.GONE);
                    }

                    if (SaveSharedPreference.getPrefTalentFlag(mContext).equals("N") && (!mode.equals("Y"))) {
                        ((ImageView)findViewById(R.id.iv_share_profile)).setVisibility(VISIBLE);
                    }

                    // 친구리스트의 경우
                    if(fromFriend){
                        if (mode.equals("N")) {
                            ((ImageView)findViewById(R.id.iv_share_profile)).setVisibility(VISIBLE);
                        }
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
                if (SaveSharedPreference.getPrefTalentFlag(mContext).equals("Y")) {
                    params.put("MentorID", SaveSharedPreference.getUserId(mContext));
                    params.put("MenteeID", targetID);
                } else {
                    params.put("MentorID", targetID);
                    params.put("MenteeID", SaveSharedPreference.getUserId(mContext));
                }
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }

    public void getTalentCount(){
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Profile/getTalentCount.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONArray objArr = new JSONArray(response);
                    for (int i = 0; i < objArr.length(); i++){
                        JSONObject obj = objArr.getJSONObject(i);
                        String talentFlag = obj.getString("TalentFlag");

                        talentCnt = obj.getInt("TalentCount");
                        Log.d("TalentRegistCount", talentFlag + " : " + talentCnt);
                        // Y인 경우 Teacher
                        if(talentFlag.equals("Y")){
                            ((TextView)findViewById(R.id.tv_isteacher_profile)).setText("Student의 프로필");
                            // Teacher 등록한게 없는 경우
                            if (isNewTalent){
                                ((RelativeLayout)findViewById(R.id.rl_toolbar_profile)).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
                                ((TextView)findViewById(R.id.tv_toolbarprofle)).setVisibility(VISIBLE);
                                ((TextView)findViewById(R.id.tv_toolbarprofle)).setText(title);
                                ((Spinner)findViewById(R.id.sp_talent_profile)).setVisibility(GONE);

                                ((RelativeLayout)findViewById(R.id.rl_picarea_profile)).setVisibility(GONE);
                                ((LinearLayout)findViewById(R.id.ll_introarea_profile)).setVisibility(GONE);

                                ((LinearLayout)findViewById(R.id.ll_notalent_profile)).setVisibility(VISIBLE);
                                ((TextView)findViewById(R.id.tv_notalent1_profile)).setVisibility(VISIBLE);
                                ((TextView)findViewById(R.id.tv_notalent2_profile)).setVisibility(VISIBLE);
                                ((TextView)findViewById(R.id.tv_notalent3_profile)).setVisibility(VISIBLE);
                                ((TextView)findViewById(R.id.tv_notalent1_profile)).setText("당신의 재능");
                                ((TextView)findViewById(R.id.tv_notalent3_profile)).setText("나누어주세요.");

                                ((ImageView)findViewById(R.id.iv_notalent_profile)).setVisibility(VISIBLE);
                                ((ImageView)findViewById(R.id.iv_notalent_profile)).setImageResource(R.drawable.pic_notalent_teacher);
                                ((ImageView)findViewById(R.id.iv_addtalent_profile)).setVisibility(VISIBLE);
                                changeOpenFlagImage();
                            }
                            else
                            {
                                ((RelativeLayout)findViewById(R.id.rl_toolbar_profile)).setBackgroundColor(Color.parseColor("#64000000"));
                                ((TextView)findViewById(R.id.tv_toolbarprofle)).setVisibility(GONE);
                                ((Spinner)findViewById(R.id.sp_talent_profile)).setVisibility(VISIBLE);

                                ((RelativeLayout)findViewById(R.id.rl_picarea_profile)).setVisibility(VISIBLE);
                                ((LinearLayout)findViewById(R.id.ll_introarea_profile)).setVisibility(VISIBLE);
                                ((LinearLayout)findViewById(R.id.ll_notalent_profile)).setVisibility(GONE);
                                ((TextView)findViewById(R.id.tv_notalent1_profile)).setVisibility(GONE);
                                ((TextView)findViewById(R.id.tv_notalent2_profile)).setVisibility(GONE);
                                ((TextView)findViewById(R.id.tv_notalent3_profile)).setVisibility(GONE);
                                ((ImageView)findViewById(R.id.iv_notalent_profile)).setVisibility(GONE);
                                ((ImageView)findViewById(R.id.iv_addtalent_profile)).setVisibility(GONE);
                            }
                        }
                        else{

                            ((TextView)findViewById(R.id.tv_isteacher_profile)).setText("Teacher의 프로필");
                            // Student 등록한게 없는 경우
                            if (isNewTalent){
                                ((RelativeLayout)findViewById(R.id.rl_toolbar_profile)).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
                                ((RelativeLayout)findViewById(R.id.rl_picarea_profile)).setVisibility(GONE);
                                ((LinearLayout)findViewById(R.id.ll_introarea_profile)).setVisibility(GONE);

                                ((TextView)findViewById(R.id.tv_toolbarprofle)).setVisibility(VISIBLE);
                                ((TextView)findViewById(R.id.tv_toolbarprofle)).setText(title);
                                ((Spinner)findViewById(R.id.sp_talent_profile)).setVisibility(GONE);

                                ((LinearLayout)findViewById(R.id.ll_notalent_profile)).setVisibility(VISIBLE);
                                ((TextView)findViewById(R.id.tv_notalent1_profile)).setVisibility(VISIBLE);
                                ((TextView)findViewById(R.id.tv_notalent2_profile)).setVisibility(VISIBLE);
                                ((TextView)findViewById(R.id.tv_notalent3_profile)).setVisibility(VISIBLE);

                                ((ImageView)findViewById(R.id.iv_notalent_profile)).setVisibility(VISIBLE);
                                ((TextView)findViewById(R.id.tv_notalent1_profile)).setText("당신의 배움");
                                ((TextView)findViewById(R.id.tv_notalent3_profile)).setText("응원합니다.");
                                ((ImageView)findViewById(R.id.iv_notalent_profile)).setImageResource(R.drawable.pic_notalent_student);
                                ((ImageView)findViewById(R.id.iv_addtalent_profile)).setVisibility(VISIBLE);
                            }else
                            {
                                ((RelativeLayout)findViewById(R.id.rl_toolbar_profile)).setBackgroundColor(Color.parseColor("#64000000"));
                                ((TextView)findViewById(R.id.tv_toolbarprofle)).setVisibility(GONE);
                                ((Spinner)findViewById(R.id.sp_talent_profile)).setVisibility(VISIBLE);

                                ((RelativeLayout)findViewById(R.id.rl_picarea_profile)).setVisibility(VISIBLE);
                                ((LinearLayout)findViewById(R.id.ll_introarea_profile)).setVisibility(VISIBLE);
                                ((LinearLayout)findViewById(R.id.ll_notalent_profile)).setVisibility(GONE);
                                ((TextView)findViewById(R.id.tv_notalent1_profile)).setVisibility(GONE);
                                ((TextView)findViewById(R.id.tv_notalent2_profile)).setVisibility(GONE);
                                ((TextView)findViewById(R.id.tv_notalent3_profile)).setVisibility(GONE);
                                ((ImageView)findViewById(R.id.iv_notalent_profile)).setVisibility(GONE);
                                ((ImageView)findViewById(R.id.iv_addtalent_profile)).setVisibility(GONE);
                            }
                        }
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
                if(fromFriend){
                    params.put("talentFlag", mode);
                }else {
                    params.put("talentFlag", SaveSharedPreference.getPrefTalentFlag(mContext));
                }
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }

    private void getMyProfileInfo_new() {
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "/Profile/getMyProfileInfo_new.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject obj = new JSONObject(response);
                    SaveSharedPreference.setPrefGender(mContext, obj.getString("GENDER"));
                    SaveSharedPreference.setPrefUserBirth(mContext, obj.getString("USER_BIRTH"));
                    if(obj.has("PROFILE_DESCRIPTION")) {
                        SaveSharedPreference.setPrefUserDescription(mContext, obj.getString("PROFILE_DESCRIPTION"));
                    }
                    SaveSharedPreference.setPrefTalentPoint(mContext, obj.getInt("TALENT_POINT"));
                    SaveSharedPreference.setMyPicturePath(obj.getString("FILE_PATH"), obj.getString("S_FILE_PATH"));
                    SaveSharedPreference.setPrefUserBirthFlag(mContext, obj.getString("BIRTH_FLAG"));

                    if (obj.has("GP_LNG") && obj.has("GP_LAT")) {
                        SaveSharedPreference.setPrefUserGpLng(mContext, obj.getString("GP_LNG"));
                        SaveSharedPreference.setPrefUserGpLat(mContext, obj.getString("GP_LAT"));
                    }

                    if (obj.has("SumScore") && obj.has("CountScore")) {
                        int sumScore = Integer.parseInt((String) obj.getString("SumScore"));
                        int countScore = Integer.parseInt((String) obj.getString("CountScore"));

                        int avgScore =  (sumScore + 9) / (countScore + 1);

                        SaveSharedPreference.setPrefUserScore(mContext, String.valueOf(avgScore));
                    } else {
                        SaveSharedPreference.setPrefUserScore(mContext, "");
                    }


                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, SaveSharedPreference.getErrorListener(mContext)) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap();
                params.put("userID", SaveSharedPreference.getUserId(mContext));
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }

}
