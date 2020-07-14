package com.example.bengkel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bengkel.R;
import com.example.bengkel.model.Chatroom;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatroomRecyclerAdapter extends RecyclerView.Adapter<ChatroomRecyclerAdapter.ViewHolder>{

    private ArrayList<Chatroom> mChatrooms = new ArrayList<>();
    private ChatroomRecyclerClickListener mChatroomRecyclerClickListener;
    private Context mContext;

    public ChatroomRecyclerAdapter(ArrayList<Chatroom> chatrooms, ChatroomRecyclerClickListener chatroomRecyclerClickListener) {
        this.mChatrooms = chatrooms;
        mChatroomRecyclerClickListener = chatroomRecyclerClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notif_list_item, parent, false);
        final ViewHolder holder = new ViewHolder(view, mChatroomRecyclerClickListener);
        mContext = parent.getContext();

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ((ViewHolder)holder).tvTitle.setText(mChatrooms.get(position).getTitle());
        ((ViewHolder)holder).tvContent.setText(mChatrooms.get(position).getContent());
        ((ViewHolder)holder).tvTime.setText(mChatrooms.get(position).getTitle());

        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.post_placeholder)
                .placeholder(R.drawable.post_placeholder);

        Glide.with(mContext)
                .setDefaultRequestOptions(requestOptions)
                .load(mChatrooms.get(position).getIcon())
                .into((holder).imgIcon);
    }

    @Override
    public int getItemCount() {
        return mChatrooms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener
    {
        TextView tvTitle, tvContent, tvTime;
        CircleImageView imgIcon;
        CardView cardChat;
        ChatroomRecyclerClickListener clickListener;

        public ViewHolder(View itemView, ChatroomRecyclerClickListener clickListener) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tv_title);
            tvContent=itemView.findViewById(R.id.tv_content);
            tvTime=itemView.findViewById(R.id.tv_time);
            imgIcon=itemView.findViewById(R.id.img_icon);
            cardChat=itemView.findViewById(R.id.card_chat);
            this.clickListener = clickListener;

            cardChat.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onChatroomSelected(mChatrooms.get(getAdapterPosition()));
        }
    }

    public interface ChatroomRecyclerClickListener {
        public void onChatroomSelected(Chatroom chatroom);
    }
}
















