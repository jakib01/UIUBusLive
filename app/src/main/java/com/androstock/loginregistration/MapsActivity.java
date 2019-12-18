package com.androstock.loginregistration;
/*code*///
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.Double.parseDouble;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    String latitude = "";
    String longitude = "";
    LatLng currentlatLong = null;

    MarkerOptions mark = null;

    Marker marker;
    SupportMapFragment mapFragment;

    private Polyline mPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String postUrl= "http://uiulivebus.wasdpa-bd.org/server/apis/latlong.php";
        String postBody="{" +

                "    \"busId\": \"1\"\n" +

                "}";
        try {
            Object resJson = new JSONObject();
            resJson = postJson(postUrl, postBody);
            Log.d("Res json: ", resJson.toString());
            JSONObject json = new JSONObject(resJson.toString());
            latitude = json.getString("lat");
            longitude = json.getString("long");
            handler.postDelayed(runnable, delay);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        getCurrentPosition();

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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // draw line
        mMap = googleMap;

//        LatLng Dhanmondi15 = new LatLng(23.744364,90.372783);
//        mMap.addMarker(new MarkerOptions().position(Dhanmondi15).title("Marker in Dhanmondi 15"));
//
//        LatLng UIU = new LatLng(23.798115,90.449225);
//        mMap.addMarker(new MarkerOptions().position(UIU).title("Marker in UIU"));
//
//        LatLng ManikMia = new LatLng(23.758438,90.376954);
//
//        //Define list to get all latlng for the route
//        List<LatLng> path = new ArrayList();
//
//        mPolyline = mMap.addPolyline(new PolylineOptions().geodesic(true));
//
//
//        //Execute Directions API request
//        GeoApiContext context = new GeoApiContext.Builder()
//                .apiKey("AIzaSyAWFT3WuLWVC0GxjPgpQRQ2OGNXD8iT8ys")
//                .build();
//
////        drawRoute(context, path);
//        DirectionsApiRequest req = DirectionsApi.getDirections(context, "23.744364,90.372783", "23.798115,90.449225");
//        try {
//            DirectionsResult res = req.await();
//
//            //Loop through legs and steps to get encoded polylines of each step
//            if (res.routes != null && res.routes.length > 0) {
//                DirectionsRoute route = res.routes[0];
//
//                if (route.legs !=null) {
//                    for(int i=0; i<route.legs.length; i++) {
//                        DirectionsLeg leg = route.legs[i];
//                        if (leg.steps != null) {
//                            for (int j=0; j<leg.steps.length;j++){
//                                DirectionsStep step = leg.steps[j];
//                                if (step.steps != null && step.steps.length >0) {
//                                    for (int k=0; k<step.steps.length;k++){
//                                        DirectionsStep step1 = step.steps[k];
//                                        EncodedPolyline points1 = step1.polyline;
//                                        if (points1 != null) {
//                                            //Decode polyline and add points to list of route coordinates
//                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
//                                            for (com.google.maps.model.LatLng coord1 : coords1) {
//                                                path.add(new LatLng(coord1.lat, coord1.lng));
//                                            }
//                                        }
//                                    }
//                                } else {
//                                    EncodedPolyline points = step.polyline;
//                                    if (points != null) {
//                                        //Decode polyline and add points to list of route coordinates
//                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
//                                        for (com.google.maps.model.LatLng coord : coords) {
//                                            path.add(new LatLng(coord.lat, coord.lng));
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        } catch(Exception ex) {
//            Log.e("", ex.getLocalizedMessage());
//        }
//
//        //Draw the polyline
//        if (path.size() > 0) {
//            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
//            mMap.addPolyline(opts);
//        }
//
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ManikMia, 16));





