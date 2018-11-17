package com.example.toms.recetarioamedida.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class Util {

    public static Boolean hayInternet(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
