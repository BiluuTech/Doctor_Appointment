package app.biluutech.medicineprescription;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private ImageView appLogo;
    private TextView medicineTV, prescriptionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.text_delay);

        appLogo = (ImageView) findViewById(R.id.appLogoIV);
        medicineTV = (TextView) findViewById(R.id.medicineTV);
        prescriptionTV = (TextView) findViewById(R.id.prescriptionTV);

        medicineTV.startAnimation(animation1);
        prescriptionTV.startAnimation(animation1);

        appLogo.startAnimation(animation);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);

    }
}
