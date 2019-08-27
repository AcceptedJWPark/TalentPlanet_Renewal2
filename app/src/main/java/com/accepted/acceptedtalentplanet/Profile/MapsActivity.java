package com.accepted.acceptedtalentplanet.Profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.accepted.acceptedtalentplanet.R;
import com.accepted.acceptedtalentplanet.SaveSharedPreference;
import com.accepted.acceptedtalentplanet.VolleySingleton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private Context mContext;
    private GoogleMap mMap;
    private Geocoder geocoder;

    ImageView img_rightbtn;
    ImageView img_open_dl;
    ImageView img_alarm;
    TextView tv_Choose;
    EditText et_searchaddr_map;
    LinearLayout ll_searchbtn_map;
    RelativeLayout rl_bg_map;

    private boolean isUser;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        mContext = getApplicationContext();

        intent = getIntent();
        isUser = intent.getBooleanExtra("isUser", false);

        String mode = SaveSharedPreference.getPrefTalentFlag(mContext);

        if (mode.equals("Y")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
                ((LinearLayout)findViewById(R.id.ll_searchbtn_map)).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));
                ((RelativeLayout)findViewById(R.id.rl_bg_map)).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentor));

            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
                ((LinearLayout)findViewById(R.id.ll_searchbtn_map)).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));
                ((RelativeLayout)findViewById(R.id.rl_bg_map)).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_mentee));

            }
        }

        img_rightbtn = findViewById(R.id.img_rightbtn);
        img_open_dl = findViewById(R.id.img_open_dl);
        img_alarm = findViewById(R.id.img_alarm);
        tv_Choose = findViewById(R.id.tv_Choose);
        ll_searchbtn_map = findViewById(R.id.ll_searchbtn_map);
        et_searchaddr_map = findViewById(R.id.et_searchaddr_map);
        rl_bg_map = findViewById(R.id.rl_bg_map);

        tv_Choose.setVisibility(View.VISIBLE);
        if (isUser) {
            tv_Choose.setText("유저 위치");
            rl_bg_map.setVisibility(View.GONE);
        } else {
            tv_Choose.setText("주소 찾기");
        }


        img_rightbtn.setVisibility(View.GONE);
        img_alarm.setVisibility(View.GONE);
        img_open_dl.setImageResource(R.drawable.icon_back);

        img_open_dl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this);
        if (!isUser) {
// 맵 터치 이벤트 구현 //
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
                @Override
                public void onMapClick(LatLng point) {
                    MarkerOptions mOptions = new MarkerOptions();
                    // 마커 타이틀
                    mOptions.title("현재 선택 위치");
                    Double latitude = point.latitude; // 위도
                    Double longitude = point.longitude; // 경도

                    final Geocoder geocoder = new Geocoder(mContext);
                    try {
                        List<Address> list = geocoder.getFromLocation(latitude,longitude,10);
                        if (list.size()==0) {
                            mOptions.snippet("해당되는 주소 정보는 없습니다");
                        } else {
                            String[] addr = list.get(0).getAddressLine(0).split(" ");
                            String showAddr = "";
                            for (int i=1;i<4;i++) {
                                if (addr[i].isEmpty() || addr[i] == null) {
                                    continue;
                                }
                                if (i == 1) {
                                    if (addr[i].equals("서울특별시")) {
                                        showAddr = "서울";
                                        continue;
                                    } else if (addr[i].equals("경기도")) {
                                        showAddr = "경기";
                                        continue;
                                    } else if (addr[i].equals("인천광역시")) {
                                        showAddr = "인천";
                                        continue;
                                    } else if (addr[i].equals("대구광역시")) {
                                        showAddr = "대구";
                                        continue;
                                    } else if (addr[i].equals("강원도")) {
                                        showAddr = "강원";
                                        continue;
                                    } else if (addr[i].equals("대전광역시")) {
                                        showAddr = "대전";
                                        continue;
                                    } else if (addr[i].equals("전라북도")) {
                                        showAddr = "전북";
                                        continue;
                                    } else if (addr[i].equals("전라남도")) {
                                        showAddr = "전남";
                                        continue;
                                    } else if (addr[i].equals("세종특별자치시")) {
                                        showAddr = "세종";
                                        continue;
                                    } else if (addr[i].equals("경상북도")) {
                                        showAddr = "경북";
                                        continue;
                                    } else if (addr[i].equals("경상남도")) {
                                        showAddr = "경남";
                                        continue;
                                    } else if (addr[i].equals("충청북도")) {
                                        showAddr = "충북";
                                        continue;
                                    } else if (addr[i].equals("충청남도")) {
                                        showAddr = "충남";
                                        continue;
                                    } else if (addr[i].equals("제주특별자치도")) {
                                        showAddr = "제주";
                                        continue;
                                    } else if (addr[i].equals("부산광역시")) {
                                        showAddr = "부산";
                                        continue;
                                    } else if (addr[i].equals("울산광역시")) {
                                        showAddr = "울산";
                                        continue;
                                    } else if (addr[i].equals("광주광역시")) {
                                        showAddr = "광주";
                                        continue;
                                    } else {
                                        showAddr = addr[i];
                                        continue;
                                    }
                                }
                                showAddr += " "+addr[i];
                            }
                            mOptions.snippet(showAddr);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("입출력오류", "입출력 오류 - 서버에서 주소변환시 에러발생");
                    }
                    mMap.clear();

                    // 마커의 스니펫(간단한 텍스트) 설정
//                mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                    // LatLng: 위도 경도 쌍을 나타냄
                    mOptions.position(new LatLng(latitude, longitude));
                    // 마커(핀) 추가
                    googleMap.addMarker(mOptions);
                }
            });
        }

        // 버튼 이벤트
        ll_searchbtn_map.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String str = et_searchaddr_map.getText().toString();
                if (str.equals("")) {
                    Toast.makeText(mContext, "주소 / 건물명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    mMap.clear();
                    List<Address> addressList = null;
                    try {
                        // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                        addressList = geocoder.getFromLocationName(
                                str, // 주소
                                10); // 최대 검색 결과 개수
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    // 콤마를 기준으로 split
                    if (addressList.size() == 0) {
                        Toast.makeText(mContext, "지도에서 " + str + "을(를) 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Address addrObj = addressList.get(0);
                    //String []splitStr = addressList.get(0).toString().split(",");
                    String address = addrObj.getAddressLine(0);

                    Log.d("what?", address);
                    // 좌표(위도, 경도) 생성
                    LatLng point = new LatLng(addrObj.getLatitude(), addrObj.getLongitude());
                    // 마커 생성
                    MarkerOptions mOptions2 = new MarkerOptions();
                    mOptions2.title("검색 결과");

                    String[] addr = address.split(" ");
                    String showAddr = "";
                    for (int i=1;i<4;i++) {
                        try {
                            if (addr[i].isEmpty() || addr[i] == null) {
                                continue;
                            }
                        }catch(ArrayIndexOutOfBoundsException e){
                            break;
                        }
                        if (i == 1) {
                            if (addr[i].equals("서울특별시")) {
                                showAddr = "서울";
                                continue;
                            } else if (addr[i].equals("경기도")) {
                                showAddr = "경기";
                                continue;
                            } else if (addr[i].equals("인천광역시")) {
                                showAddr = "인천";
                                continue;
                            } else if (addr[i].equals("대구광역시")) {
                                showAddr = "대구";
                                continue;
                            } else if (addr[i].equals("강원도")) {
                                showAddr = "강원";
                                continue;
                            } else if (addr[i].equals("대전광역시")) {
                                showAddr = "대전";
                                continue;
                            } else if (addr[i].equals("전라북도")) {
                                showAddr = "전북";
                                continue;
                            } else if (addr[i].equals("전라남도")) {
                                showAddr = "전남";
                                continue;
                            } else if (addr[i].equals("세종특별자치시")) {
                                showAddr = "세종";
                                continue;
                            } else if (addr[i].equals("경상북도")) {
                                showAddr = "경북";
                                continue;
                            } else if (addr[i].equals("경상남도")) {
                                showAddr = "경남";
                                continue;
                            } else if (addr[i].equals("충청북도")) {
                                showAddr = "충북";
                                continue;
                            } else if (addr[i].equals("충청남도")) {
                                showAddr = "충남";
                                continue;
                            } else if (addr[i].equals("제주특별자치도")) {
                                showAddr = "제주";
                                continue;
                            } else if (addr[i].equals("부산광역시")) {
                                showAddr = "부산";
                                continue;
                            } else if (addr[i].equals("울산광역시")) {
                                showAddr = "울산";
                                continue;
                            } else if (addr[i].equals("광주광역시")) {
                                showAddr = "광주";
                                continue;
                            } else {
                                showAddr = addr[i];
                                continue;
                            }
                        }
                        showAddr += " "+addr[i];
                    }
                    mOptions2.snippet(showAddr);
                    mOptions2.position(point);
                    // 마커 추가
                    mMap.addMarker(mOptions2);
                    // 해당 좌표로 화면 줌
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


            }
        });

        mMap.setOnInfoWindowClickListener(this);

        double myLAT = 0;
        double myLNG = 0;
        if (isUser) {
            myLAT = intent.getDoubleExtra("GP_LAT", 0);
            myLNG = intent.getDoubleExtra("GP_LNG", 0);
        } else {
            myLAT = Double.parseDouble(SaveSharedPreference.getPrefUserGpLat(mContext));
            myLNG = Double.parseDouble(SaveSharedPreference.getPrefUserGpLng(mContext));
        }

        if (myLAT != 0 || myLNG != 0) {
            LatLng myLocation = new LatLng(myLAT, myLNG);
            if (isUser) {
                mMap.addMarker(new MarkerOptions().position(myLocation).title("유저 위치"));
            } else {
                mMap.addMarker(new MarkerOptions().position(myLocation).title("내 위치"));
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
        } else {
            LatLng blueHouse = new LatLng(37.5866118, 126.9726223);
            mMap.addMarker(new MarkerOptions().position(blueHouse).title("청와대"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(blueHouse, 15));
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (isUser) {
            return;
        }

        LatLng location = marker.getPosition();

        final double lat = location.latitude;
        final double lng = location.longitude;

        AlertDialog.Builder ad = new AlertDialog.Builder(MapsActivity.this);
        ad.setTitle("주소 변경");
        ad.setMessage("선택하신 주소로 변경하시겠습니까?");
        ad.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RequestQueue postRequestQueue = VolleySingleton.getInstance(mContext).getRequestQueue();
                StringRequest postJsonRequest = new StringRequest(Request.Method.POST, SaveSharedPreference.getServerIp() + "Profile/updateMyLocation.do", new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("result").equals("success")){
                                SaveSharedPreference.setPrefUserGpLat(mContext, String.valueOf(lat));
                                SaveSharedPreference.setPrefUserGpLng(mContext, String.valueOf(lng));

                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
//                                Toast.makeText(mContext, "선택 지역으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
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
                        params.put("GP_LAT", String.valueOf(lat));
                        params.put("GP_LNG", String.valueOf(lng));
                        return params;
                    }
                };

                postRequestQueue.add(postJsonRequest);
            }
        });

        ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, "취소하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        ad.show();
    }
}
