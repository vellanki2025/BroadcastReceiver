package com.android.broadcastreceiver.customcallback

class Main {

    fun main() {
        val receiver = CustomBroadcastReceiver()

        // Sample Observer
        val observer1 = object : Observer {
            override fun onReceive(intent: Intent) {
                println("Observer 1 received intent: ${intent.action}")
            }
        }

        val observer2 = object : Observer {
            override fun onReceive(intent: Intent) {
                println("Observer 2 received intent: ${intent.action}")
            }
        }

        // Register Observers
        receiver.register(observer1, IntentFilter("ACTION_TEST"))
        receiver.register(observer2, IntentFilter("ACTION_TEST_2"))

        // Send Broadcasts
        receiver.sendBroadcast(Intent("ACTION_TEST"))
        receiver.sendBroadcast(Intent("ACTION_TEST_2"))

        // Unregister Observer
        receiver.unregister(observer1)
        receiver.sendBroadcast(Intent("ACTION_TEST"))
    }

}