package com.example.printo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var btn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener()
        {
            val intent = Intent(this,ServerActivity::class.java)
            startActivity(intent)
        }
    }
}



