package com.bytebyte6.common

import android.content.Context
import android.net.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * 先打开Wifi然后打开数据，不会有数据Network的回调
 * 先打开数据然后打开Wifi，数据和Wifi的Network都会回调
 */
class NetworkHelper(context: Context) {

    enum class NetworkType {
        WIFI, MOBILE, NONE
    }

    private val _networkType = MutableLiveData<NetworkType>()
    val networkType: LiveData<NetworkType> = _networkType

    /**
     * 网络是否连接
     */
    private val _networkConnected = MutableLiveData<Boolean>()
    val networkConnected: LiveData<Boolean> = _networkConnected

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkRequest = NetworkRequest.Builder().build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            this@NetworkHelper.logd("onAvailable network=$network")
            postNetworkIsConnected()
            postNetworkType()
        }

        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
            this@NetworkHelper.logd("onBlockedStatusChanged network=$network")
        }

        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
            this@NetworkHelper.logd("onLinkPropertiesChanged network=$network")
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            this@NetworkHelper.logd("onLosing network=$network")
        }

        override fun onUnavailable() {
            this@NetworkHelper.logd("onUnavailable")
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            this@NetworkHelper.logd(
                "onCapabilitiesChanged network=$network" +
                        " networkCapabilities=$networkCapabilities"
            )
        }

        override fun onLost(network: Network) {
            this@NetworkHelper.logd("onLost network=$network")
            postNetworkIsConnected()
            postNetworkType()
        }
    }

    private fun postNetworkType() {
        _networkType.postValue(getNetworkType())
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

    fun getNetworkType(): NetworkType {
        if (networkIsConnected()) {
            connectivityManager.allNetworks.forEach {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(it)
                if (networkCapabilities != null) {
                    val hasWifi =
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    if (hasWifi) {
                        return NetworkType.WIFI
                    }
                }
            }
            return NetworkType.MOBILE
        } else {
            return NetworkType.NONE
        }
    }

    fun networkIsConnected(): Boolean {
        return connectivityManager.allNetworks.isNotEmpty()
    }

    fun isMobile() = getNetworkType() == NetworkType.MOBILE

    fun isWifi() = getNetworkType() == NetworkType.WIFI

    fun isNone() = getNetworkType() == NetworkType.NONE
}