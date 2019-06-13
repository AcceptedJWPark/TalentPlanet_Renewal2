package accepted.talentplanet_renewal2.Profile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import accepted.talentplanet_renewal2.Classes.TalentObject_Home;
import accepted.talentplanet_renewal2.GeoPoint;
import accepted.talentplanet_renewal2.Home.MainActivity;
import accepted.talentplanet_renewal2.MyTalent;
import accepted.talentplanet_renewal2.PermissionUtil;
import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mContext = getApplicationContext();

       // makeTestTalentArr();
        mentorTalentList = new ArrayList<>();
        menteeTalentList = new ArrayList<>();

        tv_toolbar = findViewById(R.id.tv_toolbar);
        tv_toolbar.setText("Profile");
        ((ImageView) findViewById(R.id.img_open_dl)).setImageResource(R.drawable.icon_back);
        findViewById(R.id.img_show1x15).setVisibility(View.GONE);
        findViewById(R.id.img_show3x5).setVisibility(View.GONE);

        // 받은 값 구현
        Intent intent = new Intent(this.getIntent());
        if (intent.getStringExtra("userName") != null) {
            ((TextView)findViewById(R.id.tv_name_profile)).setText(intent.getStringExtra("userName"));
            ((TextView)findViewById(R.id.tv_birth_profile)).setText(intent.getStringExtra("userInfo"));
        }

        Point pt = new Point();
        getWindowManager().getDefaultDisplay().getSize(pt);
        ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getSize(pt);
        int height = pt.y;

        View trash;
        lv_point = findViewById(R.id.lv_point_profile);

        lv_mentor_profile = findViewById(R.id.lv_mentor_profile);
        lv_mentee_profile = findViewById(R.id.lv_mentee_profile);
        
        mLayoutManagerMentor = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mLayoutManagerMentee = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        lv_mentor_profile.setLayoutManager(mLayoutManagerMentor);
        lv_mentee_profile.setLayoutManager(mLayoutManagerMentee);
        
        getAllTalent("Y");
        getAllTalent("N");

        vp = findViewById(R.id.vp_profile_mentor);
        vp.setAdapter(new talentlist_pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);
        onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    ((LinearLayout) findViewById(R.id.ll_firstpage_profile_mentor)).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.tv_series_profile_mentor)).setVisibility(View.GONE);
                } else if (position == 1) {
                    ((LinearLayout) findViewById(R.id.ll_firstpage_profile_mentor)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_series_profile_mentor)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_series_profile_mentor)).setText("2");
                } else {
                    ((LinearLayout) findViewById(R.id.ll_firstpage_profile_mentor)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_series_profile_mentor)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_series_profile_mentor)).setText("3");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
        vp.addOnPageChangeListener(onPageChangeListener);
        findViewById(R.id.ll_totalshow_profile_mentor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp.setCurrentItem(0);
            }
        });

        //point 부분
        userDate.add(new ItemData_Profile(true,"박종우","남성 / 29세", "2019/05/17 16:48PM 완료","+50"));
        userDate.add(new ItemData_Profile(false,"민권홍","남성 / 30세", "2019/05/14 16:48PM 완료","-50"));
        userDate.add(new ItemData_Profile(true,"이태훈","남성 / 29세", "2019/05/12 18:48PM 완료","+50"));
        userDate.add(new ItemData_Profile(false,"문건우","남성 / 25세", "2019/05/11 11:48AM 완료","-50"));
        userDate.add(new ItemData_Profile(true,"조현배","남성 / 27세", "2019/05/04 10:48PM 완료","+50"));

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


        Intent i = getIntent();
        keyword1 = i.getStringExtra("talent1");
        keyword2 = i.getStringExtra("talent2");
        keyword3 = i.getStringExtra("talent3");


        isRegisted = i.getBooleanExtra("talentFlag", true);
        isHavingData = i.getBooleanExtra("isHavingData", false);

        if(isHavingData) {
            data = (MyTalent)i.getSerializableExtra("data");
            location = data.getLocation();

        }
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
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
                            for(int i = 0; i < talentArr.length(); i++){
                                JSONObject obj = talentArr.getJSONObject(i);
                                TalentObject_Home item = new TalentObject_Home(obj.getString("Name"), getResources().getIdentifier(obj.getString("BackgroundID"), "drawable", getPackageName()), getResources().getIdentifier(obj.getString("IconID"), "drawable", getPackageName()), 0);
                                item.setCateCode((int)obj.getLong("Code"));
                                if(talentFlag.equals("Y")) {
                                    mentorTalentList.add(item);
                                }else if(talentFlag.equals("N")){
                                    menteeTalentList.add(item);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(talentFlag.equals("Y")){
                            mentorAdapter = new ListAdapter_Talent(mentorTalentList);
                            lv_mentor_profile.setAdapter(mentorAdapter);
                        }else if(talentFlag.equals("N")){
                            menteeAdapter = new ListAdapter_Talent(menteeTalentList);
                            lv_mentee_profile.setAdapter(menteeAdapter);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(this.getClass().getName(), "test 1 [error]: " +error);
                requestQueue.stop();
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("UserID", "ansrjsdn7@naver.com");
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
        if(!isHavingData)
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


}
