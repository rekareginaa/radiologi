package com.example.radiologi.admin.home.fragments;

import android.content.Context;
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

import com.example.radiologi.model.ListitemAdmin;
import com.example.radiologi.R;

public class AdapterAdmin extends RecyclerView.Adapter<AdapterAdmin.ViewHolder> {

    LayoutInflater inflater;
    List<ListitemAdmin> listitemAdmins;
    Context context;
    int status;

    private AdapterAdmin.OnItemClickListener mListener = null;

    public AdapterAdmin(Context context, int status) {
        this.listitemAdmins = new ArrayList<>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.status = status;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.listitem_admin, parent, false);
        return new ViewHolder(v);
    }

    void add(ListitemAdmin item) {
        listitemAdmins.add(item);
        notifyItemInserted(listitemAdmins.size());
    }

    void addAll(List<ListitemAdmin> list) {
        for (ListitemAdmin member: list) {
            add(member);
        }
    }

    void clear() {
        listitemAdmins.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ListitemAdmin listitemAdmin = listitemAdmins.get(position);

        switch (status) {
            case 0:
                if (listitemAdmin.getStatus().equals("0")) {
                    holder.noRekam.setText(listitemAdmin.getNoRekam());
                    holder.namaLengkap.setText(listitemAdmin.getNamaLengkap());
                    holder.jenisKelamin.setText(listitemAdmin.getGender());
                    holder.tanggalLahir.setText(listitemAdmin.getTangLahir());
                    holder.itemView.setOnClickListener(view -> mListener.onItemClick(listitemAdmin));
                    holder.adminHolder.setOnClickListener(view -> mListener.onItemClick(listitemAdmin));
                }
            case 1:
                if (listitemAdmin.getStatus().equals("1")) {
                    holder.adminHolder.setCardBackgroundColor(Color.parseColor("#038C7F"));
                    holder.noRekam.setTextColor(Color.WHITE);
                    holder.namaLengkap.setTextColor(Color.WHITE);
                    holder.jenisKelamin.setTextColor(Color.WHITE);
                    holder.separator.setTextColor(Color.WHITE);
                    holder.tanggalLahir.setTextColor(Color.WHITE);
                    holder.noRekam.setText(listitemAdmin.getNoRekam());
                    holder.namaLengkap.setText(listitemAdmin.getNamaLengkap());
                    holder.jenisKelamin.setText(listitemAdmin.getGender());
                    holder.tanggalLahir.setText(listitemAdmin.getTangLahir());
                    holder.itemView.setOnClickListener(view -> mListener.onItemClick(listitemAdmin));
                    holder.adminHolder.setOnClickListener(view -> mListener.onItemClick(listitemAdmin));
                } else if (listitemAdmin.getStatus().equals("2")) {
                    holder.adminHolder.setCardBackgroundColor(Color.WHITE);
                    holder.noRekam.setTextColor(Color.DKGRAY);
                    holder.namaLengkap.setTextColor(Color.DKGRAY);
                    holder.jenisKelamin.setTextColor(Color.DKGRAY);
                    holder.separator.setTextColor(Color.DKGRAY);
                    holder.tanggalLahir.setTextColor(Color.DKGRAY);
                    holder.noRekam.setText(listitemAdmin.getNoRekam());
                    holder.namaLengkap.setText(listitemAdmin.getNamaLengkap());
                    holder.jenisKelamin.setText(listitemAdmin.getGender());
                    holder.tanggalLahir.setText(listitemAdmin.getTangLahir());
                    holder.itemView.setOnClickListener(view -> mListener.onItemClick(listitemAdmin));
                    holder.adminHolder.setOnClickListener(view -> mListener.onItemClick(listitemAdmin));
                }
        }
    }

    @Override
    public int getItemCount() {
        return listitemAdmins.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView noRekam;
        public TextView namaLengkap;
        public TextView jenisKelamin;
        public TextView separator;
        public TextView tanggalLahir;
        public CardView adminHolder;

        public ViewHolder(View itemView) {
            super(itemView);

            noRekam = itemView.findViewById(R.id.noRekam);
            namaLengkap = itemView.findViewById(R.id.namaLengkap);
            jenisKelamin = itemView.findViewById(R.id.jenisKelamin);
            separator = itemView.findViewById(R.id.separator);
            tanggalLahir = itemView.findViewById(R.id.tanggalLahir);
            adminHolder = itemView.findViewById(R.id.admin);
        }
    }

    public void setOnClickListener(AdapterAdmin.OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ListitemAdmin listitemAdmin);
    }
}
