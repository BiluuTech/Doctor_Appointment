package app.biluutech.medicineprescription.Activities.Doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.biluutech.medicineprescription.R;
import app.biluutech.medicineprescription.ReviewModel;

public class DoctorRecentReviewsActivity extends AppCompatActivity {

    private String doctorID;

    private RecyclerView recyclerView;

    private DatabaseReference reviewReference, reviewCheckReference;

    private CardView messageBox;

    private Toolbar toolbar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_recent_reviews);

        toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Recent Reviews");

        doctorID = getIntent().getStringExtra("DoctorID");

        recyclerView = findViewById(R.id.recent_reviews_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageBox = findViewById(R.id.message_box_reviews);

        reviewReference = FirebaseDatabase.getInstance().getReference().child("Doctor").child(doctorID).child("Reviews");

//        reviewCheckReference = FirebaseDatabase.getInstance().getReference().child("Doctor").child(doctorID);

//        reviewCheckReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
////                if (snapshot.exists()) {
//                    if (snapshot.child("Reviews").exists()) {
//
//                        messageBox.setVisibility(View.GONE);
//                        onStart();
////                        for (DataSnapshot npsnapshot : snapshot.getChildren()) {
////                            ReviewModel l = npsnapshot.getValue(ReviewModel.class);
////                            reviewModelList.add(l);
////                        }
////                        reviewsAdapter = new ReviewsAdapter(reviewModelList);
////                        recyclerView.setAdapter(reviewsAdapter);
//
//                    } else {
//                        messageBox.setVisibility(View.VISIBLE);
//                    }
////                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();

                FirebaseRecyclerOptions<ReviewModel> options =
                new FirebaseRecyclerOptions.Builder<ReviewModel>()
                        .setQuery(reviewReference, ReviewModel.class)
                        .build();


        FirebaseRecyclerAdapter<ReviewModel, DoctorRecentReviewsActivity.RecentReviewsViewHolder> adapter =
                new FirebaseRecyclerAdapter<ReviewModel, DoctorRecentReviewsActivity.RecentReviewsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final DoctorRecentReviewsActivity.RecentReviewsViewHolder holder, final int position, @NonNull final ReviewModel model) {

                        holder.patientName.setText(model.getPatientName());
                        holder.comment.setText(model.getComment());


                    }

                    @NonNull
                    @Override
                    public DoctorRecentReviewsActivity.RecentReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_reviews_item_layout, parent, false);
                        return new RecentReviewsViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class RecentReviewsViewHolder extends RecyclerView.ViewHolder {

        TextView patientName, comment;

        public RecentReviewsViewHolder(@NonNull View itemView) {
            super(itemView);

            patientName = itemView.findViewById(R.id.review_patient_name_TV);
            comment = itemView.findViewById(R.id.review_patient_comment_TV);

        }

    }

}