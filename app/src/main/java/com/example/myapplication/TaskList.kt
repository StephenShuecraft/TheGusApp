package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button

class TaskList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        val userName = intent.getStringExtra("USER_KEY")

        // Button to navigate to RobotPage
        val buttonToRobotPage = findViewById<Button>(R.id.button4)
        buttonToRobotPage.setOnClickListener {
            val intent = Intent(this, RobotPage::class.java).apply {putExtra("USER_KEY", userName)}
            startActivity(intent)
        }

        // Button to navigate to TaskForm
        val buttonToCreateTask = findViewById<Button>(R.id.CreateTaskBut)
        buttonToCreateTask.setOnClickListener {
            val intent = Intent(this, TaskForm::class.java).apply {putExtra("USER_KEY", userName)}
            startActivity(intent)
        }
    }
}