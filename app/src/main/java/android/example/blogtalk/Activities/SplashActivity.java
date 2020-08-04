package android.example.blogtalk.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.blogtalk.R;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private ImageView splashImage;
    private TextView splashText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashImage = findViewById(R.id.splash_image);
        splashText = findViewById(R.id.splash_text);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.myanimation);
        Animation transition = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        splashImage.startAnimation(animation);
        splashText.startAnimation(animation);
        final Intent i = new Intent(SplashActivity.this,LoginActivity.class);
        Thread timer = new Thread(){
            public void run() {
                try{
                    sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}