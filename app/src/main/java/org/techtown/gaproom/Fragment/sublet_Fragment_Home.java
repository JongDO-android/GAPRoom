package org.techtown.gaproom.Fragment;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.techtown.gaproom.Activity.AddRoomActivity;
import org.techtown.gaproom.Adapter.ReviewAdapter;
import org.techtown.gaproom.Model.MarkerItem;
import org.techtown.gaproom.Model.ReviewItem;
import org.techtown.gaproom.Model.Room;
import org.techtown.gaproom.Model.TimeDescending_Review;
import org.techtown.gaproom.Model.User;
import org.techtown.gaproom.Pattern.RoomListSingleton;
import org.techtown.gaproom.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class sublet_Fragment_Home extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private FragmentActivity mContext;

    /*Conformed Marker List*/
    private ArrayList<MarkerItem> markerlist = new ArrayList<>();

    /*Custom Marker*/
    private View marker_root_view;
    private View marker_root_view_unselected;
    private TextView text_Phone;
    private TextView text_address;
    private ImageView imageView_marker;
    private Marker SelectedMarker = null;

    /*Google Map Variable*/
    private Marker currentMarker = null;
    private Marker searchMarker = null;
    private GoogleMap gMap;
    private MapView mapView = null;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient mFuse;
    private Geocoder geocoder;
    private Location currentLocation;
    private final LatLng mDefaultLocation = new LatLng(37.56, 126.97);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000 * 60;  // 1분 단위 시간 갱신
    private static final int FASTEST_UPDATE_INTERVAL_MS = 1000 * 30 ; // 30초 단위로 화면 갱신

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    /*Search View*/
    private SearchView searchView;
    private String search_address;

    private String TAG = sublet_Fragment_Home.class.getSimpleName();

    /*Recycler View Variable*/
    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;
    private ArrayList<ReviewItem> reviewItems = new ArrayList<>();

    /*Fire base Variable*/
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ChildEventListener childEventListener;

    public sublet_Fragment_Home(){

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sublet_home, container, false);
        recyclerView = v.findViewById(R.id.recycler_view);

        setCustomMarkerView();
        ImageButton add_room_btn = v.findViewById(R.id.add_room_btn);
        add_room_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddRoomActivity.class);
                startActivity(intent);
            }
        });

        /*search view*/
        searchView = v.findViewById(R.id.searchView);
        searchView.setQueryHint("주소로 방 찾기");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<Address> list = null;
                try {
                    list = geocoder.getFromLocationName(query, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(list != null){
                    if(list.size() > 0){
                        double lat = list.get(0).getLatitude();
                        double lon = list.get(0).getLongitude();
                        LatLng latLng = new LatLng(lat, lon);
                        if(searchMarker != null){
                            searchMarker.remove();
                        }
                        if(currentMarker != null){
                            currentMarker.remove();
                        }

                        searchMarker = gMap.addMarker(new MarkerOptions().position(latLng));
                        searchMarker.setTitle(query);

                        gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        gMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        geocoder = new Geocoder(mContext);

        /*현재 위치 버튼*/
        Button cur_loc_btn = v.findViewById(R.id.current_loc_btn);
        cur_loc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFuse.getLastLocation().addOnSuccessListener(mContext, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            currentMarker = gMap.addMarker(new MarkerOptions()
                                    .position(latLng));

                            if(searchMarker != null){
                                searchMarker.remove();
                            }
                            currentMarker.setTitle("현재 위치");
                            gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            gMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
                        }
                    }
                });
            }
        });
        recyclerView.setHasFixedSize(true);

        reviewAdapter = new ReviewAdapter(getContext(), reviewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(reviewAdapter);

        initDatabase();


        /*방 등록 했을 때, 파이어베이스에서 값 불러와서 구글맵에 마커 찍기*/
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewAdapter.clear();
                markerlist.clear();
                for(DataSnapshot addressData : dataSnapshot.getChildren()){
                    User user = addressData.getValue(User.class);
                    String address = (String) addressData.child("Room").child("address").getValue();
                    String period_start = (String) addressData.child("Room").child("start_day").getValue();
                    String period_end = (String) addressData.child("Room").child("end_day").getValue();
                    String phone = (String) addressData.child("phone_number").getValue();
                    String form = (String) addressData.child("Room").child("room_form").getValue();
                    String floor = (String) addressData.child("Room").child("floor").getValue();
                    String width = (String) addressData.child("Room").child("width").getValue();

                    String price = (String) addressData.child("Room").child("price").getValue();
                    Long curT = (Long) addressData.child("Room").child("time").getValue();
                    ReviewItem reviewItem = new ReviewItem();


                    assert user != null;
                    reviewItem.setGender(user.getGender());
                    reviewItem.setPeriod_start(period_start);
                    reviewItem.setPeriod_end(period_end);
                    reviewItem.setForm(form);
                    reviewItem.setFloor(floor);
                    reviewItem.setWidth(width);
                    reviewItem.setAddress(address);
                    reviewItem.setPrice(price);
                    reviewItem.setCurrentTime(curT);


                    reviewAdapter.addItem(reviewItem);

                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocationName(address, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(addresses != null){
                        if(addresses.size() > 0){
                            Address ads = addresses.get(0);
                            double lat = ads.getLatitude();
                            double lon = ads.getLongitude();

                            MarkerItem markerItem = new MarkerItem(lat, lon, phone, address);
                            markerlist.add(markerItem);

                            for(int i = 0 ; i < markerlist.size() ; i++){
                                addMarker(markerlist.get(i),false);
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(savedInstanceState != null){
            currentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            CameraPosition cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        mapView = (MapView) v.findViewById(R.id.map_view);
        if(mapView != null){
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        }

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Activity activity;
        if(context instanceof Activity){
            activity = (Activity) context;
            mContext = (FragmentActivity) activity;
        }
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MapsInitializer.initialize(mContext);

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);

        mFuse = LocationServices.getFusedLocationProviderClient(mContext);

    }


    /*fire base reference 불러오는 Method*/
    private void initDatabase() {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("/added_room_list/");

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mRef.addChildEventListener(childEventListener);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.map_view);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        setDefaultLocation();
        getLocationPermission();
        updateLocationUI();

        getDeviceLocation();
        setCustomMarkerView();

        if(markerlist.size() > 0){
            for(int i = 0 ; i < markerlist.size() ; i ++){
                addMarker(markerlist.get(i), false);
            }
        }
    }

    /*Google Map 현재 위치 지도에 표시하는 Method*/
    private void updateLocationUI() {
        if(gMap == null){
            return;
        }
        if(mLocationPermissionGranted){
            gMap.setMyLocationEnabled(true);
            gMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else{
            gMap.setMyLocationEnabled(false);
            gMap.getUiSettings().setMyLocationButtonEnabled(false);
            currentLocation = null;
            getLocationPermission();
        }

    }
    private void setDefaultLocation() {
        if(currentMarker != null){
            currentMarker.remove();
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mDefaultLocation);
        markerOptions.title("위치 정보를 가져올 수 없습니다.");
        markerOptions.snippet("GPS 활성 여부를 확인해주세요.");
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        currentMarker = gMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM);
        gMap.moveCamera(cameraUpdate);

    }
    private String getCurrentAddress(LatLng latLng){
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            Toast. makeText( mContext,
                    "위치로부터 주소를 인식할 수 없습니다. 네트워크가 연결되어 있는지 확인해 주세요.",
                    Toast.LENGTH_SHORT ).show();

            e.printStackTrace();
            return "주소 인식 불가";
        }

        if(addresses.size() < 1){
            return "해당 위치에 주소 없음";
        }

        Address address = addresses.get(0);
        StringBuilder addressStringBuilder = new StringBuilder();
        for(int i = 0 ; i < address.getMaxAddressLineIndex() ; i ++){
            addressStringBuilder.append(address.getAddressLine(i));
            if(i < address.getMaxAddressLineIndex())
                addressStringBuilder.append("\n");
        }

        return addressStringBuilder.toString();
    }
    LocationCallback locationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            List<Location> locations = locationResult.getLocations();
            if(locations.size() > 0){
                Location location = locations.get(locations.size()-1);

                LatLng currentPos = new LatLng(location.getLatitude(), location.getLongitude());

                String markerTitle = getCurrentAddress(currentPos);
                String markerSnippet = "위도 :" + String.valueOf(location.getLatitude())
                        + "경도 :" + String.valueOf(location.getLongitude());

                setCurrentLocation(location, markerTitle, markerSnippet);
                currentLocation = location;

            }
        }
    };
    private void setCurrentLocation(Location location, String markerTitle, String markerSnippet){
        if(currentMarker != null) currentMarker.remove();

        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);

        currentMarker = gMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        gMap.moveCamera(cameraUpdate);
    }
    private void getDeviceLocation(){
        if(mLocationPermissionGranted){
            mFuse.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }

    }
    private void getLocationPermission(){
        if(ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted = true;
        } else{
            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode){
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    public boolean checkLocationServiceStatus(){
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStop();
        if(mFuse != null){
            mFuse.removeLocationUpdates(locationCallback);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if(mLocationPermissionGranted){
            mFuse.requestLocationUpdates(locationRequest, locationCallback, null);
            if(gMap != null){
                gMap.setMyLocationEnabled(true);
            }
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mFuse != null){
            mFuse.removeLocationUpdates(locationCallback);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    /*Marker Method*/
    private void setCustomMarkerView(){
        marker_root_view = LayoutInflater.from(mContext).inflate(R.layout.custom_marker, null);
        marker_root_view_unselected = LayoutInflater.from(mContext).inflate(R.layout.custom_marker_unselected, null);
        text_address = marker_root_view.findViewById(R.id.text_address);
        text_Phone = marker_root_view.findViewById(R.id.text_phone);
        imageView_marker = marker_root_view.findViewById(R.id.marker_icon);
    }
    private Marker addMarker(MarkerItem markerItem, boolean isSelected){
        LatLng latLng = new LatLng(markerItem.getLat(), markerItem.getLon());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(markerItem.getAddress());
        markerOptions.snippet(markerItem.getPhone());

        if(isSelected) {
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(mContext, marker_root_view_unselected)));
        }
        else{
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(mContext, marker_root_view_unselected)));
        }

       return gMap.addMarker(markerOptions);
    }
    private Bitmap createDrawableFromView(Context context, View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(marker.getPosition());
        gMap.animateCamera(cameraUpdate);

        return false;
    }

}
