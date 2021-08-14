package com.example.printo
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.*
import java.net.*
import java.util.*
import kotlin.math.abs
import java.io.FileOutputStream as FileOutputStream1


class ServerHandler(private val socket : Socket) : Runnable
{
    private  var PATH :String = ServerActivity.getIns().PATH
    private  var PATH_FOR_DATA = ServerActivity.getIns().PATH_FOR_DATA

    @RequiresApi(Build.VERSION_CODES.O)
    override fun run() {
        try {
            //getting input/output stream from socket and buffer it
            val inputStream = socket.getInputStream()
            val outputStream = socket.getOutputStream()
            val bReader = BufferedReader(InputStreamReader(inputStream))
            val sb = StringBuilder()
            var tmp: String

            //code for reading http request header
            while (true) {
                tmp = bReader.readLine()
                if (tmp.isEmpty())
                    break
                sb.append(tmp + "\r\n")
            }
            //getting method and url form http msg
            val httpMsg: String = sb.toString()
            val req = httpMsg.split("\r\n")
            val firstline = req[0].split(" ")
            val method = firstline[0]
            val url = firstline[1]

            //server code for responding to POST request
            if (method == "POST")
            {
                val size = req[3].split(" ")[1].toInt()
                val file = File("$PATH_FOR_DATA/Printo$url")
                val fileOutput = FileOutputStream1(file)
                var i = 0
                val chunk = 1024*1024
                var buffer : ByteArray
                lateinit var data : ByteArray
                //receiving and decoding base 64 data sent by browser
                while(true)
                {
                    if(i==size)
                        break

                    var j = 0
                    val resize = size - i
                    buffer = if(resize > chunk) ByteArray(chunk) else ByteArray(abs(resize))
                    while(true)
                    {
                        if (j == buffer.size)
                            break

                        buffer[j] = bReader.read().toByte()
                        j++
                        i++
                    }
                    data = Base64.getDecoder().decode(buffer)
                    fileOutput.write(data,0,data.size)
                }
                fileOutput.flush()
                fileOutput.close()
            }
            //code for server response to GET http method
            else if(method == "GET")
            {
                if (url == "/")
                   sendFile(outputStream,"text/html","/index.html")
               else
                   sendFile(outputStream,"application/octet-stream",url)
            }
            outputStream.flush()
            outputStream.close()
            inputStream.close()
            socket.close()
        }
        catch (e : Exception) { Log.w("REST",e.toString()) }
    }

    //routine for sending file over http
    private fun sendFile(outputStream: OutputStream,type : String,file : String)
    {
        try
        {
            outputStream.write("HTTP/1.1 200 OK\r\n".toByteArray())
            outputStream.write("Content-Type:$type\r\n".toByteArray())
            outputStream.write("\r\n\r\n".toByteArray())
            val fileInput = FileInputStream("$PATH/clientSide$file")
            var read: Int
            while (true) {
                read = fileInput.read()
                if (read == -1)
                    break
                outputStream.write(read)
            }
        }
        catch (e : Exception) { Log.w("FILE_SENDING",e.toString()) }
        return
    }
}