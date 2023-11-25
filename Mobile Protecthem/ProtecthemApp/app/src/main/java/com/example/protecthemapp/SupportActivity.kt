package com.example.protecthemapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SupportActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)

        val button1 = findViewById<Button>(R.id.button_111)
        button1.setOnClickListener {
            openWebsite("http://www.tears.co.za")
        }

        val button2 = findViewById<Button>(R.id.button_112)
        button2.setOnClickListener {
            openWebsite("https://traumacentre.org.za/")
        }

        val button3 = findViewById<Button>(R.id.button_113)
        button3.setOnClickListener {
            openWebsite("https://rapecrisis.org.za/programmes/road-to-justice/thuthuzela-care-centres/")
        }

        val button4 = findViewById<Button>(R.id.button_114)
        button4.setOnClickListener {
            openWebsite("https://www.childwelfaresagauteng.org/")
        }
    }

    private fun openWebsite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}

