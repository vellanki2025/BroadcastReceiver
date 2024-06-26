package com.android.broadcastreceiver.customcallback

import java.util.Collections
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors

// 1. Thread Safety
class ScaleCustomBroadcastReceiver {

    private val observersMap: MutableMap<Observer, IntentFilter> = Collections.synchronizedMap(mutableMapOf())

    fun register(observer: Observer, intentFilter: IntentFilter) {
        synchronized(this) {
            observersMap[observer] = intentFilter
        }
    }

    fun unregister(observer: Observer) {
        synchronized(this) {
            observersMap.remove(observer)
        }
    }

    fun sendBroadcast(intent: Intent) {
        synchronized(this) {
            observersMap.forEach { (observer, filter) ->
                if (filter.action == intent.action) {
                    observer.onReceive(intent)
                }
            }
        }
    }
}

// 2. Efficient Intent Matching
// Use more sophisticated intent matching, similar to the native Android implementation. This can include data types, categories, and actions.
class CustomBroadcastReceiver2 {

    private val observersMap: MutableMap<Observer, IntentFilter> = Collections.synchronizedMap(mutableMapOf())

    // Extended IntentFilter class with categories and data types
    data class IntentFilter(val action: String, val categories: Set<String>? = null, val dataType: String? = null)

    // Extend intent matching logic
    fun sendBroadcast(intent: Intent) {
        synchronized(this) {
            observersMap.forEach { (observer, filter) ->
                if (filter.action == intent.action
//                    &&
//                    (filter.categories == null || intent.categories?.containsAll(filter.categories) == true) &&
//                    (filter.dataType == null || filter.dataType == intent.dataType)
                ) {
                    observer.onReceive(intent)
                }
            }
        }
    }
}

// 3. Observer Management
// Use a CopyOnWriteArrayList for managing observers to handle concurrent modifications efficiently.
class CustomBroadcastReceiver3 {

    private val observersMap: CopyOnWriteArrayList<Pair<Observer, IntentFilter>> = CopyOnWriteArrayList<Pair<Observer, IntentFilter>>()

    fun register(observer: Observer, intentFilter: IntentFilter) {
        observersMap.add(Pair(observer, intentFilter))
    }

    fun unregister(observer: Observer) {
        observersMap.removeAll { it.first == observer }
    }

    fun sendBroadcast(intent: Intent) {
        observersMap.forEach { (observer, filter) ->
            if (filter.action == intent.action) {
                observer.onReceive(intent)
            }
        }
    }
}

// 4. Asynchronous Broadcasting
// Offload the broadcasting to a separate thread to avoid blocking the main thread, especially when there are many observers.
class CustomBroadcastReceiver4 {
    private val observersMap: MutableMap<Observer, IntentFilter> = Collections.synchronizedMap(mutableMapOf())
    private val executor = Executors.newSingleThreadExecutor()

    fun register(observer: Observer, intentFilter: IntentFilter) {
        synchronized(this) {
            observersMap[observer] = intentFilter
        }
    }

    fun unregister(observer: Observer) {
        synchronized(this) {
            observersMap.remove(observer)
        }
    }

    fun sendBroadcast(intent: Intent) {
        executor.execute {
            synchronized(this) {
                observersMap.forEach { (observer, filter) ->
                    if (filter.action == intent.action) {
                        observer.onReceive(intent)
                    }
                }
            }
        }
    }
}



