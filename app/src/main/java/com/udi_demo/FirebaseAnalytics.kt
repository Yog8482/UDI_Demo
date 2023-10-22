package com.udi_demo

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object FirebaseAnalytics {
    private var firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    fun logEvent(eventName:String, bundle: Bundle){
        firebaseAnalytics.logEvent(eventName, bundle)
    }
    fun setUser(user:String){
        firebaseAnalytics.setUserId(user)
    }

}

object FirebaseEvents{
    const val KEYBOARD_KEY_PRESS ="KEYBOARD_KEY_PRESSED"
}