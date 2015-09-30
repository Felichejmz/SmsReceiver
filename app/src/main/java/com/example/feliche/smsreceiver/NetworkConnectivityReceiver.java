package com.example.feliche.smsreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.logging.Handler;

public class NetworkConnectivityReceiver extends BroadcastReceiver {
    private static final String TAG = "NET_CONN_LIST: ";
    private NetworkInfo networkInfo;
    private NetworkInfo networkInfo_2;
    //private Boolean mListener = ;
    private State mState;
    private String mReason;
    private boolean mIsFailOver;
    private NetworkConnectivityReceiver mReceiver;

    private static final boolean DBG = true;

    public NetworkConnectivityReceiver() {
        mState = State.UNKNOW;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(!action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            Log.w(TAG, "onReceiver() called with " + mState.toString() + " and " + intent);
        }

        boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false);
        if (noConnectivity) {
            mState = State.NOT_CONNECTED;
        }else{
            mState = State.CONNECTED;
        }

        networkInfo = (NetworkInfo)intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
        networkInfo_2 = (NetworkInfo)intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);
        mReason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
        mIsFailOver = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER,false);

        if(DBG){
            Log.d(TAG, "onReceive():networkInfo=" + networkInfo + "netWorkinfo_2=");
            Log.d(TAG, networkInfo_2 == null ? "[none]" : networkInfo_2 + "noConn=" +
                    noConnectivity + mState.toString());
        }

        MainActivity inst = MainActivity.instance();
        inst.updateNetworkStatus(networkInfo.getState());
    }

    public enum State {
        UNKNOW,
        CONNECTED,
        NOT_CONNECTED
    }
}
