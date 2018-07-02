package com.intern.yef.yefmobileapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.intern.yef.yefmobileapp.Models.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    DatabaseReference mRef;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    boolean userExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Register");

        userExists = false;

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        //On click listener for Register button
        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        //On click listener for Login
        TextView register = (TextView) findViewById(R.id.login_from_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser() {

        //Name field
        EditText registerName = (EditText) findViewById(R.id.register_name) ;
        final String nameString = registerName.getText().toString().trim();

        //Email field
        EditText registerEmail = (EditText) findViewById(R.id.register_email);
        final String emailString = registerEmail.getText().toString().trim();

        //Password field
        EditText registerPassword = (EditText) findViewById(R.id.register_password);
        final String passwordString = registerPassword.getText().toString();

        //Confirm password field
        EditText confirmRegisterPassword = (EditText) findViewById(R.id.confim_register_password);
        String confirmPasswordString = confirmRegisterPassword.getText().toString();


        if (nameString.equalsIgnoreCase("")) {
            registerName.setError("Name required!");
            return;

        }
        else if (emailString.equalsIgnoreCase("")) {
            registerEmail.setError("Email required!");
            return;

        } else if (passwordString.equalsIgnoreCase("") || passwordString.length() < 5) {
            registerPassword.setError("Password must be atleast 5 characters long");
            return;
        } else if (!passwordString.equals(confirmPasswordString)) {
            confirmRegisterPassword.setError("Passwords don't match!");
            return;
        }
        progressDialog.setMessage("Registering...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

              if(!task.isSuccessful()) {
                    // error occurred
                  Toast.makeText(Register.this,"Error "+ task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
              }
              else{
                  final FirebaseUser newUser = task.getResult().getUser();
                  //success creating user
                  //setting up display
                  final UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(emailString).build();

                  newUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          progressDialog.dismiss();
                          if(task.isSuccessful()){
                              Log.d(Register.class.getName(),"User profile updated");
                              /*** Create user in firebase DB and redirect on Success ***/
                                createUserInDb(nameString,emailString,newUser.getUid());
                          }
                          else{
                              //error
                              Toast.makeText(Register.this,"Error "+ task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                              progressDialog.dismiss();
                          }
                      }
                  });
              }

            }

        });

    }

    private void createUserInDb(final String name, String email, String uid){
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference();
        UserData userData = new UserData(name,email,uid);
        myRef.child("Users").push().setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    //error
                    Toast.makeText(Register.this,"Error "+ task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
                else{
                    //success adding user
                    //go to login
                    finish();
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                }

            }
        });

    }

/*   WORKED WITH PREVIOUS REGISTER METHODS

    check if user exists method

    */
}
