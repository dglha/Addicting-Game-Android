package com.dlha.addictinggame.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.d("TAG123123", "From: ${p0.from}")
        if (p0.data.isNotEmpty()) {
            Log.d("TAG", "Message data payload: ${p0.data}")
        }
        p0.notification?.let {
            Log.d("TAG", "Message Notification Body: ${it.body}")
        }
    }
}