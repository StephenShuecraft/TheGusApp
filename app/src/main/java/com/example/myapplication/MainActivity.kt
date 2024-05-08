package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initializing database
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("userData")

        val USER = findViewById<EditText>(R.id.USER)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val btnNext = findViewById<Button>(R.id.Submit)

        btnNext.setOnClickListener {
            val userName = USER.text.toString()
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            // Validate inputs
            if (userName.isEmpty() || selectedRadioButtonId == -1) {
                // Show toast message when any field is empty
                Toast.makeText(this, "Data is required", Toast.LENGTH_SHORT).show()
            } else {
                // Handle the selection of radio button
                val radioButton = findViewById<RadioButton>(selectedRadioButtonId)
                val option = radioButton.text.toString()

                // Initialize Firebase Database
                val database = FirebaseDatabase.getInstance()
                val ref = database.getReference("userData")
                val userId = ref.push().key

                val user = User(userName, option)
                userId?.let {
                    ref.child(it).setValue(user).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, RobotPage::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Failed to send data", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

data class User(val name: String, val option: String)