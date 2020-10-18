package com.example.justplaytogether

import android.R
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.database.FirebaseListAdapter
import com.firebase.ui.database.FirebaseListOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase


class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.justplaytogether.R.layout.activity_chat)

        val fab =
            findViewById<View>(com.example.justplaytogether.R.id.fab2) as FloatingActionButton

        displayChatMessages()
        fab.setOnClickListener {
            val input = findViewById<View>(com.example.justplaytogether.R.id.input2)  as? TextView

            // Read the input field and push a new instance
            // of ChatMessage to the Firebase database
            if (input != null) {
               // println(input?.text.toString())
                FirebaseDatabase.getInstance()
                    .reference
                    .push()
                    .setValue(
                        ChatMessage(
                            input.text.toString(),"2"
                        )
                    )
            }

            // Clear the input
            if (input != null) {
                input.setText("")
            }
            displayChatMessages()

        }

    }

    private fun displayChatMessages() {

        val listOfMessages: ListView =
            findViewById<View>(com.example.justplaytogether.R.id.list_of_messages) as ListView

        val options: FirebaseListOptions<ChatMessage> = FirebaseListOptions.Builder<ChatMessage>()
            .setLayout(com.example.justplaytogether.R.layout.message)
            .setQuery(FirebaseDatabase.getInstance().reference, ChatMessage::class.java)
            .build()

         listOfMessages.adapter = object : FirebaseListAdapter<ChatMessage>(options
        ) {
            // Populate view as needed
            override fun populateView(
                v: View,
                model: ChatMessage,
                position: Int
            ) {
                // Get references to the views of message.xml
                val messageText =
                    v.findViewById<View>(com.example.justplaytogether.R.id.message_text) as TextView
                val messageUser =
                    v.findViewById<View>(com.example.justplaytogether.R.id.message_user) as TextView

                // Set their text
                messageText.text = model.messageText
                messageUser.text = model.messageUser
            }
        }
    }

}
