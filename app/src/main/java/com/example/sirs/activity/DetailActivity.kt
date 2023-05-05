package com.example.sirs.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.sirs.R
import com.example.sirs.utils.Constant
import com.google.android.gms.location.LocationServices
import org.json.JSONException
import org.json.JSONObject


class DetailActivity : AppCompatActivity() {


    private lateinit var backButton:ImageView
    private lateinit var imagePlace:ImageView
    private lateinit var placeName:TextView
    private lateinit var placeAddres:TextView
    private lateinit var placePhone:TextView
    private lateinit var callButton:LinearLayout
    private lateinit var covidBackground:CardView
    private lateinit var covidIconStatus:ImageView
    private lateinit var covidStatus:TextView

    private lateinit var siranapButton:CardView
    private lateinit var bpjsBackground:CardView
    private lateinit var bpjsIconStatus:ImageView
    private lateinit var bpjsStatus:TextView

    private lateinit var akreditasiCard:CardView
    private lateinit var akreditasiStatus:TextView

    private lateinit var distanceKm:TextView
    private lateinit var directionButton:CardView

    private lateinit var parentView:LinearLayout
    private lateinit var progressBar: ProgressBar

    private var mContext = this@DetailActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        backButton = findViewById(R.id.backButton)
        imagePlace = findViewById(R.id.imageDetail)
        placeAddres = findViewById(R.id.detailAddress)
        placeName = findViewById(R.id.detailName)
        placePhone = findViewById(R.id.detailTelp)
        callButton = findViewById(R.id.dialBtn)
        siranapButton = findViewById(R.id.siranapBtn)
        covidBackground = findViewById(R.id.covidBackground)
        covidIconStatus = findViewById(R.id.covidIconStatus)
        covidStatus = findViewById(R.id.statusCovid)
        bpjsBackground = findViewById(R.id.bpjsBackground)
        bpjsIconStatus = findViewById(R.id.bpjsIconStatus)
        bpjsStatus = findViewById(R.id.statusBpjs)
        akreditasiCard = findViewById(R.id.akreditasi)
        akreditasiStatus = findViewById(R.id.statusAkreditasi)
        distanceKm = findViewById(R.id.detailDistance)
        progressBar = findViewById(R.id.progressBar)
        directionButton = findViewById(R.id.gotoLocation)
        parentView = findViewById(R.id.layoutDetail)
        parentView.visibility = View.GONE
        backButton.setOnClickListener {
            onBackPressed()
        }
        val inten = intent
        val idTempat = inten.getStringExtra("id_place")
        if (idTempat != null) {
            showDetail(idTempat)

    }

    }


    private fun showDetail(idTempat:String){

        val stringRequest = @SuppressLint("NotifyDataSetChanged")
        object : StringRequest(Method.GET, Constant.mainApiUrl + "/${idTempat}", Response.Listener { response ->
            try {

                val jsonObject = JSONObject(response)
                val dataObject = jsonObject.getJSONObject("data")

                Glide.with(mContext).load(dataObject.getString("image")).placeholder(R.color.grey).into(imagePlace)
                placeName.text = dataObject.getString("name")
                placeAddres.text = dataObject.getString("alamat")
                placePhone.text = dataObject.getString("phone")

                callButton.setOnClickListener(View.OnClickListener {
                    val phoneNumber = placePhone.text.toString()
                    val phoneCallIntent = Intent(Intent.ACTION_CALL)
                    phoneCallIntent.data = Uri.parse("tel:$phoneNumber")
                    startActivity(phoneCallIntent)
                })

                val placeLatitude = dataObject.getDouble("latitude")
                val placeLongitude = dataObject.getDouble("longitude")
                val akreditasiText = dataObject.getString("akreditasi")

                if(akreditasiText != "null"){
                    akreditasiStatus.text = "Akreditasi: $akreditasiText"
                }else{
                    akreditasiCard.visibility = View.GONE
                }

                val isBpjs = dataObject.getString("bpjs")
                val isCovid = dataObject.getString("tipe")

                if(isBpjs != "null"){
                    bpjsBackground.setCardBackgroundColor(resources.getColor(R.color.green))
                    bpjsIconStatus.setImageResource(R.drawable.yes_vector)
                    bpjsStatus.text = "Mendukung BPJS"
                }else{
                    bpjsBackground.setCardBackgroundColor(resources.getColor(R.color.red))
                    bpjsIconStatus.setImageResource(R.drawable.no_vector)
                    bpjsStatus.text = "Tidak Mendukung BPJS"
                }

                if(isCovid == "covid"){
                    covidBackground.setCardBackgroundColor(resources.getColor(R.color.green))
                    covidIconStatus.setImageResource(R.drawable.yes_vector)
                    covidStatus.text = "Mendukung penanganan COVID-19"
                }else{
                    covidBackground.setCardBackgroundColor(resources.getColor(R.color.red))
                    covidIconStatus.setImageResource(R.drawable.no_vector)
                    covidStatus.text = "Tidak Mendukung penanganan COVID-19"
                }

                siranapButton.setOnClickListener{
                    val openURL = Intent(android.content.Intent.ACTION_VIEW)
                    openURL.data = Uri.parse("https://yankes.kemkes.go.id/app/siranap/")
                    startActivity(openURL)
                }

                directionButton.setOnClickListener {
                    val gmmIntentUri = Uri.parse("google.navigation:q=$placeLatitude,$placeLongitude")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    if (mapIntent.resolveActivity(packageManager) != null) {
                        startActivity(mapIntent);
                    } else {
                        Toast.makeText(
                            this@DetailActivity,
                            "Google Maps Belum Terinstal. Install Terlebih dahulu.",
                            Toast.LENGTH_LONG
                        ).show();
                    }
                }


                val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext)
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.e("TES", "PR")
                }
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    val loc1 = Location("")
                    loc1.latitude = placeLatitude
                    loc1.longitude = placeLongitude

                    val loc2 = Location("")
                    loc2.latitude = it.latitude
                    loc2.longitude = it.longitude

                    val distance = loc1.distanceTo(loc2)
                    distanceKm.text = "${(distance / 1000).toInt()} km"

                }

            } catch (e: JSONException) {
                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
            progressBar.visibility = View.GONE
            parentView.visibility = View.VISIBLE
        }, Response.ErrorListener { error ->
            when (error) {
                is TimeoutError -> Toast.makeText(mContext, "Terjadi kesalahan, tolong periksa koneksi anda", Toast.LENGTH_SHORT).show()
                is NetworkError -> Toast.makeText(mContext, "Tidak dapat terhubung ke internet, tolong periksa koneksi anda", Toast.LENGTH_SHORT).show()
                is ParseError -> Toast.makeText(mContext, "Terjadi kesalahan, coba beberapa saat lagi", Toast.LENGTH_SHORT).show()
                is NoConnectionError -> Toast.makeText(mContext, "Tidak dapat terhubung ke internet, tolong periksa koneksi anda", Toast.LENGTH_SHORT).show()
            }
        }){}

        val request = Volley.newRequestQueue(this)
        request.add(stringRequest)


    }


}
