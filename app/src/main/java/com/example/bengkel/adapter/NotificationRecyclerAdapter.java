package com.example.bengkel.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bengkel.R;
import com.example.bengkel.model.Notif;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.ViewHolder> {
    ArrayList<Notif> notifArrayList=new ArrayList<>();

    public NotificationRecyclerAdapter(ArrayList<Notif> notifArrayList) {
        this.notifArrayList = notifArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notif_list_item,parent,false);

        NotificationRecyclerAdapter.ViewHolder holder=new NotificationRecyclerAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.nama.setText(notifArrayList.get(position).getName());
        holder.content.setText(notifArrayList.get(position).getContent());
        holder.time.setText(notifArrayList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return notifArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,content,time;
        CircleImageView imgIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama=itemView.findViewById(R.id.tv_title);
            content=itemView.findViewById(R.id.tv_content);
            time=itemView.findViewById(R.id.tv_time);
            imgIcon=itemView.findViewById(R.id.img_icon);
        }
    }
}
