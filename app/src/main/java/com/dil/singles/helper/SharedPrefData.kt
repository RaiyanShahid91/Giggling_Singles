package com.dil.singles.helper

import android.content.Context
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences
import android.util.Log
import com.dil.singles.helper.Constants.SAVEDATA

class SharedPrefData(context: Context) {

    var sharedPreferences: SharedPreferences = context.getSharedPreferences(SAVEDATA, MODE_PRIVATE)
    private var myDataEdit = sharedPreferences.edit()

    fun saveDataString(key: String, value: String) {
        myDataEdit.putString(key, value)
        myDataEdit.commit()
        Log.d(key, value)
    }

    fun saveDataInt(key: String, value: Int) {
        myDataEdit.putInt(key, value)
        myDataEdit.commit()
    }

    fun getDataString(key: String): String {
        Log.d(key, sharedPreferences.getString(key, "").toString())
        return sharedPreferences.getString(key, "").toString()
    }

    fun getDataInt(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }

    fun deleteData(key: String) {
        myDataEdit.remove(key).commit()
    }
}