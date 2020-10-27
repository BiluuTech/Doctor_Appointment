package app.biluutech.medicineprescription.Activities.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import app.biluutech.medicineprescription.R;

public class DoctorRegisterActivity extends AppCompatActivity {

    private EditText doctorRegisterName, doctorRegisterEmail, doctorRegisterPassword,doctorRegisterAge, doctorRegisterClinicName,doctorRegisterSpecialization, doctorRegisterPhone;
    private Button doctorRegisterBtn, doctorAlreadyHaveAccountBtn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog loadingBar;
    private RadioButton radioGenderMale, radioGenderFemale;
    private String dGender = "";

    private DatabaseReference databaseReference;

    private RelativeLayout rootDoctorRegisterLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register);

        rootDoctorRegisterLayout = findViewById(R.id.root_doctor_register);

        doctorRegisterName = (EditText) findViewById(R.id.doctor_register_name);
        doctorRegisterEmail = (EditText) findViewById(R.id.doctor_register_email);
        doctorRegisterPassword = (EditText) findViewById(R.id.doctor_register_password);
        doctorRegisterAge = (EditText) findViewById(R.id.doctor_register_age);
        doctorRegisterClinicName = (EditText) findViewById(R.id.doctor_hospital_clinic_name);
        doctorRegisterSpecialization = (EditText) findViewById(R.id.doctor_register_specialization);
        doctorRegisterPhone = (EditText) findViewById(R.id.doctor_register_phone);

        radioGenderMale = (RadioButton) findViewById(R.id.radi0_male);
        radioGenderFemale = (RadioButton) findViewById(R.id.radio_female);

        doctorRegisterBtn = (Button) findViewById(R.id.doctor_register_btn);
        doctorAlreadyHaveAccountBtn = (Button) findViewById(R.id.doctor_already_have_account_btn);

        loadingBar = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Doctor");

        doctorAlreadyHaveAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorRegisterActivity.this, DoctorLoginActivity.class));
                finish();
            }
        });

        doctorRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

    }

    private void CreateAccount() {

        final String dName = doctorRegisterName.getText().toString();
        final String dEmail = doctorRegisterEmail.getText().toString();
        final String dPassword = doctorRegisterPassword.getText().toString();
        final String dAge = doctorRegisterAge.getText().toString();
        final String dAddress = doctorRegisterClinicName.getText().toString();
        final String dSpecialization = doctorRegisterSpecialization.getText().toString();
        final String dPhone = doctorRegisterPhone.getText().toString();

        if (radioGenderMale.isChecked()) {
            dGender = "Male";
        }
        if (radioGenderFemale.isChecked()) {
            dGender = "Female";
        }


        if (TextUtils.isEmpty(dName)) {
            Snackbar.make(rootDoctorRegisterLayout,"Please write your name...",Snackbar.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(dEmail)) {
            Snackbar.make(rootDoctorRegisterLayout,"Please write your email...",Snackbar.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(dPassword)) {
            Snackbar.make(rootDoctorRegisterLayout,"Please write your password...",Snackbar.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(dAge)) {
            Snackbar.make(rootDoctorRegisterLayout,"Please write your age...",Snackbar.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(dAddress)) {
            Snackbar.make(rootDoctorRegisterLayout,"Please write your clinic name...",Snackbar.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(dSpecialization)) {
            Snackbar.make(rootDoctorRegisterLayout,"Please write your specialization...",Snackbar.LENGTH_LONG).show();
        }else if (TextUtils.isEmpty(dPhone)) {
            Snackbar.make(rootDoctorRegisterLayout,"Please write your Phone number...",Snackbar.LENGTH_LONG).show();
        }
        else if (!radioGenderMale.isChecked() && !radioGenderFemale.isChecked()) {
            Snackbar.make(rootDoctorRegisterLayout,"Please select your gender...",Snackbar.LENGTH_LONG).show();
        } else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            firebaseAuth.createUserWithEmailAndPassword(doctorRegisterEmail.getText().toString(), doctorRegisterPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                loadingBar.dismiss();
                                HashMap<String, Object> doctorData = new HashMap<>();
                                doctorData.put("dID", firebaseAuth.getCurrentUser().getUid());
                                doctorData.put("name", dName);
                                doctorData.put("email", dEmail);
                                doctorData.put("age", dAge);
                                doctorData.put("address", dAddress);
                                doctorData.put("specialization", dSpecialization);
                                doctorData.put("phone", dPhone);
                                doctorData.put("gender", dGender);
                                doctorData.put("DoctorStatus", "Not Approved");

                                databaseReference.child(firebaseAuth.getCurrentUser().getUid()).updateChildren(doctorData);

                                Snackbar.make(rootDoctorRegisterLayout,"Account created successfully",Snackbar.LENGTH_LONG).show();
                                startActivity(new Intent(DoctorRegisterActivity.this,DoctorLoginActivity.class));
                                finish();

                            } else {
                                loadingBar.dismiss();
                                Snackbar.make(rootDoctorRegisterLayout,task.getException().getMessage(),Snackbar.LENGTH_LONG).show();

                            }

                        }
                    });

        }
    }

}
