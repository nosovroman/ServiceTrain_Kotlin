package com.example.kservice2

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MyService : Service() {

    private val CHANNEL_ID = "channelID"
    private var notificationId = 101

    companion object {
        var firstEnter = true
        var time = 10
    }

    private lateinit var mediaPlayer : MediaPlayer
//    private val CHANNEL_ID = "channelID"
//    private var notificationId = 101

    override fun onBind(intent: Intent): IBinder? {
        //return null
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(applicationContext, "Служба создана", Toast.LENGTH_SHORT).show()
        mediaPlayer = MediaPlayer.create(this, R.raw.skrillex)
        mediaPlayer.isLooping = false;
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        onTaskRemoved(intent!!)
        Toast.makeText(applicationContext, "Служба запущена", Toast.LENGTH_SHORT).show()
        mediaPlayer.start()

        if (firstEnter) {
            firstEnter = false
            Log.i("f", "запущено")
            var runnable = Runnable {
                for (i in 1..5) {
                    Thread.sleep(3000)
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

            var thread = Thread(runnable)
            thread.start()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(applicationContext, "Служба остановлена", Toast.LENGTH_SHORT).show()
        mediaPlayer.stop()
        stopSelf()
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        if (time != 0) {
            Toast.makeText(applicationContext, "Перезапуск", Toast.LENGTH_SHORT).show()
            val restartServiceIntent = Intent(applicationContext, this.javaClass)
            restartServiceIntent.setPackage(packageName)
            startService(restartServiceIntent)
            super.onTaskRemoved(rootIntent)
        } else {
            Toast.makeText(applicationContext, "Перезапуска не будет", Toast.LENGTH_SHORT).show()
        }
    }
}