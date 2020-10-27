package app.biluutech.medicineprescription;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import app.biluutech.medicineprescription.Activities.Admin.AdminLoginActivity;
import app.biluutech.medicineprescription.Activities.Doctor.DoctorLoginActivity;
import app.biluutech.medicineprescription.Activities.Patient.PatientLoginActivity;

public class MainActivity extends AppCompatActivity {

    private Button doctorButton, patientButton;
    private ProgressDialog loadingBar;
    private TextView adminTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doctorButton = (Button) findViewById(R.id.doctor_btn);
        patientButton = (Button) findViewById(R.id.patient_btn);
        loadingBar = new ProgressDialog(this);
        adminTV = (TextView) findViewById(R.id.admin_TV);

        adminTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AdminLoginActivity.class));
            }
        });

        patientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, PatientLoginActivity.class);
                startActivity(intent);
            }
        });

        doctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, DoctorLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
