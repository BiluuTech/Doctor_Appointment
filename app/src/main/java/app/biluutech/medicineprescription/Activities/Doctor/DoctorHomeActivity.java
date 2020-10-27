package app.biluutech.medicineprescription.Activities.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.biluutech.medicineprescription.MainActivity;
import app.biluutech.medicineprescription.R;
import app.biluutech.medicineprescription.SliderAdapter;
import app.biluutech.medicineprescription.SliderItem;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorHomeActivity extends AppCompatActivity {

    private TextView doctorNameTextView, doctorAgeTextView, doctorAddressTextView, doctorSpecializationTextView;
    private CircleImageView doctorProfileImageView;

    private DatabaseReference doctorsReference;
    private FirebaseAuth firebaseAuth;

    private TextView txtMarquee;

    private ViewPager2 viewPager2;
    private Handler sliderHandler =  new Handler();

    private BottomNavigationView dnavView;

    private BottomNavigationView.OnNavigationItemSelectedListener monNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:


                    dnavView.getMenu().getItem(0).setChecked(true);
                    Intent intentHome = new Intent(DoctorHomeActivity.this, DoctorHomeActivity.class);
                    startActivity(intentHome);
                    finish();

                    return true;

                case R.id.navigation_profile:

                    dnavView.getMenu().getItem(1).setChecked(true);
                    Intent intentProfile = new Intent(DoctorHomeActivity.this, DoctorProfileActivity.class);
                    startActivity(intentProfile);
                    finish();

                    return true;

                case R.id.navigation_appointments:
                    dnavView.getMenu().getItem(2).setChecked(true);
                    Intent intentAppointments = new Intent(DoctorHomeActivity.this, DoctorAppointmentsActivity.class);
                    startActivity(intentAppointments);
                    finish();
                    return true;

                case R.id.navigation_logout:

                    dnavView.getMenu().getItem(3).setChecked(true);
                    final FirebaseAuth mAuth;
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();

                    Intent intentMain = new Intent(DoctorHomeActivity.this, MainActivity.class);
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
        setContentView(R.layout.activity_doctor_home);


        dnavView = findViewById(R.id.doctor_nav_view);
        dnavView.setOnNavigationItemSelectedListener(monNavigationItemSelectedListener);

        txtMarquee = findViewById(R.id.marquee);
        txtMarquee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        txtMarquee.setSelected(true);

        doctorNameTextView = (TextView) findViewById(R.id.doctor_profile_name);
        doctorAgeTextView = (TextView) findViewById(R.id.doctor_profile_age);
        doctorAddressTextView = (TextView) findViewById(R.id.doctor_profile_address);
        doctorSpecializationTextView = (TextView) findViewById(R.id.doctor_profile_specilization);
        doctorProfileImageView = (CircleImageView) findViewById(R.id.doctor_profile_image);

        firebaseAuth = FirebaseAuth.getInstance();

        viewPager2 = findViewById(R.id.viewpager_image_slider_doctor);

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


        doctorsReference = FirebaseDatabase.getInstance().getReference().child("Doctor").child(firebaseAuth.getCurrentUser().getUid());

        doctorsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    if (dataSnapshot.child("image").exists()) {

                        String name = dataSnapshot.child("name").getValue().toString();
                        String age = dataSnapshot.child("age").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        String specialization = dataSnapshot.child("specialization").getValue().toString();
                        String image = dataSnapshot.child("image").getValue().toString();

                        doctorNameTextView.setText(name);
                        doctorAgeTextView.setText("Age "+age);
                        doctorAddressTextView.setText(address);
                        doctorSpecializationTextView.setText(specialization);
                        Picasso.get().load(image).into(doctorProfileImageView);

                    } else {

                        String name = dataSnapshot.child("name").getValue().toString();
                        String age = dataSnapshot.child("age").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        String specialization = dataSnapshot.child("specialization").getValue().toString();

                        doctorNameTextView.setText(name);
                        doctorAgeTextView.setText("Age " + age);
                        doctorAddressTextView.setText(address);
                        doctorSpecializationTextView.setText(specialization);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

}
