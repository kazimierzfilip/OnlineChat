package com.example.student.onlinechat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText mEditLogin, mEditPassword;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditLogin = findViewById(R.id.edit_login);
        mEditPassword = findViewById(R.id.edit_password);

        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    public void logIn(View view){
        String email = mEditLogin.getText().toString().trim();
        String password = mEditPassword.getText().toString().trim();

        if(email.compareTo("")!=0 && password.compareTo("")!=0) {
            mFirebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(MainActivity.this, new
                            OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this,
                                                "Authentication failed.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(MainActivity.this,
                                                "Authentication successfull.",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this,
                                                ChatActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
        }
    }

    public void signUp(View view){
        String email = mEditLogin.getText().toString().trim();
        String password = mEditPassword.getText().toString().trim();

        if(email.compareTo("")!=0 && password.compareTo("")!=0) {
            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(MainActivity.this,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(MainActivity.this,
                                            "createUserWithEmail:onComplete:" + task.isSuccessful(),
                                            Toast.LENGTH_SHORT).show();
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this,
                                                "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this,
                                                "Authentication successfull.",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this,
                                                ChatActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
        }
    }

}
