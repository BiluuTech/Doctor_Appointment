package app.biluutech.medicineprescription.Activities.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.biluutech.medicineprescription.R;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText adminLoginEmail, adminLoginPassword;
    private Button adminLoginBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference RootRef;

    String adminPassword;
    String adminEmail;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.right_to_left);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.left_to_right);
        Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.bottom_to_up);

        adminLoginEmail = findViewById(R.id.admin_login_email);
        adminLoginPassword = findViewById(R.id.admin_login_password);
        adminLoginBtn = findViewById(R.id.admin_login_btn);

        adminLoginEmail.startAnimation(animation1);
        adminLoginPassword.startAnimation(animation2);
        adminLoginBtn.startAnimation(animation3);

        loadingBar = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        final String adminEmail = adminLoginEmail.getText().toString();
        final String adminPassword = adminLoginPassword.getText().toString();

        adminLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingBar.setTitle("Login Account");
                loadingBar.setMessage("Please wait, while we are checking the credentials.");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                RootRef = FirebaseDatabase.getInstance().getReference().child("Admin");
                RootRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String adminEmail = adminLoginEmail.getText().toString();
                        String adminPassword = adminLoginPassword.getText().toString();

                        String fEmail = snapshot.child("Admin Email").getValue().toString();
                        String fpassword = snapshot.child("Admin Password").getValue().toString();

                        if (fEmail.equals(adminEmail) && fpassword.equals(adminPassword)){
                            loadingBar.dismiss();
                            Toast.makeText(AdminLoginActivity.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AdminLoginActivity.this,AdminHomeActivity.class));
                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(AdminLoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
}
