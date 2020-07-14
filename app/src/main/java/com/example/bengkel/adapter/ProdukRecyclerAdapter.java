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
import com.example.bengkel.model.Produk;
import com.example.bengkel.ui.bengkel.ProfileBengkelActivity;

import java.util.ArrayList;
import java.util.List;

public class ProdukRecyclerAdapter extends RecyclerView.Adapter<ProdukRecyclerAdapter.ViewHolder> implements Filterable {


    ArrayList<Produk> produks=new ArrayList<>();
    ArrayList<Produk> produksAll=new ArrayList<>();;
    private OnItemClickListener listener;

    public ProdukRecyclerAdapter(ArrayList<Produk> produks) {

        this.produks=produks;
        produksAll= new ArrayList<>(produks);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_produk_list_item,parent,false);

        ViewHolder holder=new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        (holder).tvNamaProduk.setText(produks.get(position).getNamaProduk());
        (holder).tvStok.setText("stok : "+produks.get(position).getStok());
        (holder).tvHarga.setText("Rp "+produks.get(position).getHarga());
    }

    @Override
    public int getItemCount() {
        return produks.size();
    }

    public void refresh(ArrayList<Produk> produks){
        this.produksAll=produks;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        Log.d("GET FILTER PRODUCT",produks.size()+"");
        Log.d("GET FILTER ALL",produksAll.size()+"");

        return produksFilter;
    }

    private Filter produksFilter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Produk> produkList=new ArrayList<>();
            if (constraint==null||constraint.length()==0){
                produkList.addAll(produksAll);
            }else {
                String filterPattern=constraint.toString().toLowerCase().trim();
                Log.d("FILTER ALL",produks.size()+"");
                for(Produk item:produksAll){
                    if (item.getNamaProduk().toLowerCase().contains(filterPattern)){
                        produkList.add(item);
                        Log.d("FILTERED",filterPattern);
                    }
                }
                Log.d("FILTER SIZE",produkList.size()+"");

            }
            FilterResults results=new FilterResults();
            results.values=produkList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            produks.clear();
            produks.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNamaProduk, tvStok, tvHarga;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaProduk=itemView.findViewById(R.id.tv_nama_produk);
            tvStok=itemView.findViewById(R.id.tv_stok);
            tvHarga=itemView.findViewById(R.id.tv_harga);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(produks.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemLongClick(produks.get(position));
                    }
                    return true;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Produk produk);
        void onItemLongClick(Produk produk);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
