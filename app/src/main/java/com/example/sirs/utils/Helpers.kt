package com.example.sirs.utils

import android.content.Context

class Helpers {

    fun checkisFirstTime(mContext: Context): Boolean {
        val shared = mContext.getSharedPreferences(Constant.myShared, Context.MODE_PRIVATE)
        return shared.getBoolean("app_status", true)
    }

}