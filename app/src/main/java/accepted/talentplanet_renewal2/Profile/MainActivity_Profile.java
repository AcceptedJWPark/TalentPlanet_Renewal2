package accepted.talentplanet_renewal2.Profile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
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
import org.w3c.dom.Text;

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

import accepted.talentplanet_renewal2.BuildConfig;
import accepted.talentplanet_renewal2.Classes.TalentObject_Home;
import accepted.talentplanet_renewal2.GeoPoint;
import accepted.talentplanet_renewal2.Home.MainActivity;
import accepted.talentplanet_renewal2.MyTalent;
import accepted.talentplanet_renewal2.PermissionUtil;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.VolleyMultipartRequest;
import accepted.talentplanet_renewal2.VolleySingleton;


import static android.graphics.Color.WHITE;


public class MainActivity_Profile extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        GoogleMap.OnMapClickListener{

    TextView tv_toolbar;
    talentlist_viewpager vp;
    Context mContext;
    ViewPager.OnPageChangeListener onPageChangeListener;
    ImageView iv_mentor_plus;
    ImageView iv_mentee_plus;

    ListView lv_point;
    RecyclerView lv_mentor_profile;
    RecyclerView lv_mentee_profile;
    private ArrayList<ItemData_Profile> userDate = new ArrayList<>();

    private ArrayList<TalentObject_Home> mentorTalentList;
    private ArrayList<TalentObject_Home> menteeTalentList;

    ListAdapter_Point adapter;
    ListAdapter_Talent mentorAdapter;
    ListAdapter_Talent menteeAdapter;

    RecyclerView.LayoutManager mLayoutManagerMentor;
    RecyclerView.LayoutManager mLayoutManagerMentee;

    private String targetUserID;
//    private TextView tv_profile_mentor_count, tv_profile_mentee_count;
    private TextView tv_profile_description;
    private TextView tv_birth_profile;
    private TextView tv_addr_profile;
//    private TextView tv_profile_point;

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

    // 프로필 사진 관련 변수
    private ImageView iv_cimg_pic_profile;
    private Uri photoUri;
    private String currentPhotoPath;
    private String mImageCaptureName;
    private boolean inPerson;
    private final int CAMERA_CODE = 1111;
    private final int GALLERY_CODE = 1112;
    private String mode;

    // 리스트 뷰를 초기화 하기 위한 변수
    private String listReset;

    private String userID;

    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider";

    // 친구 관련 변수
    private boolean friendFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mContext = getApplicationContext();

//        tv_profile_mentor_count = findViewById(R.id.tv_profile_mentor_count);
//        tv_profile_mentee_count = findViewById(R.id.tv_profile_mentee_count);

        img_gender_profile = findViewById(R.id.img_gender_profile);

        tv_profile_description = findViewById(R.id.tv_profile_description);
//        tv_birth_profile = findViewById(R.id.tv_birth_profile);
        tv_addr_profile = findViewById(R.id.tv_addr_profile);
//        tv_profile_point = findViewById(R.id.tv_profile_point);

        img_gender_profile = findViewById(R.id.img_gender_profile);

        mentorTalentList = new ArrayList<>();
        menteeTalentList = new ArrayList<>();

