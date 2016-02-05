package com.support.android.designlibdemo;

/**
 * Created by Amit on 17-06-2015.
 */
//import com.support.android.designlibdemo.util.LruBitmapCache;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;


import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();


    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Parse.enableLocalDatastore(getApplicationContext());

        Parse.initialize(this,"fEnRRM70Ev1JlaL0b4GHaCSVoJd5ysTHDKrWALhk","1MxTLTv3ElGSwyorhTnnPmRsJxwe5bVRQQUHOdjN");

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        ParseFacebookUtils.initialize(this);
      //  PushService.setDefaultPushCallback(this, MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        //  PushService.setDefaultPushCallback();
        ParsePush.subscribeInBackground("MovieBuzz", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.e("Subscribed to parse", "Successfully subscribed to Parse!");
            }
        });

        //change 1



        PackageInfo info;
        try
        {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyhash = new String(Base64.encode(md.digest(), 0));
                // String something = new String(Base64.encodeBytes(md.digest()));



            }



        }
        catch (PackageManager.NameNotFoundException e1)
        {
            Log.e("name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e)
        {
            Log.e("no such an algorithm", e.toString());
        }
        catch (Exception e)
        {
            Log.e("exception", e.toString());
        }


    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }


}