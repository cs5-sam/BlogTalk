package android.example.blogtalk.Adapter;

import android.content.Context;
import android.example.blogtalk.Models.Chat;
import android.example.blogtalk.Models.Comment;
import android.example.blogtalk.R;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context mContext;
    private List<Chat> mData;

    public ChatAdapter(Context mContext, List<Chat> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_chatmessage,parent,false);
        return new ChatViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Glide.with(mContext).load(mData.get(position).getUimg()).into(holder.image_user);
        holder.tv_content.setText(mData.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image_user;
        TextView tv_name,tv_content,tv_date;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            image_user = itemView.findViewById(R.id.chat_display_user_image);
            tv_content = itemView.findViewById(R.id.chat_display_message_text);
        }
    }

    private String timeStampStringConvert(long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("hh:mm",calendar).toString();
        return  date;
    }
}