        tv_toolbar = findViewById(R.id.tv_toolbar);
        tv_toolbar.setText("Profile");
        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);
        findViewById(R.id.img_show1x15).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        // 받은 값 구현
        Intent intent = new Intent(this.getIntent());
        inPerson = intent.getBooleanExtra("inPerson", false);
        if (intent.getStringExtra("userName") != null) {
            ((TextView)findViewById(R.id.tv_name_profile)).setText(intent.getStringExtra("userName") + " / " + intent.getStringExtra("userInfo"));
//            ((TextView)findViewById(R.id.tv_birth_profile)).setText(intent.getStringExtra("userInfo"));
            targetUserID = intent.getStringExtra("targetUserID");
        }

        if (inPerson) {
            // 유저가 로그인했을 경우
            String userName = SaveSharedPreference.getUserName(mContext);
            String userInfo = SaveSharedPreference.getPrefUserBirth(mContext);
            userID = SaveSharedPreference.getUserId(mContext);

            ((TextView)findViewById(R.id.tv_name_profile)).setText(userName + " / " + userInfo);
            // 본인의 아이디에서 숨겨야할 요소
            ((Button)findViewById(R.id.btn_profile_btn)).setVisibility(View.GONE);
//            tv_profile_point.setText(String.valueOf(SaveSharedPreference.getTalentPoint(mContext)));
            //((LinearLayout)findViewById(R.id.ll_pointbox_profile)).setVisibility(View.GONE);
            ((Button)findViewById(R.id.btn_sendMessage_profile)).setVisibility(View.GONE);
        } else  {
//            ((LinearLayout)findViewById(R.id.ll_pointbox_profile)).setVisibility(View.GONE);
            userID = intent.getStringExtra("userID");
        }

        Point pt = new Point();
        getWindowManager().getDefaultDisplay().getSize(pt);
        ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getSize(pt);
        int height = pt.y;

        View trash;
        lv_point = findViewById(R.id.lv_point_profile);

        lv_mentor_profile = findViewById(R.id.lv_mentor_profile);
//        lv_mentee_profile = findViewById(R.id.lv_mentee_profile);
        
        mLayoutManagerMentor = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mLayoutManagerMentee = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        lv_mentor_profile.setLayoutManager(mLayoutManagerMentor);
//        lv_mentee_profile.setLayoutManager(mLayoutManagerMentee);


        // 추후 mode 값 변경 작업 필요
        mode = "Y";
        if (mode.equals("Y")) {
            getAllTalent("Y");
        } else {
            getAllTalent("N");
        }



        adapter = new ListAdapter_Point(userDate);
        lv_point.setAdapter(adapter);

        // 뒤로가기 이벤트
        ((ImageView) findViewById(R.id.img_open_dl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 지도
        mActivity = this;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.frg_Map_TalentRegister);
        mapFragment.getMapAsync(this);

        createLocationRequest();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.frg_AutoComplete_TalentRegister);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d("Place : ", place.getLatLng().latitude + ", " + place.getLatLng().longitude);
                Location location = new Location("");
                location.setLatitude(place.getLatLng().latitude);
                location.setLongitude(place.getLatLng().longitude);

                mCurrentLocation = location;

                setCurrentLocation(location, place.getName().toString(), place.getAddress().toString());
            }

            @Override
            public void onError(Status status) {
                Log.d("Error : ", String.valueOf(status));
            }
        });

        if(!inPerson){
               autocompleteFragment.getView().setVisibility(View.GONE);
               mapFragment.getView().setOnClickListener(null);
        }

        // 프로필 사진 관련
        final android.support.v7.app.AlertDialog.Builder AlarmDeleteDialog = new android.support.v7.app.AlertDialog.Builder(MainActivity_Profile.this);

        iv_cimg_pic_profile = findViewById(R.id.cimg_pic_profile);
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

        if (inPerson) {
            tv_profile_description.setText(SaveSharedPreference.getPrefUserDescription(mContext));
            tv_profile_description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity_Profile.this);
                    ad.setTitle("자기소개 변경");
                    ad.setMessage("변경할 내용을 입력해 주세요.");

                    final EditText et = new EditText(MainActivity_Profile.this);

                    et.setText(SaveSharedPreference.getPrefUserDescription(mContext));
                    ad.setView(et);

                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String txt = et.getText().toString();
                            updateMyProfileInfo(txt);
                            tv_profile_description.setText(txt);
                            SaveSharedPreference.setPrefUserDescription(mContext, txt);
                        }
                    });

                    ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    ad.show();
                }
            });



