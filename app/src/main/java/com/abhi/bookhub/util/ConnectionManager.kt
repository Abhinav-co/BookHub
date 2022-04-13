package com.abhi.bookhub.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class ConnectionManager {

    fun connectivityCheck(context: Context):Boolean{
        val connectivityManager= context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeConnection:NetworkInfo?=connectivityManager.activeNetworkInfo

        if (activeConnection?.isConnected!=null)
            return activeConnection.isConnected
        else{
            return false
        }
    }
}