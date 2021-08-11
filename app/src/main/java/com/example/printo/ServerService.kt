package com.example.printo

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ServerService : Service()
{
    lateinit var server : Server
    override fun onBind(intent: Intent): IBinder
    {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
        try
        {
            server = Server()
            server.start()
        }
        catch (e : Exception) { }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy()
    {
        super.onDestroy()
        try { server.stop() } catch (e : Exception) { }
    }
}