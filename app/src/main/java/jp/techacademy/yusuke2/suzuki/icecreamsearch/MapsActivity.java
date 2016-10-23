package jp.techacademy.yusuke2.suzuki.icecreamsearch;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.location.Address;
import android.location.Geocoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.internal.zznu.is;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng latLng2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public void onMapSearch(View view) {
        //EditTextの生成
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        //EditTextの値の取得と変数への格納
        String location = locationSearch.getText().toString();
        //空のAddressListの作成
        List<Address> addressList = null;

        //locationに値がセットされない場合
        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                //Geocoderに住所を示す文字列を渡して、緯度経度を示す値をaddressListに格納
                addressList = geocoder.getFromLocationName(location, 1);

                //addressListから緯度経度を取得し、addressに渡す。
                Address address = addressList.get(0);

                //緯度：latitudeと経度：longitudeをaddressから取得し、latgに格納
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                //検索場所へのマーカーの設定
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                //マップを検索された場所に移動
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // URL生成（現在地を元に）
                double lat = address.getLatitude();
                double lng = address.getLongitude();
                String urlstring = "https://maps.googleapis.com/maps/api/place/search/json";
                urlstring += "?location=" + lat + "," + lng;
                urlstring += "&sensor=true&rankby=distance&types=convenience_store&key=AIzaSyDIFum2371izcbg4jLjq2D3SV3zlzaqUkI";
                //AIzaSyDmcjb2rvqsWxWGzpxvKvFKfZnEkrhgxgI
//              URL u = new URL(urlStrBuilder.toString());

                Uri uri = Uri.parse(urlstring);
                AsyncHttpRequest task = new AsyncHttpRequest(this);
                task.execute(uri);

                String str = "a";

                Intent intent1 = getIntent();
                if(intent1 != null) {
                    latLng2 = intent1.getExtras().getParcelable("jp.techacademy.yusuke2.suzuki.icecreamsearch.latLng0");
                    if(latLng2 != null) {
                        new Thread(){
                            @Override
                            public void run(){
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        MapsActivity mapsActivity = new MapsActivity();
                                        GoogleMap mMap2 = mapsActivity.getGoogleMap();
                                        MarkerOptions options = new MarkerOptions();
                                        options.position(latLng2);
                                        mMap2.addMarker(options);
                                    }
                                });
                            }
                        }.start();
                    }else{
                        finish();
                    }
                }

            } catch (IOException e) {
               e.printStackTrace();
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // 地図タイプ設定
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //東京駅を初期表示し、ズームする
        CameraPosition camerapos = new CameraPosition.Builder().target(new LatLng(35.681382, 139.766084)).zoom(15.5f).build();
        // 地図の中心の変更する
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camerapos));
//        // 現在位置表示の有効化 →おそらくパーミッションの設定をしないといけない。
//        mMap.setMyLocationEnabled(true);

        // 設定の取得
        UiSettings settings = mMap.getUiSettings();
        // 現在位置に移動するボタンの有効化
        settings.setMyLocationButtonEnabled(true);
        // ズームイン・アウトボタンの有効化
        settings.setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//             TODO: Consider calling
//                ActivityCompat#requestPermissions
//             here to request the missing permissions, and then overriding
//               public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                      int[] grantResults)
//             to handle the case where the user grants the Manifest.permission. See the documentation
//             for ActivityCompat#requestPermissions for more details.

            //        // 現在位置ボタンの表示を行なう
//        mMap.setMyLocationEnabled(true);

            return;
        }
    }

    public GoogleMap getGoogleMap() {
        return mMap;
    }
}
