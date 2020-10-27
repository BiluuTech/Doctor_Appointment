package app.biluutech.medicineprescription.Activities.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import app.biluutech.medicineprescription.Activities.Doctor.DoctorRecentReviewsActivity;
import app.biluutech.medicineprescription.Activities.Doctor.DoctorReviewActivity;
import app.biluutech.medicineprescription.DoctorModel;
import app.biluutech.medicineprescription.MainActivity;
import app.biluutech.medicineprescription.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class PatientSearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference patientsReference, doctorsReference;
    private FirebaseAuth firebaseAuth;

    private ImageButton searchBtn;
    private EditText searchEt;
    private String searchInput;

    private String patientID;

    private BottomNavigationView navView;

    private BottomNavigationView.OnNavigationItemSelectedListener monNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    navView.getMenu().getItem(0).setChecked(true);
                    Intent intentHome = new Intent(PatientSearchActivity.this, PatientHomeActivity.class);
                    startActivity(intentHome);
                    finish();

                    return true;

                case R.id.navigation_profile:
                    navView.getMenu().getItem(1).setChecked(true);

                    Intent intentProfile = new Intent(PatientSearchActivity.this, PatientProfileActivity.class);
                    startActivity(intentProfile);
                    finish();

                    return true;

                case R.id.navigation_search:
                    navView.getMenu().getItem(2).setChecked(true);

                    Intent intentSearch = new Intent(PatientSearchActivity.this, PatientSearchActivity.class);
                    startActivity(intentSearch);
                    finish();

                    return true;
                case R.id.navigation_allergies:
                    navView.getMenu().getItem(3).setChecked(true);

                    Intent intentDisease = new Intent(PatientSearchActivity.this, AllergiesActivity.class);
                    startActivity(intentDisease);
                    finish();

                    return true;

                case R.id.navigation_logout:
                    final FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    navView.getMenu().getItem(4).setChecked(true);

                    Intent intentMain = new Intent(PatientSearchActivity.this, MainActivity.class);
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
        setContentView(R.layout.activity_patient_search);

        navView = findViewById(R.id.nav_view);

        navView.getMenu().getItem(2).setChecked(true);
        navView.setOnNavigationItemSelectedListener(monNavigationItemSelectedListener);

        searchEt = findViewById(R.id.search_ET);
        searchBtn = findViewById(R.id.search_btn);

        firebaseAuth = FirebaseAuth.getInstance();

        patientID = firebaseAuth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.doctors_patient_list_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput = searchEt.getText().toString();

                onStart();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        doctorsReference = FirebaseDatabase.getInstance().getReference().child("Doctor");

        FirebaseRecyclerOptions<DoctorModel> options =
                new FirebaseRecyclerOptions.Builder<DoctorModel>()
                        .setQuery(doctorsReference.orderByChild("specialization").startAt(searchInput), DoctorModel.class)
                        .build();

        FirebaseRecyclerAdapter<DoctorModel, PatientSearchActivity.PatientDoctorsSearchViewHolder> adapter =
                new FirebaseRecyclerAdapter<DoctorModel, PatientSearchActivity.PatientDoctorsSearchViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PatientSearchActivity.PatientDoctorsSearchViewHolder holder, final int position, @NonNull final DoctorModel model) {

//                       if(model.getDoctorStatus()=="Approved") {

//                        String st = model.getDoctorStatus();
//                         if (st.equals("Approved")){


                        holder.doctorName.setText(model.getName());
                        holder.doctorEmail.setText(model.getEmail());
                        holder.doctorGender.setText(model.getGender());
                        holder.doctorAddress.setText(model.getAddress());
                        holder.doctorSpecialization.setText(model.getSpecialization());
                        holder.doctorStatus.setText(model.getDoctorStatus());
                        holder.doctorPhone.setText(model.getPhone());

                        holder.recentReviewsBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String did = model.getdID();

                                Intent intent = new Intent(PatientSearchActivity.this, DoctorRecentReviewsActivity.class);
                                intent.putExtra("DoctorID",did);
                                startActivity(intent);

                            }
                        });

                        holder.doctorReviewBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String did = model.getdID();

                                Intent intent = new Intent(PatientSearchActivity.this, DoctorReviewActivity.class);
                                intent.putExtra("DoctorID",did);
                                intent.putExtra("PatientID",patientID);
                                startActivity(intent);
                            }
                        });

                        Picasso.get().load(model.getImage()).placeholder(R.drawable.profile).into(holder.doctorImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final String doctorID = model.getdID();

                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Yes",
                                                "No"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(PatientSearchActivity.this);
                                builder.setTitle("Do you want to appoint this doctor ?");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if (i == 0) {

                                            Intent intent = new Intent(PatientSearchActivity.this, AppointmentActivity.class);
                                            intent.putExtra("DOCTOR ID", doctorID);
                                            intent.putExtra("PATIENT ID", patientID);
                                            startActivity(intent);


                                        } else if (i == 1) {

                                        }

                                    }
                                });

                                builder.show();

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public PatientDoctorsSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_patient_list_item, parent, false);
                        return new PatientDoctorsSearchViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class PatientDoctorsSearchViewHolder extends RecyclerView.ViewHolder {
        CircleImageView doctorImage;
        TextView doctorName, doctorEmail, doctorAddress, doctorGender, doctorSpecialization, doctorStatus, doctorPhone;
        Button doctorReviewBtn, recentReviewsBtn;

        public PatientDoctorsSearchViewHolder(@NonNull View itemView) {
            super(itemView);

            doctorImage = itemView.findViewById(R.id.doctor_image);
            doctorName = itemView.findViewById(R.id.doctor_name_TV);
            doctorEmail = itemView.findViewById(R.id.doctor_email_TV);
            doctorAddress = itemView.findViewById(R.id.doctor_address_TV);
            doctorGender = itemView.findViewById(R.id.doctor_gender_Tv);
            doctorSpecialization = itemView.findViewById(R.id.doctor_specialization_TV);
            doctorStatus = itemView.findViewById(R.id.doctor_status_TV);
            doctorPhone = itemView.findViewById(R.id.doctor_phone_TV);

            doctorReviewBtn = itemView.findViewById(R.id.doctor_review_btn);
            recentReviewsBtn = itemView.findViewById(R.id.recent_reviews_btn);

        }
    }
}