//        double lati = parseDouble()
        // Add a marker in Sydney and move the camera
        currentlatLong = new LatLng(parseDouble(latitude), parseDouble(longitude));
        mark = new MarkerOptions()
                .position(currentlatLong);
        Marker marker = mMap.addMarker(mark);
        marker.setPosition(currentlatLong);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentlatLong,16));
    }



    public void onLocationChanged(String latitude, String longitude) {
//        LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
        currentlatLong = new LatLng(parseDouble(latitude), parseDouble(longitude));
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(currentlatLong).title("Bus Current position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentlatLong, 16));
    }
//    public void setApi() {
//        String postUrl= "http://uiulivebus.wasdpa-bd.org/server/apis/login.php";
//        String postBody="{" +
//
//                "    \"email\": \""+ email.getText() +"\",\n" +
//                "    \"password\": \""+ password.getText() +"\"\n" +
//                "}";
//
//        Log.e("str ---> ", postBody);
//
//    }

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000; //Delay for 1 seconds.  One second = 1000 milliseconds.



    @Override
    protected void onResume() {
        //start handler as activity become visible

        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                //do something
                String postUrl= "http://uiulivebus.wasdpa-bd.org/server/apis/latlong.php";
                String postBody="{" +

                        "    \"busId\": \"1\"\n" +

                        "}";
                Log.d("", postBody);
                try {
                    Object resJson = new JSONObject();
                    resJson = postJson(postUrl, postBody);
                    Log.d("Res json: ", resJson.toString());
                    JSONObject json = new JSONObject(resJson.toString());
                    JSONArray data = json.getJSONArray("data");
//                    Log.d("---->>> ", data.getString(0));
                    JSONObject dataObj = new JSONObject(data.get(0).toString());
                    Log.d("---->>> ", data.getString(0));
                    latitude = dataObj.getString("lat");
                    longitude = dataObj.getString("long");
                    Log.d("---->>> ", latitude + "     " + longitude);
                    if(!latitude.equals("") && !longitude.equals("")) {
//                        onLocationChanged(latitude, longitude);
//                        currentlatLong = new LatLng(parseDouble(latitude), parseDouble(longitude));
//                        mark = new MarkerOptions()
//                                .position(currentlatLong);
//                        Marker marker = mMap.addMarker(mark);
//                        marker.setPosition(currentlatLong);
//                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentlatLong, 16));
                        onLocationChanged(latitude, longitude);

                    }
                    handler.postDelayed(runnable, delay);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, delay);

        super.onResume();
    }

    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(16).build();
    }

// If onPause() is not included the threads will double up when you
// reload the activity

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

    public Object postJson(String url, String jsonStr){

        final MediaType mediaType
                = MediaType.parse("application/json");

        OkHttpClient httpClient = new OkHttpClient();

        //post json using okhttp
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, jsonStr))
                .build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e("TAG:", "Got response from server for JSON post using OkHttp ");
                return response.body().string();
            }

        } catch (IOException e) {
            Log.e("TAG", "error in getting response for json post request okhttp");
        }
        return null;
    }

    public void getCurrentPosition() {
        String postUrl= "http://uiulivebus.wasdpa-bd.org/server/apis/latlong.php";
        String postBody="{" +

                "    \"busId\": \"1\"\n" +

                "}";
        Log.d("", postBody);
        try {
            Object resJson = new JSONObject();
            resJson = postJson(postUrl, postBody);
            Log.d("Res json: ", resJson.toString());
            JSONObject json = new JSONObject(resJson.toString());
            JSONArray data = json.getJSONArray("data");
//                    Log.d("---->>> ", data.getString(0));
            JSONObject dataObj = new JSONObject(data.get(0).toString());
            Log.d("---->>> ", data.getString(0));
            latitude = dataObj.getString("lat");
            longitude = dataObj.getString("long");
            Log.d("---->>> ", latitude + "     " + longitude);
            if(!latitude.equals("") && !longitude.equals("")) {
//                        onLocationChanged(latitude, longitude);
                mapFragment.getMapAsync(this);

            }
            handler.postDelayed(runnable, delay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void drawRoute(GeoApiContext context, List<LatLng> path) {
        DirectionsApiRequest req = DirectionsApi.getDirections(context, "41.385064,2.173403", "40.416775,-3.70379");
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            Log.e("", ex.getLocalizedMessage());
        }

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
            mMap.addPolyline(opts);
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentlatLong, 6));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }
}
