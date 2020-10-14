package android.example.blogtalk.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.example.blogtalk.Fragments.ResetPasswordFragment;
import android.example.blogtalk.R;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private EditText userNewName;
    private Button changeBtn,resetPasswordBtnSettings;
    private CircleImageView userNewImage;
    private ProgressBar loadingBar;


    static int PReqCode = 3;
    static int REQUESTCODE = 3;
    Uri pickedImageUri;

    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        userNewImage = findViewById(R.id.settings_user_image);
        userNewName = findViewById(R.id.settings_name_edit_text);

        changeBtn = findViewById(R.id.settings_change_btn);
        loadingBar = findViewById(R.id.settings_progressBar);
        resetPasswordBtnSettings = findViewById(R.id.forgot_btn_settings);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        loadingBar.setVisibility(View.INVISIBLE);
        changeBtn.setVisibility(View.VISIBLE);

        userNewName.setText(user.getDisplayName());
        Glide.with(this).load(user.getPhotoUrl()).into(userNewImage);

        userNewImage.setOnClickListener(new View.OnClickListener() {
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

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setVisibility(View.VISIBLE);
                changeBtn.setVisibility(View.INVISIBLE);

                String name = userNewName.getText().toString();

                if (pickedImageUri == null) {
                    showMessage("Please choose an image");
                    loadingBar.setVisibility(View.INVISIBLE);
                    changeBtn.setVisibility(View.VISIBLE);

                }
                else if(name.equals("")){
                    name = user.getDisplayName();
                    uploadData(name);
                }
                else {
                    uploadData(name);
                }
            }
        });

        resetPasswordBtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i =  new Intent(SettingsActivity.this,LoginActivity.class);
                startActivity(i);
//                getSupportFragmentManager().beginTransaction().add(android.R.id.content,new ResetPasswordFragment()).commit();
                finish();
            }
        });
    }


    private void uploadData(final String name){
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
                        user.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            // user info updated
                                            showMessage("Data updated");
                                            updateUri();
                                        }
                                    }
                                });
                    }
                });
            }
        });

    }

    private void updateUri() {
        Intent homeIntent = new Intent(SettingsActivity.this,Home.class);
        startActivity(homeIntent);
        finish();
    }

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESTCODE);

    }

    private void checkAndRequestForPermission() {
        if(ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(SettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions(SettingsActivity.this,
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
            userNewImage.setImageURI(pickedImageUri);

        }
    }
}