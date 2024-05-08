package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class RobotPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_robot_page)

        val userName = intent.getStringExtra("USER_KEY")
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("userData")
        // Assuming this code is inside an Activity
        // Assuming this code is inside an Activity





        // Set up Firebase listeners to handle user data
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var found = false
                for (userSnapshot in dataSnapshot.children) {
                    val name = userSnapshot.child("name").getValue(String::class.java)
                    if (name == userName) {
                        found = true
                        val option = userSnapshot.child("option").getValue(String::class.java)
                        initializeViews(option, true) // Assuming taskIncomplete is true for demonstration
                        break // Exit loop once the desired user is found
                    }
                }
                if (!found) {
                    Log.d("Database", "No matching documents for user Stephen.")
                    initializeViews(null, false) // Default to no mood if no document found
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Database", "Failed to read value.", databaseError.toException())
                initializeViews(null, false) // Default to no mood on failure
            }
        })

        setupTaskButton()
    }

    private fun initializeViews(option: String?, taskIncomplete: Boolean) {
        val nor: ImageView
        val norTalk: ImageView
        val gusTalking = findViewById<TextView>(R.id.gusTalking)

        // Determine the mood based on taskIncomplete and option
        if (taskIncomplete && option == "Scolding") {
            nor = findViewById<ImageView>(R.id.madHigh)
            norTalk = findViewById<ImageView>(R.id.madHighTalk)
        } else if (taskIncomplete && option == "Guilt Trip") {
            nor = findViewById<ImageView>(R.id.sadHigh)
            norTalk = findViewById<ImageView>(R.id.sadHighTalk)
        } else {
            nor = findViewById<ImageView>(R.id.norHigh)
            norTalk = findViewById<ImageView>(R.id.norHighTalk)
        }
        nor.visibility = View.VISIBLE

        setUpButtonActions(nor, norTalk, gusTalking, if (taskIncomplete && option == "Scolding") roasts else if (taskIncomplete && option == "Guilt Trip") guiltTrips else defaultPhrases)
    }

    private fun setUpButtonActions(nor: ImageView, norTalk: ImageView, gusTalking: TextView, messages: Array<String>) {
        val talkGus = findViewById<Button>(R.id.talkButton)
        talkGus.setOnClickListener {
            var count = 0
            object : CountDownTimer(1300, 1000) {
                var isVisible = false

                override fun onTick(millisUntilFinished: Long) {
                    if (isVisible) {
                        norTalk.visibility = View.INVISIBLE
                        nor.visibility = View.VISIBLE
                        isVisible = false
                    } else {
                        nor.visibility = View.INVISIBLE
                        norTalk.visibility = View.VISIBLE
                        isVisible = true
                    }
                }

                override fun onFinish() {
                    norTalk.visibility = View.INVISIBLE
                    nor.visibility = View.VISIBLE
                    count++
                    if (count < 8) {
                        this.start()
                    }
                }
            }.start()

            val randomMessage = messages.random()
            gusTalking.text = randomMessage
        }
    }

    private fun setupTaskButton() {
        val taskButton = findViewById<Button>(R.id.taskView)
        taskButton.setOnClickListener {
            val intent = Intent(this, TaskList::class.java)
            startActivity(intent)
        }
    }

    companion object {
        val roasts = arrayOf(
            "Looks like your task completion is as empty as your promises",
            "You’re setting a world record for the longest delay on a simple task",
            "You move slower than a snail on vacation. What’s the hold-up?",
            "At this pace, we’ll finish next century. Speed it up!",
            "Did you need a written invitation to finish that task?",
            "Your timeline is more fictional than a fairytale",
            "Task incomplete again? You’re consistent at least",
            "Is ‘later’ finally here, or are you setting a new date?",
            "If procrastination were a sport, you’d be an Olympic champion.",
            "I've seen better commitment from a placeholder"
        )

        val defaultPhrases = arrayOf(
            "Hello, my name is GUS!",
            "How are you today?",
            "It’s nice to see you",
            "What Task are you doing today?",
            "Hope you're having a great day!",
            "I know you can do the task!",
            "Thank you for your time that you spend on your tasks!",
            "Have a great day!",
            "If you need anything, just ask I can't really help but I will try",
            "I'm here to motivate you!"
        )

        val guiltTrips = arrayOf(
            "I guess we know who doesn't follow through on their promises",
            "Everyone else managed to find the time. Just saying",
            "I hate to think you don't care, but it's getting harder not to",
            "It's fine, I didn’t expect much anyway",
            "Sure, take your time. It’s not like anyone else is depending on you",
            "Well, if it was important to you, you’d have done it by now",
            "Oh, I'll just do it myself. Seems like I'm the only one who cares",
            "You always say you'll do better next time. I'm still waiting for that time",
            "It must be nice not having to worry about letting people down",
            "Don’t worry about us, we’ll manage somehow. We always do"
        )
    }
}
