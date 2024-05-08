package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.*

class TaskList : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        val userName = intent.getStringExtra("USER_KEY") ?: "Unknown"  // Default to "Unknown" if null

        // TextView to display tasks
        val tasksView = findViewById<TextView>(R.id.description)  // Make sure you have a TextView with this ID in your layout

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("Tasks")

        // Query to fetch tasks by username
        database.orderByChild("username").equalTo(userName)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val stringBuilder = StringBuilder()
                    for (snapshot in dataSnapshot.children) {
                        val task = snapshot.child("task").getValue(String::class.java) ?: "No Task"
                        val time = snapshot.child("time").getValue(String::class.java) ?: "No Time"
                        val date = snapshot.child("date").getValue(String::class.java) ?: "No Date"
                        stringBuilder.append("Task: $task, Time: $time, Date: $date\n")
                    }
                    tasksView.text = stringBuilder.toString()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    tasksView.text = "Failed to load tasks: ${databaseError.message}"
                }
            })

        // Button to navigate to RobotPage
        val buttonToRobotPage = findViewById<Button>(R.id.button4)
        buttonToRobotPage.setOnClickListener {
            val intent = Intent(this, RobotPage::class.java).apply { putExtra("USER_KEY", userName) }
            startActivity(intent)
        }

        // Button to navigate to TaskForm
        val buttonToCreateTask = findViewById<Button>(R.id.CreateTaskBut)
        buttonToCreateTask.setOnClickListener {
            val intent = Intent(this, TaskForm::class.java).apply { putExtra("USER_KEY", userName) }
            startActivity(intent)
        }
    }
}