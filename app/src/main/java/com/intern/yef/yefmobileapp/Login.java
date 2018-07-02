package com.intern.yef.yefmobileapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Login");

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        //Check if user is already logged in
        if(firebaseAuth.getCurrentUser()!= null)
        {
            //Start LoggedIn activity
            finish();
            Intent intent=new Intent(getApplicationContext(),LoggedInChat.class);
            startActivity(intent);
        }

        Button loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                userLogin();
            }
        });

        //On click listener for Register
        TextView register = (TextView) findViewById(R.id.register_textview);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
            }
        });
    }

    private void userLogin(){

        EditText loginEmail = (EditText) findViewById(R.id.login_email);
        String  emailString = loginEmail.getText().toString().trim();

        EditText loginPassword = (EditText) findViewById(R.id.login_password);
        String  passwordString = loginPassword.getText().toString();

        if(emailString.equalsIgnoreCase(""))
        {
            loginEmail.setError("Email required!");//it gives user to info message //use any one //
            return;

        }
        else if(passwordString.equalsIgnoreCase("") ) {
            loginPassword.setError("Password required!");
            return;
        }
        //Email and passwords entered

        progressDialog.setMessage("Logging In");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    //Logged in
                    //Start LoggedIn activity
                    finish();
                    Intent intent=new Intent(getApplicationContext(),LoggedInChat.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(Login.this,"Error " + task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
