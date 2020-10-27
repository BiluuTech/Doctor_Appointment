package app.biluutech.medicineprescription.Activities.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import app.biluutech.medicineprescription.MainActivity;
import app.biluutech.medicineprescription.Activities.Patient.PatientProfileActivity;
import app.biluutech.medicineprescription.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfileActivity extends AppCompatActivity {

    private CircleImageView doctorProfileImageView;
    private EditText doctorFullNameEditText, doctorAgeEditText, doctorAddressEditText, doctorSpecializationEditText;
    private TextView doctorCloseTextBtn, doctorSaveTextButton;
    private ImageButton doctorProfileChangeBtn;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String checker = "";

    private FirebaseAuth firebaseAuth;

    private BottomNavigationView dnavView;

    private RelativeLayout rootDoctorProfile;


    private BottomNavigationView.OnNavigationItemSelectedListener monNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    dnavView.getMenu().getItem(0).setChecked(true);
                    Intent intentHome = new Intent(DoctorProfileActivity.this, DoctorHomeActivity.class);
                    startActivity(intentHome);
                    finish();
                    return true;

                case R.id.navigation_profile:
                    dnavView.getMenu().getItem(1).setChecked(true);
                    Intent intentProfile = new Intent(DoctorProfileActivity.this, DoctorProfileActivity.class);
                    startActivity(intentProfile);
                    finish();
                    return true;

                case R.id.navigation_appointments:
                    dnavView.getMenu().getItem(2).setChecked(true);
                    Intent intentAppointments = new Intent(DoctorProfileActivity.this, DoctorAppointmentsActivity.class);
                    startActivity(intentAppointments);
                    finish();
                    return true;

                case R.id.navigation_logout:
                    final FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    dnavView.getMenu().getItem(3).setChecked(true);
                    Intent intentMain = new Intent(DoctorProfileActivity.this, MainActivity.class);
                    intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentMain);
                    finish();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        dnavView = findViewById(R.id.doctor_nav_view);

        dnavView.setOnNavigationItemSelectedListener(monNavigationItemSelectedListener);

        dnavView.getMenu().getItem(1).setChecked(true);

        rootDoctorProfile = findViewById(R.id.root_doctor_profile);


        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        doctorProfileImageView = (CircleImageView) findViewById(R.id.doctor_settings_profile_image);

        doctorFullNameEditText = (EditText) findViewById(R.id.doctor_settings_full_name);
        doctorAgeEditText = (EditText) findViewById(R.id.doctor_settings_age);
        doctorAddressEditText = (EditText) findViewById(R.id.doctor_settings_address);
        doctorSpecializationEditText = (EditText) findViewById(R.id.doctor_settings_specialization);

        doctorProfileChangeBtn = (ImageButton) findViewById(R.id.doctor_profile_image_change_btn);

        doctorCloseTextBtn = (TextView) findViewById(R.id.doctor_close_settings_btn);
        doctorSaveTextButton = (TextView) findViewById(R.id.doctor_update_account_settings_btn);

        userInfoDisplay(doctorProfileImageView, doctorFullNameEditText, doctorAgeEditText, doctorAddressEditText, doctorSpecializationEditText);

        doctorCloseTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorProfileActivity.this, DoctorHomeActivity.class));
                dnavView.getMenu().getItem(0).setChecked(true);            }
        });

        doctorProfileChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(DoctorProfileActivity.this);
            }
        });

        doctorSaveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker.equals("clicked")) {
                    userInfoSaved();
                } else {
                    updateOnlyUserInfo();
                }
            }
        });

    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Doctor");

        HashMap<String, Object> doctorMap = new HashMap<>();
        doctorMap.put("name", doctorFullNameEditText.getText().toString());
        doctorMap.put("age", doctorAgeEditText.getText().toString());
        doctorMap.put("address", doctorAddressEditText.getText().toString());
        doctorMap.put("specialization", doctorSpecializationEditText.getText().toString());
        ref.child(firebaseAuth.getCurrentUser().getUid()).updateChildren(doctorMap);

        startActivity(new Intent(DoctorProfileActivity.this, DoctorHomeActivity.class));
        Snackbar.make(rootDoctorProfile, "Profile info updated successfully", Snackbar.LENGTH_LONG).show();
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            doctorProfileImageView.setImageURI(imageUri);
        } else {
            Snackbar.make(rootDoctorProfile, "Error: Try again", Snackbar.LENGTH_LONG).show();

            startActivity(new Intent(DoctorProfileActivity.this, PatientProfileActivity.class));
            finish();
        }
    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(doctorFullNameEditText.getText().toString())) {
            Snackbar.make(rootDoctorProfile, "Name is mandatory", Snackbar.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(doctorAgeEditText.getText().toString())) {
            Snackbar.make(rootDoctorProfile, "Age is mandatory", Snackbar.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(doctorAddressEditText.getText().toString())) {
            Snackbar.make(rootDoctorProfile, "Address is mandatory", Snackbar.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(doctorSpecializationEditText.getText().toString())) {
            Snackbar.make(rootDoctorProfile, "Specialization is mandatory", Snackbar.LENGTH_LONG).show();
        } else if (checker.equals("clicked")) {
            uploadImage();
        }
    }


    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageProfilePrictureRef
                    .child(firebaseAuth.getCurrentUser().getUid() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                myUrl = downloadUrl.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Doctor");

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap.put("name", doctorFullNameEditText.getText().toString());
                                userMap.put("age", doctorAgeEditText.getText().toString());
                                userMap.put("address", doctorAddressEditText.getText().toString());
                                userMap.put("specialization", doctorSpecializationEditText.getText().toString());
                                userMap.put("image", myUrl);
                                ref.child(firebaseAuth.getCurrentUser().getUid()).updateChildren(userMap);

                                progressDialog.dismiss();

                                startActivity(new Intent(DoctorProfileActivity.this, DoctorHomeActivity.class));
                                Snackbar.make(rootDoctorProfile, "Profile info updated successfully", Snackbar.LENGTH_LONG).show();
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Snackbar.make(rootDoctorProfile, "Something went wrong", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            Snackbar.make(rootDoctorProfile, "Image is not selected, Try again", Snackbar.LENGTH_LONG).show();
        }
    }

    private void userInfoDisplay(final CircleImageView doctorProfileImageView, final EditText doctorFullNameEditText, final EditText doctorAgeEditText, final EditText doctorAddressEditText, final EditText doctorSpecializationEditText) {
        DatabaseReference PatientRef = FirebaseDatabase.getInstance().getReference().child("Doctor").child(firebaseAuth.getCurrentUser().getUid());

        PatientRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("image").exists()) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String age = dataSnapshot.child("age").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        String specialization = dataSnapshot.child("specialization").getValue().toString();

                        Picasso.get().load(image).into(doctorProfileImageView);
                        doctorFullNameEditText.setText(name);
                        doctorAgeEditText.setText(age);
                        doctorAddressEditText.setText(address);
                        doctorSpecializationEditText.setText(specialization);
                    } else {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String age = dataSnapshot.child("age").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        String specialization = dataSnapshot.child("specialization").getValue().toString();

                        doctorFullNameEditText.setText(name);
                        doctorAgeEditText.setText(age);
                        doctorAddressEditText.setText(address);
                        doctorSpecializationEditText.setText(specialization);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}