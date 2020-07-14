package com.example.bengkel.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bengkel.R;
import com.example.bengkel.model.Layanan;

import java.util.ArrayList;
import java.util.List;

public class LayananRecyclerAdapter extends RecyclerView.Adapter<LayananRecyclerAdapter.ViewHolder> implements Filterable {


    ArrayList<Layanan> layanans=new ArrayList<>();
    ArrayList<Layanan> layanansAll=new ArrayList<>();;
    private OnItemClickListener listener;

    public LayananRecyclerAdapter(ArrayList<Layanan> layanans) {
        this.layanans=layanans;
        layanansAll= new ArrayList<>(layanans);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_layanan_list_item,parent,false);

        ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        (holder).tvNamaLayanan.setText(layanans.get(position).getNama());
    }

    @Override
    public int getItemCount() {
        return layanans.size();
    }

    public void refresh(ArrayList<Layanan> layanans){
        this.layanansAll=layanans;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        Log.d("GET FILTER PRODUCT",layanans.size()+"");
        Log.d("GET FILTER ALL",layanansAll.size()+"");

        return layanansFilter;
    }

    private Filter layanansFilter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Layanan> layananList=new ArrayList<>();
            if (constraint==null||constraint.length()==0){
                layananList.addAll(layanansAll);
            }else {
                String filterPattern=constraint.toString().toLowerCase().trim();
                Log.d("FILTER ALL",layanans.size()+"");
                for(Layanan item:layanansAll){
                    if (item.getNama().toLowerCase().contains(filterPattern)){
                        layananList.add(item);
                        Log.d("FILTERED",filterPattern);
                    }
                }
                Log.d("FILTER SIZE",layananList.size()+"");

            }
            FilterResults results=new FilterResults();
            results.values=layananList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            layanans.clear();
            layanans.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNamaLayanan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaLayanan=itemView.findViewById(R.id.tv_nama_layanan);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(layanans.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemLongClick(layanans.get(position));
                    }
                    return true;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Layanan layanan);
        void onItemLongClick(Layanan layanan);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
