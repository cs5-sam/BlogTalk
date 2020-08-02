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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rey.material.widget.CheckBox;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    CircleImageView userImage;
    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    Uri pickedImageUri;

    private EditText userEmail, userPassword,userConfirmPassword, userName;
    private ProgressBar loadingBar;
    private Button registerBtn;
    private TextView loginTextActivity;
    CheckBox termsCheckBox;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initializing views
        userImage = findViewById(R.id.register_user_image);
        userEmail = findViewById(R.id.register_email_edit_text);
        userPassword = findViewById(R.id.register_password_edit_text);
        userConfirmPassword = findViewById(R.id.register_confirm_password_edit_text);
        userName = findViewById(R.id.register_name_edit_text);
        loadingBar = findViewById(R.id.register_progressBar);
        registerBtn = findViewById(R.id.register_btn);
        loginTextActivity = findViewById(R.id.login_here_text);
        termsCheckBox = findViewById(R.id.terms_and_condition);

        auth = FirebaseAuth.getInstance();

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermission();
                }
                else{
                    openGallery();
                }

            }
        });

        loginTextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        termsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("Long press to read");
            }
        });

        termsCheckBox.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(RegisterActivity.this,TermsActivity.class);
                startActivity(i);
                return false;
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerBtn.setVisibility(View.INVISIBLE);
                loadingBar.setVisibility(View.VISIBLE);

                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                final String confirmPassword = userConfirmPassword.getText().toString();
                final String name = userName.getText().toString();

                if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty() || !password.equals(confirmPassword) || pickedImageUri == null){

                    // fields empty or incorrect;
                    showMessage("Please verify all fields, if not, add image");
                    registerBtn.setVisibility(View.VISIBLE);
                    loadingBar.setVisibility(View.INVISIBLE);
                }
                else if (!termsCheckBox.isChecked()){
                    showMessage("Please read terms & conditions.");
                    registerBtn.setVisibility(View.VISIBLE);
                    loadingBar.setVisibility(View.INVISIBLE);
                }
                else{
                    // everything is ok and all fields are filled
                    CreateUserAccount(email,name,password);
                }
            }
        });
    }

    private void CreateUserAccount(final String email,final String name,final String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // user account created
                            showMessage("Account created successfully!");
                            // after account created we update information i.e name, picture
                            if(pickedImageUri != null){
                                updateUserInfo(name,pickedImageUri,auth.getCurrentUser());
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Add image", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            showMessage("Error occured! " + task.getException().getMessage());
                            registerBtn.setVisibility(View.VISIBLE);
                            loadingBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    // update user photo and name
    private void updateUserInfo(final String name,final Uri pickedImageUri,final FirebaseUser currentUser) {

        //upload user image to firebase storage and get url
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = storageReference.child(pickedImageUri.getLastPathSegment());
        imageFilePath.putFile(pickedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // image uploaded successfully
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // uri contain user image url
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();
                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            // user info updated
                                            showMessage("Registration Complete");
                                            updateUri();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    private void updateUserInfoWithoutImage(final String name,final FirebaseUser currentUser) {

        //upload user image to firebase storage and get url

                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            // user info updated
                                            showMessage("Registration Complete");
                                            updateUri();
                                        }
                                    }
                                });
    }

    private void updateUri() {
        Intent homeIntent = new Intent(RegisterActivity.this,Home.class);
        startActivity(homeIntent);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESTCODE);

    }

    private void checkAndRequestForPermission() {
        if(ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }
        else{
            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null){
            // the user has successfully picked an image
            // we save its reference
            pickedImageUri = data.getData();
            userImage.setImageURI(pickedImageUri);

        }
    }
}