//            tv_birth_profile.setText(SaveSharedPreference.getPrefUserBirth(mContext));

            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

            mCurrentLocation = new Location("");
            mCurrentLocation.setLatitude(Double.parseDouble(SaveSharedPreference.getPrefUserGpLat(mContext)));
            mCurrentLocation.setLongitude(Double.parseDouble(SaveSharedPreference.getPrefUserGpLng(mContext)));
            Glide.with(mContext).load(SaveSharedPreference.getImageUri() + SaveSharedPreference.getMyThumbPicturePath()).into((ImageView) findViewById(R.id.cimg_pic_profile));
        }
        getProfileData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    private void getProfileData(){
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SaveSharedPreference.getServerIp() + "Profile/getMyProfileInfo_new.do",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject profileData = new JSONObject(response);
                            tv_profile_description.setText(SaveSharedPreference.getPrefUserDescription(mContext));

//                            tv_birth_profile.setText(SaveSharedPreference.getPrefUserBirth(mContext));

                            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

                            mCurrentLocation = new Location("");
                            mCurrentLocation.setLatitude(Double.parseDouble(SaveSharedPreference.getPrefUserGpLat(mContext)));
                            mCurrentLocation.setLongitude(Double.parseDouble(SaveSharedPreference.getPrefUserGpLng(mContext)));

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
                                tv_addr_profile.setText(addr.getAddressLine(0));

                                Log.d("MyLocation", "location: " + mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude() + ", " + addr.getAddressLine(0) + "," + addr.toString());
                                setCurrentLocation(mCurrentLocation,"기존위치" , addr.getAddressLine(0));
                            }

                            ((Button)findViewById(R.id.btn_sendMessage_profile)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int roomID = 0;
                                    try {
                                        roomID = SaveSharedPreference.makeChatRoom(mContext, targetUserID, profileData.getString("USER_NAME"), profileData.getString("S_FILE_PATH"));
                                        if (roomID < 0) {
                                            return;
                                        }
                                        Intent i = new Intent(mContext, accepted.talentplanet_renewal2.Messanger.Chatting.MainActivity.class);
                                        i.putExtra("userID", targetUserID);
                                        i.putExtra("roomID", roomID);
                                        i.putExtra("userName", profileData.getString("USER_NAME"));
                                        startActivity(i);

                                        finish();
                                    }catch(JSONException e){
                                        e.printStackTrace();
                                    }

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String gender = SaveSharedPreference.getPrefUserGender(mContext);
                        if (gender.equals("남")) {
                            img_gender_profile.setImageDrawable(getResources().getDrawable(R.drawable.icon_male));
                        } else {
                            img_gender_profile.setImageDrawable(getResources().getDrawable(R.drawable.icon_female));
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
                params.put("userID", targetUserID);
                return params;
            }
        };

        // Add StringRequest to the RequestQueue
        requestQueue.add(stringRequest);
    }

    private void getAllTalent(final String talentFlag) {
        final RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                SaveSharedPreference.getServerIp() + "Profile/getAllMyTalent.do",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray talentArr = new JSONArray(response);

                            mentorTalentList = new ArrayList<TalentObject_Home>();
                            menteeTalentList = new ArrayList<TalentObject_Home>();
                            for(int i = 0; i < talentArr.length(); i++){
                                JSONObject obj = talentArr.getJSONObject(i);
                                TalentObject_Home item = new TalentObject_Home(obj.getString("Name"), getResources().getIdentifier(obj.getString("BackgroundID"), "drawable", getPackageName()), getResources().getIdentifier(obj.getString("IconID"), "drawable", getPackageName()), 0, obj.getString("TalentID"));
                                item.setCateCode((int)obj.getLong("Code"));
                                item.setUserID(obj.getString("UserID"));
                                item.setTalentDescription(obj.getString("TalentDescription"));

                                // 최초 로딩시 첫 재능을 보여주기 위한 부분
                                if (i == 0) {
                                    Glide.with(mActivity).load(item.getBackgroundResourceID()).into((ImageView)findViewById(R.id.iv_talent_profile));

                                    findViewById(R.id.tv_tag_profile).setVisibility(View.VISIBLE);
                                    findViewById(R.id.tv_description_profile).setVisibility(View.VISIBLE);

                                    // 유저 재능내용을 가져오는 부분
                                    String hashTagString = "";
                                    String userText = item.getTalentDescription();
                                    userText = userText.replaceAll("#", " #");
                                    String[] tagParse = userText.split(" ");

                                    for (int j=0;j<tagParse.length;j++) {
                                        String aTag = tagParse[j];
                                        if (aTag.startsWith("#")) {
                                            hashTagString += aTag + " ";
                                        }
                                    }

                                    hashTagString.trim();

                                    ((TextView)findViewById(R.id.tv_description_profile)).setText(userText);
                                    ((TextView)findViewById(R.id.tv_tag_profile)).setText(hashTagString);
                                }

                                if(talentFlag.equals("Y")) {

                                    mentorTalentList.add(item);
                                }else if(talentFlag.equals("N")) {
                                    menteeTalentList.add(item);
                                }

                                friendFlag = obj.getInt("FriendFlag") > 0;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(talentFlag.equals("Y")){
                            if (inPerson) {
                                mentorTalentList.add(new TalentObject_Home("추가", R.drawable.icon_profile_plus, 0, 0, ""));
//                                tv_profile_mentor_count.setText((mentorTalentList.size()-1) + "");
//                                ((TextView) findViewById(R.id.tv_mentor_count_profile)).setText(SaveSharedPreference.getUserName(mContext) + "님의 관심멘토는" + String.valueOf(mentorTalentList.size()-1) + "건 입니다.");
                            } else {
//                                tv_profile_mentor_count.setText(mentorTalentList.size() + "");
//                                ((TextView) findViewById(R.id.tv_mentor_count_profile)).setText(SaveSharedPreference.getUserName(mContext) + "님의 관심멘토는" + String.valueOf(mentorTalentList.size()) + "건 입니다.");
                            }
                            mentorAdapter = new ListAdapter_Talent(mActivity , mentorTalentList, "MENTOR", inPerson);
                            lv_mentor_profile.setAdapter(mentorAdapter);

                        }else if(talentFlag.equals("N")){
                            if (inPerson) {
                                menteeTalentList.add(new TalentObject_Home("추가", R.drawable.icon_profile_plus, 0, 0, ""));
//                                tv_profile_mentee_count.setText((menteeTalentList.size()-1) + "");
//                                ((TextView) findViewById(R.id.tv_mentee_count_profile)).setText(SaveSharedPreference.getUserName(mContext) + "님의 관심멘티는" +String.valueOf(menteeTalentList.size()-1)+ "건 입니다.");
                            } else {
//                                tv_profile_mentee_count.setText(menteeTalentList.size() + "");
//                                ((TextView) findViewById(R.id.tv_mentee_count_profile)).setText(SaveSharedPreference.getUserName(mContext) + "님의 관심멘티는" +String.valueOf(menteeTalentList.size())+ "건 입니다.");
                            }

                            menteeAdapter = new ListAdapter_Talent(mActivity, menteeTalentList, "MENTEE", inPerson);
                            lv_mentor_profile.setAdapter(menteeAdapter);

                        }

                        if(friendFlag){
                            ((Button)findViewById(R.id.btn_profile_btn)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(mContext, "이미 등록된 친구입니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            ((Button)findViewById(R.id.btn_profile_btn)).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addFriend();
                                }
                            });
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
                params.put("UserID", userID);
                params.put("CheckUserID", SaveSharedPreference.getUserId(mContext));
                params.put("TalentFlag", talentFlag);
                return params;
            }
        };

        // Add StringRequest to the RequestQueue
        requestQueue.add(stringRequest);
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
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }

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

                    Glide.with(mContext).load(SaveSharedPreference.getImageUri() + SaveSharedPreference.getMyThumbPicturePath()).into((ImageView) findViewById(R.id.cimg_pic_dl));
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
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
    }

    public void updateMyProfileInfo(final String text){
        RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
        StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Profile/updateMyProfileInfo.do", new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getString("result").equals("success")){
                        SaveSharedPreference.setPrefUserDescription(mContext, text);
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
                params.put("userID", SaveSharedPreference.getUserId(mContext));
                params.put("PROFILE_DESCRIPTION", text);
                return params;
            }
        };

        postRequestQueue.add(postJsonRequest);
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
}
