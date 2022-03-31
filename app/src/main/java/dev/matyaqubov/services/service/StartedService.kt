package dev.matyaqubov.services.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings
import android.widget.Toast

class StartedService : Service() {

    private var player: MediaPlayer? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Toast.makeText(this, "Started Service Created", Toast.LENGTH_SHORT).show()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)

        player!!.isLooping = true

        player!!.start()

        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        player!!.stop()
        Toast.makeText(this, "Started Service Stopped", Toast.LENGTH_SHORT).show()
    }

}