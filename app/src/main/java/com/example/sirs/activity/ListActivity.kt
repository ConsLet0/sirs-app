package com.example.sirs.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sirs.ItemModel
import com.example.sirs.R
import com.example.sirs.adapter.ListAdapter
import com.example.sirs.utils.Constant
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Method

class ListActivity : AppCompatActivity() {

    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var recyclerView: RecyclerView
    private lateinit var titleText: TextView
    private lateinit var listAdapter:ListAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var itemList:ArrayList<ItemModel>
    private lateinit var fullItemList:ArrayList<ItemModel>
    private lateinit var searchView:androidx.appcompat.widget.SearchView
    private lateinit var backButton:ImageView
    var mContext = this@ListActivity
    var url = Constant.mainApiUrl
    var categoryId  = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        //nestedScrollView = findViewById(R.id.nestedScroll)
        progressBar = findViewById(R.id.progressList)
        recyclerView = findViewById(R.id.recyData)
        backButton = findViewById(R.id.backButton)
        searchView = findViewById(R.id.search)
        titleText = findViewById(R.id.titleText)
        backButton.setOnClickListener{
            onBackPressed()
        }

        val inte = intent
        categoryId = inte.getStringExtra("category")!!

        if(categoryId == "search"){
            categoryId = ""
            searchView.visibility = View.VISIBLE
            titleText.text = "Cari"
        }else{
            searchView.visibility = View.GONE
            titleText.text = categoryId.replace("_", " ").uppercase()
        }



        itemList = ArrayList()
        fullItemList = ArrayList()
        listAdapter = ListAdapter(this, itemList)
        recyclerView.setHasFixedSize(false)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = listAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String): Boolean {

                itemList.clear()
                val searchData = fullItemList.filter {
                     it.namaTempat!!.lowercase().contains(newText.lowercase())
                }

                for(i in searchData.indices){
                    val dataSearch = searchData[i]
                    val itemModel = ItemModel()
                    itemModel.gambarTempat = dataSearch.gambarTempat
                    itemModel.latitude = dataSearch.latitude
                    itemModel.longitude = dataSearch.longitude
                    itemModel.idTempat = dataSearch.idTempat
                    itemModel.namaTempat = dataSearch.namaTempat
                    itemModel.alamatTempat = dataSearch.alamatTempat
                    itemList.add(itemModel)
                }
                listAdapter.notifyDataSetChanged()
                return false
            }

            @SuppressLint("SetTextI18n")
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })

        showAllData(categoryId)


    }

    private fun showAllData(categoryString:String){
        progressBar.visibility = View.VISIBLE
        val stringRequest = @SuppressLint("NotifyDataSetChanged")
        object : StringRequest(Method.GET,
            "$url?category=$categoryString", Response.Listener { response ->
            try {

                val jsonObject = JSONObject(response)
                val dataObject = jsonObject.getJSONArray("data")
                for (i in 0 until dataObject.length()) {
                    val obj = dataObject.getJSONObject(i)
                    val itemModel = ItemModel()
                    itemModel.gambarTempat = obj.getString("image")
                    itemModel.latitude = obj.getDouble("latitude")
                    itemModel.longitude = obj.getDouble("longitude")
                    itemModel.idTempat = obj.getString("id")
                    itemModel.namaTempat = obj.getString("name")
                    itemModel.alamatTempat = obj.getString("alamat")
                    fullItemList.add(itemModel)
                    itemList.add(itemModel)
                }
            } catch (e: JSONException) {
                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
            progressBar.visibility = View.GONE

            Log.e("Total Item", itemList.size.toString())
            listAdapter.notifyDataSetChanged()
        }, Response.ErrorListener {error ->
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