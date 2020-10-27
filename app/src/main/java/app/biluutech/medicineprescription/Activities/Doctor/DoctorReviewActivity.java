package app.biluutech.medicineprescription.Activities.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import app.biluutech.medicineprescription.Activities.Patient.PatientHomeActivity;
import app.biluutech.medicineprescription.R;

public class DoctorReviewActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText reviewComment;

    private Button submitReviewBtn;

    private String doctorID, patientID, patientName;

    private DatabaseReference doctorReviewReference;
    private DatabaseReference patientReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_review);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Place Review");

        reviewComment = findViewById(R.id.review_comment);
        submitReviewBtn = findViewById(R.id.review_submit_btn);

        doctorReviewReference = FirebaseDatabase.getInstance().getReference("Doctor");
        patientReference = FirebaseDatabase.getInstance().getReference("Patient");

        doctorID = getIntent().getStringExtra("DoctorID");
        patientID = getIntent().getStringExtra("PatientID");

        patientReference.child(patientID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    patientName = snapshot.child("Patient Name").getValue().toString();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        submitReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(reviewComment.getText().toString())) {

                    HashMap<String, Object> reviewMap = new HashMap<>();
                    reviewMap.put("comment", reviewComment.getText().toString());
                    reviewMap.put("patientName", patientName);

                    doctorReviewReference.child(doctorID).child("Reviews").child(patientID).updateChildren(reviewMap);
                    startActivity(new Intent(DoctorReviewActivity.this, PatientHomeActivity.class));
                }
                else
                {
                    reviewComment.setError("Please comment");
                }

            }
        });

    }
}