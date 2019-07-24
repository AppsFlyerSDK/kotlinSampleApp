package com.appsflyer.kotlinsampleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.AppsFlyerLibCore
import kotlinx.android.synthetic.main.activity_main.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.text.method.ScrollingMovementMethod




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        simpleInAppEventsBtn.setOnClickListener {
            AppsFlyerLib.getInstance().trackEvent(this@MainActivity, "Sample Event", emptyMap())
        }

        customInAppEventsBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, CustomInAppEventsActivity::class.java))
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(oaoaReceiver, IntentFilter("oaoaData"));
        LocalBroadcastManager.getInstance(this).registerReceiver(conversionDataReceiver, IntentFilter("conversionData"));
        oaoaDataTextView.setMovementMethod(ScrollingMovementMethod())
        conversionDataTextView.setMovementMethod(ScrollingMovementMethod())
    }

    override fun onResume() {
        super.onResume()
        oaoaDataTextView.text = getString(R.string.oaoaData)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
    private val oaoaReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val oaoaData = intent.getStringExtra("oaoaDataString")
            oaoaDataTextView.text = oaoaData
        }
    }
    private val conversionDataReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val conversionData = intent.getStringExtra("conversionDataString")
            conversionDataTextView.text = conversionData
        }
    }
}