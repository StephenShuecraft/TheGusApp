package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.content.Intent

class RobotPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_robot_page)

        // takes user to task list on Task Button press
        val taskButton = findViewById<Button>(R.id.taskView)
        taskButton.setOnClickListener {
            // Create an Intent to start TaskListActivity
            val intent = Intent(this, TaskList::class.java)
            startActivity(intent)
        }

        var isMad: Boolean = false
        var isSad: Boolean = true

        // Initialize variables with nullable ImageView and ensure they are not null later
        var nor: ImageView? = null
        var norTalk: ImageView? = null

        if (isMad) {
            nor = findViewById<ImageView>(R.id.madHigh)
            norTalk = findViewById<ImageView>(R.id.madHighTalk)
            nor.visibility = View.VISIBLE
        } else if (isSad) {
            nor = findViewById<ImageView>(R.id.sadHigh)
            norTalk = findViewById<ImageView>(R.id.sadHighTalk)
            nor.visibility = View.VISIBLE
        } else {
            nor = findViewById<ImageView>(R.id.norHigh)
            norTalk = findViewById<ImageView>(R.id.norHighTalk)
            nor.visibility = View.VISIBLE
        }

        val talkGus = findViewById<Button>(R.id.talkButton)
        talkGus.setOnClickListener {
            // Start a repeating timer that handles the visibility change
            object : CountDownTimer(1300, 1000) {  // Total 1300ms, interval 1000ms
                var isVisible = false
                var count = 0

                override fun onTick(millisUntilFinished: Long) {
                    nor?.let { imageView ->
                        norTalk?.let { talkView ->
                            if (isVisible) {
                                // Switch to normal view
                                talkView.visibility = View.INVISIBLE
                                imageView.visibility = View.VISIBLE
                                isVisible = false
                            } else {
                                // Switch to talking view
                                imageView.visibility = View.INVISIBLE
                                talkView.visibility = View.VISIBLE
                                isVisible = true
                            }
                        }
                    }
                    count++
                }

                override fun onFinish() {
                    // Ensure the original state is restored at the end
                    norTalk?.visibility = View.INVISIBLE
                    nor?.visibility = View.VISIBLE
                    if (count < 8) { // Ensure it runs more than once
                        this.start() // Restart the timer to complete remaining cycles
                    }
                }
            }.start()
        }
    }
}