package app.biluutech.medicineprescription.Activities.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import app.biluutech.medicineprescription.R;

public class PatientLoginActivity extends AppCompatActivity {

    private TextView patientLoginEmail, patientLoginPassword;
    private Button patientLoginBtn, patientDontHaveAccountBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private ProgressDialog loadingBar;

    private RelativeLayout rootPatientLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);

        rootPatientLogin = findViewById(R.id.root_patient_login);

        patientLoginEmail = (TextView) findViewById(R.id.patient_login_email);
        patientLoginPassword = (TextView) findViewById(R.id.patient_login_password);
        patientLoginBtn = (Button) findViewById(R.id.patient_login_btn);
        patientDontHaveAccountBtn = (Button) findViewById(R.id.patient_dont_have_account_btn);

        loadingBar = new ProgressDialog(this);


        firebaseAuth = FirebaseAuth.getInstance();

        patientDontHaveAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientLoginActivity.this, PatientRegisterActivity.class));
                finish();
            }
        });

        patientLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(patientLoginEmail.getText().toString())) {
                    Snackbar.make(rootPatientLogin, "Please enter email !", Snackbar.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(patientLoginPassword.getText().toString())) {
                    Snackbar.make(rootPatientLogin, "Please enter password !", Snackbar.LENGTH_LONG).show();
                } else {

                    loadingBar.setTitle("Login Account");
                    loadingBar.setMessage("Please wait, while we are checking the credentials.");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    firebaseAuth.signInWithEmailAndPassword(patientLoginEmail.getText().toString(), patientLoginPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        loadingBar.dismiss();
                                        startActivity(new Intent(PatientLoginActivity.this, PatientHomeActivity.class));
                                        finish();

                                    } else {
                                        loadingBar.dismiss();
                                        Snackbar.make(rootPatientLogin, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
