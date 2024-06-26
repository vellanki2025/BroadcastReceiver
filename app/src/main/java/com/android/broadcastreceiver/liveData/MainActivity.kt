package com.android.broadcastreceiver.liveData

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.broadcastreceiver.R

class MainActivity : AppCompatActivity() {

    private lateinit var networkChangeReceiver: NetworkChangeReceiver
    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView)

        networkChangeReceiver = NetworkChangeReceiver()

        networkChangeReceiver.isConnected.observe(this) { isConnected ->
            handleNetworkChange(isConnected)
        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkChangeReceiver)
    }

    private fun handleNetworkChange(isConnected: Boolean) {
        if (isConnected) {
            textView?.text = "Network Connected"
            // Handle network connected
            Toast.makeText(this, "Network Connected", Toast.LENGTH_SHORT).show()
        } else {
            textView?.text = "Network Disconnected"
            // Handle network disconnected
            Toast.makeText(this, "Network Disconnected", Toast.LENGTH_SHORT).show()
        }
    }
}