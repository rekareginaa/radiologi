package com.example.radiologi.admin.home.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.radiologi.R;
import com.example.radiologi.data.entitiy.ItemAdminEntity;

public class AdapterAdmin extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    LayoutInflater inflater;
    List<ItemAdminEntity> listitemAdmins;

    List<Integer> listViewType;
    Context context;
    int status;

    private AdapterAdmin.OnItemClickListener mListener = null;

    public static final int ITEM_VIEW_TYPE_LOADING = 1;
    public static final int ITEM_VIEW_TYPE_CONTENT = 2;

    public AdapterAdmin(Context context, int status, List<Integer> listViewType) {
        this.listitemAdmins = new ArrayList<>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.status = status;
        this.listViewType = listViewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_VIEW_TYPE_CONTENT:
                View v = inflater.inflate(R.layout.listitem_admin, parent, false);
                return new ContentHolder(v);
            case ITEM_VIEW_TYPE_LOADING:
                View view = inflater.inflate(R.layout.item_loading, parent, false);
                return new LoadingHolder(view);
            default:
                return null;
        }
    }

    void add(ItemAdminEntity item) {
        listitemAdmins.add(item);
        notifyItemInserted(listitemAdmins.size());
    }

    @SuppressLint("NotifyDataSetChanged")
    void addAll(List<ItemAdminEntity> list) {
        clear();
        for (ItemAdminEntity member : list) {
            add(member);
        }
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    void clear() {
        listitemAdmins.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int viewType = listViewType.get(position);

        if (viewType == ITEM_VIEW_TYPE_LOADING) {
        } else if (viewType == ITEM_VIEW_TYPE_CONTENT) {
            if (holder instanceof ContentHolder){
                final ItemAdminEntity listitemAdmin = listitemAdmins.get(position);
                final ContentHolder contentHolder = (ContentHolder) holder;
                switch (status) {
                    case 0:
                        if (listitemAdmin.getStatus().equals("0")) {
                            contentHolder.noRekam.setText(listitemAdmin.getNorekam());
                            contentHolder.namaLengkap.setText(listitemAdmin.getNamaPasien());
                            contentHolder.jenisKelamin.setText(listitemAdmin.getGender());
                            contentHolder.tanggalLahir.setText(listitemAdmin.getTanggalLahir());
                            contentHolder.itemView.setOnClickListener(view -> mListener.onItemClick(listitemAdmin));
                            contentHolder.adminHolder.setOnClickListener(view -> mListener.onItemClick(listitemAdmin));
                        }
                    case 1:
                        if (listitemAdmin.getStatus().equals("1")) {
                            contentHolder.adminHolder.setCardBackgroundColor(Color.parseColor("#038C7F"));
                            contentHolder.noRekam.setTextColor(Color.WHITE);
                            contentHolder.namaLengkap.setTextColor(Color.WHITE);
                            contentHolder.jenisKelamin.setTextColor(Color.WHITE);
                            contentHolder.separator.setTextColor(Color.WHITE);
                            contentHolder.tanggalLahir.setTextColor(Color.WHITE);
                            contentHolder.noRekam.setText(listitemAdmin.getNorekam());
                            contentHolder.namaLengkap.setText(listitemAdmin.getNamaPasien());
                            contentHolder.jenisKelamin.setText(listitemAdmin.getGender());
                            contentHolder.tanggalLahir.setText(listitemAdmin.getTanggalLahir());
                            contentHolder.itemView.setOnClickListener(view -> mListener.onItemClick(listitemAdmin));
                            contentHolder.adminHolder.setOnClickListener(view -> mListener.onItemClick(listitemAdmin));
                        } else if (listitemAdmin.getStatus().equals("2")) {
                            contentHolder.adminHolder.setCardBackgroundColor(Color.WHITE);
                            contentHolder.noRekam.setTextColor(Color.DKGRAY);
                            contentHolder.namaLengkap.setTextColor(Color.DKGRAY);
                            contentHolder.jenisKelamin.setTextColor(Color.DKGRAY);
                            contentHolder.separator.setTextColor(Color.DKGRAY);
                            contentHolder.tanggalLahir.setTextColor(Color.DKGRAY);
                            contentHolder.noRekam.setText(listitemAdmin.getNorekam());
                            contentHolder.namaLengkap.setText(listitemAdmin.getNamaPasien());
                            contentHolder.jenisKelamin.setText(listitemAdmin.getGender());
                            contentHolder.tanggalLahir.setText(listitemAdmin.getTanggalLahir());
                            contentHolder.itemView.setOnClickListener(view -> mListener.onItemClick(listitemAdmin));
                            contentHolder.adminHolder.setOnClickListener(view -> mListener.onItemClick(listitemAdmin));
                        }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return listitemAdmins.size();
    }

    @Override
    public int getItemViewType(int position) {
        return listViewType.get(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refresh(ArrayList<ItemAdminEntity> listData, ArrayList<Integer> listViewType){
        this.listitemAdmins = listData;
        this.listViewType = listViewType;
        notifyDataSetChanged();
    }

    public static class ContentHolder extends RecyclerView.ViewHolder {

        public TextView noRekam;
        public TextView namaLengkap;
        public TextView jenisKelamin;
        public TextView separator;
        public TextView tanggalLahir;
        public CardView adminHolder;

        public ContentHolder(View itemView) {
            super(itemView);

            noRekam = itemView.findViewById(R.id.noRekam);
            namaLengkap = itemView.findViewById(R.id.namaLengkap);
            jenisKelamin = itemView.findViewById(R.id.jenisKelamin);
            separator = itemView.findViewById(R.id.separator);
            tanggalLahir = itemView.findViewById(R.id.tanggalLahir);
            adminHolder = itemView.findViewById(R.id.admin);
        }
    }

    public static class LoadingHolder extends RecyclerView.ViewHolder {
        public LoadingHolder(View itemView) {
            super(itemView);
        }
    }

    public void setOnClickListener(AdapterAdmin.OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ItemAdminEntity listitemAdmin);
    }
}
