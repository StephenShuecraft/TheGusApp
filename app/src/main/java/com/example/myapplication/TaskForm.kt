package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class TaskForm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        // Initialize UI elements
        val editTextTask = findViewById<EditText>(R.id.editTextText2)
        val editTextTime = findViewById<EditText>(R.id.editTextTime)
        val editTextDate = findViewById<EditText>(R.id.editTextDate2)
        val buttonCreate = findViewById<Button>(R.id.button4)
        val userName = intent.getStringExtra("USER_KEY")

        // Initialize Firebase Database
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Tasks")

        buttonCreate.setOnClickListener {
            val task = editTextTask.text.toString().trim()
            val time = editTextTime.text.toString().trim()
            val date = editTextDate.text.toString().trim()
            val name = userName.toString().trim()

            if (task.isNotEmpty() && time.isNotEmpty() && date.isNotEmpty()) {
                // Create a unique ID for each task
                val taskId = ref.push().key

                taskId?.let {
                    val taskMap = HashMap<String, Any>()
                    taskMap["username"] = name
                    taskMap["task"] = task
                    taskMap["time"] = time
                    taskMap["date"] = date

                    // Save the task along with the username to Firebase
                    ref.child(it).setValue(taskMap).addOnCompleteListener { taskCreation ->
                        if (taskCreation.isSuccessful) {
                            Toast.makeText(this, "Task created successfully", Toast.LENGTH_SHORT).show()
                            // Close TaskForm and return to TaskList
                            val intent = Intent(this, TaskList::class.java).apply {putExtra("USER_KEY", userName)}
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Failed to create task", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please enter task, time, and date", Toast.LENGTH_SHORT).show()
            }
        }
    }
}