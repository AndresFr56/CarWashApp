package com.grupo5.carwashapp.repository;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.grupo5.carwashapp.models.Vehiculo;

public class VehiculoRepository {
    private final DatabaseReference mDatabase;

    public VehiculoRepository() {
        mDatabase = FirebaseDatabase.getInstance().getReference("Vehiculos");
    }

    public void crearVehiculo(Vehiculo vehiculo, OnCompleteListener<Void> listener) {
        String key = mDatabase.push().getKey();
        mDatabase.child(key).setValue(vehiculo).addOnCompleteListener(listener);
    }
    public void actualizarVehiculo(String vehiculoId, Vehiculo vehiculo, OnCompleteListener<Void> listener) {
        mDatabase.child(vehiculoId).setValue(vehiculo).addOnCompleteListener(listener);
    }

    public void eliminarVehiculo(String vehiculoId, OnCompleteListener<Void> listener) {
        mDatabase.child(vehiculoId).removeValue().addOnCompleteListener(listener);
    }
    public void existePlaca(String placa, ValueEventListener listener) {
        mDatabase.orderByChild("placa")
                .equalTo(placa)
                .addListenerForSingleValueEvent(listener);
    }
    public void obtenerVehiculoPorPlaca(String placa, ValueEventListener listener) {
        mDatabase.orderByChild("placa")
                .equalTo(placa)
                .addListenerForSingleValueEvent(listener);
    }
    public void obtenerVehiculosPorCliente(String uidCliente, ValueEventListener listener) {
        mDatabase.orderByChild("clienteId")
                .equalTo(uidCliente)
                .addListenerForSingleValueEvent(listener);
    }
}
