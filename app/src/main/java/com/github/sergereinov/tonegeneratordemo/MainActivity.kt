package com.github.sergereinov.tonegeneratordemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.media.ToneGenerator
import android.widget.AdapterView.OnItemClickListener
import android.media.AudioManager
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import java.lang.Exception
import java.lang.reflect.Field


class MainActivity : AppCompatActivity() {

    private val TAG = "ToneGeneratorDemo"
    lateinit var generator: ToneGenerator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        generator = ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME)

        val durationText: TextView = findViewById(R.id.durationText)
        val durationSeekBar: SeekBar = findViewById(R.id.durationSeekBar)
        durationSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                durationText.text = "$progress ms"
            }
        })
        durationSeekBar.progress = 500

        val itemsList: ListView = findViewById<View>(R.id.itemsList) as ListView
        val tones = ToneGenerator::class.java.declaredFields
            .filter {
                it.type === Integer.TYPE &&
                        it.name != "MAX_VOLUME" &&
                        it.name != "MIN_VOLUME" &&
                        it.name != "TONE_UNKNOWN"
            }
            .map { it.name }

        itemsList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tones)
        itemsList.onItemClickListener = OnItemClickListener { av, _, i, _ ->
            val item = av.getItemAtPosition(i)
            try {
                val tone: Field = ToneGenerator::class.java.getField(item.toString())
                generator.startTone(tone.getInt(null), durationSeekBar.progress)
            } catch (ex: Exception) {
                Log.e(TAG, ex.toString())
            }
        }

        val stopButton: Button = findViewById(R.id.stopButton)
        stopButton.setOnClickListener { generator.stopTone() }
    }

    override fun onPause() {
        super.onPause()
        generator.stopTone()
    }

    override fun onDestroy() {
        super.onDestroy()
        generator.release()
    }
}
