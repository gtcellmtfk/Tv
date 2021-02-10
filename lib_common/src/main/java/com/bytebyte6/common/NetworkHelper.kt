package com.bytebyte6.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * 先打开Wifi然后打开数据，不会有数据Network的回调
 * 先打开数据然后打开Wifi，数据和Wifi的Network都会回调
 */
class NetworkHelper(context: Context) {

    /**
     * 网络是否连接
     */
    private val _networkConnected = MutableLiveData<Boolean>()
    val networkConnected: LiveData<Boolean> = _networkConnected

    /**
     * Wifi
     */
    private val _networkIsWifi = MutableLiveData<Boolean>()
    val networkIsWifi: LiveData<Boolean> = _networkIsWifi

    /**
     *蜂窝网络
     */
    private val _networkIsCellular = MutableLiveData<Boolean>()
    val networkIsCellular: LiveData<Boolean> = _networkIsCellular

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networks = mutableSetOf<Network>()

    private val networkRequest = NetworkRequest.Builder().build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            this@NetworkHelper.logd(
                "onCapabilitiesChanged network=$network" +
                        " networkCapabilities$networkCapabilities"
            )
            networks.add(network)
            postNetworkIsConnected()
            postState(networkCapabilities)
        }

        override fun onLost(network: Network) {
            this@NetworkHelper.logd("onLost network=$network")
            networks.remove(network)
            postNetworkIsConnected()
            if (networkIsConnected()) {
                postState(connectivityManager.getNetworkCapabilities(networks.last()))
            }
        }

        override fun onUnavailable() {
            this@NetworkHelper.logd("onUnavailable")
            postNetworkIsConnected()
        }
    }

    private fun postState(networkCapabilities: NetworkCapabilities?) {
        if (networkCapabilities == null)
            return

        val hasWifi = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        _networkIsWifi.postValue(hasWifi)

        val hasCellular = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        _networkIsCellular.postValue(hasCellular)
    }

    private fun postNetworkIsConnected() {
        val connected = networkIsConnected()
        _networkConnected.postValue(connected)
    }

    fun registerNetworkCallback() {
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    fun networkIsCellular(): Boolean {
        if (networkIsConnected()) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(networks.last())
                ?: return false
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } else {
            return false
        }
    }

    fun networkIsWifi(): Boolean {
        if (networkIsConnected()) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(networks.last())
                ?: return false
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } else {
            return false
        }
    }

    fun networkIsConnected(): Boolean {
        return networks.isNotEmpty()
    }
}