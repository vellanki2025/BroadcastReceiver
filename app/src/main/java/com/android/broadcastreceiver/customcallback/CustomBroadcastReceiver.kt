package com.android.broadcastreceiver.customcallback

// Observer Interface
interface Observer {
    fun onReceive(intent: Intent)
}

// Intent Class (Simplified)
data class Intent(val action: String, val data: Any? = null)

// IntentFilter Class (Simplified)
data class IntentFilter(val action: String)

// Custom BroadcastReceiver Class
class CustomBroadcastReceiver {
    private val observersMap: MutableMap<Observer, IntentFilter> = mutableMapOf()

    fun register(observer: Observer, intentFilter: IntentFilter) {
        observersMap[observer] = intentFilter
    }

    fun unregister(observer: Observer) {
        observersMap.remove(observer)
    }

    fun sendBroadcast(intent: Intent) {
        observersMap.forEach { (observer, filter) ->
            if (filter.action == intent.action) {
                observer.onReceive(intent)
            }
        }
    }
}
