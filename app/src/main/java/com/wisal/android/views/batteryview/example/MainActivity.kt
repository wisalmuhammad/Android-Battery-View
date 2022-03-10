package com.wisal.android.views.batteryview.example

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.wisal.android.views.batteryview.BatteryView


class MainActivity : AppCompatActivity() {

    private lateinit var batteryView: BatteryView
    private lateinit var chargingView: RadioButton
    private lateinit var noChargingView: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        batteryView = findViewById(R.id.battery_view)
        val seekbar = findViewById<SeekBar>(R.id.seek_bar)
        chargingView = findViewById(R.id.charging)
        noChargingView = findViewById(R.id.no_charging)

        seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                batteryView.batteryLevel = progress
            }
        })
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked
            val chargingView = findViewById<RadioButton>(R.id.charging)
            val noChargingView = findViewById<RadioButton>(R.id.no_charging)

            when (view.getId()) {
                R.id.charging ->
                    if (checked) {
                        batteryView.isCharging = checked
                        noChargingView.isChecked = !checked
                    }
                R.id.no_charging ->
                    if (checked) {
                        batteryView.isCharging = !checked
                        chargingView.isChecked = !checked
                    }
            }
        }
    }

}