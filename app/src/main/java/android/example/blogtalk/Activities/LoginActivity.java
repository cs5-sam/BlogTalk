package android.example.blogtalk.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.blogtalk.Fragments.ResetPasswordFragment;
import android.example.blogtalk.R;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rey.material.widget.CheckBox;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    private EditText userEmail,userPassword;
    private Button loginBtn;
    private ProgressBar loadingBar;
    private FirebaseAuth auth;
    private Intent HomeActivity;
    private TextView registerText;
    private TextView forgotText;
    private CheckBox showPasswordChkBox;
    private String code = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initializing view
        userEmail = findViewById(R.id.login_email_edit_text);
        userPassword = findViewById(R.id.login_password_edit_text);
        loginBtn = findViewById(R.id.login_btn);
        loadingBar = findViewById(R.id.login_progressBar);
        registerText = findViewById(R.id.register_here_text);
        forgotText = findViewById(R.id.reset_password_text_login);
        showPasswordChkBox = findViewById(R.id.show_password);

        auth = FirebaseAuth.getInstance();
        HomeActivity = new Intent(this, android.example.blogtalk.Activities.Home.class);

        loadingBar.setVisibility(View.INVISIBLE);

        showPasswordChkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    userPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    userPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerActivity = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(registerActivity);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setVisibility(View.VISIBLE);
                loginBtn.setVisibility(View.INVISIBLE);

                final String mail = userEmail.getText().toString();
                final String password = userPassword.getText().toString();

                if(mail.isEmpty() || password.isEmpty()){
                    showMessage("Please verify fields");
                    loginBtn.setVisibility(View.VISIBLE);
                    loadingBar.setVisibility(View.INVISIBLE);
                }
                else{
                    signIn(mail,password);
                }
            }
        });
        forgotText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetMethod();
            }
        });

    }

    private void ResetMethod(){
        getSupportFragmentManager().beginTransaction().add(android.R.id.content,new ResetPasswordFragment()).commit();
    }

    private void signIn(final String mail,final String password) {
        auth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    loadingBar.setVisibility(View.INVISIBLE);
                    loginBtn.setVisibility(View.VISIBLE);
                    updateUI();
                }
                else{
                    showMessage(task.getException().getMessage());
                    loginBtn.setVisibility(View.VISIBLE);
                    loadingBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateUI() {
        startActivity(HomeActivity);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            // user is already logged in
            updateUI();
        }
    }
}
