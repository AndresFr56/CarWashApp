package com.grupo5.carwashapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.models.Servicio;

import java.util.List;

public class ServicioAdapter extends RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder> {

    private final List<Servicio> listaServicios;
    private final OnServicioClickListener listener;

    // Constructor
    public ServicioAdapter(List<Servicio> listaServicios, OnServicioClickListener listener) {
        this.listaServicios = listaServicios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_servicio, parent, false);
        return new ServicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioViewHolder holder, int position) {
        Servicio servicio = listaServicios.get(position);

        holder.txtNombre.setText(servicio.getNombre_servicio());
        holder.txtDescripcion.setText(servicio.getDescripcion_servicio());

        holder.itemView.setOnClickListener(v -> listener.onServicioClick(servicio));
    }

    @Override
    public int getItemCount() {
        return listaServicios.size();
    }

    // Interface para clicks (igual que Factura)
    public interface OnServicioClickListener {
        void onServicioClick(Servicio servicio);
    }

    // ViewHolder
    public static class ServicioViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre, txtDescripcion;

        public ServicioViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.consul_txt_nombre_serv);
            txtDescripcion = itemView.findViewById(R.id.consul_serv_descrip);
        }
    }
}
