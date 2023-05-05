package com.example.sirs.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.sirs.R
import com.example.sirs.utils.Constant

class IntroActivity : AppCompatActivity() {

    lateinit var nextButton:CardView
    lateinit var titleText:TextView
    lateinit var messageText:TextView
    lateinit var imageView:ImageView
    lateinit var bakcgroundData:RelativeLayout
    var position = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        nextButton = findViewById(R.id.nextButton)
        titleText = findViewById(R.id.title)
        messageText = findViewById(R.id.message)
        imageView = findViewById(R.id.image)
        bakcgroundData = findViewById(R.id.background)


        nextButton.setOnClickListener {
            if(position == 1){
                titleText.text = getString(R.string.title_intro2)
                messageText.text = getString(R.string.intro2)
                imageView.setImageResource(R.drawable.intro2)
                bakcgroundData.setBackgroundColor(resources.getColor(R.color.intro2))
            }else if(position == 2){
                titleText.text = getString(R.string.title_intro3)
                messageText.text = getString(R.string.intro3)
                imageView.setImageResource(R.drawable.intro3)
                bakcgroundData.setBackgroundColor(resources.getColor(R.color.intro3))
            }else{
                val shared = getSharedPreferences(Constant.myShared, Context.MODE_PRIVATE)
                val editor = shared.edit()
                editor.putBoolean("app_status", false)
                editor.apply()
                startActivity(Intent(this@IntroActivity, HomeActivity::class.java))
            }
            position += 1
        }




    }
}