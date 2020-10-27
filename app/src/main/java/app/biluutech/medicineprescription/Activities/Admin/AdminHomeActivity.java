package app.biluutech.medicineprescription.Activities.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.biluutech.medicineprescription.DoctorModel;
import app.biluutech.medicineprescription.R;
import app.biluutech.medicineprescription.SliderAdapter;
import app.biluutech.medicineprescription.SliderItem;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdminHomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference doctorsReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;

    private ViewPager2 viewPager2;
    private Handler sliderHandler =  new Handler();

//
//    DoctorAdapter adapter;
//    List<DoctorModel> doctorModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        doctorsReference = FirebaseDatabase.getInstance().getReference().child("Doctor");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.doctor_admin_list_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewPager2 = findViewById(R.id.viewpager_image_slider);

        List<SliderItem> sliderItems = new ArrayList<>();

        sliderItems.add(new SliderItem(R.drawable.f8));
        sliderItems.add(new SliderItem(R.drawable.f7));
        sliderItems.add(new SliderItem(R.drawable.f1));
        sliderItems.add(new SliderItem(R.drawable.f2));
        sliderItems.add(new SliderItem(R.drawable.f5));
        sliderItems.add(new SliderItem(R.drawable.f6));
        sliderItems.add(new SliderItem(R.drawable.f3));
        sliderItems.add(new SliderItem(R.drawable.f4));


        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(20));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);

            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {

            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable,3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<DoctorModel> options =
                new FirebaseRecyclerOptions.Builder<DoctorModel>()
                        .setQuery(doctorsReference, DoctorModel.class)
                        .build();


        FirebaseRecyclerAdapter<DoctorModel, AdminDoctorsViewHolder> adapter =
                new FirebaseRecyclerAdapter<DoctorModel, AdminDoctorsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final AdminDoctorsViewHolder holder, final int position, @NonNull final DoctorModel model) {

                        holder.doctorName.setText(model.getName());
                        holder.doctorEmail.setText(model.getEmail());
                        holder.doctorGender.setText(model.getGender());
                        holder.doctorAddress.setText(model.getAddress());
                        holder.doctorSpecialization.setText(model.getSpecialization());

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
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminHomeActivity.this);
                                builder.setTitle("Do you want to approve this doctor ?");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if (i == 0) {
                                            ChangeProductState(doctorID);
                                        } else if (i == 1) {

                                            doctorsReference.child(doctorID)
                                                    .child("DoctorStatus")
                                                    .setValue("Not Approved")
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            Toast.makeText(AdminHomeActivity.this, "This doctor has been not approved, and not available for patient.", Toast.LENGTH_SHORT).show();

                                                        }
                                                    });

                                        }

                                    }
                                });

                                builder.show();

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminDoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_admin_list_item, parent, false);
                        return new AdminDoctorsViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class AdminDoctorsViewHolder extends RecyclerView.ViewHolder {

        CircleImageView doctorImage;
        TextView doctorName, doctorEmail, doctorAddress, doctorGender, doctorSpecialization;

        public AdminDoctorsViewHolder(@NonNull View itemView) {
            super(itemView);

            doctorImage = itemView.findViewById(R.id.doctor_image);
            doctorName = itemView.findViewById(R.id.doctor_name_TV);
            doctorEmail = itemView.findViewById(R.id.doctor_email_TV);
            doctorAddress = itemView.findViewById(R.id.doctor_address_TV);
            doctorGender = itemView.findViewById(R.id.doctor_gender_Tv);
            doctorSpecialization = itemView.findViewById(R.id.doctor_specialization_TV);

        }
    }



    private void ChangeProductState(String doctorID) {
        doctorsReference.child(doctorID)
                .child("DoctorStatus")
                .setValue("Approved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(AdminHomeActivity.this, "This doctor has been approved, and available for patient.", Toast.LENGTH_SHORT).show();

                    }
                });
    }

}
