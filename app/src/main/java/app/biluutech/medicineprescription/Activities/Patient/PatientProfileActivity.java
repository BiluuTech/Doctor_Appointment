package app.biluutech.medicineprescription.Activities.Patient;

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
import app.biluutech.medicineprescription.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class PatientProfileActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText fullNameEditText, ageEditText;
    private TextView closeTextBtn, saveTextButton;
    private ImageButton profileChangeBtn;

    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String checker = "";

    private FirebaseAuth firebaseAuth;
    private RelativeLayout rootprofileLayout;

    private BottomNavigationView navView;

    private BottomNavigationView.OnNavigationItemSelectedListener monNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    navView.getMenu().getItem(0).setChecked(true);
                    Intent intentHome = new Intent(PatientProfileActivity.this, PatientHomeActivity.class);
                    startActivity(intentHome);
                    finish();

                    return true;

                case R.id.navigation_profile:
                    navView.getMenu().getItem(1).setChecked(true);
                    Intent intentProfile = new Intent(PatientProfileActivity.this, PatientProfileActivity.class);
                    startActivity(intentProfile);
                    finish();

                    return true;

                case R.id.navigation_search:
                    navView.getMenu().getItem(2).setChecked(true);

                    Intent intentSearch = new Intent(PatientProfileActivity.this, PatientSearchActivity.class);
                    startActivity(intentSearch);
                    finish();

                    return true;
                case R.id.navigation_allergies:
                    navView.getMenu().getItem(3).setChecked(true);

                    Intent intentDisease = new Intent(PatientProfileActivity.this, AllergiesActivity.class);
                    startActivity(intentDisease);
                    finish();

                    return true;

                case R.id.navigation_logout:
                    final FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    navView.getMenu().getItem(3).setChecked(true);
                    Intent intentMain = new Intent(PatientProfileActivity.this, MainActivity.class);
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
        setContentView(R.layout.activity_patient_profile);

        rootprofileLayout = findViewById(R.id.root_patient_profile);

        navView = findViewById(R.id.nav_view);

        navView.getMenu().getItem(1).setChecked(true);
        navView.setOnNavigationItemSelectedListener(monNavigationItemSelectedListener);


        firebaseAuth = FirebaseAuth.getInstance();

        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");

        profileImageView = (CircleImageView) findViewById(R.id.settings_profile_image);
        fullNameEditText = (EditText) findViewById(R.id.settings_full_name);
        ageEditText = (EditText) findViewById(R.id.settings_age);
        profileChangeBtn = (ImageButton) findViewById(R.id.profile_image_change_btn);
        closeTextBtn = (TextView) findViewById(R.id.close_settings_btn);
        saveTextButton = (TextView) findViewById(R.id.update_account_settings_btn);

        userInfoDisplay(profileImageView, fullNameEditText, ageEditText);

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(PatientProfileActivity.this,PatientHomeActivity.class));
                navView.getMenu().getItem(0).setChecked(true);

            }
        });

        profileChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(PatientProfileActivity.this);
            }
        });

        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });

    }

    private void updateOnlyUserInfo()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Patient");

        HashMap<String, Object> patientMap = new HashMap<>();
        patientMap. put("Patient Name", fullNameEditText.getText().toString());
        patientMap. put("Patient Age", ageEditText.getText().toString());
        ref.child(firebaseAuth.getCurrentUser().getUid()).updateChildren(patientMap);

        startActivity(new Intent(PatientProfileActivity.this, PatientHomeActivity.class));
        Snackbar.make(rootprofileLayout, "Profile info updated successfully", Snackbar.LENGTH_LONG).show();
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImageView.setImageURI(imageUri);
        }
        else
        {
            Snackbar.make(rootprofileLayout, "Error: try again", Snackbar.LENGTH_LONG).show();

            startActivity(new Intent(PatientProfileActivity.this, PatientProfileActivity.class));
            finish();
        }
    }

    private void userInfoSaved()
    {
        if (TextUtils.isEmpty(fullNameEditText.getText().toString()))
        {
            Snackbar.make(rootprofileLayout, "Name is mandatory", Snackbar.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(ageEditText.getText().toString()))
        {
            Snackbar.make(rootprofileLayout, "Age is mandatory", Snackbar.LENGTH_LONG).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }


    private void uploadImage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null)
        {
            final StorageReference fileRef = storageProfilePrictureRef
                    .child(firebaseAuth.getCurrentUser().getUid() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                Uri downloadUrl = task.getResult();
                                myUrl = downloadUrl.toString();

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Patient");

                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap. put("Patient Name", fullNameEditText.getText().toString());
                                userMap. put("Patient Age", ageEditText.getText().toString());
                                userMap. put("image", myUrl);
                                ref.child(firebaseAuth.getCurrentUser().getUid()).updateChildren(userMap);

                                progressDialog.dismiss();

                                startActivity(new Intent(PatientProfileActivity.this, PatientHomeActivity.class));
                                Snackbar.make(rootprofileLayout, "Profile info updated successfully", Snackbar.LENGTH_LONG).show();
                                finish();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Snackbar.make(rootprofileLayout, "Something went wrong", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        else
        {
            Snackbar.make(rootprofileLayout, "Image is not selected", Snackbar.LENGTH_LONG).show();
        }
    }

    private void userInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText ageEditText)
    {
        DatabaseReference PatientRef = FirebaseDatabase.getInstance().getReference().child("Patient").child(firebaseAuth.getCurrentUser().getUid());

        PatientRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.child("image").exists())
                    {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("Patient Name").getValue().toString();
                        String age = dataSnapshot.child("Patient Age").getValue().toString();

                        Picasso.get().load(image).placeholder(R.drawable.profile).into(profileImageView);
                        fullNameEditText.setText(name);
                        ageEditText.setText(age);
                    }
                    else {

                        String name = dataSnapshot.child("Patient Name").getValue().toString();
                        String age = dataSnapshot.child("Patient Age").getValue().toString();

                        fullNameEditText.setText(name);
                        ageEditText.setText(age);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
