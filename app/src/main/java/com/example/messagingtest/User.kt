package com.example.messagingtest

import android.content.Context
import android.content.SharedPreferences


object User {
    private const val KEY_TOKEN = "TOKEN"
    private const val FILE_NAME = "TESTINGO"
    private fun preferences(context: Context): SharedPreferences {

        val sharedPreferences: SharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences
    }

    fun getToken(context: Context): String? {
        return preferences(context).getString(KEY_TOKEN, null)
    }

    fun setToken(context: Context, token: String) {
        preferences(context).edit().apply {
            putString(KEY_TOKEN, token)
            apply()
        }
    }

    fun removeToken(context: Context) {
        preferences(context).edit().apply {
            remove(KEY_TOKEN)
            apply()
        }
    }
}