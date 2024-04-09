package com.example.a3and

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.widget.Button

class NoInternetActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nointernet)
        val retryButton = findViewById<Button>(R.id.retryButton)
        retryButton.setOnClickListener {
            val intent = Intent(this,LoadingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}