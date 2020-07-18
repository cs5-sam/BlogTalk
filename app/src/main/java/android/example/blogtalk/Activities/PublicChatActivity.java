package android.example.blogtalk.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.example.blogtalk.Adapter.ChatAdapter;
import android.example.blogtalk.Adapter.CommentAdapter;
import android.example.blogtalk.Models.Chat;
import android.example.blogtalk.Models.Comment;
import android.example.blogtalk.R;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.example.blogtalk.Activities.PostDetailActivity.COMMENT_KEY;

public class PublicChatActivity extends AppCompatActivity {

    CircleImageView chatUserImage;
    EditText chatMessage;
    Button sendBtn;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    String postKey;

    RecyclerView RvChat;
    ChatAdapter chatAdapter;
    List<Chat> listChat;
    static String CHAT_KEY = "Chat";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_chat);

        chatUserImage = findViewById(R.id.chat_user_image);
        chatMessage = findViewById(R.id.chat_message_edit_text);
        sendBtn = findViewById(R.id.chat_send_btn);
        RvChat = findViewById(R.id.rv_chat);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        Glide.with(this).load(user.getPhotoUrl()).into(chatUserImage);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendBtn.setVisibility(View.INVISIBLE);
                DatabaseReference chatReference = firebaseDatabase.getReference(CHAT_KEY).push();
                String chat_content = chatMessage.getText().toString();
                String uid = user.getUid();
                String uimg = user.getPhotoUrl().toString();
                Chat chat = new Chat(uid,chat_content,uimg);
                chatReference.setValue(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        chatMessage.setText("");
                        sendBtn.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("error",e.getMessage());
                    }
                });
            }
        });
        iniChat();
    }

    private void iniChat() {
        RvChat.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference chatRef = firebaseDatabase.getReference(CHAT_KEY);
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listChat = new ArrayList<>();
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Chat chat = snapshot1.getValue(Chat.class);
                    listChat.add(chat);
                }
                chatAdapter = new ChatAdapter(getApplicationContext(),listChat);
                RvChat.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
