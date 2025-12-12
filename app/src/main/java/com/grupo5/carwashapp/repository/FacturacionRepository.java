package com.grupo5.carwashapp.repository;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grupo5.carwashapp.interfaces.FacturaCallBack;
import com.grupo5.carwashapp.models.Factura;
import com.grupo5.carwashapp.models.enums.EstadoFacturas;

import java.util.ArrayList;
import java.util.List;

public class FacturacionRepository {
    private final DatabaseReference dbRef;

    public FacturacionRepository() {
        dbRef = FirebaseDatabase.getInstance().getReference("Facturas");
    }

    public void crearFactura(Factura factura, final FacturaCallBack callback) {
        String key = dbRef.push().getKey();
        factura.setUid(key);

        dbRef.child(key).setValue(factura)
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    callback.onError(e.getMessage());
                });
    }

    public void buscarFacturasPorCedula(String cedula, final FacturaCallBack callback) {
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
                        callback.onFacturasLoaded(listaFacturas);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }

    public void anularFactura(String facturaId, final FacturaCallBack callback) {
        dbRef.child(facturaId).child("estado").setValue(EstadoFacturas.ANULADA)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }
}
