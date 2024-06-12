package com.example.weatherapplicationmvvm.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalManager @Inject constructor(@ApplicationContext context: Context){

    private var prefs = context.getSharedPreferences(Constants.PREFS_TOKEN_FILE,Context.MODE_PRIVATE)

    fun setUserLoginStatus(userLogin:Boolean){
        val editor = prefs.edit()
        editor.putBoolean(Constants.NEW_USER,true)
        editor.apply()
    }

    fun getUserLoginStatus():Boolean {
        return prefs.getBoolean(Constants.NEW_USER,false)
    }

}