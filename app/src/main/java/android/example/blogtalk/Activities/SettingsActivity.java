package android.example.blogtalk.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.example.blogtalk.R;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private EditText newPassword;
    private ProgressBar loadingBar;
    private Button changePasswordBtn, deactivateAccountBtn;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        newPassword = findViewById(R.id.settings_password_edit_text);
        changePasswordBtn = findViewById(R.id.change_password_btn);
        deactivateAccountBtn = findViewById(R.id.deactivate_btn);
        loadingBar = findViewById(R.id.settings_progressBar);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        changePasswordBtn.setVisibility(View.VISIBLE);
        loadingBar.setVisibility(View.INVISIBLE);

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordBtn.setVisibility(View.INVISIBLE);
                loadingBar.setVisibility(View.VISIBLE);
                change();
            }
        });

        deactivateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deactivateAccountBtn.setVisibility(View.INVISIBLE);
                loadingBar.setVisibility(View.VISIBLE);
                deactivate();
            }
        });

    }

    public void change(){
        if(user != null) {
            user.updatePassword(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        showMessage("Password changed successfully!");
                        changePasswordBtn.setVisibility(View.VISIBLE);
                        loadingBar.setVisibility(View.INVISIBLE);
                        auth.signOut();
                        Intent i = new Intent(SettingsActivity.this,LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        showMessage("Error occurred");
                        changePasswordBtn.setVisibility(View.VISIBLE);
                        loadingBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    public void deactivate(){
        if(user != null){
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                 if(task.isSuccessful()) {
                        showMessage("Account deleted!");
                         deactivateAccountBtn.setVisibility(View.VISIBLE);
                         loadingBar.setVisibility(View.INVISIBLE);
                         Intent i = new Intent(SettingsActivity.this,LoginActivity.class);
                         startActivity(i);
                         finish();
                    }
                    else{
                        showMessage("Could not be deleted");
                        deactivateAccountBtn.setVisibility(View.VISIBLE);
                        loadingBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}