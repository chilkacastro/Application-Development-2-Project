package com.example.moodplanet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodplanet.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView banner, registerUser, returnToLogin;
    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private String validNamePattern;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser newUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        returnToLogin = (TextView) findViewById(R.id.returnToLogin);
        returnToLogin.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextFirstName = (EditText) findViewById(R.id.firstName);
        editTextFirstName.setOnClickListener(this);

        editTextLastName = (EditText) findViewById(R.id.lastName);
        editTextLastName.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextEmail.setOnClickListener(this);

        editTextPassword = (EditText) findViewById(R.id.password);
        editTextPassword.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.banner:
            case R.id.returnToLogin:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();

        if (firstName.isEmpty() && lastName.isEmpty() && email.isEmpty() && password.isEmpty()) {
            editTextFirstName.setError("First Name is required!");
            editTextLastName.setError("Last Name is required!");
            editTextEmail.setError("Email is required!");
            editTextPassword.setError("Password is required!");
            return;
        }

        if (firstName.isEmpty()) {
            editTextFirstName.setError("First Name is required!");
            editTextFirstName.requestFocus();
            return;
        }

        validNamePattern = "(?i)[a-z]([- ',.a-z]{0,23}[a-z])?";
        if (!firstName.matches(validNamePattern)) {
            editTextFirstName.setError("First name not valid");
            editTextFirstName.requestFocus();
            return;
        }

        if (lastName.isEmpty()) {
            editTextLastName.setError("Last Name is required!");
            editTextLastName.requestFocus();
            return;
        }

        if (!lastName.matches(validNamePattern)) {
            editTextLastName.setError("First name not valid");
            editTextLastName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Minimum password length should be 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                        if (isNewUser) {
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                User user = new User(firstName, lastName, email);

                                                FirebaseDatabase.getInstance().getReference("Users")
                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {

                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(RegisterUserActivity.this,
                                                                    "User has been registered successfully. Check the sent verification email!", Toast.LENGTH_LONG).show();
                                                            newUser = FirebaseAuth.getInstance().getCurrentUser();
                                                            newUser.sendEmailVerification();

                                                            progressBar.setVisibility(View.VISIBLE);

                                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                        } else {
                                                            Toast.makeText(RegisterUserActivity.this,
                                                                    "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                                            progressBar.setVisibility(View.GONE);
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(RegisterUserActivity.this,
                                                        "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterUserActivity.this, " Failed to register! Email exists already!", Toast.LENGTH_LONG).show();
                            editTextEmail.setError("Email exists already!");
                            editTextEmail.requestFocus();
                            progressBar.setVisibility(View.GONE);
                            return;
                        }
                    }
                });
    }

}