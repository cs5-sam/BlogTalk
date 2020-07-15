package android.example.blogtalk.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.example.blogtalk.R;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends AppCompatActivity {

    ImageView postImage;
    CircleImageView commentImage,postUserImage;
    TextView textPostDesc, textPostDate, textPostTitle;
    EditText commentEdit;
    Button addCommentBtn;
    String PostKey;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // status bar hide
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();

        postImage = findViewById(R.id.post_detail_image);
        commentImage = findViewById(R.id.post_comment_image);
        postUserImage = findViewById(R.id.post_user_image_detail);

        textPostDesc = findViewById(R.id.post_description_detail);
        textPostDate = findViewById(R.id.post_date_detail);
        textPostTitle = findViewById(R.id.post_title_detail);

        commentEdit = findViewById(R.id.post_comment_edit_text);
        addCommentBtn = findViewById(R.id.post_submit_comment_btn);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

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
        Glide.with(this).load(userPostImage).into(postUserImage);
        // displaying comment user image
        Glide.with(this).load(user.getPhotoUrl()).into(commentImage);

        // ID
        PostKey = getIntent().getExtras().getString("postKey");
        //String date = timeStampStringConvert(getIntent().getExtras().getLong("postDate"));
        //textPostDate.setText(date);

    }

    /*private String timeStampStringConvert(long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("yyyy-MM-dd",calendar).toString();
        return  date;
    }*/
}
