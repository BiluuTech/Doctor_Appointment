package app.biluutech.medicineprescription.Activities.Patient;

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

public class PatientRegisterActivity extends AppCompatActivity {

    private EditText patientRegisterEmail, patientRegisterPassword, patientRegisterName, patientRegisterAge;
    private RadioButton radioGenderMale, radioGenderFemale;
    private Button patientRegisterBtn, patientAlreadyHaveAccountBtn;
    private ProgressDialog loadingBar;
    private String gender = "";

    private RelativeLayout rootPatientRegisterLayout;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);

        rootPatientRegisterLayout = findViewById(R.id.root_patient_register);

        patientRegisterName = (EditText) findViewById(R.id.patient_register_username);
        patientRegisterEmail = (EditText) findViewById(R.id.patient_register_email);
        patientRegisterPassword = (EditText) findViewById(R.id.patient_register_password);
        patientRegisterAge = (EditText) findViewById(R.id.patient_register_age);

        radioGenderMale = (RadioButton) findViewById(R.id.radi0_male);
        radioGenderFemale = (RadioButton) findViewById(R.id.radio_female);

        patientRegisterBtn = (Button) findViewById(R.id.patient_register_btn);
        patientAlreadyHaveAccountBtn = (Button) findViewById(R.id.patient_already_have_account_btn);
        loadingBar = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Patient");

        patientAlreadyHaveAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientRegisterActivity.this, PatientLoginActivity.class));
                finish();
            }
        });

        patientRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateAccount();

            }
        });

    }

    private void CreateAccount() {

        final String name = patientRegisterName.getText().toString();
        final String age = patientRegisterAge.getText().toString();
        final String email = patientRegisterEmail.getText().toString();
        final String password = patientRegisterPassword.getText().toString();

        if (radioGenderMale.isChecked()) {
            gender = "Male";
        }
        if (radioGenderFemale.isChecked()) {
            gender = "Female";
        }

        if (TextUtils.isEmpty(name)) {
            Snackbar.make(rootPatientRegisterLayout, "Please write your name...", Snackbar.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(age)) {
            Snackbar.make(rootPatientRegisterLayout, "Please write your age...", Snackbar.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(email)) {
            Snackbar.make(rootPatientRegisterLayout, "Please write your email...", Snackbar.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Snackbar.make(rootPatientRegisterLayout, "Please write your password...", Snackbar.LENGTH_LONG).show();
        } else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            firebaseAuth.createUserWithEmailAndPassword(patientRegisterEmail.getText().toString(), patientRegisterPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                loadingBar.dismiss();

                                HashMap<String, Object> patientData = new HashMap<>();
                                patientData.put("Patient Id", firebaseAuth.getCurrentUser().getUid());
                                patientData.put("Patient Name", name);
                                patientData.put("Patient Age", age);
                                patientData.put("Patient Email", email);
                                patientData.put("Patient Gender", gender);

                                databaseReference.child(firebaseAuth.getCurrentUser().getUid()).updateChildren(patientData);

                                Snackbar.make(rootPatientRegisterLayout, "Account Created Successfully !", Snackbar.LENGTH_LONG).show();

                                startActivity(new Intent(PatientRegisterActivity.this, PatientLoginActivity.class));
                                finish();
                            } else {

                                loadingBar.dismiss();
                                Snackbar.make(rootPatientRegisterLayout,task.getException().getMessage(),Snackbar.LENGTH_LONG).show();

                            }

                        }
                    });

        }
    }
}
