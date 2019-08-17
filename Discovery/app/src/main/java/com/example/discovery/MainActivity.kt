package com.example.discovery


import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception
import java.lang.StringBuilder


class MainActivity : AppCompatActivity() {


    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val txtView = findViewById<TextView>(R.id.textView)
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

        btnDisc.setOnClickListener {
            if (!adapter.isDiscovering) {
                startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE), 1)
                //Toast.makeText(applicationContext, "Discovering", Toast.LENGTH_SHORT).show()
            }

            val current = LocalDateTime.now().minusMonths(1)
            val formatter = DateTimeFormatter.ofPattern("d-M-yyyy")
            val formatted = current.format(formatter)
            val today = LocalDateTime.now().format(formatter)

            val link =
                "https://firestore.googleapis.com/v1/projects/attendancelistapp/databases/(default)/documents/attendance/$formatted/students/201513389"

            //Para probar que funciona el día que se tomó la asistencia
            //val link = "https://firestore.googleapis.com/v1/projects/attendancelistapp/databases/(default)/documents/attendance/5-7-2019/students/201513389"


            doAsync {
                var code:String = ""
                try {
                    val data = URL(link).readText()
                    val parser: Parser = Parser.default()
                    val stringBuilder: StringBuilder = StringBuilder(data)
                    val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                    code = "${json.string("name")}"
                } catch (e: Exception){
                    println("Not found")
                    code=""
                }
                uiThread {
                    if (code.contains("201513389"))
                        //Siempre se esta suponiendo que se ejecuta el mismo día que se llama a lista
                        txtView.setText("Attended on: $today")
                    if(code.equals(""))
                        txtView.setText("No results")
                }

            }
        }
    }
}
