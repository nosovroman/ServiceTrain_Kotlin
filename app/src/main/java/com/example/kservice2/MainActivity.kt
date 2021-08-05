package com.example.kservice2


import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.kservice2.MyService.Companion.time
import java.security.Provider

class MainActivity : AppCompatActivity() {

    private lateinit var startButton : Button
    private lateinit var stopButton : Button
    private lateinit var counterTV : TextView

    private val CHANNEL_ID = "channelID"
    private var notificationId = 101

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initElements()

        val intent = Intent(this, MyService::class.java)

        //createNotificationChannel()

        startButton.setOnClickListener{
            startService(intent)
            time = 1
        }

        stopButton.setOnClickListener{
            stopService(intent)
            time = 0
        }

    }

    private fun initElements() {
        startButton = findViewById(R.id.startBtn)
        stopButton = findViewById(R.id.endBtn)
        counterTV = findViewById(R.id.counter)
    }

    private fun sendNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Уведомление таймера")
            .setContentText("Время пришло!")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_MAX);
        NotificationManagerCompat.from(this).notify(notificationId, builder.build())
        notificationId++
    }
}