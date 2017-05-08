package app.lampstudio.com.weatherapp.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import app.lampstudio.com.weatherapp.R;
import app.lampstudio.com.weatherapp.until.AppController;

public class OpenWeather2 extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient mGoogleApiClient;
    String tag_json_obj = "object";
    String tag_json_arry = "array_object";
    String TAG = "OpenW2";
    String url = "http://api.openweathermap.org/data/2.5/weather?lat=";
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_weather2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        img = (ImageView) findViewById(R.id.img_wt);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d("openweather", String.valueOf(mLastLocation.getLatitude()));
            Log.d("openweather", String.valueOf(mLastLocation.getLongitude()));
            JSONRequest(String.valueOf(mLastLocation.getLongitude()),String.valueOf(mLastLocation.getLatitude()));
            JSONArrayRequest(String.valueOf(mLastLocation.getLongitude()),String.valueOf(mLastLocation.getLatitude()),7);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public final String locationWeatherUrl(String lon, String lat){
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=4747c18a7c9e2347b4287c743549aa83";
        return url;
    }
    public final String locationWeatherUrlManyDay(String lon, String lat,int day){
        String url = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=" + lat + "&lon=" + lon +"&cnt="+day+ "&appid=4747c18a7c9e2347b4287c743549aa83";
        return url;
    }

    public void JSONRequest(String lon,String lat) {
        Toast.makeText(this, "touch JSONRequest", Toast.LENGTH_SHORT).show();
//        url += lat+"&lon="+lon+"&APPID=4747c18a7c9e2347b4287c743549aa83";
        url = locationWeatherUrl(lon, lat);
        Log.d(TAG, url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.
                GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String image = response.optJSONArray("weather").optJSONObject(0).optString("icon");
                        Log.d(TAG, "img = "+image);
                        image = "http://openweathermap.org/img/w/"+image+".png";
                        Picasso.with(OpenWeather2.this).load(image).into(img);
//                        try {
//                        voleyDemo.setText(response.optJSONObject("phone").optString("bcv"));
//                            voleyDemo.setText(response.getJSONObject("phone").getString("bcv"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            voleyDemo.setText("Ex");
//                        }
//                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
//                pDialog.hide();
            }
        });

// Adding request to request queue

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void JSONArrayRequest(String lon,String lat,int day) {
        String url = locationWeatherUrlManyDay(lon, lat,day);
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG + "himhim", response.toString());
//                        pDialog.hide();
//                        voleyDemo.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error:" + error.getMessage());
//                pDialog.hide();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(req, tag_json_arry);
    }
}
