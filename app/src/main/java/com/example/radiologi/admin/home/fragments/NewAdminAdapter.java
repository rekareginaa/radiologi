package com.example.radiologi.admin.home.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.radiologi.R;
import com.example.radiologi.data.entitiy.ItemAdminEntity;
import com.example.radiologi.utils.rv.BaseViewHolder;

import java.util.List;

public class NewAdminAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    private final int status;
    private final List<ItemAdminEntity> adminEntities;
    private OnItemClickListener mListener = null;

    public NewAdminAdapter(List<ItemAdminEntity> adminEntities, int status){
        this.status = status;
        this.adminEntities = adminEntities;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                  inflater.inflate(R.layout.listitem_admin, parent, false)
                );
            case VIEW_TYPE_LOADING:
                return new LoadingViewHolder(
                        inflater.inflate(R.layout.item_loading, parent, false)
                );
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible){
            return (position == adminEntities.size() -1 && isLoaderVisible) ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        }else{
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return adminEntities == null ? 0 : adminEntities.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addItems(List<ItemAdminEntity> itemAdminEntities){
        adminEntities.addAll(itemAdminEntities);
        notifyDataSetChanged();
    }

    public void addLoading(){
        isLoaderVisible = true;
        adminEntities.add(new ItemAdminEntity("", "", "", "", "", "", "", "", "", "", "", "", 0));
        notifyItemInserted(adminEntities.size() - 1);
    }

    public void removeLoading(){
        isLoaderVisible = false;
        int position = adminEntities.size() - 1;
        ItemAdminEntity itemAdminEntity = getItem(position);
        if (itemAdminEntity != null){
            adminEntities.remove(position);
            notifyItemRemoved(position);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear(){
        adminEntities.clear();
        notifyDataSetChanged();
    }

    ItemAdminEntity getItem(int position){
        return adminEntities.get(position);
    }

    public class ViewHolder extends BaseViewHolder{

        public TextView noRekam;
        public TextView namaLengkap;
        public TextView jenisKelamin;
        public TextView separator;
        public TextView tanggalLahir;
        public CardView adminHolder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noRekam = itemView.findViewById(R.id.noRekam);
            namaLengkap = itemView.findViewById(R.id.namaLengkap);
            jenisKelamin = itemView.findViewById(R.id.jenisKelamin);
            separator = itemView.findViewById(R.id.separator);
            tanggalLahir = itemView.findViewById(R.id.tanggalLahir);
            adminHolder = itemView.findViewById(R.id.admin);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            ItemAdminEntity item = adminEntities.get(position);
            switch (status) {
                case 0:
                    if (item.getStatus().equals("0")) {
                        noRekam.setText(item.getNorekam());
                        namaLengkap.setText(item.getNamaPasien());
                        jenisKelamin.setText(item.getGender());
                        tanggalLahir.setText(item.getTanggalLahir());
                        itemView.setOnClickListener(view -> mListener.onItemClick(item));
                        adminHolder.setOnClickListener(view -> mListener.onItemClick(item));
                    }
                case 1:
                    if (item.getStatus().equals("1")) {
                        adminHolder.setCardBackgroundColor(Color.parseColor("#038C7F"));
                        noRekam.setTextColor(Color.WHITE);
                        namaLengkap.setTextColor(Color.WHITE);
                        jenisKelamin.setTextColor(Color.WHITE);
                        separator.setTextColor(Color.WHITE);
                        tanggalLahir.setTextColor(Color.WHITE);
                        noRekam.setText(item.getNorekam());
                        namaLengkap.setText(item.getNamaPasien());
                        jenisKelamin.setText(item.getGender());
                        tanggalLahir.setText(item.getTanggalLahir());
                        itemView.setOnClickListener(view -> mListener.onItemClick(item));
                        adminHolder.setOnClickListener(view -> mListener.onItemClick(item));
                    } else if (item.getStatus().equals("2")) {
                        adminHolder.setCardBackgroundColor(Color.WHITE);
                        noRekam.setTextColor(Color.DKGRAY);
                        namaLengkap.setTextColor(Color.DKGRAY);
                        jenisKelamin.setTextColor(Color.DKGRAY);
                        separator.setTextColor(Color.DKGRAY);
                        tanggalLahir.setTextColor(Color.DKGRAY);
                        noRekam.setText(item.getNorekam());
                        namaLengkap.setText(item.getNamaPasien());
                        jenisKelamin.setText(item.getGender());
                        tanggalLahir.setText(item.getTanggalLahir());
                        itemView.setOnClickListener(view -> mListener.onItemClick(item));
                        adminHolder.setOnClickListener(view -> mListener.onItemClick(item));
                    }
            }
        }
    }

    private static class LoadingViewHolder extends BaseViewHolder{

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void clear() {

        }
    }

    public void setOnClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnItemClickListener {
        void onItemClick(ItemAdminEntity itemAdminEntity);
    }
}
