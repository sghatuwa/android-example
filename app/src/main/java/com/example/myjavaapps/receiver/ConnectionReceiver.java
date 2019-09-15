package com.example.myjavaapps.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class ConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("API123",""+intent.getAction());
        Log.d("context", "jjjj ConnectionReceiver");
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction()))
            Toast.makeText(context, "SOME_ACTION is received", Toast.LENGTH_LONG).show();
        else {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            Log.d("context", "ConnectionReceiver "+ isConnected);
            Toast.makeText(context, "Network is changed or reconnected", Toast.LENGTH_LONG).show();
            if (isConnected) {
                try {
                    Toast.makeText(context, "Network is connected", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "Network is changed or reconnected", Toast.LENGTH_LONG).show();
            }
        }
    }
}
