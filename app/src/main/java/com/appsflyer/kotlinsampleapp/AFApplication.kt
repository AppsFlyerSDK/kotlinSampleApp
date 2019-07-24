package com.appsflyer.kotlinsampleapp

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.AppsFlyerLibCore

class AFApplication : Application() {
        private val logTag = AppsFlyerLibCore.LOG_TAG;
        private val devKey = "pJtNoWRvepn9EBtYG4jAUQ";

        override fun onCreate() {
            super.onCreate()
            val conversionDataArray: ArrayList<String> = ArrayList()
            val oaoaDataArray: ArrayList<String> = ArrayList()

            AppsFlyerLib.getInstance().init(devKey, object : AppsFlyerConversionListener {

                override fun onAppOpenAttribution(data: MutableMap<String, String>?) {
                    Log.i(logTag, "[onAppOpenAttribution]")
                    oaoaDataArray.clear()
                    data?.map {
                        Log.d(BuildConfig.FLAVOR, "${it.key} : ${it.value}")
                        oaoaDataArray.add( it.key + ": " + it.value + "\n")
                    }

                    broadcastIntent(Intent("oaoaData"), "oaoaDataString", parseString(oaoaDataArray))
                }

                override fun onAttributionFailure(error: String?) {
                    Log.e(logTag, "[onAttributionFailure] $error")
                }

                override fun onInstallConversionDataLoaded(data: MutableMap<String, String>?) {
                    Log.i(logTag, "[onInstallConversionDataLoaded]")
                    conversionDataArray.clear()
                    data?.let { cvData ->
                        cvData.map {
                            Log.i(BuildConfig.FLAVOR, "${it.key} : ${it.value}")
                            conversionDataArray.add( it.key + ": " + it.value + "\n")
                        }

                        broadcastIntent(Intent("conversionData"), "conversionDataString", parseString(conversionDataArray))

                        /* Deferred Deep Link */

                        /* If it's first open */
                        if (cvData["is_first_launch"] == "true") { //If it's first open
                            /* Check if it's from deferred deep linking */
                            if (cvData["af_adset"] == "deferreddeeplink") { // Assume af_adset is used for passing deferred deep link info.
                                Log.d(logTag, cvData["af_ad"])
                            }
                        }
                    }
                }

                override fun onInstallConversionFailure(error: String?) {
                    Log.e(logTag, "[onAttributionFailure] $error")
                }
            })

            AppsFlyerLib.getInstance().setDebugLog(true)
            AppsFlyerLib.getInstance().startTracking(this)
        }

    fun broadcastIntent(intent: Intent, dataType: String, data: String){
        intent.putExtra(dataType, data)
        LocalBroadcastManager.getInstance(this@AFApplication).sendBroadcast(intent);
    }

    fun parseString(dataArray: ArrayList<String>): String{
        var newDataString = dataArray.joinToString()
        newDataString = newDataString.replace(",", "")
        return newDataString
    }

}