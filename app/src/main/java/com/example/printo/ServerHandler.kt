package com.example.printo
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.io.*
import java.net.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.math.abs
import java.io.FileOutputStream as FileOutputStream1


class ServerHandler(private val socket : Socket) : Runnable
{
    private  var PATH :String = ServerActivity.getIns().PATH
    private  var PATH_FOR_DATA = ServerActivity.getIns().PATH_FOR_DATA
    private lateinit var inputStream: InputStream
    private lateinit var outputStream: OutputStream
    private lateinit var bReader : BufferedReader
    private lateinit var sb : java.lang.StringBuilder
    private lateinit var tmp : String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun run()
    {
        try {
            //getting input/output stream from socket and buffer it
            inputStream = socket.getInputStream()
            bReader = BufferedReader(InputStreamReader(inputStream))
            sb = StringBuilder()

            //code for reading http request header
            while (true)
            {
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
            val url = firstline[1].replace("%20"," ")

            //server code for responding to POST request
            if (method == "POST")
            {
                val size = if(req[2].split(" ")[1].startsWith("Mozilla",true))
                {
                    req[6].split(" ")[1].toInt()
                } else {
                    req[3].split(" ")[1].toInt()
                }

                val file = File("$PATH_FOR_DATA/Printo$url")
                val fileOutput = FileOutputStream1(file)
                var i = 0
                val chunk = 1024*1024*5
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
                sendMassage("com",url)
                bReader.close()
            }

            //code for server response to GET http method
            else if(method == "GET")
            {
                if (url == "/")
                   sendFile("text/html","/index.html")
                else
                   sendFile(getMime(url),url)
            }
        }
        catch (e : Exception) { Log.w("REST",e.toString()) }
    }

    //routine for sending file over http
    private fun sendFile(type : String,file : String)
    {
        try
        {
            outputStream = socket.getOutputStream()
            outputStream.write("HTTP/1.1 200 OK\r\n".toByteArray())
            outputStream.write("Content-Type:$type\r\n".toByteArray())
            outputStream.write("\r\n".toByteArray())
            val fileInput = FileInputStream("$PATH/clientSide$file")
            val bfin = BufferedInputStream(fileInput)
            var read: Int
            val buf  =  ByteArray(1024)
            while (true) {
                read = bfin.read(buf)
                if (read == -1)
                    break
                outputStream.write(buf,0,read)
            }
            bfin.close()
            fileInput.close()
            outputStream.close()
        }
        catch (e : Exception) { Log.w("FILE_SENDING",e.toString()) }
        return
    }
    //routine for getting mime type
    @SuppressLint("NewApi")
    private fun getMime(file : String) : String
    {
        return when {
            file.endsWith(".js") -> "text/javascript"
            file.endsWith(".css") -> "text/css"
            else -> Files.probeContentType(Paths.get("$PATH/clientSide$file")).toString()
        }
    }
    private fun sendMassage(key : String, msg : String)
    {
        val intent = Intent("file_event")
        intent.putExtra(key,msg)
        LocalBroadcastManager.getInstance(ServerActivity.getIns()).sendBroadcast(intent)
    }
}