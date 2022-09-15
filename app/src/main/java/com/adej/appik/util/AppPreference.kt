package com.adej.appik.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.adej.appik.R

 class AppPreference(context: Context) {
    private val prefs: SharedPreferences
    private val context: Context
    var sk: String?
        get() = prefs.getString(context.resources.getString(R.string.sk), "")
        set(pref) {
            val editor = prefs.edit()
            editor.putString(context.resources.getString(R.string.sk), pref)
            editor.commit()
        }
    var user: String?
        get() = prefs.getString(context.resources.getString(R.string.user), "")
        set(user) {
            val editor = prefs.edit()
            editor.putString(context.resources.getString(R.string.user), user)
            editor.commit()
        }
    var skv: String?
        get() = prefs.getString(context.resources.getString(R.string.skv), "")
        set(pref) {
            val editor = prefs.edit()
            editor.putString(context.resources.getString(R.string.skv), pref)
            editor.commit()
        }
    var baseUrl: String?
        get() = prefs.getString(context.resources.getString(R.string.base_url), "")
        set(pref) {
            val editor = prefs.edit()
            editor.putString(context.resources.getString(R.string.base_url), pref)
            editor.commit()
        }
    val role: String?
        get() = prefs.getString(context.resources.getString(R.string.skv), "")
    var date: String?
        get() = prefs.getString(context.resources.getString(R.string.date), "")
        set(pref) {
            val editor = prefs.edit()
            editor.putString(context.resources.getString(R.string.date), pref)
            editor.commit()
        }
    var secureKey: String?
        get() = prefs.getString(context.resources.getString(R.string.secure_key), "")
        set(pref) {
            val editor = prefs.edit()
            editor.putString(context.resources.getString(R.string.secure_key), pref)
            editor.commit()
        }

    init {
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
        this.context = context
    }
}