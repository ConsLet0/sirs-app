package com.example.sirs.adapter

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sirs.ItemModel
import com.example.sirs.R
import com.example.sirs.activity.DetailActivity
import com.example.sirs.utils.Constant
import com.google.android.gms.location.LocationServices


class ListAdapter(val context: Context, val dataList: ArrayList<ItemModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val url = Constant.mainApiUrl


    override fun onCreateViewHolder(parent: ViewGroup, v: Int) : RecyclerView.ViewHolder {

        val view = inflater.inflate(R.layout.data_search, parent, false)
        return DataViewHolder(view)


    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holderMain: RecyclerView.ViewHolder, position: Int) {

        val dataArrayList = dataList[position]
        val holder = holderMain as DataViewHolder

        holder.namaTempat.text = dataArrayList.namaTempat
        holder.alamatTempat.text = dataArrayList.alamatTempat
        Glide.with(context).load(dataArrayList.gambarTempat).placeholder(R.color.grey).into(holder.imageTempat)

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            val loc1 = Location("")
            loc1.latitude = dataArrayList.latitude!!
            loc1.longitude = dataArrayList.longitude!!

            val loc2 = Location("")
            loc2.latitude = it.latitude
            loc2.longitude = it.longitude

            val distance = loc1.distanceTo(loc2)
            holder.distance.text = "${(distance / 1000).toInt()} km"
        }


        holder.areaParent.setOnClickListener {
            val inte = Intent(context, DetailActivity::class.java)
            inte.putExtra("id_place", dataArrayList.idTempat)
            context.startActivity(inte)
        }


    }





    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class DataViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        var namaTempat:TextView
        var alamatTempat:TextView
        var distance:TextView
        var imageTempat:ImageView
        var areaParent:CardView

        init {
            namaTempat = view.findViewById(R.id.placeName)
            alamatTempat = view.findViewById(R.id.placeAddress)
            distance = view.findViewById(R.id.placeDistance)
            imageTempat = view.findViewById(R.id.placeImage)
            areaParent = view.findViewById(R.id.areaParent)

        }
    }
}
