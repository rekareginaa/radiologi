package com.example.radiologi.dokter.home.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.radiologi.R;
import com.example.radiologi.data.entitiy.ItemDoctorEntity;

import java.util.ArrayList;
import java.util.List;

public class AdapterDokter extends RecyclerView.Adapter<AdapterDokter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<ItemDoctorEntity> listitemDokters;

    private AdapterDokter.OnItemClickListener mListener = null;

    public AdapterDokter(Context context) {
        this.listitemDokters = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.listitem_dokter, parent, false);
        return new ViewHolder(v);
    }

    void add(ItemDoctorEntity item) {
        listitemDokters.add(item);
        notifyItemInserted(listitemDokters.size());
    }

    void addAll(List<ItemDoctorEntity> list) {
        for (ItemDoctorEntity member: list) {
            add(member);
        }
    }

    void clear() {
        listitemDokters.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ItemDoctorEntity listitemDokter = listitemDokters.get(position);

        holder.noRekam.setText(listitemDokter.getNorekam());
        holder.namaLengkap.setText(listitemDokter.getNamaPasien());
        holder.jenisKelamin.setText(listitemDokter.getGender());
        holder.tanggalLahir.setText(listitemDokter.getTanggalLahir());
        holder.itemView.setOnClickListener(view -> mListener.onItemClick(listitemDokter));
        holder.dokterHolder.setOnClickListener(view -> mListener.onItemClick(listitemDokter));
    }

    @Override
    public int getItemCount() { return listitemDokters.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView noRekam;
        public TextView namaLengkap;
        public TextView jenisKelamin;
        public TextView tanggalLahir;
        public CardView dokterHolder;

        public ViewHolder(View itemView) {
            super(itemView);

            noRekam = itemView.findViewById(R.id.noRekam);
            namaLengkap = itemView.findViewById(R.id.namaLengkap);
            jenisKelamin = itemView.findViewById(R.id.jenisKelamin);
            tanggalLahir = itemView.findViewById(R.id.tanggalLahir);
            dokterHolder = itemView.findViewById(R.id.dokter);
        }
    }
    public void setOnClickListener(AdapterDokter.OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ItemDoctorEntity listitemDokter);
    }
}
