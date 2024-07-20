package com.project.ecommercecineplanet

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import java.util.UUID
import javax.inject.Inject

@HiltAndroidApp
class EcommerceCineplanetApp: Application() {
    override fun onCreate() {
        super.onCreate()
        generateAndStoreSessionId(this)
    }
    private fun generateAndStoreSessionId(context: Context) {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val sessionIdKey = "session_id"
        var sessionId = sharedPreferences.getString(sessionIdKey, null)

        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString()
            sharedPreferences.edit().putString(sessionIdKey, sessionId).apply()
        }
    }
}