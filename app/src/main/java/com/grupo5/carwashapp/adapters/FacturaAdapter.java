package com.grupo5.carwashapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.grupo5.carwashapp.R;
import com.grupo5.carwashapp.models.Factura;

import java.util.List;

public class FacturaAdapter extends RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder> {
    private final List<Factura> listaFacturas;
    private final OnFacturaClickListener listener;

    public FacturaAdapter(List<Factura> listaFacturas, OnFacturaClickListener listener) {
        this.listaFacturas = listaFacturas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FacturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_factura, parent, false);
        return new FacturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacturaViewHolder holder, int position) {
        Factura factura = listaFacturas.get(position);

        holder.txtPlaca.setText(factura.getVehiculoPlaca());
        holder.txtFecha.setText(factura.getFechaEmision());
        holder.txtTotal.setText(String.format("$%.2f", factura.getTotal()));
        holder.txtEstado.setText(factura.getEstado().toString());

        Context context = holder.itemView.getContext();
        String estado = factura.getEstado().toString();

        if ("PAGADA".equals(estado)) {
            int colorVerde = ContextCompat.getColor(context, R.color.green);
            holder.txtEstado.setTextColor(colorVerde);
        } else if ("PENDIENTE".equals(estado)) {
            int colorNaranja = ContextCompat.getColor(context, R.color.orange);
            holder.txtEstado.setTextColor(colorNaranja);
        } else if ("ANULADA".equals(estado)) {
            int colorRojo = ContextCompat.getColor(context, R.color.btnEliminar);
            holder.txtEstado.setTextColor(colorRojo);
        }
        holder.itemView.setOnClickListener(v -> listener.onFacturaClick(factura));
    }

    public void actualizarItem(Factura facturaModificada) {
        if (listaFacturas == null) return;

        for (int i = 0; i < listaFacturas.size(); i++) {
            if (listaFacturas.get(i).getUid().equals(facturaModificada.getUid())) {
                listaFacturas.set(i, facturaModificada);
                notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return listaFacturas.size();
    }

    public interface OnFacturaClickListener {
        void onFacturaClick(Factura factura);
    }

    public static class FacturaViewHolder extends RecyclerView.ViewHolder {
        TextView txtPlaca, txtFecha, txtTotal, txtEstado;

        public FacturaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPlaca = itemView.findViewById(R.id.item_lbl_placa);
            txtFecha = itemView.findViewById(R.id.item_lbl_fecha);
            txtTotal = itemView.findViewById(R.id.item_lbl_total);
            txtEstado = itemView.findViewById(R.id.item_lbl_estado);
        }
    }
}
