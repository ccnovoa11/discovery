package com.example.discovery

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnOn = findViewById<Button>(R.id.buttonOnOff)
        val btnDisc = findViewById<Button>(R.id.buttonDisc)
        val adapter = BluetoothAdapter.getDefaultAdapter()

        btnOn.setOnClickListener {
            if (!adapter.isEnabled) {
                startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1)
                Toast.makeText(applicationContext, "Turned on", Toast.LENGTH_SHORT).show()
            } else {
                adapter.disable()
                Toast.makeText(applicationContext, "Turned off", Toast.LENGTH_SHORT).show()
            }
        }

        btnDisc.setOnClickListener{
            if (!adapter.isDiscovering){
                startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE), 1)
                Toast.makeText(applicationContext, "Discovering", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
