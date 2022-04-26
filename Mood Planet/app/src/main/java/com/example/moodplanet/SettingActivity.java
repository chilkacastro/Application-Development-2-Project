package com.example.moodplanet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodplanet.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SettingActivity extends AppCompatActivity {

    private Button edit, logout;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String userID;
    private TextView fname, lname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        edit = findViewById(R.id.editAccountBtn);
        fname = findViewById(R.id.firstNameTV);
        lname = findViewById(R.id.lastNameTV);

        //-------------logout button------------------
        logout = findViewById(R.id.setringLogoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SettingActivity.this, MainActivity.class));
            }
        });
//        ----------------------end logout button---------------------------




//        ---------------------------display the user first name and last name------------------
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = firebaseUser.getUid();

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user != null){
                    String firstname = user.firstName;
                    String lastname = user.lastName;
                    fname.setText(firstname);
                    lname.setText(lastname);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SettingActivity.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

        //        -------------end display the user first name and last name------------------


    }
}