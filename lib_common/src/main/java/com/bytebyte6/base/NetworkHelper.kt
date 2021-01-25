package com.bytebyte6.base

import android.content.Context
import android.net.*
import android.net.Network
import androidx.core.net.ConnectivityManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NetworkHelper(context: Context) {

    private val networkConnected = MutableLiveData<Event<Boolean>>()

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    init {
        val networkRequest = NetworkRequest.Builder()
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                logd("onAvailable network=$network")
                networkConnected.postValue(com.bytebyte6.base.Event(true))
            }

            override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
                logd("onBlockedStatusChanged network=$network blocked=$blocked")
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                logd("onCapabilitiesChanged network=$network networkCapabilities$networkCapabilities")
            }

            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                logd("onLinkPropertiesChanged network=$network linkProperties=$linkProperties")
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                logd("onLosing network=$network maxMsToLive=$maxMsToLive")
            }

            override fun onLost(network: Network) {
                logd("onLost network=$network")
                networkConnected.postValue(com.bytebyte6.base.Event(false))
            }

            override fun onUnavailable() {
                logd("onUnavailable")
            }
        }

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

    }

    fun liveData():LiveData<Event<Boolean>> = networkConnected

    fun isConnected(): Boolean = ConnectivityManagerCompat.isActiveNetworkMetered(connectivityManager)
}