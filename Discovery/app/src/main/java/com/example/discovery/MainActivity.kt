package com.example.discovery


import android.app.Dialog
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception
import java.lang.StringBuilder
import java.net.NetworkInterface
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btnOn = findViewById<Button>(R.id.buttonOnOff)
        val btnDisc = findViewById<Button>(R.id.buttonDisc)
        val btnList = findViewById<Button>(R.id.buttonList)
        val txtViewResult = findViewById<TextView>(R.id.textViewExist)
        val adapter = BluetoothAdapter.getDefaultAdapter()

        context = this

        btnList.setOnClickListener {
            val activityIntent = Intent(this, StudentListActivity::class.java)
            startActivity(activityIntent)

        }

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

            getStudentList().execute()
        }
    }


    internal inner class getStudentList : AsyncTask<Void, Void, String>() {
        lateinit var progressDialog: Dialog
        var internet = false

        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog(context)
            progressDialog.setCancelable(false)
            progressDialog.setTitle("Searching MAC on today's attendace")
            progressDialog.show()

        }

        override fun doInBackground(vararg params: Void?): String {
            if (isNetworkAvailable()) {
                internet = true
                val client = OkHttpClient()
                val current = LocalDateTime.now().minusMonths(1)
                val formatter = DateTimeFormatter.ofPattern("d-M-yyyy")
                val formatted = current.format(formatter)
                val today = LocalDateTime.now().format(formatter)

                //val link = "https://firestore.googleapis.com/v1/projects/attendancelistapp/databases/(default)/documents/attendance/5-7-2019/students/201513389"
                val link = "https://firestore.googleapis.com/v1/projects/attendancelistapp/databases/(default)/documents/attendance/$formatted/students/201513389"

                val request = Request.Builder().url(link).build()
                val response = client.newCall(request).execute()
                return response.body?.string().toString()


            } else {
                return ""
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            progressDialog.dismiss()

            //textViewExist.text = result.toString()


            if (result.toString().contains("F4:60:E2:F3:C1:6F")) {
                //Siempre se esta suponiendo que se ejecuta el mismo día que se llama a lista
                textViewExist.text = "The MAC F4:60:E2:F3:C1:6F attended today"
            } else {
                textViewExist.text ="No results"
            }

        }

        private fun isNetworkAvailable(): Boolean {
            val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    }
}
