package com.support.android.designlibdemo;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
//import com.google.android.gms.drive.*;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Amit on 23-06-2015.
 */
public class LogOutFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    private static final int LOGIN_REQUEST = 0;
    private LocationManager locationManager;
    private String provider;
    private TextView titleTextView;
    private TextView emailTextView;
    private TextView nameTextView;
    private Button loginOrLogoutButton;
    private GoogleApiClient mGoogleApiClient;

    private TextView txtlogout;
    private Button logoutbutton;

    private ParseUser currentUser;
    public Location mLastLocation;
    private static final String ARG_SECTION_NUMBER = "section_number";


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foo(getContext());
       // buildGoogleApiClient();

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(LocationServices.API)
                .build();
        if(mGoogleApiClient == null){
            Log.e("mGoogleApiClient","null");
            Log.e("mGoogleApiClient","null");
        }
        else
        {
            Log.e("mGoogleApiClient","not null");
            Log.e("mGoogleApiClient","not null");
            Log.e("mGoogleApiClient","not null");
        }

    }
    public SingleShotLocationProvider.GPSCoordinates locationRec;
    public Location loc;
    public void foo(Context context) {
        // when you need location
        // if inside activity context = this;


        SingleShotLocationProvider.requestSingleUpdate(context,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates locations) {
                        Log.e("Location", "my location is " + locations.toString());
                        Log.e("Location", "my location is " + locations.toString());
                        Log.e("Location", "my location is " + locations.toString());
                        Log.e("Location", "my location is " + locations.latitude);
                        Log.e("Location", "my location is " + locations.longitude);
                        locationRec = locations;
                        loc = locations.location;
                        /*Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (addresses != null && addresses.size() > 0)
                            Log.e("address is ",addresses.get(0).getLocality());
                            Log.e("address is ",addresses.get(0).getLocality());
                            Log.e("address is ",addresses.get(0).getLocality());

                            System.out.println(addresses.get(0).getLocality());
                    }*/
                        Log.e("City name is ",locations.getCity);
                        Log.e("City name is ",locations.getCity);
                        Log.e("City name is ",locations.getCity);
                    }
                    });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.logout_profile, container, false);

        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                Document doc = null;
                try {
                    doc = Jsoup.connect("https://www.google.com/movies?near=mumbai&stok=ABAPP2uQGU8tJe9BoVffKC6qb0II5g90XQ%3A1448090182634&q=prem+ratan+dhan+payo").get();
                    String title = doc.title();
                    String href = doc.text();
                    ArrayList<Element> ele;
                    Elements links = doc.select("a[href]");
                    //Elements e1 = doc.getEle
                    Elements e = doc.getElementsByAttributeValueContaining("id", "link_1_theater_");
                    for (Element src : e) {
                        if (src.tagName().equals("a")) {

                            String theatrename = src.text();
                            //Log.e("theatre name", theatrename);
                            //Log.e("theatre name", theatrename);
                            //Log.e("theatre name", theatrename);

                        } else {
                            String theatrename = "hello";
                            /*Log.e("theatre name", theatrename);
                            Log.e("theatre name", theatrename);
                            Log.e("theatre name", theatrename);
                            *///  print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
                        }
                    }

                    /*Log.e("Doc title", title);
                    Log.e("Doc title", title);
                    Log.e("Doc Text", href);
                    Log.e("Doc Text", href);
                    */// String a = doc.id();
                    //doc.getElementById("")

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        task.execute("Hello");


        JSONObject data = null;


        txtlogout = (TextView) rootView.findViewById(R.id.textView_log_out);

        logoutbutton = (Button) rootView.findViewById(R.id.button_log_out);

        txtlogout.setText("Log Out From TellyMovieBuzz");
        logoutbutton.setText("Log Out");
        currentUser = ParseUser.getCurrentUser();

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    // User clicked to log out.
                    Intent intentstop = new Intent(getActivity(), MyStartServiceReceiver.class);
                    PendingIntent senderstop = PendingIntent.getBroadcast(getActivity(),
                            1234567, intentstop, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager alarmManagerstop = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                    alarmManagerstop.cancel(senderstop);


                    ParseUser.logOut();
                    currentUser = null;
                    // SampleProfileActivity.showProfileLoggedOut();


                    Intent intent = new Intent(getActivity(), SampleProfileActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });


        return rootView;

    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if(mLastLocation == null )
        {
            Log.e("Null","Null");
            Log.e("Null","Null");
            Log.e("Null","Null");
        }
        if (mLastLocation != null) {
            Log.e("latitude",(String.valueOf(mLastLocation.getLatitude())));
            Log.e("latitude",(String.valueOf(mLastLocation.getLatitude())));
            Log.e("Longitude",(String.valueOf(mLastLocation.getLongitude())));
            Log.e("Longitude",(String.valueOf(mLastLocation.getLongitude())));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("Conn sus","conn failed");
        Log.e("Conn sus","conn failed");
        Log.e("Conn sus","conn failed");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e("Conn failed","conn failed");
        Log.e("Conn failed","conn failed");
        Log.e("Conn failed","conn failed");
    }



    /*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }  */
}


