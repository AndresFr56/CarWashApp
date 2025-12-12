package com.grupo5.carwashapp.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VehiculoRepository {
    private final DatabaseReference mDatabase;

    public VehiculoRepository() {
        mDatabase = FirebaseDatabase.getInstance().getReference("Vehiculos");
    }

    public void obtenerVehiculosPorCliente(String uidCliente, ValueEventListener listener) {
        mDatabase.orderByChild("clienteId")
                .equalTo(uidCliente)
                .addListenerForSingleValueEvent(listener);
    }
}
