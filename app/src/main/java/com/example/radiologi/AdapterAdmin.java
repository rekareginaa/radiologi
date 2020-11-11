package com.example.radiologi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterAdmin extends RecyclerView.Adapter<AdapterAdmin.ViewHolder> {

    private LayoutInflater inflater;
    private List<ListitemAdmin> listitemAdmins;
    private Context context;

    private AdapterAdmin.OnItemClickListener mListener = null;

    public AdapterAdmin(Context context) {
        this.listitemAdmins = new ArrayList<>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListitemAdmin listitemAdmin = listitemAdmins.get(position);

        holder.noRekam.setText(listitemAdmin.getNoRekam());
        holder.namaLengkap.setText(listitemAdmin.getNamaLengkap());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(listitemAdmin);
            }
        });

        holder.adminHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(listitemAdmin);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listitemAdmins.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView noRekam;
        public TextView namaLengkap;
        public CardView adminHolder;

        public ViewHolder(View itemView) {
            super(itemView);

            noRekam = (TextView) itemView.findViewById(R.id.noRekam);
            namaLengkap = (TextView) itemView.findViewById(R.id.namaLengkap);
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
