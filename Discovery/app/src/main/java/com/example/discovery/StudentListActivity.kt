package com.example.discovery

import android.app.DatePickerDialog
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.net.URL
import java.util.*

class StudentListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)

        val btnDate = findViewById<Button>(R.id.buttonDate)
        val textViewDate = findViewById<TextView>(R.id.textViewDate)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

         btnDate.setOnClickListener{
             val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ view, nyear, nmonth, ndayOfMonth ->
                 //Log.d("ESTA ES LA URL","https://firestore.googleapis.com/v1/projects/attendancelistapp/databases/(default)/documents/attendance/"+ndayOfMonth + "-" + nmonth + "-" + nyear+"/students?pageSize=50")
                 getJsonURL("https://firestore.googleapis.com/v1/projects/attendancelistapp/databases/(default)/documents/attendance/"+ndayOfMonth + "-" + nmonth + "-" + nyear+"/students?pageSize=50")
                 var monthView = month+1
                 textViewDate.setText(""+ ndayOfMonth + "/" + monthView + "/" + nyear)
             }, year, month, day)

             dpd.show()
         }
    }

    internal inner class compareStudents : AsyncTask<Void, Void, String> (){
        override fun onPreExecute() {
            super.onPreExecute()
        }
        override fun doInBackground(vararg params: Void?): String {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
        }

    }

    private fun getJsonURL(url: String): String {
        return URL(url).readText()

    }
}
