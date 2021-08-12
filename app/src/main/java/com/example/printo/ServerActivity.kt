package com.example.printo

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.io.File
import java.net.NetworkInterface

class ServerActivity : AppCompatActivity()
{

    private lateinit var serverInt : Intent
    private lateinit var qrCodeImage: ImageView
    private lateinit var textMsg : TextView

    //great suggestion bro @atul

    lateinit var PATH : String
     var PATH_FOR_DATA : String = "/sdcard"

        companion object
        {
            @JvmStatic
            lateinit var ins : ServerActivity
            @JvmName("getIns1")
            fun getIns() : ServerActivity
            {
                return ins
            }
        }

    override fun onCreate(savedInstanceState: Bundle?)
        {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.server_screen)
        textMsg = findViewById(R.id.textView)
        ins = this
        PATH = this.externalCacheDir!!.absolutePath.toString()
        //PATH_FOR_DATA  = PATH.removeSuffix("/Android/data/com.example.printo/cache")
        writeResource()
        createDir()
        serverInt = Intent(this,ServerService::class.java)
        startService(serverInt)
        qrCode()
        }
    //routine for generating qr code
    @SuppressLint("SetTextI18n")
    private fun qrCode()
        {
            //this is qr code
            qrCodeImage = findViewById(R.id.imgbarcode)
            textMsg.text = "OR visit : http://${getIp()}:5050 on your browser!"
            try {
                //place your text here @ text
                val text = getIp()
                val qrCode  = BarcodeEncoder()
                val imgCode = qrCode.encodeBitmap(text, BarcodeFormat.QR_CODE,
                    500, 500)

                //setting the generated bitmap to imageview
                qrCodeImage.setImageBitmap(imgCode)

            } catch (e: Exception){
                e.printStackTrace()
            }
        }

    //routine for writing assets to local folder
    private fun writeResource()
    {
        val path = "clientSide"
        val assetManager = this.assets
        lateinit var assetsList : Array<String>
        try
        {
            assetsList = assetManager.list(path) as Array<String>

            if(assetsList.isEmpty())
            {
                Toast.makeText(this,"empty", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val filepath = "$PATH/$path"
                val dir = File(filepath)
                if (!dir.exists() && !path.startsWith("images")&&!path.startsWith("sounds")&&!path.startsWith("webkit"))
                    if(!dir.mkdir()) { }
                for (item in assetsList)
                {
                    val p : String = if(path == "")
                        ""
                    else
                        "$path/"

                    if(!path.startsWith("images") && !path.startsWith("sounds") && !path.startsWith("webkit"))
                        copy(p + item)
                }
            }
        }
        catch (e : java.lang.Exception) {  }
    }

    //routine module of writeResource()
    private fun copy(filename: String)
    {
        val assetManager = this.assets
        val inputStream : InputStream
        val outputStream : OutputStream
        val newFileName : String
        try
        {
            inputStream = assetManager.open(filename)
            newFileName = if (filename.endsWith(".jpg"))
                PATH + "/" + filename.substring(0,filename.length - 4)
            else
                "$PATH/$filename"
            outputStream = FileOutputStream(newFileName)
            val buf = ByteArray(1024)
            var read : Int
            while(true)
            {
                read = inputStream.read(buf)
                if(read ==-1)
                    break
                outputStream.write(buf,0,read)
            }
            inputStream.close()
            outputStream.flush()
            outputStream.close()

        }
        catch (e : java.lang.Exception){  }
    }

    //createDir where received file will store
    private fun createDir()
    {
        val dir = File("$PATH_FOR_DATA/Printo")
        if(!dir.exists())
            dir.mkdir()
    }

    //getting AP ip address
    private fun getIp() : String {
        var ip = ""
        try {
            val enumNet = NetworkInterface.getNetworkInterfaces()
            while (enumNet.hasMoreElements()) {
                val networkInterface = enumNet.nextElement()
                val enumInet = networkInterface.inetAddresses
                while (enumInet.hasMoreElements())
                {
                    val inetAddress = enumInet.nextElement()
                    if (inetAddress.isSiteLocalAddress && (networkInterface.name.lowercase()
                            .contains("wlan")) || networkInterface.name.lowercase().contains("ap")
                    )
                        ip = inetAddress.hostAddress as String
                }
            }
        }
        catch (e : Exception) { }
        return ip
    }

    override fun onStart()
    {
        super.onStart()

    }
    //handle back key press event
    override fun onBackPressed()
    {
        super.onBackPressed()
        stopService(serverInt)
    }
}
