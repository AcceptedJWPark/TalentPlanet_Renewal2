package accepted.talentplanet_renewal2.Profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import accepted.talentplanet_renewal2.R;
import accepted.talentplanet_renewal2.SaveSharedPreference;
import accepted.talentplanet_renewal2.VolleySingleton;

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
    Button btn_searchbtn_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        mContext = getApplicationContext();

        img_rightbtn = findViewById(R.id.img_rightbtn);
        img_open_dl = findViewById(R.id.img_open_dl);
        img_alarm = findViewById(R.id.img_alarm);
        tv_Choose = findViewById(R.id.tv_Choose);
        btn_searchbtn_map = findViewById(R.id.btn_searchbtn_map);
        et_searchaddr_map = findViewById(R.id.et_searchaddr_map);

        tv_Choose.setVisibility(View.VISIBLE);
        tv_Choose.setText("주소 찾기");

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
                        mOptions.snippet(addr[1]+" "+addr[2]+" "+addr[3]);
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

        // 버튼 이벤트
        btn_searchbtn_map.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                String str = et_searchaddr_map.getText().toString();
                if (str.equals("")) {
                    Toast.makeText(mContext, "주소 / 건물명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
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

                    String []splitStr = addressList.get(0).toString().split(",");
                    String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소

                    String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                    String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도

                    // 좌표(위도, 경도) 생성
                    LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    // 마커 생성
                    MarkerOptions mOptions2 = new MarkerOptions();
                    mOptions2.title("검색 결과");

                    String[] addr = address.split(" ");
                    mOptions2.snippet(addr[1]+" "+addr[2]+" "+addr[3]);
                    mOptions2.position(point);
                    // 마커 추가
                    mMap.addMarker(mOptions2);
                    // 해당 좌표로 화면 줌
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
                }
            }
        });

        mMap.setOnInfoWindowClickListener(this);

        Intent intent = getIntent();
        String myLAT = intent.getStringExtra("GP_LAT");
        String myLNG = intent.getStringExtra("GP_LNG");

        if (!myLAT.equals("") || !myLNG.equals("")) {
            LatLng myLocation = new LatLng(Double.parseDouble(myLAT), Double.parseDouble(myLNG));
            mMap.addMarker(new MarkerOptions().position(myLocation).title("내 위치"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
        } else {
            LatLng blueHouse = new LatLng(37.5866118, 126.9726223);
            mMap.addMarker(new MarkerOptions().position(blueHouse).title("청와대"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(blueHouse, 15));
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
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
