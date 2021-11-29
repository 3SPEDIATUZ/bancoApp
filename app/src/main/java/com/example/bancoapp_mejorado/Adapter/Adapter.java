package com.example.bancoapp_mejorado.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.bancoapp_mejorado.Metodos.Transaction;
import com.example.bancoapp_mejorado.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolderDatos> {

    ArrayList<Transaction> listaDatos;

    String tipo ;
    ImageView ivIcono;
    private RecyclerItemClick itemClick;

    public Adapter(ArrayList<Transaction> listaDatos){
        this.listaDatos = listaDatos;
    }

    public Adapter(ArrayList<Transaction> listaDatos, RecyclerItemClick itemClick) {
        this.listaDatos = listaDatos;
        this.itemClick = itemClick;


    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null, false);
        return new ViewHolderDatos(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
            DecimalFormat formatter = new DecimalFormat("###,###,###.00");
            if(listaDatos.get(position).isTransaction_estate()){
                holder.tvMonto_trans.setTextColor(Color.parseColor("#0F9B33"));
                holder.tvMonto_trans.setText("$ " + formatter.format(Integer.parseInt(listaDatos.get(position).getTransaction_amount())));
            }else{
                holder.tvMonto_trans.setTextColor(Color.parseColor("#FA0A03"));
                holder.tvMonto_trans.setText("$- " + formatter.format(Integer.parseInt(listaDatos.get(position).getTransaction_amount())));
            }
            holder.tvDescripcion_trans.setText(listaDatos.get(position).getTransaction_description());
            holder.tvFecha_trans.setText(listaDatos.get(position).getTransaction_date());



            final Transaction i = listaDatos.get(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.OnClick(i);
                }
            });


    }

    @Override
    public int getItemCount() {
        return listaDatos.size();
    }



    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView tvMonto_trans, tvDescripcion_trans, tvFecha_trans;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);

            tvMonto_trans = itemView.findViewById(R.id.tvValor_monto);
            tvDescripcion_trans = itemView.findViewById(R.id.tvDescripcion);
            tvFecha_trans = itemView.findViewById(R.id.tvFecha_transaccion);

        }
    }


    public  interface RecyclerItemClick {
        void OnClick(Transaction item);
    }


}
