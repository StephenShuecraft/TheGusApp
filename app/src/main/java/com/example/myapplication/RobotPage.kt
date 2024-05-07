package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageView

class RobotPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_robot_page)

        val norHigh = findViewById<ImageView>(R.id.norHigh)
        val norHighTalk = findViewById<ImageView>(R.id.norHighTalk)

        var talkGus = findViewById<Button>(R.id.talkButton)
        talkGus.setOnClickListener {
            // Start a repeating timer that handles the visibility change
            object : CountDownTimer(1300, 1000) {  // total 8000ms, interval 2000ms
                var isVisible = false
                var count = 0

                override fun onTick(millisUntilFinished: Long) {
                    if (isVisible) {
                        // Switch to normal view
                        norHighTalk.visibility = View.INVISIBLE
                        norHigh.visibility = View.VISIBLE
                        isVisible = false
                    } else {
                        // Switch to talking view
                        norHigh.visibility = View.INVISIBLE
                        norHighTalk.visibility = View.VISIBLE
                        isVisible = true
                    }
                    count++
                }

                override fun onFinish() {
                    // Ensure the original state is restored at the end
                    norHighTalk.visibility = View.INVISIBLE
                    norHigh.visibility = View.VISIBLE
                    if (count < 8) { // Ensure it runs more than once
                        this.start() // Restart the timer to complete remaining cycles
                    }
                }
            }.start()
        }
    }
}