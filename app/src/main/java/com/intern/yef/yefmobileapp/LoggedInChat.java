package com.intern.yef.yefmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.intern.yef.yefmobileapp.Models.ChatMessage;
import com.intern.yef.yefmobileapp.Models.UserData;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoggedInChat extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseListAdapter<ChatMessage> adapter;
    private ListView listViewOfMessages;

    private String loggedInUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_chat);

        //messages listview
        listViewOfMessages = (ListView)findViewById(R.id.list_of_messages);

        firebaseAuth = FirebaseAuth.getInstance();
        final UserInfo currentUser = firebaseAuth.getCurrentUser();

        Log.d("activity", "In LoggedInChat Activity");

        //display messages
        displayChatMessages();

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("Chat with YEF");

        //On click listener for send message image button
        ImageButton sendMessageButton =
                (ImageButton)findViewById(R.id.sendMessageImagebutton);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.messageEditText);

                if(input.getText().toString().equals("")){
                    Toast.makeText(LoggedInChat.this, "You must enter a message", Toast.LENGTH_SHORT).show();
                }else {
                    //message is entered, send
                    //push a new instance
                    // of ChatMessage to the Firebase database
                    ChatMessage newMessage = new ChatMessage(input.getText().toString(),currentUser.getEmail(),currentUser.getUid());
                    FirebaseDatabase.getInstance()
                            .getReference().child("Messages")
                            .push()
                            .setValue(newMessage);

                    // Clear the input
                    input.setText("");
                }
                // Read the input field and
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_signout_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_signout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(LoggedInChat.this, Login.class));
                Toast.makeText(LoggedInChat.this,"Signed Out Successfully",Toast.LENGTH_SHORT).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void displayChatMessages(){

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference().child("Messages")) {



            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
//Set bigger size of yef team username
                if(model.getMessageUser()=="yefindia@gmail.com")
                {messageUser.setAllCaps(true);}

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listViewOfMessages.setAdapter(adapter);
    }

}


