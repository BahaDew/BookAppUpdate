package uz.bahadew.bookappupdate.data

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyShared @Inject constructor(private val sharedPreferences: SharedPreferences) {
    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, default: String): String {
        return sharedPreferences.getString(key, default) ?: ""
    }

    fun putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, default: Int): Int {
        return sharedPreferences.getInt(key, default)
    }
}