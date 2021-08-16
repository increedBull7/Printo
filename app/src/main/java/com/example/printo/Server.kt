package com.example.printo

import android.util.Log
import java.net.ServerSocket
import kotlin.jvm.Volatile as Volatile1

class Server
{
    lateinit var serverThread : Thread
    lateinit var server : ServerSocket
    private val PORT = 5050
    @Volatile1 var isStart : Boolean = true

    fun start()
    {
        serverThread = Thread {
        server = ServerSocket(PORT)
            try
            {
                while (isStart)
                    Thread(ServerHandler(server.accept())).start()

            } catch (e: java.net.SocketException) { Log.w("fThread",e.toString())}
        }
        isStart = true
        serverThread.priority = Thread.MAX_PRIORITY
        serverThread.start()
    }
    fun stop()
    {
        try
        {
            isStart = false
            server.close()
        }
        catch (e : Exception) { Log.w("step",e.toString()) }
    }
}