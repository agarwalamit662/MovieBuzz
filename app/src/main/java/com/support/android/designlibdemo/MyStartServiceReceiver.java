package com.support.android.designlibdemo;

/**
 * Created by amitagarwal3 on 10/6/2015.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyStartServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"kjsjfhewdw",Toast.LENGTH_LONG);
        Intent service = new Intent(context, LocalWordService.class);
        context.startService(service);
    }
}
