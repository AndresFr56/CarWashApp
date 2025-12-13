package com.grupo5.carwashapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.models.CatalogoServicio;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CatalogoServiciosAdapter extends RecyclerView.Adapter<CatalogoServiciosAdapter.ViewHolder> {
    private final OnItemClickListener listener;
    private List<CatalogoServicio> listaServCompleta;
    private List<CatalogoServicio> listaServFiltrada;

    public CatalogoServiciosAdapter(List<CatalogoServicio> listaServicios, OnItemClickListener listener) {
        this.listaServCompleta = new ArrayList<>(listaServicios);
        this.listaServFiltrada = new ArrayList<>(listaServicios);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_catalogo_servicio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CatalogoServicio servicio = listaServFiltrada.get(position);

        holder.txtNombre.setText(servicio.getNombre());
        holder.txtDescripcion.setText(servicio.getDescripcion());

        holder.txtPrecio.setText(String.format(Locale.US, "$ %.2f", servicio.getPrecio()));

        holder.btnEditar.setOnClickListener(v -> listener.onEditarClick(servicio));
        holder.btnEliminar.setOnClickListener(v -> listener.onEliminarClick(servicio));
    }

    @Override
    public int getItemCount() {
        return listaServFiltrada.size();
    }

    public void setLista(List<CatalogoServicio> nuevaLista) {
        this.listaServCompleta = new ArrayList<>(nuevaLista);
        this.listaServFiltrada = new ArrayList<>(nuevaLista);
        notifyDataSetChanged();
    }

    public void filtrar(String textoBusqueda) {
        int longitud = textoBusqueda.length();

        listaServFiltrada.clear();

        if (longitud == 0) {
            listaServFiltrada.addAll(listaServCompleta);
        } else {
            for (CatalogoServicio c : listaServCompleta) {
                if (c.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                    listaServFiltrada.add(c);
                }
            }
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onEditarClick(CatalogoServicio servicio);

        void onEliminarClick(CatalogoServicio servicio);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtPrecio, txtDescripcion;
        ImageView btnEditar, btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.consul_catalogo_txt_nombre_serv);
            txtPrecio = itemView.findViewById(R.id.consul_catalogo_txt_precio_serv);
            txtDescripcion = itemView.findViewById(R.id.consul_catalogo_serv_descrip);
            btnEditar = itemView.findViewById(R.id.consul_catalogo_serv_btn_actualizar);
            btnEliminar = itemView.findViewById(R.id.consul_catalogo_serv_btn_eliminar);
        }
    }
}
