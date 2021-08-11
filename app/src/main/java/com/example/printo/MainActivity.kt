package com.example.printo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.ComponentName




//https://www.freepik.com/vectors/business' Business vector created by catalyststuff - www.freepik.com


class MainActivity : AppCompatActivity() {
    lateinit var btn : Button
    lateinit var hotspotButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //this is for hotspot button
        var isHotspotOn: Boolean = false
        hotspotButton = findViewById(R.id.hotspot)
        hotspotButton.setOnClickListener(){
            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val cn = ComponentName("com.android.settings", "com.android.settings.TetherSettings")
            intent.component = cn
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            isHotspotOn = true
        }



        //for server button
        btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener()
        {
            if(isHotspotOn){
            val intent = Intent(this,ServerActivity::class.java)
            startActivity(intent)
            }
        }





    }
}



