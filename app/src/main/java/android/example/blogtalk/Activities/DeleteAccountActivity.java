package android.example.blogtalk.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.blogtalk.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DeleteAccountActivity extends AppCompatActivity {

    private EditText userEmail,userPassword;
    private Button deleteBtn;
    private ProgressBar loadingBar;

    private String password,email;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        userPassword = findViewById(R.id.delete_password_edit_text);
        userEmail = findViewById(R.id.delete_email_edit_text);
        deleteBtn = findViewById(R.id.delete_account_btn);
        loadingBar = findViewById(R.id.delete_progressBar);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = userPassword.getText().toString();
                email = userEmail.getText().toString();
                if(email.equals("") || password.equals("")){
                    showMessage("Please fill each field");
                }else{
                    loadingBar.setVisibility(View.VISIBLE);
                    deleteBtn.setVisibility(View.INVISIBLE);
                    deleteAccount();
                }
            }
        });
    }

    private void deleteAccount() {

        if(user!=null){
            AuthCredential credential = EmailAuthProvider
                    .getCredential(email, password);

            // Prompt the user to re-provide their sign-in credentials
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent i = new Intent(DeleteAccountActivity.this,LoginActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                            else{
                                                showMessage("Error occurred");
                                                loadingBar.setVisibility(View.INVISIBLE);
                                                deleteBtn.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });

                        }
                    });
        }
    }

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}
