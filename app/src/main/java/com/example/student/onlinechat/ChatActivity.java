package com.example.student.onlinechat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabaseReference;
    private ChatListAdapter mAdapter;
    private EditText mEditMessage;
    private ListView mListMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mEditMessage = findViewById(R.id.edit_message);
        mListMessages = findViewById(R.id.list_messages);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user==null){
                    startActivity(new Intent(ChatActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(ChatActivity.this,user.getEmail(),
                            Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        mAdapter = new ChatListAdapter(this, mDatabaseReference, currentUser.getEmail());
        mListMessages.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener!=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
        mAdapter.cleanup();
    }

    public void sendMessage(View view){
        String input = mEditMessage.getText().toString();
        if(!input.equals("")){
            Message chat = new Message(input,currentUser.getEmail());
            mDatabaseReference.child("messages").push().setValue(chat);
            mEditMessage.setText("");
        }
    }
}
