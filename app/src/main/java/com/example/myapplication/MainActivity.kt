package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseReference


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val USER = findViewById<EditText>(R.id.USER)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val btnNext = findViewById<Button>(R.id.Submit)

        btnNext.setOnClickListener {
            val userName = USER.text.toString()
            val selectedRadioButtonId = radioGroup.checkedRadioButtonId
            // Validate inputs
            if (userName.isEmpty() || selectedRadioButtonId == -1) {
                Toast.makeText(this, "Both fields required", Toast.LENGTH_SHORT).show()
            } else {
                // Initialize Firebase Database
                val database = FirebaseDatabase.getInstance()
                val ref = database.getReference("userData")

                // Query Firebase to see if username already exists
                ref.orderByChild("name").equalTo(userName)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                // Username already exists
                                Toast.makeText(this@MainActivity, "Logging in", Toast.LENGTH_SHORT)
                                    .show()
                                // Corrected Intent context
                                val intent = Intent(this@MainActivity, RobotPage::class.java)
                                startActivity(intent)
                            } else {
                                // Username does not exist, proceed to create user
                                createUser(userName, ref, selectedRadioButtonId)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@MainActivity,
                                "Failed to check username: ${error.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }
    }

    private fun createUser(userName: String, ref: DatabaseReference, selectedRadioButtonId: Int) {
        val radioButton = findViewById<RadioButton>(selectedRadioButtonId)
        val option = radioButton.text.toString()
        val userId = ref.push().key
        val user = User(userName, option)
        userId?.let {
            ref.child(it).setValue(user).addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(this@MainActivity, RobotPage::class.java).apply {
                        putExtra("USER_KEY", userName)}
                    startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "Failed to send data", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
data class User(val name: String, val option: String)
