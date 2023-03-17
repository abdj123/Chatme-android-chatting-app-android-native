package com.example.uidesign1;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.*;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.MessageHoler> {

    private final ArrayList<messageHold> messages;
    private final String senderimg;
    private final String reciverimg;
    private final Context context;

    public static final int msg_sent = 0;
    public static final int msg_receiv = 1;


    public messageAdapter(ArrayList<messageHold> messages, String senderimg, String reciverimg, Context context) {
        this.messages = messages;
        this.senderimg = senderimg;
        this.reciverimg = reciverimg;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.floatin2, parent, false);
        return new MessageHoler(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MessageHoler holder, int position) {

        holder.receiver_message_text.setVisibility(View.GONE);
        holder.sender_message_text.setVisibility(View.GONE);

        if (messages.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {

            holder.sender_message_text.setVisibility(View.VISIBLE);

            holder.sender_message_text.setBackgroundResource(R.drawable.best_back2);
            holder.sender_message_text.setTextColor(Color.BLACK);
            holder.receiver_message_text.setHeight(0);
            holder.receiver_message_text.setElevation(0);
            holder.sender_message_text.setText(messages.get(position).getContent());
        }else {
            holder.receiver_message_text.setVisibility(View.VISIBLE);

            holder.receiver_message_text.setBackgroundResource(R.drawable.best_back3);
            holder.sender_message_text.setHeight(0);
            holder.sender_message_text.setElevation(0);
            holder.receiver_message_text.setTextColor(Color.BLACK);
            holder.receiver_message_text.setText(messages.get(position).getContent());

        }

    }
    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageHoler extends RecyclerView.ViewHolder {
//        ConstraintLayout cclayout;
//        TextView textmsg;
//        ImageView textingimg;

        TextView showmsg;
        RelativeLayout realboy;

        TextView receiver_message_text, sender_message_text;

        public MessageHoler(@NonNull View itemView) {
            super(itemView);
            showmsg = itemView.findViewById(R.id.showmsg);
            realboy = itemView.findViewById(R.id.realboy);

            sender_message_text = itemView.findViewById(R.id.sender_message_text);
            receiver_message_text = itemView.findViewById(R.id.receiver_message_text);
        }
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (messages.get(position).getSender().equals(fuser.getUid())) {
            return msg_receiv;
        } else {
            return msg_sent;
        }
    }
}
