package app.biluutech.medicineprescription.Activities.Doctor;

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

import app.biluutech.medicineprescription.R;

public class DoctorLoginActivity extends AppCompatActivity {

    private TextView doctorLoginEmail, doctorLoginPassword;
    private Button doctorLoginBtn, doctorDontHaveAccountBtn;

    private ProgressDialog loadingBar;

    private FirebaseAuth firebaseAuth;

    private RelativeLayout rootDoctorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);

        doctorLoginEmail = (TextView) findViewById(R.id.doctor_login_email);
        doctorLoginPassword = (TextView) findViewById(R.id.doctor_login_password);
        doctorLoginBtn = (Button) findViewById(R.id.doctor_login_btn);
        doctorDontHaveAccountBtn = (Button) findViewById(R.id.doctor_dont_have_account_btn);

        rootDoctorLayout = findViewById(R.id.root_doctor_login);

        loadingBar = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        doctorDontHaveAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorLoginActivity.this, DoctorRegisterActivity.class));
                finish();
            }
        });

        doctorLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(doctorLoginEmail.getText().toString())) {
                    Snackbar.make(rootDoctorLayout, "Please enter email", Snackbar.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(doctorLoginPassword.getText().toString())) {
                    Snackbar.make(rootDoctorLayout, "Please enter password", Snackbar.LENGTH_LONG).show();
                } else {

                    loadingBar.setTitle("Login Account");
                    loadingBar.setMessage("Please wait, while we are checking the credentials.");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    firebaseAuth.signInWithEmailAndPassword(doctorLoginEmail.getText().toString(), doctorLoginPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        loadingBar.dismiss();
                                        Snackbar.make(rootDoctorLayout, "Please enter email", Snackbar.LENGTH_LONG).show();
                                        startActivity(new Intent(DoctorLoginActivity.this, DoctorHomeActivity.class));
                                        finish();

                                    } else {
                                        loadingBar.dismiss();
                                        Snackbar.make(rootDoctorLayout, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

    }

}
