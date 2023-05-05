package com.example.sirs.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.example.sirs.R

class HomeActivity : AppCompatActivity() {

    private lateinit var rsButton:CardView
    private lateinit var praktekButton:CardView
    private lateinit var apotikButton:CardView
    private lateinit var klinikButton:CardView
    private lateinit var searchButton:CardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        rsButton = findViewById(R.id.btnRumahSakit)
        praktekButton = findViewById(R.id.btnPraktek)
        apotikButton = findViewById(R.id.btnApotik)
        klinikButton = findViewById(R.id.btnKlinik)
        searchButton = findViewById(R.id.searchButton)

        rsButton.setOnClickListener {
            val inte = Intent(this@HomeActivity, ListActivity::class.java)
            inte.putExtra("category", "rumah_sakit")
            startActivity(inte)
        }

        praktekButton.setOnClickListener {
            val inte = Intent(this@HomeActivity, ListActivity::class.java)
            inte.putExtra("category", "praktek_pribadi")
            startActivity(inte)
        }

        apotikButton.setOnClickListener {
            val inte = Intent(this@HomeActivity, ListActivity::class.java)
            inte.putExtra("category", "apotik")
            startActivity(inte)
        }

        klinikButton.setOnClickListener {
            val inte = Intent(this@HomeActivity, ListActivity::class.java)
            inte.putExtra("category", "klinik")
            startActivity(inte)
        }

        searchButton.setOnClickListener {
            val inte = Intent(this@HomeActivity, ListActivity::class.java)
            inte.putExtra("category", "search")
            startActivity(inte)
        }

    }


}