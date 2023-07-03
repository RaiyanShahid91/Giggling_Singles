package com.dil.singles.fragments.chats;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dil.singles.R;
import com.dil.singles.databinding.ReceiverLayoutBinding;
import com.dil.singles.databinding.SenderLayoutBinding;
import com.dil.singles.helper.TimeAgo;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message> messages;

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

    String senderRoom;
    String receiverRoom;


    public MessagesAdapter(Context context, ArrayList<Message> messages, String senderRoom, String receiverRoom) {
        this.context = context;
        this.messages = messages;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (FirebaseAuth.getInstance().getUid() == message.getSenderId()) {
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        int[] reactions = new int[]{
                R.drawable.ic_fb_like,
                R.drawable.ic_fb_love,
                R.drawable.ic_fb_laugh,
                R.drawable.ic_fb_wow,
                R.drawable.ic_fb_sad,
                R.drawable.ic_fb_angry,
                R.drawable.superstar,
                R.drawable.kiss,
                R.drawable.cool
        };


        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build();

        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {

            if (pos < 0) {
                return false;
            }

            message.setFeeling(pos);

            FirebaseDatabase.getInstance().getReference()
                    .child("chats")
                    .child(senderRoom)
                    .child("messages")
                    .child(message.getMessageId()).setValue(message);
            return true;
        });

        if (holder.getClass() == SentViewHolder.class) {
            SentViewHolder viewHolder = (SentViewHolder) holder;
            viewHolder.binding.textMessage.setText(message.getMessage());

            String timeAgo = TimeAgo.getTimeAgo(Long.parseLong(String.valueOf(message.getTimestamp())));
            viewHolder.binding.txtDate.setText(timeAgo);

        } else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.binding.textMessage.setText(message.getMessage());

            String timeAgo = TimeAgo.getTimeAgo(Long.parseLong(String.valueOf(message.getTimestamp())));
            viewHolder.binding.txtDate.setText(timeAgo);


            if (message.getFeeling() >= 0) {
                viewHolder.binding.feeling.setImageResource(reactions[message.getFeeling()]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            } else {
                viewHolder.binding.feeling.setVisibility(View.GONE);
            }

            viewHolder.binding.textMessage.setOnTouchListener((view, motionEvent) -> {
                popup.onTouch(view, motionEvent);
                return false;
            });

//            viewHolder.binding.textMessage.setOnClickListener(view -> {
//                int[] coordinates = new int[2];
//                view.getLocationOnScreen(coordinates);
//
//                long downTime = SystemClock.uptimeMillis();
//                long eventTime = SystemClock.uptimeMillis();
//                int action = MotionEvent.ACTION_DOWN;
//                int x = coordinates[0];
//                int y = coordinates[1];
//                int metaState = 0;
//                MotionEvent event = MotionEvent.obtain(downTime, eventTime, action, x, y, metaState);
//                popup.onTouch(view,event);
//            });

        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class SentViewHolder extends RecyclerView.ViewHolder {

        SenderLayoutBinding binding;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SenderLayoutBinding.bind(itemView);
        }
    }

    public static class ReceiverViewHolder extends RecyclerView.ViewHolder {

        ReceiverLayoutBinding binding;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ReceiverLayoutBinding.bind(itemView);
        }
    }

}
