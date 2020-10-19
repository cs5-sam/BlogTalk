package android.example.blogtalk.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.example.blogtalk.Adapter.CommentAdapter;
import android.example.blogtalk.Models.Comment;
import android.example.blogtalk.R;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends AppCompatActivity {

    private ImageView postImage;
    private CircleImageView commentImage,postUserImage;
    private TextView textPostDesc, textPostDate, textPostTitle;
    private EditText commentEdit;
    private Button addCommentBtn;
    private String PostKey;
    private AdView adView;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;

    RecyclerView RvComment;
    CommentAdapter commentAdapter;
    List<Comment> listComment;
    static  String COMMENT_KEY = "Comment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // status bar hide
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();

        RvComment = findViewById(R.id.rv_comment);
        postImage = findViewById(R.id.post_detail_image);
        commentImage = findViewById(R.id.post_comment_image);
        postUserImage = findViewById(R.id.post_user_image_detail);

        textPostDesc = findViewById(R.id.post_description_detail);
        textPostDate = findViewById(R.id.post_date_detail);
        textPostTitle = findViewById(R.id.post_title_detail);

        commentEdit = findViewById(R.id.post_comment_edit_text);
        addCommentBtn = findViewById(R.id.post_submit_comment_btn);
        adView = findViewById(R.id.ad_view);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // AD REQUEST
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);

        // add Comment button click listener
        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addCommentBtn.setVisibility(View.INVISIBLE);
                DatabaseReference commentReference = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey).push();
                String comment_content = commentEdit.getText().toString();
                if (comment_content.equals("")) {
                    showMessage("Enter valid comment");
                    addCommentBtn.setVisibility(View.VISIBLE);
                } else {

                    String uid = user.getUid();
                    String uname = user.getDisplayName();
                    String uimg = user.getPhotoUrl().toString();
                    Comment comment = new Comment(comment_content, uid, uimg, uname);
                    commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            showMessage("comment added");
                            commentEdit.setText("");
                            addCommentBtn.setVisibility(View.VISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMessage("Error occurred " + e.getMessage());
                            addCommentBtn.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        // displaying image
        String postImagetext = getIntent().getExtras().getString("postImage");
        Glide.with(this).load(postImagetext).into(postImage);
        // displaying title
        String postTitle = getIntent().getExtras().getString("title");
        textPostTitle.setText(postTitle);
        // displaying description
        String postDesc = getIntent().getExtras().getString("description");
        textPostDesc.setText(postDesc);
        // displaying userImage
        String userPostImage = getIntent().getExtras().getString("userPhoto");

        if(userPostImage != null){
            Glide.with(this).load(userPostImage).into(postUserImage);
        }
        else{
            Glide.with(this).load(R.drawable.userphoto).into(postUserImage);
        }

        // displaying comment user image

        if(user.getPhotoUrl() != null){
            Glide.with(this).load(user.getPhotoUrl()).into(commentImage);
        }
        else{
            Glide.with(this).load(R.drawable.userphoto).into(commentImage);
        }

        // ID
        PostKey = getIntent().getExtras().getString("postKey");
        //String date = timeStampStringConvert(getIntent().getExtras().getLong("postDate"));
        //textPostDate.setText("By || "+user.getDisplayName());

        // Initialize RecyclerView
        iniRvComment();
    }

    private void iniRvComment() {

        RvComment.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listComment = new ArrayList<>();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Comment comment = snapshot1.getValue(Comment.class);
                    listComment.add(comment);
                }
                commentAdapter = new CommentAdapter(getApplicationContext(),listComment);
                RvComment.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /*private String timeStampStringConvert(long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("yyyy-MM-dd",calendar).toString();
        return  date;
       }*/
}
