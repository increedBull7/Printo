package com.example.printo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class ServerActivity : AppCompatActivity() {

    private lateinit var qrCodeImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.server_screen)

        //this is qr code
        qrCodeImage = findViewById(R.id.imgbarcode)

        try {
            //place your text here @ text
            val text:String = "Badhiya app banra hai bawa"
            val qrCode  = BarcodeEncoder()
            val imgCode = qrCode.encodeBitmap(text, BarcodeFormat.QR_CODE,
                    500, 500)

            //setting the generated bitmap to imageview
            qrCodeImage.setImageBitmap(imgCode)

            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
