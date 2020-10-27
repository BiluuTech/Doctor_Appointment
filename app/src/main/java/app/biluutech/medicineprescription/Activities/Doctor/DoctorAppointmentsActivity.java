package app.biluutech.medicineprescription.Activities.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import app.biluutech.medicineprescription.DoctorModel;
import app.biluutech.medicineprescription.MainActivity;
import app.biluutech.medicineprescription.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorAppointmentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference doctorsReference, doctorsAReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;

    private CardView messageBox;

    private BottomNavigationView dnavView;

    private BottomNavigationView.OnNavigationItemSelectedListener monNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.navigation_home:


                    dnavView.getMenu().getItem(0).setChecked(true);
                    Intent intentHome = new Intent(DoctorAppointmentsActivity.this, DoctorHomeActivity.class);
                    startActivity(intentHome);
                    finish();

                    return true;

                case R.id.navigation_profile:

                    dnavView.getMenu().getItem(1).setChecked(true);
                    Intent intentProfile = new Intent(DoctorAppointmentsActivity.this, DoctorProfileActivity.class);
                    startActivity(intentProfile);
                    finish();

                    return true;

                case R.id.navigation_appointments:
                    dnavView.getMenu().getItem(2).setChecked(true);
                    Intent intentAppointments = new Intent(DoctorAppointmentsActivity.this, DoctorAppointmentsActivity.class);
                    startActivity(intentAppointments);
                    finish();

                    return true;

                case R.id.navigation_logout:

                    dnavView.getMenu().getItem(3).setChecked(true);
                    final FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();

                    Intent intentMain = new Intent(DoctorAppointmentsActivity.this, MainActivity.class);
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
        setContentView(R.layout.activity_doctor_appointments);

        dnavView = findViewById(R.id.doctor_nav_view);

        dnavView.setOnNavigationItemSelectedListener(monNavigationItemSelectedListener);

        dnavView.getMenu().getItem(2).setChecked(true);

        firebaseAuth = FirebaseAuth.getInstance();

        doctorsReference = FirebaseDatabase.getInstance().getReference().child("Doctor").child(firebaseAuth.getCurrentUser().getUid()).child("Appointmnets");
        doctorsAReference = FirebaseDatabase.getInstance().getReference().child("Doctor").child(firebaseAuth.getCurrentUser().getUid());

        recyclerView = findViewById(R.id.doctor_appointments_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageBox = findViewById(R.id.message_box_appointments);

        doctorsAReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("Appointmnets").exists()){

                    messageBox.setVisibility(View.GONE);
                    onStart();
                }
                else
                {

                    messageBox.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<DoctorModel> options =
                new FirebaseRecyclerOptions.Builder<DoctorModel>()
                        .setQuery(doctorsReference, DoctorModel.class)
                        .build();


        FirebaseRecyclerAdapter<DoctorModel, DoctorAppointmentsActivity.AdminDoctorsViewHolder> adapter =
                new FirebaseRecyclerAdapter<DoctorModel, DoctorAppointmentsActivity.AdminDoctorsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final DoctorAppointmentsActivity.AdminDoctorsViewHolder holder, final int position, @NonNull final DoctorModel model) {

                        holder.patientName.setText(model.getName());
                        holder.patientGender.setText(model.getGender());
                        holder.patientAge.setText(model.getAge());
                        holder.patientTime.setText(model.getTime());
                        holder.patientDate.setText(model.getDate());

                        Picasso.get().load(model.getImage()).placeholder(R.drawable.profile).into(holder.patientImage);

                    }

                    @NonNull
                    @Override
                    public DoctorAppointmentsActivity.AdminDoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_appointment_layout, parent, false);
                        return new AdminDoctorsViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class AdminDoctorsViewHolder extends RecyclerView.ViewHolder {

        CircleImageView patientImage;
        TextView patientName, patientGender, patientAge, patientDate, patientTime;

        public AdminDoctorsViewHolder(@NonNull View itemView) {
            super(itemView);

            patientImage = itemView.findViewById(R.id.patient_appointment_image);
            patientName = itemView.findViewById(R.id.review_patient_name_TV);
            patientAge = itemView.findViewById(R.id.patient_appointment_age_TV);
            patientGender = itemView.findViewById(R.id.review_patient_comment_TV);
            patientDate = itemView.findViewById(R.id.patient_appointment_date_TV);
            patientTime = itemView.findViewById(R.id.patient_appointment_time_TV);

        }
    }

}
