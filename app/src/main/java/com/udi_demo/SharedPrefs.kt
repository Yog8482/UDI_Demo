package com.udi_demo

import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KClass

class SharedPrefs(context: Context) {
    companion object {
        private const val PREF = "UDIPrefs"
        const val APP_TYPE = "app_type"
    }

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    fun saveAppType(type: String) {
        put(APP_TYPE, type)
    }

    fun getAppType() = get(APP_TYPE, String::class.java) as String

    private fun <T> get(key: String, clazz: Class<String>): T =
        when (clazz) {
            String::class.java -> sharedPref.getString(key, "")
            Boolean::class.java -> sharedPref.getBoolean(key, false)
            Float::class.java -> sharedPref.getFloat(key, -1f)
            Double::class.java -> sharedPref.getFloat(key, -1f)
            Int::class.java -> sharedPref.getInt(key, -1)
            Long::class.java -> sharedPref.getLong(key, -1L)
            else -> null
        } as T

    private fun <T> put(key: String, data: T) {
        val editor = sharedPref.edit()
        when (data) {
            is String -> editor.putString(key, data)
            is Boolean -> editor.putBoolean(key, data)
            is Float -> editor.putFloat(key, data)
            is Double -> editor.putFloat(key, data.toFloat())
            is Int -> editor.putInt(key, data)
            is Long -> editor.putLong(key, data)
        }
        editor.apply()
    }

    fun clear() {
        sharedPref.edit().run {
            remove(APP_TYPE)
        }.apply()
    }
}