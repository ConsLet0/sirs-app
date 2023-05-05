package com.example.sirs.activity

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.postDelayed
import com.example.sirs.R
import com.example.sirs.utils.Helpers

class SplashActivity : AppCompatActivity() {

    val mContext = this@SplashActivity


    private var locationPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            loadApp()
        }, 1000)

    }

    private fun loadApp(){
        requestPermissions()
        if(!Helpers().checkisFirstTime(mContext)){
            startActivity(Intent(mContext, HomeActivity::class.java))
        }else{
            startActivity(Intent(mContext, IntroActivity::class.java))

        }
    }

    private fun requestPermissions() {
        permissionRequest.launch(locationPermissions)
    }

    private val permissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        val granted = permissions.entries.all {
            it.value == true
        }
        permissions.entries.forEach {
            Log.e("LOG_TAG", "${it.key} = ${it.value}")
        }

        if (granted) {

        } else {

        }
    }
}