package com.example.bengkel.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bengkel.R;
import com.example.bengkel.model.Mekanik;
import com.example.bengkel.model.Order;
import com.google.android.gms.common.data.DataBuffer;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder> {
    ArrayList<Order> orders=new ArrayList<>();
    ArrayList<Order> ordersAll=new ArrayList<>();;
    private OnItemClickListener listener;

    public OrderRecyclerAdapter(ArrayList<Order> orders) {
        this.orders = orders;
        ordersAll=new ArrayList<Order>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_list_item,parent,false);

        ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        (holder).tvNamaUser.setText(orders.get(position).getNama());
        (holder).tvStatus.setText(orders.get(position).getStatus());
        Date date=orders.get(position).getTimestamp();
        String dateStr=DateFormat.getDateTimeInstance().format(date);
        (holder).tvTime.setText(dateStr);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgStatus;
        TextView tvNamaUser, tvStatus,tvTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaUser=itemView.findViewById(R.id.tv_nama_user);
            tvStatus=itemView.findViewById(R.id.tv_status);
            tvTime=itemView.findViewById(R.id.tv_time);
            imgStatus=itemView.findViewById(R.id.img_status);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onItemClick(orders.get(position));
            }
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Order order);
        void onItemLongClick(Order order);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
