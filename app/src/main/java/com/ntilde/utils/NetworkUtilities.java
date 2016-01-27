package com.ntilde.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ntilde.app.DonantesApplication;

/**
 * Created by emanuel on 20/11/15.
 */
public class NetworkUtilities {

    public static boolean hasNetworkConnection(){

        boolean status=false;

        try{

            ConnectivityManager cm = (ConnectivityManager) DonantesApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                    status= true;
            }

        }catch(Exception e){
            return false;

        }

        return status;

        }
}
