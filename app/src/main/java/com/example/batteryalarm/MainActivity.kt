package com.example.batteryalarm

import android.content.*
import android.media.MediaPlayer
import android.os.BatteryManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var batteryReceiver: BroadcastReceiver
    private var alarmTriggered = false
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound)

        batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: return

                if ((level < 30 || level > 70) && !alarmTriggered) {
                    mediaPlayer.start()
                    alarmTriggered = true
                } else if (level in 30..70) {
                    alarmTriggered = false
                }
            }
        }

        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
        mediaPlayer.release()
    }
}