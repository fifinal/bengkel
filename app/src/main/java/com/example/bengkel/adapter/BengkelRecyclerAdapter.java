package com.example.bengkel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bengkel.R;
import com.example.bengkel.model.Bengkel;
import com.example.bengkel.model.BengkelLocation;

import java.util.ArrayList;

public class BengkelRecyclerAdapter extends RecyclerView.Adapter<BengkelRecyclerAdapter.ViewHolder> {

//    ArrayList<Bengkel> bengkels=new ArrayList<>();
    ArrayList<BengkelLocation> bengkelLocations=new ArrayList<>();
    private BengkelRecyclerAdapter.OnItemClickListener listener;
    private Context mContext;

//    public BengkelRecyclerAdapter(ArrayList<Bengkel> bengkels) {
//        this.bengkels=bengkels;
//    }

    public BengkelRecyclerAdapter(ArrayList<BengkelLocation> bengkelLocations, Context context) {
        this.bengkelLocations=bengkelLocations;
        this.mContext = context;

//        for (BengkelLocation bengkelLocation:bengkelLocations){
//            bengkels.add(bengkelLocation.getBengkel());
//        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bengkel_list_item,parent,false);

        ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvNamaBengkel.setText(bengkelLocations.get(position).getBengkel().getNamaBengkel());
        holder.tvAlamatBengkel.setText(bengkelLocations.get(position).getBengkel().getAlamatBengkel());
        String operational=bengkelLocations.get(position).getBengkel().getBuka()+" - "+bengkelLocations.get(position).getBengkel().getTutup();
        holder.tvBuka.setText(operational);
        holder.tvTelp.setText(bengkelLocations.get(position).getBengkel().getTelp());
        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.post_placeholder)
                .placeholder(R.drawable.post_placeholder);

        Glide.with(mContext)
                .setDefaultRequestOptions(requestOptions)
                .load(bengkelLocations.get(position).getBengkel().getImgBengkel())
                .into((holder).imgBengkel);
    }

    @Override
    public int getItemCount() {
        return bengkelLocations.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgBengkel;
        TextView tvNamaBengkel,tvAlamatBengkel,tvBuka,tvTelp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBengkel=itemView.findViewById(R.id.img_produk);
            tvNamaBengkel=itemView.findViewById(R.id.tv_nama_produk);
            tvAlamatBengkel=itemView.findViewById(R.id.tv_stok);
            tvBuka=itemView.findViewById(R.id.tv_buka);
            tvTelp=itemView.findViewById(R.id.tv_telp);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(v,bengkelLocations.get(position),position);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemLongClick(bengkelLocations.get(position).getBengkel());
                    }
                    return false;
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view,BengkelLocation bengkelLocation,int position);
        void onItemLongClick(Bengkel bengkel);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
