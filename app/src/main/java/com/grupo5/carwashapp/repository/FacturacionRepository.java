package com.grupo5.carwashapp.repository;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grupo5.carwashapp.interfaces.RepositoryCallBack;
import com.grupo5.carwashapp.models.Factura;
import com.grupo5.carwashapp.models.enums.EstadoFacturas;
import com.grupo5.carwashapp.models.enums.FormaPago;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacturacionRepository {
    private final DatabaseReference dbRef;

    public FacturacionRepository() {
        dbRef = FirebaseDatabase.getInstance().getReference("Facturas");
    }

    public void crearFactura(Factura factura, final RepositoryCallBack<String> callback) {
        DatabaseReference nuevaFacturaRef = dbRef.push();
        String idNuevaFactura = nuevaFacturaRef.getKey();
        factura.setUid(idNuevaFactura);

        dbRef.child(idNuevaFactura).setValue(factura)
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess(idNuevaFactura);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure(e);
                });
    }

    public void buscarFacturasPorCedula(String cedula, final RepositoryCallBack<List<Factura>> callback) {
        dbRef.orderByChild("clienteCedula").equalTo(cedula)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Factura> listaFacturas = new ArrayList<>();

                        for (DataSnapshot data : snapshot.getChildren()) {
                            Factura fact = data.getValue(Factura.class);
                            if (fact != null) {
                                fact.setUid(data.getKey());
                                listaFacturas.add(fact);
                            }
                        }
                        callback.onSuccess(listaFacturas);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onFailure(error.toException());
                    }
                });
    }

    public void actualizarEstadoPago(String uid, FormaPago formaPago, String observaciones, final RepositoryCallBack<Void> callback) {
        Map<String, Object> actualizaciones = new HashMap<>();

        actualizaciones.put("estado", EstadoFacturas.PAGADA);

        actualizaciones.put("formaPago", formaPago.name());
        actualizaciones.put("observaciones", observaciones);

        dbRef.child(uid).updateChildren(actualizaciones)
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onFailure(e));
    }

    public void anularFactura(String facturaId, final RepositoryCallBack<Void> callback) {
        dbRef.child(facturaId).child("estado").setValue(EstadoFacturas.ANULADA)
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onFailure(e));
    }
}
