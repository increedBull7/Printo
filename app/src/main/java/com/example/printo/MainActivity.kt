package com.example.printo

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.Exception

//https://www.freepik.com/vectors/business' Business vector created by catalyststuff - www.freepik.com


class MainActivity : AppCompatActivity() {
    private lateinit var btn : Button
    private lateinit var switch : SwitchCompat
    private val READ = 100
    private val WRITE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn = findViewById(R.id.button)
        switch = findViewById(R.id.switch_one)
        switch.isChecked = isApOn()
        btn.isEnabled = isApOn()

        switch.setOnCheckedChangeListener()
        {
            switch,isChecked->
            run {
                if(isChecked)
                {
                    if (isApOn())
                        switch.isChecked = true
                    else
                    {
                        //yha per hotspot on kerne ke code pelo
                        switch.isChecked = true
                    }
                }
                else
                {

                //yha per hotspot off kerne ke code pele
                }
                btn.isEnabled = isApOn()
            }
        }
        // checking for required permission
        btn.setOnClickListener()
        {
           val read = checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE,READ)
           val write = checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE)
           if(read && write)
           {
               val intent = Intent(this,ServerActivity::class.java)
               startActivity(intent)
           }
        }
    }
    //code related to above onClick
    private fun checkPermission(permission:String ,requestCode:Int) :Boolean
    {
        return if(ContextCompat.checkSelfPermission(this@MainActivity, permission)==PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission),requestCode)
            false
        }
        else
        {
            true
        }

    }

    //code to be execute when permission denied
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == READ )
        {
            if(grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_DENIED)
            {
                Toast.makeText(this@MainActivity,"permission not granted", Toast.LENGTH_SHORT).show()
            }
        }
        else if(requestCode == WRITE)
        {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED)
            {
                Toast.makeText(this@MainActivity,"permission not granted",Toast.LENGTH_SHORT).show()
            }
        }
    }
    //routine for detecting hotspot is on
    private fun isApOn() : Boolean
    {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        try
        {
            val method = wifiManager.javaClass.getDeclaredMethod("isWifiApEnabled")
            method.isAccessible = true
            return method.invoke(wifiManager) as Boolean
        }
        catch (e : Exception) { }
        return false
    }
}



