package com.example.radiologi.dokter.home.fragments;

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
import com.example.radiologi.data.entitiy.ItemDoctorEntity;
import com.example.radiologi.utils.rv.BaseViewHolder;

import java.util.List;

public class NewAdapterDokter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;

    private final int status;
    private final List<ItemDoctorEntity> doctorEntities;
    private OnItemClickListener mListener = null;

    public NewAdapterDokter(List<ItemDoctorEntity> doctorEntities, int status){
        this.doctorEntities = doctorEntities;
        this.status = status;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case VIEW_TYPE_NORMAL:
                return new ContentViewHolder(
                        inflater.inflate(R.layout.listitem_dokter, parent, false)
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
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return doctorEntities == null ? 0 : doctorEntities.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible){
            return (position == doctorEntities.size() - 1 && isLoaderVisible) ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        }else{
            return VIEW_TYPE_NORMAL;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addItems(List<ItemDoctorEntity> doctorEntities){
        this.doctorEntities.addAll(doctorEntities);
        notifyDataSetChanged();
    }

    public void addLoading(){
        isLoaderVisible = true;
        doctorEntities.add(new ItemDoctorEntity("", "", "", "", "", "", "", "", "", "", "", "", 0));
        notifyItemInserted(doctorEntities.size() - 1);
    }

    public void removeLoading(){
        isLoaderVisible = false;
        int position = doctorEntities.size() - 1;
        ItemDoctorEntity doctorEntity = getItem(position);
        if (doctorEntity != null){
            doctorEntities.remove(position);
            notifyItemRemoved(position);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear(){
        doctorEntities.clear();
        notifyDataSetChanged();
    }

    ItemDoctorEntity getItem(int position) {return doctorEntities.get(position);}

    public class ContentViewHolder extends BaseViewHolder{
        public TextView noRekam;
        public TextView namaLengkap;
        public TextView jenisKelamin;
        public TextView tanggalLahir;
        public CardView dokterHolder;
        public TextView separator;

        public ContentViewHolder(View itemView){
            super(itemView);

            noRekam = itemView.findViewById(R.id.noRekam);
            namaLengkap = itemView.findViewById(R.id.namaLengkap);
            jenisKelamin = itemView.findViewById(R.id.jenisKelamin);
            tanggalLahir = itemView.findViewById(R.id.tanggalLahir);
            dokterHolder = itemView.findViewById(R.id.dokter);
            separator = itemView.findViewById(R.id.separator);
        }

        @Override
        protected void clear() {

        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            ItemDoctorEntity item = doctorEntities.get(position);
            noRekam.setText(item.getNorekam());
            namaLengkap.setText(item.getNamaPasien());
            jenisKelamin.setText(item.getGender());
            tanggalLahir.setText(item.getTanggalLahir());
            dokterHolder.setOnClickListener(view -> mListener.onItemClick(item));

            if (status == 1){
                if (item.getStatus().equals("1")){
                    dokterHolder.setCardBackgroundColor(Color.parseColor("#038C7F"));
                    noRekam.setTextColor(Color.WHITE);
                    namaLengkap.setTextColor(Color.WHITE);
                    jenisKelamin.setTextColor(Color.WHITE);
                    separator.setTextColor(Color.WHITE);
                    tanggalLahir.setTextColor(Color.WHITE);
                }else if (item.getStatus().equals("2")){
                    dokterHolder.setCardBackgroundColor(Color.WHITE);
                    noRekam.setTextColor(Color.DKGRAY);
                    namaLengkap.setTextColor(Color.DKGRAY);
                    jenisKelamin.setTextColor(Color.DKGRAY);
                    separator.setTextColor(Color.DKGRAY);
                    tanggalLahir.setTextColor(Color.DKGRAY);
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
        void onItemClick(ItemDoctorEntity doctorEntity);
    }
}
