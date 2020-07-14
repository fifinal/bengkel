package com.example.bengkel.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bengkel.R;
import com.example.bengkel.model.Mekanik;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LaporanRecyclerAdapter extends RecyclerView.Adapter<LaporanRecyclerAdapter.ViewHolder> {
    ArrayList<Mekanik> mekaniks=new ArrayList<>();
    ArrayList<Mekanik> mekaniksAll=new ArrayList<>();;
    private OnItemClickListener listener;

    public LaporanRecyclerAdapter(ArrayList<Mekanik> mekaniks) {
        this.mekaniks = mekaniks;
        mekaniksAll=new ArrayList<>(mekaniks);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mekanik_list_item,parent,false);

        ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        (holder).tvNamaMekanik.setText(mekaniks.get(position).getNama());
    }

    @Override
    public int getItemCount() {
        return mekaniks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView imgProfile;
        TextView tvNamaMekanik;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile=itemView.findViewById(R.id.img_profile_mekanik);
            tvNamaMekanik=itemView.findViewById(R.id.tv_nama_mekanik);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onItemClick(mekaniks.get(position));
            }
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Mekanik mekanik);
        void onItemLongClick(Mekanik mekanik);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
