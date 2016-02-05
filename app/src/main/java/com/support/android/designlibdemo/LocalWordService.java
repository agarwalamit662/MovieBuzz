package com.support.android.designlibdemo;

/**
 * Created by amitagarwal3 on 10/6/2015.
 */
import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LocalWordService extends Service {
    private final IBinder mBinder = new MyBinder();
    private ArrayList<String> list = new ArrayList<String>();
    private double random=0;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        random = Math.random();
        Toast.makeText(getApplicationContext(), "kmfds", Toast.LENGTH_LONG);
      //  Log.e("nkjsgn", Double.toString(Math.random()));
      //  Log.e("nkjsgn", Double.toString(Math.random()));
        /*
        MyDetailsAsync fetch = new MyDetailsAsync(this);
        fetch.execute("mydetails");


        MyFriendDetailsAsync fetchfriends = new MyFriendDetailsAsync(this);
        fetchfriends.execute("fetchfriends");

        MyMovieDetailsAsync fetchmovies = new MyMovieDetailsAsync(this);
        fetchmovies.execute("mymoviesdetails");

        MyMovieUpcomingDetailsAsync fetchmoviesupcoming = new MyMovieUpcomingDetailsAsync(this);
        fetchmoviesupcoming.execute("mymoviesdetailsupcoming");

        */
        ServiceDataSync fetch = new ServiceDataSync(this);
        fetch.execute("AllDataFetch");

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        LocalWordService getService() {
            return LocalWordService.this;
        }

    }

    public double getRandom()
    {
        return random;

    }



}