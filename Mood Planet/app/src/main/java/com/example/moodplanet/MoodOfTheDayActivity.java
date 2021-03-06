package com.example.moodplanet;

import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moodplanet.Model.MoodEntry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;

/**
 * Mood of the Day Activity Entry Actvity
 */
public class MoodOfTheDayActivity extends AppCompatActivity {
    // firebase variables
    private FirebaseUser firebaseCurrentUser;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    // xml variables
    ImageView moodImage;
    Button addButton;  //  to add the mood entry to the database
    EditText moodDescription;
    SeekBar moodRateSeekBar;
    TextView moodRateTextView, dateTimeTextView;
    Snackbar snack;

    // one mood entry variable
    private int progressRate;
    private String uid;
    private String chosenMood;
    DateTimeFormatter formatter;
    String currentTime;
    String dayOfWeek;

    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_of_the_day);

        mToolbar = findViewById(R.id.addMoodToolbar);
        mToolbar.setTitle("Add Entry");
        // toolbar depended on theme color
        SharedPreferences mSharedPreferences = getSharedPreferences("ToolbarColor", MODE_PRIVATE);
        int selectedColor = mSharedPreferences.getInt("color", getResources().getColor(R.color.colorPrimary));
        mToolbar.setBackgroundColor(selectedColor);
        getWindow().setStatusBarColor(selectedColor);

        // setup firebase
        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Mood_Entries");
        firebaseCurrentUser = FirebaseAuth.getInstance().getCurrentUser(); // gets current user
        uid = firebaseCurrentUser.getUid(); // gets user UID

        // connect to xml views
        moodImage = findViewById(R.id.imageBtn);    // CHILKA: MIGHT MODIFY LATER
        chosenMood = getIntent().getStringExtra("mood");
        moodRateSeekBar = findViewById(R.id.edit_moodSeekBar);
        moodRateTextView = findViewById(R.id.edit_moodrate_text_view);
        addButton = findViewById(R.id.updateButton);
        moodDescription = findViewById(R.id.edit_descriptionEditText);
        dateTimeTextView = findViewById(R.id.edit_date_time_textview);

        // Date & Time and Day of the Week
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a");
        currentTime = LocalDateTime.now().format(formatter);
        dayOfWeek = new SimpleDateFormat("EEEE").format(new Date());

        dateTimeTextView.setText(dayOfWeek + " " + currentTime);

        switch (chosenMood) {
            case "sad" :
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.sad));
                break;

            case "happy":
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.hehe));
                break;

            case "sleepy":
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.zzz));
                break;

            case "calm":
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.calm));
                break;

            case "scared":
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.ohno));
                break;

            case "inlove":
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.love));
                break;

            case "cheerful":
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.yay));
                break;

            case "optimistic":
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.ok));
                break;

            case "pensive":
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.hmm));
                break;

            default :
                moodImage.setImageDrawable(getResources().getDrawable(R.drawable.angry));
                break;
        }

        // mood rate seekbar functionality
        moodRateSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                moodRateTextView.setText("Mood Rate(1-5): " + String.valueOf(progress)) ;
                progressRate = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // add button functionality -> saves mood entry to database
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmpty(moodDescription.getText().toString())
                        || moodRateSeekBar.getProgress() == 0) {
//                    Toast.makeText(MoodOfTheDayActivity.this,
//                            "Please complete the fields",
//                            Toast.LENGTH_SHORT).show();

                    // used snackbar instead to display message
                    snack = Snackbar.make(view, "Please complete the required fields",
                            Snackbar.LENGTH_INDEFINITE);

                    snack.setAction("Close", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            snack.dismiss();
                        }
                    }).setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark)).show();         //
                }
                else {
                    // get the mood entry key
                    String key = databaseReference.push().getKey();

                    // get week of year
                    LocalDate date = LocalDate. now();
                    TemporalField woy = WeekFields. of(Locale. getDefault()). weekOfWeekBasedYear();
                    String weekNumber =  "" + date. get(woy);
                    String year = date.getYear() + "";

                    // Create a mood entry object
                    MoodEntry moodEntry = new MoodEntry(key, chosenMood, moodDescription.getText().toString(),
                            uid, progressRate, currentTime, dayOfWeek, weekNumber, year);

                    databaseReference.child(moodEntry.getKey()).setValue(moodEntry)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(MoodOfTheDayActivity.this, "Mood Entry Added  ",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MoodOfTheDayActivity.this, HomeActivity.class));

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MoodOfTheDayActivity.this, e.getMessage(),
                                            Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            }
        });
    }
}