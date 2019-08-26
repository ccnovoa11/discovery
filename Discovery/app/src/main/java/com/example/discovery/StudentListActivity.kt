package com.example.discovery

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StudentListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)

        val btnDate = findViewById<Button>(R.id.buttonDate)
    }
}
