package app.biluutech.medicineprescription.Activities.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

import app.biluutech.medicineprescription.R;

public class AppointmentActivity extends AppCompatActivity {

    private Button timePickerBtn, datePickerBtn, continueAppointmentBtn, callButton;

    private TextView timePickertxt, datePickertxt;

    private DatabaseReference appointRef, patientRef, callRef;

    private String doctorPhone;

    private String did, pid, dateText, timeText;

    private RelativeLayout rootAppointmentLayout;

    private String name, gender, age, image;

    private ImageButton jazzcashBtn, easypaisaBtn;

    private CardView paymentMethodCardview;

    Animation animation1, animation2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        animation1 = AnimationUtils.loadAnimation(this, R.anim.text_delay);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.bottom_to_up);

        timePickerBtn = (Button) findViewById(R.id.time_picker_btn);
        datePickerBtn = (Button) findViewById(R.id.date_picker_btn);
        continueAppointmentBtn = (Button) findViewById(R.id.continue_appointment_btn);
        callButton = (Button) findViewById(R.id.call_appointment_btn);

        paymentMethodCardview = (CardView) findViewById(R.id.payment_method_cardview);

        jazzcashBtn = (ImageButton) findViewById(R.id.jazzcash_btn);
        easypaisaBtn = (ImageButton) findViewById(R.id.easypaisa_btn);

        timePickerBtn.startAnimation(animation1);
        datePickerBtn.startAnimation(animation1);
        continueAppointmentBtn.startAnimation(animation1);

        did = getIntent().getStringExtra("DOCTOR ID");
        pid = getIntent().getStringExtra("PATIENT ID");

        rootAppointmentLayout = findViewById(R.id.root_appointment_layout);

        callRef = FirebaseDatabase.getInstance().getReference("Doctor").child(did);
        appointRef = FirebaseDatabase.getInstance().getReference("Doctor").child(did).child("Appointmnets");
        patientRef = FirebaseDatabase.getInstance().getReference().child("Patient").child(pid);

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDateButton();
            }
        });
        timePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTimeButton();
            }
        });

        callRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    doctorPhone = snapshot.child("phone").getValue().toString();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentCall = new Intent(Intent.ACTION_CALL);
                intentCall.setData(Uri.parse("tel:" + doctorPhone));

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(AppointmentActivity.this, "Please grant permission", Toast.LENGTH_SHORT).show();
                    requestPermission();
                } else {
                    startActivity(intentCall);
                }
            }
        });

    }

    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                dateText = android.text.format.DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();

                datePickerBtn.setText(dateText);
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();

    }

    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = android.text.format.DateFormat.is24HourFormat(AppointmentActivity.this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                timeText = DateFormat.format("h:mm a", calendar1).toString();

                timePickerBtn.setText(timeText);

            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();

        patientRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    if (dataSnapshot.child("image").exists()) {

                        name = dataSnapshot.child("Patient Name").getValue().toString();
                        gender = dataSnapshot.child("Patient Gender").getValue().toString();
                        age = dataSnapshot.child("Patient Age").getValue().toString();
                        image = dataSnapshot.child("image").getValue().toString();


                    } else {

                        name = dataSnapshot.child("Patient Name").getValue().toString();
                        gender = dataSnapshot.child("Patient Gender").getValue().toString();
                        age = dataSnapshot.child("Patient Age").getValue().toString();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        continueAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dateText.isEmpty()) {
                    Snackbar.make(rootAppointmentLayout, "Please select date", Snackbar.LENGTH_LONG).show();
                } else if (timeText.isEmpty()) {
                    Snackbar.make(rootAppointmentLayout, "Please select time", Snackbar.LENGTH_LONG).show();
                } else {

                    paymentMethodCardview.startAnimation(animation2);
                    paymentMethodCardview.setVisibility(View.VISIBLE);

                    jazzcashBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            HashMap<String, Object> patientAppointmentData = new HashMap<>();
                            patientAppointmentData.put("pid", pid);
                            patientAppointmentData.put("name", name);
                            patientAppointmentData.put("age", age);
                            patientAppointmentData.put("gender", gender);
                            patientAppointmentData.put("image", image);
                            patientAppointmentData.put("date", dateText);
                            patientAppointmentData.put("time", timeText);


                            appointRef.child(pid).updateChildren(patientAppointmentData);

                            Snackbar.make(rootAppointmentLayout, "Appointment Done", Snackbar.LENGTH_LONG).show();
                            startActivity(new Intent(AppointmentActivity.this, PatientHomeActivity.class));

                        }
                    });


                    easypaisaBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            HashMap<String, Object> patientAppointmentData = new HashMap<>();
                            patientAppointmentData.put("pid", pid);
                            patientAppointmentData.put("name", name);
                            patientAppointmentData.put("age", age);
                            patientAppointmentData.put("gender", gender);
                            patientAppointmentData.put("image", image);
                            patientAppointmentData.put("date", dateText);
                            patientAppointmentData.put("time", timeText);


                            appointRef.child(pid).updateChildren(patientAppointmentData);

                            Snackbar.make(rootAppointmentLayout, "Appointment Done", Snackbar.LENGTH_LONG).show();
                            startActivity(new Intent(AppointmentActivity.this, PatientHomeActivity.class));

                        }
                    });

                }

            }
        });

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(AppointmentActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
    }

}