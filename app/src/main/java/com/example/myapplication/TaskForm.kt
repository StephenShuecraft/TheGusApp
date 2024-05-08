package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class TaskForm : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        val editTextTask = findViewById<EditText>(R.id.editTextText2)
        val editTextTime = findViewById<EditText>(R.id.editTextTime)
        val editTextDate = findViewById<EditText>(R.id.editTextDate2)
        val buttonCreate = findViewById<Button>(R.id.button4)
        val userName = intent.getStringExtra("USER_KEY")

        val calendar = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            editTextTime.setText(java.text.SimpleDateFormat("HH:mm").format(calendar.time))
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            editTextDate.setText(java.text.SimpleDateFormat("MM-dd-yyyy").format(calendar.time))
        }

        editTextTime.setOnClickListener {
            TimePickerDialog(this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        editTextDate.setOnClickListener {
            DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("Tasks")

        buttonCreate.setOnClickListener {
            val task = editTextTask.text.toString().trim()
            val time = editTextTime.text.toString().trim()
            val date = editTextDate.text.toString().trim()
            val name = userName.toString().trim()

            if (task.isNotEmpty() && time.isNotEmpty() && date.isNotEmpty()) {
                val taskId = ref.push().key
                taskId?.let {
                    val taskMap = HashMap<String, Any>()
                    taskMap["username"] = name
                    taskMap["task"] = task
                    taskMap["time"] = time
                    taskMap["date"] = date

                    ref.child(it).setValue(taskMap).addOnCompleteListener { taskCreation ->
                        if (taskCreation.isSuccessful) {
                            Toast.makeText(this, "Task created successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, TaskList::class.java).apply { putExtra("USER_KEY", userName) }
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Failed to create task", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
