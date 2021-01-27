package com.bytebyte6.base

import android.content.Context
import android.net.*
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
                this@NetworkHelper.logd("onAvailable network=$network")
                networkConnected.postValue(Event(true))
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                this@NetworkHelper.logd("onCapabilitiesChanged network=$network networkCapabilities$networkCapabilities")
            }

            override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                this@NetworkHelper.logd("onLinkPropertiesChanged network=$network linkProperties=$linkProperties")
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                this@NetworkHelper.logd("onLosing network=$network maxMsToLive=$maxMsToLive")
            }

            override fun onLost(network: Network) {
                this@NetworkHelper.logd("onLost network=$network")
                networkConnected.postValue(Event(false))
            }

            override fun onUnavailable() {
                this@NetworkHelper.logd("onUnavailable")
            }
        }

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

    }

    fun liveData(): LiveData<Event<Boolean>> = networkConnected

    fun isConnected(): Boolean =
        ConnectivityManagerCompat.isActiveNetworkMetered(connectivityManager)
}