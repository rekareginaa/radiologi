package com.example.radiologi;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterDokter extends RecyclerView.Adapter<AdapterDokter.ViewHolder> {

    private  LayoutInflater inflater;
    private List<ListitemDokter> listitemDokters;
    private Context context;

    private AdapterDokter.OnItemClickListener mListener = null;

    public AdapterDokter(Context context) {
        this.listitemDokters = new ArrayList<>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.listitem_dokter, parent, false);
        return new ViewHolder(v);
    }

    void add(ListitemDokter item) {
        listitemDokters.add(item);
        notifyItemInserted(listitemDokters.size());
    }

    void addAll(List<ListitemDokter> list) {
        for (ListitemDokter member: list) {
            add(member);
        }
    }

    void clear() {
        listitemDokters.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListitemDokter listitemDokter = listitemDokters.get(position);

        holder.noRekam.setText(listitemDokter.getNoRekam());
        holder.namaLengkap.setText(listitemDokter.getNamaLengkap());
        holder.jenisKelamin.setText(listitemDokter.getGender());
        holder.tanggalLahir.setText(listitemDokter.getTangLahir());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(listitemDokter);
            }
        });
        holder.dokterHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(listitemDokter);
            }
        });
    }

    @Override
    public int getItemCount() { return listitemDokters.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView noRekam;
        public TextView namaLengkap;
        public TextView jenisKelamin;
        public TextView tanggalLahir;
        public CardView dokterHolder;

        public ViewHolder(View itemView) {
            super(itemView);

            noRekam = (TextView) itemView.findViewById(R.id.noRekam);
            namaLengkap = (TextView) itemView.findViewById(R.id.namaLengkap);
            jenisKelamin = itemView.findViewById(R.id.jenisKelamin);
            tanggalLahir = itemView.findViewById(R.id.tanggalLahir);
            dokterHolder = itemView.findViewById(R.id.dokter);
        }
    }
    public void setOnClickListener(AdapterDokter.OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ListitemDokter listitemDokter);
    }
}
